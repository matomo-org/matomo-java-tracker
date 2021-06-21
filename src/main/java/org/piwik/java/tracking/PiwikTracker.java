/*
 * Piwik Java Tracker
 *
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

/**
 * A class that sends {@link PiwikRequest}s to a specified Piwik server.
 *
 * @author brettcsorba
 */
public class PiwikTracker {
  private static final String AUTH_TOKEN = "token_auth";
  private static final String REQUESTS = "requests";
  private static final int DEFAULT_TIMEOUT = 5000;
  private final URIBuilder uriBuilder;
  private final int timeout;
  private final String proxyHost;
  private final int proxyPort;

  /**
   * Creates a tracker that will send {@link PiwikRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   * <strong>http://your-piwik-domain.tld/piwik.php</strong>.
   */
  public PiwikTracker(final String hostUrl) {
    this(hostUrl, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a tracker that will send {@link PiwikRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   * <strong>http://your-piwik-domain.tld/piwik.php</strong>.
   * @param timeout the timeout of the sent request in milliseconds
   */
  public PiwikTracker(final String hostUrl, final int timeout) {
    uriBuilder = new URIBuilder(URI.create(hostUrl));
    this.timeout = timeout;
    this.proxyHost = null;
    this.proxyPort = 0;
  }

  /**
   * Creates a tracker that will send {@link PiwikRequest}s to the specified
   * Tracking HTTP API endpoint via the provided proxy
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   * <strong>http://your-piwik-domain.tld/piwik.php</strong>.
   * @param proxyHost url endpoint for the proxy
   * @param proxyPort proxy server port number
   */
  public PiwikTracker(final String hostUrl, final String proxyHost, final int proxyPort) {
    this(hostUrl, proxyHost, proxyPort, DEFAULT_TIMEOUT);
  }

  public PiwikTracker(final String hostUrl, final String proxyHost, final int proxyPort, final int timeout) {
    uriBuilder = new URIBuilder(URI.create(hostUrl));
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
    this.timeout = timeout;
  }

  /**
   * Send a request.
   *
   * @param request request to send
   * @return the response from this request
   * @throws IOException thrown if there was a problem with this connection
   * @deprecated use sendRequestAsync instead
   */
  @Deprecated
  public HttpResponse sendRequest(final PiwikRequest request) throws IOException {
    final HttpClient client = getHttpClient();
    uriBuilder.setCustomQuery(request.getQueryString());
    HttpGet get = null;

    try {
      get = new HttpGet(uriBuilder.build());
      return client.execute(get);
    } catch (final URISyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * Send a request.
   *
   * @param request request to send
   * @return future with response from this request
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendRequestAsync(final PiwikRequest request) throws IOException {
    final CloseableHttpAsyncClient client = getHttpAsyncClient();
    client.start();
    uriBuilder.setCustomQuery(request.getQueryString());
    HttpGet get = null;

    try {
      get = new HttpGet(uriBuilder.build());
      return client.execute(get, null);
    } catch (final URISyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @return the response from these requests
   * @throws IOException thrown if there was a problem with this connection
   * @deprecated use sendBulkRequestAsync instead
   */
  @Deprecated
  public HttpResponse sendBulkRequest(final Iterable<PiwikRequest> requests) throws IOException {
    return sendBulkRequest(requests, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @return future with response from these requests
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendBulkRequestAsync(final Iterable<PiwikRequest> requests) throws IOException {
    return sendBulkRequestAsync(requests, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests the requests to send
   * @param authToken specify if any of the parameters use require AuthToken
   * @return the response from these requests
   * @throws IOException thrown if there was a problem with this connection
   * @deprecated use sendBulkRequestAsync instead
   */
  @Deprecated
  public HttpResponse sendBulkRequest(final Iterable<PiwikRequest> requests, final String authToken) throws IOException {
    if (authToken != null && authToken.length() != PiwikRequest.AUTH_TOKEN_LENGTH) {
      throw new IllegalArgumentException(authToken + " is not " + PiwikRequest.AUTH_TOKEN_LENGTH + " characters long.");
    }

    final JsonObjectBuilder ob = Json.createObjectBuilder();
    final JsonArrayBuilder ab = Json.createArrayBuilder();

    for (final PiwikRequest request : requests) {
      ab.add("?" + request.getQueryString());
    }

    ob.add(REQUESTS, ab);

    if (authToken != null) {
      ob.add(AUTH_TOKEN, authToken);
    }

    final HttpClient client = getHttpClient();
    HttpPost post = null;

    try {
      post = new HttpPost(uriBuilder.build());
      post.setEntity(new StringEntity(ob.build().toString(),
          ContentType.APPLICATION_JSON));
      return client.execute(post);
    } catch (final URISyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests the requests to send
   * @param authToken specify if any of the parameters use require AuthToken
   * @return the response from these requests
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendBulkRequestAsync(final Iterable<PiwikRequest> requests, final String authToken) throws IOException {
    if (authToken != null && authToken.length() != PiwikRequest.AUTH_TOKEN_LENGTH) {
      throw new IllegalArgumentException(authToken + " is not " + PiwikRequest.AUTH_TOKEN_LENGTH + " characters long.");
    }

    final JsonObjectBuilder ob = Json.createObjectBuilder();
    final JsonArrayBuilder ab = Json.createArrayBuilder();

    for (final PiwikRequest request : requests) {
      ab.add("?" + request.getQueryString());
    }

    ob.add(REQUESTS, ab);

    if (authToken != null) {
      ob.add(AUTH_TOKEN, authToken);
    }

    final CloseableHttpAsyncClient client = getHttpAsyncClient();
    client.start();
    HttpPost post = null;

    try {
      post = new HttpPost(uriBuilder.build());
      post.setEntity(new StringEntity(ob.build().toString(),
          ContentType.APPLICATION_JSON));
      return client.execute(post, null);
    } catch (final URISyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * Get a HTTP client. With proxy if a proxy is provided in the constructor.
   *
   * @return a HTTP client
   */
  protected HttpClient getHttpClient() {
      return HttpClientFactory.getInstanceFor(proxyHost, proxyPort, timeout);
  }

  /**
   * Get an async HTTP client. With proxy if a proxy is provided in the constructor.
   *
   * @return an async HTTP client
   */
  protected CloseableHttpAsyncClient getHttpAsyncClient() {
    return HttpClientFactory.getAsyncInstanceFor(proxyHost, proxyPort, timeout);
  }
}
