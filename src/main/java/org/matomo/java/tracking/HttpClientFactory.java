package org.matomo.java.tracking;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Internal factory for providing instances of HTTP clients.
 * Especially {@link org.apache.http.nio.client.HttpAsyncClient} instances are intended to be global resources that share the same lifecycle as the application.
 * For details see <a href='https://github.com/AsyncHttpClient/async-http-client'>Apache documentation</a>.
 *
 * @author norbertroamsys
 */
final class HttpClientFactory {

  private HttpClientFactory() {
    // utility
  }

  /**
   * Internal key class for caching {@link CloseableHttpAsyncClient} instances.
   */
  @EqualsAndHashCode
  @AllArgsConstructor
  private static final class KeyEntry {

    private final String proxyHost;
    private final int proxyPort;
    private final int timeout;

  }

  private static final Map<KeyEntry, CloseableHttpAsyncClient> ASYNC_INSTANCES = new HashMap<>();

  /**
   * Factory for getting a synchronous client by proxy and timeout configuration.
   * The clients will be created on each call.
   *
   * @param proxyHost the proxy host
   * @param proxyPort the proxy port
   * @param timeout   the timeout
   * @return the created client
   */
  public static HttpClient getInstanceFor(final String proxyHost, final int proxyPort, final int timeout) {
    return HttpClientBuilder.create().setRoutePlanner(createRoutePlanner(proxyHost, proxyPort)).setDefaultRequestConfig(createRequestConfig(timeout)).build();
  }

  /**
   * Factory for getting a asynchronous client by proxy and timeout configuration.
   * The clients will be created and cached as a singleton instance.
   *
   * @param proxyHost the proxy host
   * @param proxyPort the proxy port
   * @param timeout   the timeout
   * @return the created client
   */
  public static synchronized CloseableHttpAsyncClient getAsyncInstanceFor(final String proxyHost, final int proxyPort, final int timeout) {
    return ASYNC_INSTANCES.computeIfAbsent(new KeyEntry(proxyHost, proxyPort, timeout), key ->
      HttpAsyncClientBuilder.create().setRoutePlanner(createRoutePlanner(key.proxyHost, key.proxyPort)).setDefaultRequestConfig(createRequestConfig(key.timeout)).build());
  }

  @Nullable
  private static DefaultProxyRoutePlanner createRoutePlanner(final String proxyHost, final int proxyPort) {
    if (proxyHost != null && proxyPort != 0) {
      final HttpHost proxy = new HttpHost(proxyHost, proxyPort);
      return new DefaultProxyRoutePlanner(proxy);
    }
    return null;
  }

  private static RequestConfig createRequestConfig(final int timeout) {
    final RequestConfig.Builder config = RequestConfig.custom()
      .setConnectTimeout(timeout)
      .setConnectionRequestTimeout(timeout)
      .setSocketTimeout(timeout);
    return config.build();
  }

}
