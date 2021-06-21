package org.piwik.java.tracking;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

/**
 * Internal factory for providing instances of HTTP clients.
 * Especially {@linkAsyncHttpClient} instances are intended to be global resources that share the same lifecycle as the application.
 * For details see <a href='https://github.com/AsyncHttpClient/async-http-client'>Apache documentation</a>.
 *
 * @author norbertroamsys
 */
class HttpClientFactory {

    /**
     * Internal key class for caching {@link CloseableHttpAsyncClient} instances.
     */
    private static final class KeyEntry {

        private final String proxyHost;
        private final int proxyPort, timeout;

        public KeyEntry(final String proxyHost, final int proxyPort, final int timeout) {
            this.proxyHost = proxyHost;
            this.proxyPort = proxyPort;
            this.timeout = timeout;
        }

        @Override
        public int hashCode() {
            return Objects.hash(proxyHost, proxyPort, timeout);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (obj instanceof KeyEntry) {
                final KeyEntry other = (KeyEntry) obj;
                return Objects.equals(proxyHost, other.proxyHost) && proxyPort == other.proxyPort && timeout == other.timeout;
            } else {
                return false;
            }
        }
    }

    private static final Map<KeyEntry, CloseableHttpAsyncClient> ASYNC_INSTANCES = new HashMap<>();

    /**
     * Factory for getting a synchronous client by proxy and timeout configuration.
     * The clients will be created on each call.
     *
     * @param proxyHost the proxy host
     * @param proxyPort the proxy port
     * @param timeout the timeout
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
     * @param timeout the timeout
     * @return the created client
     */
    public static synchronized CloseableHttpAsyncClient getAsyncInstanceFor(final String proxyHost, final int proxyPort, final int timeout) {
        return ASYNC_INSTANCES.computeIfAbsent(new KeyEntry(proxyHost, proxyPort, timeout), key ->
            HttpAsyncClientBuilder.create().setRoutePlanner(createRoutePlanner(key.proxyHost, key.proxyPort)).setDefaultRequestConfig(createRequestConfig(key.timeout)).build());
    }

    private static DefaultProxyRoutePlanner createRoutePlanner(final String proxyHost, final int proxyPort) {
        if (proxyHost != null && proxyPort != 0) {
            final HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            final DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            return routePlanner;
        } else {
            return null;
        }
    }

    private static RequestConfig createRequestConfig(final int timeout) {
        final RequestConfig.Builder config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout);
        return config.build();
    }

}
