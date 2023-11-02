/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * A class that sends {@link MatomoRequest}s to a specified Matomo server.
 *
 * @author brettcsorba
 */
@Slf4j
public class MatomoTracker {

  private final TrackerConfiguration trackerConfiguration;

  private final Sender sender;

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   *                <strong>http://your-matomo-domain.tld/matomo.php</strong>. Must not be null
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(@NotNull String hostUrl) {
    this(requireNonNull(hostUrl, "Host URL must not be null"), 0);
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   *                <strong>http://your-matomo-domain.tld/matomo.php</strong>.
   * @param timeout the timeout of the sent request in milliseconds or -1 if not set
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(@NotNull String hostUrl, int timeout) {
    this(requireNonNull(hostUrl, "Host URL must not be null"), null, 0, timeout);
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl   url endpoint to send requests to.  Usually in the format
   *                  <strong>http://your-matomo-domain.tld/matomo.php</strong>.
   * @param proxyHost The hostname or IP address of an optional HTTP proxy, null allowed
   * @param proxyPort The port of an HTTP proxy or -1 if not set
   * @param timeout   the timeout of the request in milliseconds or -1 if not set
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(@NotNull String hostUrl, @Nullable String proxyHost, int proxyPort, int timeout) {
    this(TrackerConfiguration.builder().enabled(true).apiEndpoint(
        URI.create(requireNonNull(hostUrl, "Host URL must not be null"))).proxyHost(proxyHost).proxyPort(proxyPort)
      .connectTimeout(timeout == -1 ? Duration.ofSeconds(5L) : Duration.ofSeconds(timeout))
      .socketTimeout(timeout == -1 ? Duration.ofSeconds(5L) : Duration.ofSeconds(timeout)).build());
  }

  /**
   * Creates a new Matomo Tracker instance.
   *
   * @param trackerConfiguration Configurations parameters (you can use a builder)
   */
  public MatomoTracker(@NotNull TrackerConfiguration trackerConfiguration) {
    requireNonNull(trackerConfiguration, "Tracker configuration must not be null");
    trackerConfiguration.validate();
    this.trackerConfiguration = trackerConfiguration;
    ScheduledThreadPoolExecutor threadPoolExecutor = createThreadPoolExecutor();
    sender = new Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), threadPoolExecutor);
  }

  @NotNull
  private static ScheduledThreadPoolExecutor createThreadPoolExecutor() {
    DaemonThreadFactory threadFactory = new DaemonThreadFactory();
    ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1, threadFactory);
    threadPoolExecutor.setRemoveOnCancelPolicy(true);
    return threadPoolExecutor;
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint via the provided proxy.
   *
   * @param hostUrl   url endpoint to send requests to.  Usually in the format
   *                  <strong>http://your-matomo-domain.tld/matomo.php</strong>.
   * @param proxyHost url endpoint for the proxy, null allowed
   * @param proxyPort proxy server port number or -1 if not set
   * @deprecated Please use {@link MatomoTracker#MatomoTracker(TrackerConfiguration)}
   */
  @Deprecated
  public MatomoTracker(@NotNull String hostUrl, @Nullable String proxyHost, int proxyPort) {
    this(hostUrl, proxyHost, proxyPort, -1);
  }

  /**
   * Sends a tracking request to Matomo.
   *
   * @param request request to send. must not be null
   * @deprecated use sendRequestAsync instead
   */
  @Deprecated
  public void sendRequest(@NonNull MatomoRequest request) {
    if (trackerConfiguration.isEnabled()) {
      log.debug("Sending request via GET: {}", request);
      sender.sendSingle(request);
    } else {
      log.warn("Not sending request, because tracker is disabled");
    }
  }

  /**
   * Send a request.
   *
   * @param request request to send
   * @return future with response from this request
   */
  public CompletableFuture<Void> sendRequestAsync(@NotNull MatomoRequest request) {
    return sendRequestAsync(request, null);
  }

  /**
   * Send a request.
   *
   * @param request  request to send
   * @param callback callback that gets executed when response arrives, null allowed
   * @return future with response from this request
   */
  public CompletableFuture<Void> sendRequestAsync(@NotNull MatomoRequest request, @Nullable Consumer<Void> callback) {
    if (trackerConfiguration.isEnabled()) {
      validate(request);
      log.debug("Sending async request via GET: {}", request);
      CompletableFuture<Void> future = sender.sendSingleAsync(request);
      if (callback != null) {
        return future.thenAccept(callback);
      }
      return future;
    }
    log.warn("Not sending request, because tracker is disabled");
    return CompletableFuture.completedFuture(null);
  }

  private void validate(@NotNull MatomoRequest request) {
    if (trackerConfiguration.getDefaultSiteId() == null && request.getSiteId() == null) {
      throw new IllegalArgumentException("No default site ID and no request site ID is given");
    }
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @deprecated use sendBulkRequestAsync instead
   */
  @Deprecated
  public void sendBulkRequest(@NonNull Iterable<? extends MatomoRequest> requests) {
    sendBulkRequest(requests, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken, null allowed
   * @deprecated use sendBulkRequestAsync instead
   */
  @Deprecated
  public void sendBulkRequest(@NonNull Iterable<? extends MatomoRequest> requests, @Nullable String authToken) {
    if (trackerConfiguration.isEnabled()) {
      for (MatomoRequest request : requests) {
        validate(request);
      }
      log.debug("Sending requests via POST: {}", requests);
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
   * @return future with response from these requests
   */
  public CompletableFuture<Void> sendBulkRequestAsync(@NotNull Iterable<? extends MatomoRequest> requests) {
    return sendBulkRequestAsync(requests, null, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken, null allowed
   * @param callback  callback that gets executed when response arrives, null allowed
   * @return the response from these requests
   */
  public CompletableFuture<Void> sendBulkRequestAsync(
    @NotNull Iterable<? extends MatomoRequest> requests, @Nullable String authToken, @Nullable Consumer<Void> callback
  ) {
    if (trackerConfiguration.isEnabled()) {
      for (MatomoRequest request : requests) {
        validate(request);
      }
      log.debug("Sending async requests via POST: {}", requests);
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
   * @return future with response from these requests
   */
  public CompletableFuture<Void> sendBulkRequestAsync(
    @NotNull Iterable<? extends MatomoRequest> requests, @Nullable Consumer<Void> callback
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
   * @return the response from these requests
   */
  public CompletableFuture<Void> sendBulkRequestAsync(
    @NotNull Iterable<? extends MatomoRequest> requests, @Nullable String authToken
  ) {
    return sendBulkRequestAsync(requests, authToken, null);
  }
}
