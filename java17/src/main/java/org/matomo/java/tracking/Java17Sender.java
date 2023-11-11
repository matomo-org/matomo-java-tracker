package org.matomo.java.tracking;

import static java.util.Collections.singleton;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link Sender} implementation that uses the Java 17 HTTP client.
 */
@RequiredArgsConstructor
@Slf4j
public class Java17Sender implements Sender {

  @lombok.NonNull
  private final TrackerConfiguration trackerConfiguration;

  @lombok.NonNull
  private final QueryCreator queryCreator;

  @lombok.NonNull
  private final HttpClient httpClient;

  @NonNull
  @Override
  public CompletableFuture<MatomoRequest> sendSingleAsync(@NonNull @lombok.NonNull MatomoRequest request) {
    return sendAsyncAndCheckResponse(buildHttpGetRequest(request), request);
  }

  @Override
  public void sendSingle(@NonNull @lombok.NonNull MatomoRequest request) {
    sendAndCheckResponse(buildHttpGetRequest(request));
  }

  private void sendAndCheckResponse(@NonNull HttpRequest httpRequest) {
    checkResponse(send(httpRequest, () -> httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding())),
        httpRequest
    );
  }

  @Override
  public void sendBulk(
      @NonNull @lombok.NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    sendAndCheckResponse(buildHttpPostRequest(requests, overrideAuthToken));
  }

  @NonNull
  private HttpRequest buildHttpPostRequest(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    String authToken = AuthToken.determineAuthToken(overrideAuthToken, requests, trackerConfiguration);
    Collection<String> queries = new ArrayList<>();
    Map<String, String> headers = new LinkedHashMap<>(10);
    for (MatomoRequest request : requests) {
      RequestValidator.validate(request, authToken);
      if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
        headers.putAll(request.getHeaders());
      }
      queries.add(queryCreator.createQuery(request, null));
    }
    HttpRequest.Builder builder = HttpRequest
        .newBuilder()
        .uri(trackerConfiguration.getApiEndpoint())
        .header("Accept", "*/*")
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofByteArray(BulkRequest
            .builder()
            .queries(queries)
            .authToken(authToken)
            .build()
            .toBytes()));
    applyTrackerConfiguration(builder);
    addHeaders(builder, headers);
    return builder.build();
  }

  @NonNull
  @Override
  public CompletableFuture<Void> sendBulkAsync(
      @NonNull @lombok.NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    return sendAsyncAndCheckResponse(buildHttpPostRequest(requests, overrideAuthToken), null);
  }

  @NonNull
  private HttpRequest buildHttpGetRequest(@NonNull MatomoRequest request) {
    String authToken = AuthToken.determineAuthToken(null, singleton(request), trackerConfiguration);
    RequestValidator.validate(request, authToken);
    URI apiEndpoint = trackerConfiguration.getApiEndpoint();
    HttpRequest.Builder builder = HttpRequest
        .newBuilder()
        .uri(apiEndpoint.resolve(String.format("%s?%s",
            apiEndpoint.getPath(),
            queryCreator.createQuery(request, authToken)
        )));
    applyTrackerConfiguration(builder);
    addHeaders(builder, request.getHeaders());
    return builder.build();
  }

  private void addHeaders(@NonNull HttpRequest.Builder builder, @Nullable Map<String, String> headers) {
    if (headers != null) {
      for (Map.Entry<String, String> header : headers.entrySet()) {
        builder.header(header.getKey(), header.getValue());
      }
    }
  }

  @NonNull
  private <T> CompletableFuture<T> sendAsyncAndCheckResponse(
      @NonNull HttpRequest httpRequest, @Nullable T result
  ) {
    return send(httpRequest,
        () -> httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding()).thenApply(response -> {
          checkResponse(response, httpRequest);
          return result;
        })
    );
  }

  private void applyTrackerConfiguration(@NonNull HttpRequest.Builder builder) {
    if (trackerConfiguration.getSocketTimeout() != null && trackerConfiguration.getSocketTimeout().toMillis() > 0L) {
      builder.timeout(trackerConfiguration.getSocketTimeout());
    }
    if (trackerConfiguration.getUserAgent() != null && !trackerConfiguration.getUserAgent().isEmpty()) {
      builder.header("User-Agent", trackerConfiguration.getUserAgent());
    }
  }

  private <T> T send(
      @NonNull HttpRequest httpRequest, @NonNull Callable<T> callable
  ) {
    try {
      return callable.call();
    } catch (Exception e) {
      if (trackerConfiguration.isLogFailedTracking()) {
        log.error("Could not send request to Matomo: {}", httpRequest.uri(), e);
      }
      throw new MatomoException("Could not send request to Matomo", e);
    }
  }

  private void checkResponse(@NonNull HttpResponse<Void> response, @NonNull HttpRequest httpRequest) {
    if (response.statusCode() > 399) {
      if (trackerConfiguration.isLogFailedTracking()) {
        log.error("Received HTTP error code {} for URL {}", response.statusCode(), httpRequest.uri());
      }
      throw new MatomoException(String.format("Tracking endpoint responded with code %d", response.statusCode()));
    }
  }
}
