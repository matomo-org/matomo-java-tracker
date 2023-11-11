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
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link Sender} implementation that uses Java 8 {@link HttpURLConnection} to send requests to the Matomo.
 *
 * <p>This implementation uses a thread pool to send requests asynchronously. The thread pool is configured using
 * {@link TrackerConfiguration#getThreadPoolSize()}. The thread pool uses daemon threads. This means that the JVM will
 * exit even if the thread pool is not shut down.
 *
 * <p>If you use a newer Java version, please use the newer Java implementation from the Matomo Java Tracker for
 * Java 17.
 */
@Slf4j
@RequiredArgsConstructor
class Java8Sender implements Sender {

  private static final TrustManager[] TRUST_ALL_MANAGERS = {new TrustingX509TrustManager()};
  private static final HostnameVerifier TRUSTING_HOSTNAME_VERIFIER = new TrustingHostnameVerifier();

  private final TrackerConfiguration trackerConfiguration;

  private final QueryCreator queryCreator;

  private final Executor executor;
  private final Collection<String> queries = new ArrayList<>(16);

  @Override
  @NonNull
  public CompletableFuture<MatomoRequest> sendSingleAsync(
      @NonNull MatomoRequest request
  ) {
    return CompletableFuture.supplyAsync(() -> {
      sendSingle(request);
      return request;
    }, executor);
  }

  @Override
  public void sendSingle(
      @NonNull MatomoRequest request
  ) {
    String authToken = AuthToken.determineAuthToken(null, singleton(request), trackerConfiguration);
    RequestValidator.validate(request, authToken);
    HttpURLConnection connection;
    URI apiEndpoint = trackerConfiguration.getApiEndpoint();
    try {
      connection = openConnection(apiEndpoint
          .resolve(String.format("%s?%s", apiEndpoint.getPath(), queryCreator.createQuery(request, authToken)))
          .toURL());
    } catch (MalformedURLException e) {
      throw new InvalidUrlException(e);
    }
    applyTrackerConfiguration(connection);
    setUserAgentProperty(connection, request.getHeaderUserAgent(), request.getHeaders());
    addHeaders(connection, request.getHeaders());
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

  private void addHeaders(@NonNull HttpURLConnection connection, @Nullable Map<String, String> headers) {
    if (headers != null) {
      for (Map.Entry<String, String> header : headers.entrySet()) {
        connection.setRequestProperty(header.getKey(), header.getValue());
      }
    }
  }

  private HttpURLConnection openConnection(URL url) {
    HttpURLConnection connection;
    try {
      if (isEmpty(trackerConfiguration.getProxyHost()) || trackerConfiguration.getProxyPort() <= 0) {
        log.debug("Proxy host or proxy port not configured. Will create connection without proxy");
        connection = (HttpURLConnection) url.openConnection();
      } else {
        connection = openProxiedConnection(url);
      }
    } catch (IOException e) {
      throw new MatomoException("Could not open connection", e);
    }
    if (connection instanceof HttpsURLConnection) {
      applySslConfiguration((HttpsURLConnection) connection);
    }
    return connection;
  }

  private void applySslConfiguration(
      @NonNull @lombok.NonNull HttpsURLConnection connection
  ) {
    if (trackerConfiguration.isDisableSslCertValidation()) {
      try {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, TRUST_ALL_MANAGERS, new SecureRandom());
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
      } catch (Exception e) {
        throw new MatomoException("Could not disable SSL certification validation", e);
      }
    }
    if (trackerConfiguration.isDisableSslHostVerification()) {
      connection.setHostnameVerifier(TRUSTING_HOSTNAME_VERIFIER);
    }
  }

  private HttpURLConnection openProxiedConnection(
      @NonNull @lombok.NonNull URL url
  ) throws IOException {
    requireNonNull(trackerConfiguration.getProxyHost(), "Proxy host must not be null");
    if (trackerConfiguration.getProxyPort() <= 0) {
      throw new IllegalArgumentException("Proxy port must be configured");
    }
    InetSocketAddress proxyAddress =
        new InetSocketAddress(trackerConfiguration.getProxyHost(), trackerConfiguration.getProxyPort());
    Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
    if (!isEmpty(trackerConfiguration.getProxyUsername()) && !isEmpty(trackerConfiguration.getProxyPassword())) {
      Authenticator.setDefault(new ProxyAuthenticator(trackerConfiguration.getProxyUsername(),
          trackerConfiguration.getProxyPassword()
      ));
    }
    log.debug("Using proxy {} on port {}", trackerConfiguration.getProxyHost(), trackerConfiguration.getProxyPort());
    return (HttpURLConnection) url.openConnection(proxy);
  }

  private void applyTrackerConfiguration(HttpURLConnection connection) {
    connection.setUseCaches(false);
    if (trackerConfiguration.getConnectTimeout() != null) {
      connection.setConnectTimeout((int) trackerConfiguration.getConnectTimeout().toMillis());
    }
    if (trackerConfiguration.getSocketTimeout() != null) {
      connection.setReadTimeout((int) trackerConfiguration.getSocketTimeout().toMillis());
    }
  }

  private void setUserAgentProperty(
      @NonNull HttpURLConnection connection, @Nullable String headerUserAgent, @Nullable Map<String, String> headers
  ) {
    String userAgentHeader = null;
    if ((headerUserAgent == null || headerUserAgent.trim().isEmpty()) && headers != null) {
      TreeMap<String, String> caseInsensitiveMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
      caseInsensitiveMap.putAll(headers);
      userAgentHeader = caseInsensitiveMap.get("User-Agent");
    }
    if ((userAgentHeader == null || userAgentHeader.trim().isEmpty())
        && (headerUserAgent == null || headerUserAgent.trim().isEmpty())
        && trackerConfiguration.getUserAgent() != null && !trackerConfiguration.getUserAgent().isEmpty()) {
      connection.setRequestProperty("User-Agent", trackerConfiguration.getUserAgent());
    }
  }

  private void checkResponse(HttpURLConnection connection) throws IOException {
    int responseCode = connection.getResponseCode();
    if (responseCode > 399) {
      if (trackerConfiguration.isLogFailedTracking()) {
        log.error("Received HTTP error code {} for URL {}", responseCode, connection.getURL());
      }
      throw new MatomoException(String.format("Tracking endpoint responded with code %d", responseCode));
    }
  }

  private static boolean isEmpty(
      @Nullable String str
  ) {
    return str == null || str.isEmpty() || str.trim().isEmpty();
  }

  @Override
  public void sendBulk(
      @NonNull @lombok.NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    String authToken = AuthToken.determineAuthToken(overrideAuthToken, requests, trackerConfiguration);
    Collection<String> queries = new ArrayList<>();
    Map<String, String> headers = new LinkedHashMap<>();
    String headerUserAgent = null;
    for (MatomoRequest request : requests) {
      RequestValidator.validate(request, authToken);
      if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
        headers.putAll(request.getHeaders());
      }
      if (headerUserAgent == null && request.getHeaderUserAgent() != null
          && !request.getHeaderUserAgent().trim().isEmpty()) {
        headerUserAgent = request.getHeaderUserAgent();
      }
      queries.add(queryCreator.createQuery(request, null));
    }
    sendBulk(queries, authToken, headers, headerUserAgent);
  }

  private void sendBulk(
      @NonNull @lombok.NonNull Collection<String> queries,
      @Nullable String authToken,
      Map<String, String> headers,
      String headerUserAgent
  ) {
    if (queries.isEmpty()) {
      throw new IllegalArgumentException("Queries must not be empty");
    }
    HttpURLConnection connection;
    try {
      connection = openConnection(trackerConfiguration.getApiEndpoint().toURL());
    } catch (MalformedURLException e) {
      throw new InvalidUrlException(e);
    }
    preparePostConnection(connection);
    applyTrackerConfiguration(connection);
    setUserAgentProperty(connection, headerUserAgent, headers);
    addHeaders(connection, headers);
    log.debug("Sending bulk request using URI {} asynchronously", trackerConfiguration.getApiEndpoint());
    OutputStream outputStream = null;
    try {
      connection.connect();
      outputStream = connection.getOutputStream();
      outputStream.write(BulkRequest.builder().queries(queries).authToken(authToken).build().toBytes());
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

  @Override
  @NonNull
  public CompletableFuture<Void> sendBulkAsync(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    String authToken = AuthToken.determineAuthToken(overrideAuthToken, requests, trackerConfiguration);
    final Map<String, String> headers = new LinkedHashMap<>();
    String headerUserAgent = findHeaderUserAgent(requests);
    synchronized (queries) {
      for (MatomoRequest request : requests) {
        RequestValidator.validate(request, authToken);
        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
          headers.putAll(request.getHeaders());
        }
        String query = queryCreator.createQuery(request, null);
        queries.add(query);
      }
    }
    return CompletableFuture.supplyAsync(() -> sendBulkAsync(authToken, headers, headerUserAgent), executor);
  }

  @Nullable
  private Void sendBulkAsync(
      @Nullable String authToken, Map<String, String> headers, String headerUserAgent
  ) {
    synchronized (queries) {
      if (!queries.isEmpty()) {
        sendBulk(queries, authToken, headers, headerUserAgent);
        queries.clear();
      }
      return null;
    }
  }

  @Nullable
  private static String findHeaderUserAgent(@NonNull Iterable<? extends MatomoRequest> requests) {
    for (MatomoRequest request : requests) {
      if (request.getHeaderUserAgent() != null && !request.getHeaderUserAgent().trim().isEmpty()) {
        return request.getHeaderUserAgent();
      }
    }
    return null;
  }

}
