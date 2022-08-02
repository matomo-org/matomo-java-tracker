/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;

/**
 * A class that sends {@link MatomoRequest}s to a specified Matomo server.
 *
 * @author brettcsorba
 */
@Slf4j
public class MatomoTracker {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final String AUTH_TOKEN = "token_auth";
  private static final String REQUESTS = "requests";
  private static final int DEFAULT_TIMEOUT = 5000;
  private final URI hostUrl;
  private final int timeout;
  private final String proxyHost;
  private final int proxyPort;

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   *                <strong>http://your-matomo-domain.tld/matomo.php</strong>.
   */
  public MatomoTracker(final String hostUrl) {
    this(hostUrl, DEFAULT_TIMEOUT);
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint.
   *
   * @param hostUrl url endpoint to send requests to.  Usually in the format
   *                <strong>http://your-matomo-domain.tld/matomo.php</strong>.
   * @param timeout the timeout of the sent request in milliseconds
   */
  public MatomoTracker(final String hostUrl, final int timeout) {
    this(hostUrl, null, 0, timeout);
  }

  public MatomoTracker(final String hostUrl, final String proxyHost, final int proxyPort, final int timeout) {
    this.hostUrl = URI.create(hostUrl);
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
    this.timeout = timeout;
  }

  /**
   * Creates a tracker that will send {@link MatomoRequest}s to the specified
   * Tracking HTTP API endpoint via the provided proxy
   *
   * @param hostUrl   url endpoint to send requests to.  Usually in the format
   *                  <strong>http://your-matomo-domain.tld/matomo.php</strong>.
   * @param proxyHost url endpoint for the proxy
   * @param proxyPort proxy server port number
   */
  public MatomoTracker(final String hostUrl, final String proxyHost, final int proxyPort) {
    this(hostUrl, proxyHost, proxyPort, DEFAULT_TIMEOUT);
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
  public HttpResponse sendRequest(final MatomoRequest request) throws IOException {
    final HttpClient client = getHttpClient();
    try {
      HttpUriRequest get = new HttpGet(new URIBuilder(hostUrl).addParameters(QueryParameters.fromMap(request.getParameters())).build());
      log.debug("Sending request via GET: {}", request);
      return client.execute(get);
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
   * Send a request.
   *
   * @param request request to send
   * @return future with response from this request
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendRequestAsync(final MatomoRequest request) throws IOException {
    return sendRequestAsync(request, null);
  }

  /**
   * Send a request.
   *
   * @param request  request to send
   * @param callback callback that gets executed when response arrives
   * @return future with response from this request
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendRequestAsync(final MatomoRequest request, FutureCallback<HttpResponse> callback) throws IOException {
    final CloseableHttpAsyncClient client = getHttpAsyncClient();
    client.start();

    try {
      HttpUriRequest get = new HttpGet(new URIBuilder(hostUrl).addParameters(QueryParameters.fromMap(request.getParameters())).build());
      log.debug("Sending async request via GET: {}", request);
      return client.execute(get, callback);
    } catch (final URISyntaxException e) {
      throw new IOException(e);
    }
  }

  /**
   * Get an async HTTP client. With proxy if a proxy is provided in the constructor.
   *
   * @return an async HTTP client
   */
  protected CloseableHttpAsyncClient getHttpAsyncClient() {
    return HttpClientFactory.getAsyncInstanceFor(proxyHost, proxyPort, timeout);
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
  public HttpResponse sendBulkRequest(final Iterable<? extends MatomoRequest> requests) throws IOException {
    return sendBulkRequest(requests, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken
   * @return the response from these requests
   * @throws IOException thrown if there was a problem with this connection
   * @deprecated use sendBulkRequestAsync instead
   */
  @Deprecated
  public HttpResponse sendBulkRequest(final Iterable<? extends MatomoRequest> requests, final String authToken) throws IOException {
    if (authToken != null && authToken.length() != MatomoRequest.AUTH_TOKEN_LENGTH) {
      throw new IllegalArgumentException(authToken + " is not " + MatomoRequest.AUTH_TOKEN_LENGTH + " characters long.");
    }

    HttpPost post = buildPost(requests, authToken);
    final HttpClient client = getHttpClient();
    log.debug("Sending requests via POST: {}", requests);
    return client.execute(post);
  }

  private HttpPost buildPost(Iterable<? extends MatomoRequest> requests, String authToken) {
    ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
    ArrayNode requestsNode = objectNode.putArray(REQUESTS);
    for (final MatomoRequest request : requests) {
      requestsNode.add(new URIBuilder().addParameters(QueryParameters.fromMap(request.getParameters())).toString());
    }
    if (authToken != null) {
      objectNode.put(AUTH_TOKEN, authToken);
    }
    HttpPost post = new HttpPost(hostUrl);
    post.setEntity(new StringEntity(objectNode.toString(), ContentType.APPLICATION_JSON));
    return post;
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @return future with response from these requests
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendBulkRequestAsync(final Iterable<? extends MatomoRequest> requests) throws IOException {
    return sendBulkRequestAsync(requests, null, null);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken
   * @param callback  callback that gets executed when response arrives
   * @return the response from these requests
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendBulkRequestAsync(final Iterable<? extends MatomoRequest> requests, final String authToken, FutureCallback<HttpResponse> callback) throws IOException {
    if (authToken != null && authToken.length() != MatomoRequest.AUTH_TOKEN_LENGTH) {
      throw new IllegalArgumentException(authToken + " is not " + MatomoRequest.AUTH_TOKEN_LENGTH + " characters long.");
    }
    HttpPost post = buildPost(requests, authToken);
    final CloseableHttpAsyncClient client = getHttpAsyncClient();
    client.start();
    log.debug("Sending async requests via POST: {}", requests);
    return client.execute(post, callback);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.
   *
   * @param requests the requests to send
   * @param callback callback that gets executed when response arrives
   * @return future with response from these requests
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendBulkRequestAsync(final Iterable<? extends MatomoRequest> requests, FutureCallback<HttpResponse> callback) throws IOException {
    return sendBulkRequestAsync(requests, null, callback);
  }

  /**
   * Send multiple requests in a single HTTP call.  More efficient than sending
   * several individual requests.  Specify the AuthToken if parameters that require
   * an auth token is used.
   *
   * @param requests  the requests to send
   * @param authToken specify if any of the parameters use require AuthToken
   * @return the response from these requests
   * @throws IOException thrown if there was a problem with this connection
   */
  public Future<HttpResponse> sendBulkRequestAsync(final Iterable<? extends MatomoRequest> requests, final String authToken) throws IOException {
    return sendBulkRequestAsync(requests, authToken, null);
  }
}
