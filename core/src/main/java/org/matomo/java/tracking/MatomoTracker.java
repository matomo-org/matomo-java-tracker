/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The main class that sends {@link MatomoRequest}s to a specified Matomo server.
 *
 * <p>Contains several methods to send requests synchronously and asynchronously. The asynchronous
 * methods return a {@link CompletableFuture} that can be used to wait for the request to finish.
 * The synchronous methods block until the request is finished. The asynchronous methods are more
 * efficient if you want to send multiple requests at once.
 *
 * <p>Configure this tracker using the {@link TrackerConfiguration} class. You can use the {@link
 * TrackerConfiguration#builder()} to create a new configuration. The configuration is immutable and
 * can be reused for multiple trackers.
 *
 * <p>The tracker is thread-safe and can be used by multiple threads at once.
 *
 * @author brettcsorba
 */
@Slf4j
public class MatomoTracker implements AutoCloseable {

  private final TrackerConfiguration trackerConfiguration;

  @Setter(AccessLevel.PROTECTED)
  private SenderFactory senderFactory = new ServiceLoaderSenderFactory();

  private Sender sender;

  /**
   * Creates a new Matomo Tracker instance.
   *
   * @param trackerConfiguration Configurations parameters (you can use a builder)
   */
  public MatomoTracker(@NonNull TrackerConfiguration trackerConfiguration) {
    trackerConfiguration.validate();
    this.trackerConfiguration = trackerConfiguration;
  }

  /**
   * Sends a tracking request to Matomo using the HTTP GET method.
   *
   * <p>Use this method if you want to send a single request. If you want to send multiple requests
   * at once, use {@link #sendBulkRequest(Iterable)} instead. If you want to send multiple requests
   * asynchronously, use {@link #sendRequestAsync(MatomoRequest)} or {@link
   * #sendBulkRequestAsync(Collection)} instead.
   *
   * @param request request to send. must not be null
   */
  public void sendRequest(@NonNull MatomoRequest request) {
    if (trackerConfiguration.isEnabled()) {
      log.debug("Sending request via GET: {}", request);
      applyGoalIdAndCheckSiteId(request);
      initializeSender();
      sender.sendSingle(request);
    } else {
      log.warn("Not sending request, because tracker is disabled");
    }
  }

  private void initializeSender() {
    if (sender == null) {
      sender =
          senderFactory.createSender(trackerConfiguration, new QueryCreator(trackerConfiguration));
    }
  }

  /**
   * Send a request asynchronously via HTTP GET.
   *
   * <p>Use this method if you want to send a single request. If you want to send multiple requests
   * at once, use {@link #sendBulkRequestAsync(Collection)} instead. If you want to send multiple
   * requests synchronously, use {@link #sendRequest(MatomoRequest)} or {@link
   * #sendBulkRequest(Iterable)} instead.
   *
   * @param request request to send
   * @return completable future to let you know when the request is done. Contains the request.
   */
  public CompletableFuture<MatomoRequest> sendRequestAsync(@NonNull MatomoRequest request) {
    if (trackerConfiguration.isEnabled()) {
      applyGoalIdAndCheckSiteId(request);
      log.debug("Sending async request via GET: {}", request);
      initializeSender();
      return sender.sendSingleAsync(request);
    }
    log.warn("Not sending request, because tracker is disabled");
    return CompletableFuture.completedFuture(null);
  }

  private void applyGoalIdAndCheckSiteId(@NonNull MatomoRequest request) {
    if (request.getGoalId() == null
        && (request.getEcommerceId() != null
            || request.getEcommerceRevenue() != null
            || request.getEcommerceDiscount() != null
            || request.getEcommerceItems() != null
            || request.getEcommerceLastOrderTimestamp() != null
            || request.getEcommerceShippingCost() != null
            || request.getEcommerceSubtotal() != null
            || request.getEcommerceTax() != null)) {
      request.setGoalId(0);
    }
    if (trackerConfiguration.getDefaultSiteId() == null && request.getSiteId() == null) {
      throw new IllegalArgumentException("No default site ID and no request site ID is given");
    }
  }

  /**
   * Send multiple requests in a single HTTP POST call.
   *
   * <p>More efficient than sending several individual requests. If you want to send a single
   * request, use {@link #sendRequest(MatomoRequest)} instead. If you want to send multiple requests
   * asynchronously, use {@link #sendBulkRequestAsync(Collection)} instead.
   *
   * @param requests the requests to send
   */
  public void sendBulkRequest(MatomoRequest... requests) {
    sendBulkRequest(Arrays.asList(requests));
  }

  /**
   * Send multiple requests in a single HTTP POST call.
   *
   * <p>More efficient than sending several individual requests. If you want to send a single
   * request, use {@link #sendRequest(MatomoRequest)} instead. If you want to send multiple requests
   * asynchronously, use {@link #sendBulkRequestAsync(Collection)} instead.
   *
   * @param requests the requests to send
   */
  public void sendBulkRequest(@NonNull Iterable<? extends MatomoRequest> requests) {
    if (trackerConfiguration.isEnabled()) {
      for (MatomoRequest request : requests) {
        applyGoalIdAndCheckSiteId(request);
      }
      log.debug("Sending requests via POST: {}", requests);
      initializeSender();
      sender.sendBulk(requests);
    } else {
      log.warn("Not sending request, because tracker is disabled");
    }
  }

  /**
   * Send multiple requests in a single HTTP call. More efficient than sending several individual
   * requests.
   *
   * @param requests the requests to send
   * @return completable future to let you know when the request is done
   */
  public CompletableFuture<Void> sendBulkRequestAsync(MatomoRequest... requests) {
    return sendBulkRequestAsync(Arrays.asList(requests));
  }

  /**
   * Send multiple requests in a single HTTP call. More efficient than sending several individual
   * requests.
   *
   * @param requests the requests to send
   * @return completable future to let you know when the request is done
   */
  public CompletableFuture<Void> sendBulkRequestAsync(
      @NonNull Collection<? extends MatomoRequest> requests) {
    if (trackerConfiguration.isEnabled()) {
      for (MatomoRequest request : requests) {
        applyGoalIdAndCheckSiteId(request);
      }
      log.debug("Sending async requests via POST: {}", requests);
      initializeSender();
      return sender.sendBulkAsync(requests);
    }
    log.warn("Tracker is disabled");
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public void close() throws Exception {
    if (sender != null) {
      sender.close();
    }
  }
}
