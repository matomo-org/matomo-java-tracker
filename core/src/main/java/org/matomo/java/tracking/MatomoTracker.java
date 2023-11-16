/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The main class that sends {@link MatomoRequest}s to a specified Matomo server.
 *
 * <p>Contains several methods to send requests synchronously and asynchronously. The asynchronous methods return a
 * {@link CompletableFuture} that can be used to wait for the request to finish. The synchronous methods block until
 * the request is finished. The asynchronous methods are more efficient if you want to send multiple requests at once.
 *
 * <p>Configure this tracker using the {@link TrackerConfiguration} class. You can use the
 * {@link TrackerConfiguration#builder()} to create a new configuration. The configuration is immutable and can be
 * reused for multiple trackers.
 *
 * <p>The tracker is thread-safe and can be used by multiple threads at once.
 *
 * @author brettcsorba
 */
@Slf4j
public class MatomoTracker {

  private final TrackerConfiguration trackerConfiguration;

  @Setter(AccessLevel.PROTECTED)
  private SenderFactory senderFactory = new ServiceLoaderSenderFactory();

  private Sender sender;

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   *                <strong>https://your-matomo-domain.tld/matomo.php</strong>. Must not be null
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(
      @NonNull String hostUrl
  ) {
    this(hostUrl, 0);
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   *                <strong>https://your-matomo-domain.tld/matomo.php</strong>.
   * @param timeout the timeout of the sent request in milliseconds or -1 if not set
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(
      @NonNull String hostUrl, int timeout
  ) {
    this(hostUrl, null, 0, timeout);
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl   url endpoint to send requests to.  Usually in the format
   *                  <strong>https://your-matomo-domain.tld/matomo.php</strong>.
   * @param proxyHost The hostname or IP address of an optional HTTP proxy, null allowed
   * @param proxyPort The port of an HTTP proxy or -1 if not set
   * @param timeout   the timeout of the request in milliseconds or -1 if not set
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(
      @NonNull String hostUrl, @Nullable String proxyHost, int proxyPort, int timeout
  ) {
    this(TrackerConfiguration
        .builder()
        .enabled(true)
        .apiEndpoint(URI.create(hostUrl))
        .proxyHost(proxyHost)
        .proxyPort(proxyPort)
        .connectTimeout(timeout == -1 ? Duration.ofSeconds(5L) : Duration.ofSeconds(timeout))
        .socketTimeout(timeout == -1 ? Duration.ofSeconds(5L) : Duration.ofSeconds(timeout))
        .build());
  }

  /**
   * Creates a new Matomo Tracker instance.
   *
   * @param trackerConfiguration Configurations parameters (you can use a builder)
   */
  public MatomoTracker(
      @NonNull TrackerConfiguration trackerConfiguration
  ) {
    trackerConfiguration.validate();
    this.trackerConfiguration = trackerConfiguration;
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint via the provided proxy.
   *
   * @param hostUrl   url endpoint to send requests to.  Usually in the format
   *                  <strong>https://your-matomo-domain.tld/matomo.php</strong>.
   * @param proxyHost url endpoint for the proxy, null allowed
   * @param proxyPort proxy server port number or -1 if not set
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(
      @NonNull String hostUrl, @Nullable String proxyHost, int proxyPort
  ) {
    this(hostUrl, proxyHost, proxyPort, -1);
  }

  /**
   * Sends a tracking request to Matomo using the HTTP GET method.
   *
   * <p>Use this method if you want to send a single request. If you want to send multiple requests at once, use
   * {@link #sendBulkRequest(Iterable)} instead. If you want to send multiple requests asynchronously, use
   * {@link #sendRequestAsync(MatomoRequest)} or {@link #sendBulkRequestAsync(Iterable)} instead.
   *
   * @param request request to send. must not be null
   */
  public void sendRequest(@NonNull MatomoRequest request) {
    if (trackerConfiguration.isEnabled()) {
      log.debug("Sending request via GET: {}", request);
      validate(request);
      initializeSender();
      sender.sendSingle(request);
    } else {
      log.warn("Not sending request, because tracker is disabled");
    }
  }

  private void initializeSender() {
    if (sender == null) {
      sender = senderFactory.createSender(trackerConfiguration, new QueryCreator(trackerConfiguration));
    }
  }

  /**
   * Send a request asynchronously via HTTP GET.
   *
   * <p>Use this method if you want to send a single request. If you want to send multiple requests at once, use
   * {@link #sendBulkRequestAsync(Iterable)} instead. If you want to send multiple requests synchronously, use
   * {@link #sendRequest(MatomoRequest)} or {@link #sendBulkRequest(Iterable)} instead.
   *
   * @param request request to send
   * @return completable future to let you know when the request is done. Contains the request.
   */
  public CompletableFuture<MatomoRequest> sendRequestAsync(
      @NonNull MatomoRequest request
  ) {
    return sendRequestAsync(request, Function.identity());
  }

  /**
   * Send a request asynchronously via HTTP GET and specify a callback that gets executed when the response arrives.
   *
   * <p>Use this method if you want to send a single request. If you want to send multiple requests at once, use
   * {@link #sendBulkRequestAsync(Iterable, Consumer)} instead. If you want to send multiple requests synchronously, use
   * {@link #sendRequest(MatomoRequest)} or {@link #sendBulkRequest(Iterable)} instead.
   *
   * @param request  request to send
   * @param callback callback that gets executed when response arrives, must not be null
   * @return a completable future to let you know when the request is done. The future contains
   * the callback result.
   * @deprecated Please use {@link MatomoTracker#sendRequestAsync(MatomoRequest)} in combination with
   * {@link CompletableFuture#thenAccept(Consumer)} instead
   */
  @Deprecated
  public <T> CompletableFuture<T> sendRequestAsync(
      @NonNull MatomoRequest request,
      @NonNull Function<MatomoRequest, T> callback
  ) {
    if (trackerConfiguration.isEnabled()) {
      validate(request);
      log.debug("Sending async request via GET: {}", request);
      initializeSender();
      CompletableFuture<MatomoRequest> future = sender.sendSingleAsync(request);
      return future.thenApply(callback);
    }
    log.warn("Not sending request, because tracker is disabled");
    return CompletableFuture.completedFuture(null);
  }

  private void validate(
      @NonNull MatomoRequest request
  ) {
    if (trackerConfiguration.getDefaultSiteId() == null && request.getSiteId() == null) {
      throw new IllegalArgumentException("No default site ID and no request site ID is given");
    }
  }

  /**
   * Send multiple requests in a single HTTP POST call.
   *
   * <p>More efficient than sending several individual requests. If you want to send a single request, use
   * {@link #sendRequest(MatomoRequest)} instead. If you want to send multiple requests asynchronously, use
   * {@link #sendBulkRequestAsync(Iterable)} instead.
   *
   * @param requests the requests to send
   */
  public void sendBulkRequest(MatomoRequest... requests) {
    sendBulkRequest(Arrays.asList(requests), null);
  }

  /**
   * Send multiple requests in a single HTTP POST call.
   *
   * <p>More efficient than sending several individual requests. If you want to send a single request, use
   * {@link #sendRequest(MatomoRequest)} instead. If you want to send multiple requests asynchronously, use
   * {@link #sendBulkRequestAsync(Iterable)} instead.
   *
   * @param requests the requests to send
   */
  public void sendBulkRequest(@NonNull Iterable<? extends MatomoRequest> requests) {
    sendBulkRequest(requests, null);
  }

  /**
   * Send multiple requests in a single HTTP POST call. More efficient than sending
   * several individual requests.
   *
   * <p>Specify the AuthToken if parameters that require an auth token is used. If you want to send a single request,
   * use {@link #sendRequest(MatomoRequest)} instead. If you want to send multiple requests asynchronously, use
   * {@link #sendBulkRequestAsync(Iterable)} instead.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken, if null the default auth token from the
   *                  request or the tracker configuration is used.
   * @deprecated use {@link #sendBulkRequest(Iterable)} instead and set the auth token in the tracker configuration or
   * the requests directly.
   */
  @Deprecated
  public void sendBulkRequest(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String authToken
  ) {
    if (trackerConfiguration.isEnabled()) {
      for (MatomoRequest request : requests) {
        validate(request);
      }
      log.debug("Sending requests via POST: {}", requests);
      initializeSender();
      sender.sendBulk(requests, authToken);
    } else {
      log.warn("Not sending request, because tracker is disabled");
    }
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @return completable future to let you know when the request is done
   */
  public CompletableFuture<Void> sendBulkRequestAsync(MatomoRequest... requests) {
    return sendBulkRequestAsync(Arrays.asList(requests), null, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @return completable future to let you know when the request is done
   */
  public CompletableFuture<Void> sendBulkRequestAsync(
      @NonNull Iterable<? extends MatomoRequest> requests
  ) {
    return sendBulkRequestAsync(requests, null, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken, if null the default auth token from the
   *                  request or the tracker configuration is used
   * @param callback  callback that gets executed when response arrives, null allowed
   * @return a completable future to let you know when the request is done
   * @deprecated Please set the auth token in the tracker configuration or the requests directly and use
   * {@link CompletableFuture#thenAccept(Consumer)} instead for the callback.
   */
  @Deprecated
  public CompletableFuture<Void> sendBulkRequestAsync(
      @NonNull Iterable<? extends MatomoRequest> requests,
      @Nullable String authToken,
      @Nullable Consumer<Void> callback
  ) {
    if (trackerConfiguration.isEnabled()) {
      for (MatomoRequest request : requests) {
        validate(request);
      }
      log.debug("Sending async requests via POST: {}", requests);
      initializeSender();
      CompletableFuture<Void> future = sender.sendBulkAsync(requests, authToken);
      if (callback != null) {
        return future.thenAccept(callback);
      }
      return future;
    }
    log.warn("Tracker is disabled");
    return CompletableFuture.completedFuture(null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @param callback callback that gets executed when response arrives, null allowed
   * @return completable future to let you know when the request is done
   */
  public CompletableFuture<Void> sendBulkRequestAsync(
      @NonNull Iterable<? extends MatomoRequest> requests,
      @Nullable Consumer<Void> callback
  ) {
    return sendBulkRequestAsync(requests, null, callback);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken, null allowed
   * @return completable future to let you know when the request is done
   * @deprecated Please set the auth token in the tracker configuration or the requests directly and use
   * {@link #sendBulkRequestAsync(Iterable)} instead.
   */
  public CompletableFuture<Void> sendBulkRequestAsync(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String authToken
  ) {
    return sendBulkRequestAsync(requests, authToken, null);
  }
}
