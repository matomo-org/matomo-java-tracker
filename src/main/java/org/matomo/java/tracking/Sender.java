/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class Sender {

  private final TrackerConfiguration trackerConfiguration;

  private final QueryCreator queryCreator;

  private final Collection<String> queries = new ArrayList<>(16);

  private final Executor executor;

  @NonNull
  CompletableFuture<Void> sendSingleAsync(
      @NonNull
      MatomoRequest request
  ) {
    return CompletableFuture.supplyAsync(() -> {
      sendSingle(request);
      return null;
    }, executor);
  }

  void sendSingle(
      @NonNull
      MatomoRequest request
  ) {
    String authToken = AuthToken.determineAuthToken(null, singleton(request), trackerConfiguration);
    RequestValidator.validate(request, authToken);
    HttpURLConnection connection;
    URI apiEndpoint = trackerConfiguration.getApiEndpoint();
    try {
      connection = openConnection(apiEndpoint
          .resolve(String.format("%s?%s",
              apiEndpoint.getPath(),
              queryCreator.createQuery(request, authToken)
          ))
          .toURL());
    } catch (MalformedURLException e) {
      throw new InvalidUrlException(e);
    }
    configureAgentsAndTimeouts(connection);
    log.debug("Sending single request using URI {} asynchronously", apiEndpoint);
    try {
      connection.connect();
      checkResponse(connection);
    } catch (IOException e) {
      throw new MatomoException("Could not send request via GET", e);
    } finally {
      connection.disconnect();
    }
  }

  private HttpURLConnection openConnection(URL url) {
    try {
      if (isEmpty(trackerConfiguration.getProxyHost())
          || trackerConfiguration.getProxyPort() <= 0) {
        log.debug("Proxy host or proxy port not configured. Will create connection without proxy");
        return (HttpURLConnection) url.openConnection();
      }
      InetSocketAddress proxyAddress = new InetSocketAddress(
          trackerConfiguration.getProxyHost(),
          trackerConfiguration.getProxyPort()
      );
      Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
      if (!isEmpty(trackerConfiguration.getProxyUserName())
          && !isEmpty(trackerConfiguration.getProxyPassword())) {
        Authenticator.setDefault(new ProxyAuthenticator(
            trackerConfiguration.getProxyUserName(),
            trackerConfiguration.getProxyPassword()
        ));
      }
      return (HttpURLConnection) url.openConnection(proxy);
    } catch (IOException e) {
      throw new MatomoException("Could not open connection", e);
    }
  }

  private void configureAgentsAndTimeouts(HttpURLConnection connection) {
    connection.setUseCaches(false);
    connection.setRequestProperty("User-Agent", trackerConfiguration.getUserAgent());
    if (trackerConfiguration.getConnectTimeout() != null) {
      connection.setConnectTimeout((int) trackerConfiguration.getConnectTimeout().toMillis());
    }
    if (trackerConfiguration.getSocketTimeout() != null) {
      connection.setReadTimeout((int) trackerConfiguration.getSocketTimeout().toMillis());
    }
  }

  private void checkResponse(HttpURLConnection connection) throws IOException {
    if (connection.getResponseCode() > 399) {
      if (trackerConfiguration.isLogFailedTracking()) {
        log.error("Received error code {}", connection.getResponseCode());
      }
      throw new MatomoException(
          "Tracking endpoint responded with code " + connection.getResponseCode());
    }
  }

  private static boolean isEmpty(
      @Nullable
      String str
  ) {
    return str == null || str.isEmpty() || str.trim().isEmpty();
  }

  void sendBulk(
      @NonNull
      Iterable<? extends MatomoRequest> requests,
      @Nullable
      String overrideAuthToken
  ) {
    String authToken =
        AuthToken.determineAuthToken(overrideAuthToken, requests, trackerConfiguration);
    sendBulk(StreamSupport.stream(requests.spliterator(), false).map(request -> {
      RequestValidator.validate(request, authToken);
      return queryCreator.createQuery(request, null);
    }).collect(Collectors.toList()), authToken);
  }

  private void sendBulk(
      @NonNull
      Collection<String> queries,
      @Nullable
      String authToken
  ) {
    requireNonNull(queries, "Queries must not be null");
    HttpURLConnection connection;
    try {
      connection = openConnection(trackerConfiguration.getApiEndpoint().toURL());
    } catch (MalformedURLException e) {
      throw new InvalidUrlException(e);
    }
    preparePostConnection(connection);
    configureAgentsAndTimeouts(connection);
    log.debug(
        "Sending bulk request using URI {} asynchronously",
        trackerConfiguration.getApiEndpoint()
    );
    OutputStream outputStream = null;
    try {
      connection.connect();
      outputStream = connection.getOutputStream();
      outputStream.write(createPayload(queries, authToken));
      outputStream.flush();
      checkResponse(connection);
    } catch (IOException e) {
      throw new MatomoException("Could not send requests via POST", e);
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          // ignore
        }
      }
      connection.disconnect();
    }
  }

  private static void preparePostConnection(HttpURLConnection connection) {
    try {
      connection.setRequestMethod("POST");
    } catch (ProtocolException e) {
      throw new MatomoException("Could not set request method", e);
    }
    connection.setDoOutput(true);
    connection.setRequestProperty("Accept", "*/*");
    connection.setRequestProperty("Content-Type", "application/json");

  }

  private static byte[] createPayload(
      @NonNull
      Collection<String> queries,
      @Nullable
      String authToken
  ) {
    requireNonNull(queries, "Queries must not be null");
    StringBuilder payload = new StringBuilder("{\"requests\":[");
    Iterator<String> iterator = queries.iterator();
    while (iterator.hasNext()) {
      String query = iterator.next();
      payload.append("\"?").append(query).append('"');
      if (iterator.hasNext()) {
        payload.append(',');
      }
    }
    payload.append(']');
    if (authToken != null) {
      payload.append(",\"token_auth\":\"").append(authToken).append('"');
    }
    return payload.append('}').toString().getBytes(StandardCharsets.UTF_8);
  }

  @NonNull
  CompletableFuture<Void> sendBulkAsync(
      @NonNull
      Iterable<? extends MatomoRequest> requests,
      @Nullable
      String overrideAuthToken
  ) {
    String authToken =
        AuthToken.determineAuthToken(overrideAuthToken, requests, trackerConfiguration);
    synchronized (queries) {
      for (MatomoRequest request : requests) {
        RequestValidator.validate(request, authToken);
        String query = queryCreator.createQuery(request, null);
        queries.add(query);
      }
    }
    return CompletableFuture.supplyAsync(() -> sendBulkAsync(authToken), executor);
  }

  @Nullable
  private Void sendBulkAsync(
      @Nullable
      String authToken
  ) {
    synchronized (queries) {
      if (!queries.isEmpty()) {
        sendBulk(queries, authToken);
        queries.clear();
      }
      return null;
    }
  }

}
