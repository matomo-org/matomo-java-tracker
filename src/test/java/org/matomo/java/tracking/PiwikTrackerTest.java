package org.matomo.java.tracking;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatcher;
import org.piwik.java.tracking.PiwikRequest;
import org.piwik.java.tracking.PiwikTracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author brettcsorba
 */
@DisplayName("Piwik Tracker Test")
class PiwikTrackerTest {

  private static final Map<String, Collection<Object>> PARAMETERS = Collections.singletonMap("parameterName", Collections.singleton("parameterValue"));

  // https://stackoverflow.com/a/3732328
  @DisplayName("Handler")
  static class Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      String response = "OK";
      exchange.sendResponseHeaders(200, response.length());
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

  PiwikTracker piwikTracker;

  PiwikTracker localTracker;

  HttpServer server;

  public PiwikTrackerTest() {
  }

  @BeforeAll
  static void setUpClass() {
  }

  @AfterAll
  static void tearDownClass() {
  }

  @BeforeEach
  void setUp() {
    // test with mocks
    piwikTracker = spy(new PiwikTracker("http://test.com"));
    // test with local server
    localTracker = new PiwikTracker("http://localhost:8001/test");
    try {
      server = HttpServer.create(new InetSocketAddress(8001), 0);
      server.createContext("/test", new Handler());
      // creates a default executor
      server.setExecutor(null);
      server.start();
    } catch (IOException ex) {
    }
  }

  @AfterEach
  void tearDown() {
    server.stop(0);
  }

  /**
   * Test of addParameter method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Add Parameter")
  void testAddParameter() {
  }

  /**
   * Test of sendRequest method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Send Request")
  void testSendRequest() throws Exception {
    PiwikRequest request = mock(PiwikRequest.class);
    HttpClient client = mock(HttpClient.class);
    HttpResponse response = mock(HttpResponse.class);
    doReturn(client).when(piwikTracker).getHttpClient();
    doReturn(PARAMETERS).when(request).getParameters();
    doReturn(response).when(client).execute(argThat(new CorrectGetRequest("http://test.com?parameterName=parameterValue")));
    assertThat(piwikTracker.sendRequest(request)).isEqualTo(response);
  }

  /**
   * Test of sendRequestAsync method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Send Request Async")
  void testSendRequestAsync() throws Exception {
    PiwikRequest request = mock(PiwikRequest.class);
    CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
    HttpResponse response = mock(HttpResponse.class);
    Future<HttpResponse> future = mock(Future.class);
    doReturn(client).when(piwikTracker).getHttpAsyncClient();
    doReturn(PARAMETERS).when(request).getParameters();
    doReturn(response).when(future).get();
    doReturn(true).when(future).isDone();
    doReturn(future).when(client).execute(argThat(new CorrectGetRequest("http://test.com?parameterName=parameterValue")), any());
    assertThat(piwikTracker.sendRequestAsync(request).get()).isEqualTo(response);
  }

  /**
   * Test sync API with local server
   */
  @Test
  @DisplayName("Test With Local Server")
  void testWithLocalServer() throws Exception {
    // one
    PiwikRequest request = new PiwikRequest(3, new URL("http://test.com"));
    HttpResponse response = localTracker.sendRequest(request);
    String msg = EntityUtils.toString(response.getEntity());
    assertThat(msg).isEqualTo("OK");
    // bulk
    List<PiwikRequest> requests = Collections.singletonList(request);
    HttpResponse responseBulk = localTracker.sendBulkRequest(requests);
    String msgBulk = EntityUtils.toString(responseBulk.getEntity());
    assertThat(msgBulk).isEqualTo("OK");
  }

  /**
   * Test async API with local server
   */
  @Test
  @DisplayName("Test With Local Server Async")
  void testWithLocalServerAsync() throws Exception {
    // one
    PiwikRequest request = new PiwikRequest(3, new URL("http://test.com"));
    HttpResponse response = localTracker.sendRequestAsync(request).get();
    String msg = EntityUtils.toString(response.getEntity());
    assertThat(msg).isEqualTo("OK");
    // bulk
    List<PiwikRequest> requests = Collections.singletonList(request);
    HttpResponse responseBulk = localTracker.sendBulkRequestAsync(requests).get();
    String msgBulk = EntityUtils.toString(responseBulk.getEntity());
    assertThat(msgBulk).isEqualTo("OK");
  }

  /**
   * Test async API with local server
   */
  @Test
  @DisplayName("Test With Local Server Async Callback")
  void testWithLocalServerAsyncCallback() throws Exception {
    CountDownLatch latch = new CountDownLatch(2);
    BlockingQueue<HttpResponse> responses = new LinkedBlockingQueue<>();
    BlockingQueue<Exception> exceptions = new LinkedBlockingQueue<>();
    AtomicInteger cancelled = new AtomicInteger();
    FutureCallback<HttpResponse> cb = new FutureCallback<HttpResponse>() {

      @Override
      public void completed(HttpResponse httpResponse) {
        responses.add(httpResponse);
        latch.countDown();
      }

      @Override
      public void failed(Exception e) {
        exceptions.add(e);
        latch.countDown();
      }

      @Override
      public void cancelled() {
        cancelled.incrementAndGet();
        latch.countDown();
      }
    };
    // one
    PiwikRequest request = new PiwikRequest(3, new URL("http://test.com"));
    Future<HttpResponse> respFuture = localTracker.sendRequestAsync(request, cb);
    // bulk
    List<PiwikRequest> requests = Collections.singletonList(request);
    Future<HttpResponse> bulkFuture = localTracker.sendBulkRequestAsync(requests, cb);
    assertThat(latch.await(100, TimeUnit.MILLISECONDS)).as("Responses not received").isTrue();
    assertThat(cancelled.get()).as("Not expecting cancelled responses").isEqualTo(0);
    assertThat(exceptions.size()).as("Not expecting exceptions").isEqualTo(0);
    assertThat(respFuture.isDone()).as("Single response future not done").isTrue();
    assertThat(bulkFuture.isDone()).as("Bulk response future not done").isTrue();
    HttpResponse response = responses.poll(1, TimeUnit.MILLISECONDS);
    assertThat(EntityUtils.toString(response.getEntity())).isEqualTo("OK");
    HttpResponse bulkResponse = responses.poll(1, TimeUnit.MILLISECONDS);
    assertThat(EntityUtils.toString(bulkResponse.getEntity())).isEqualTo("OK");
  }

  @DisplayName("Correct Get Request")
  static class CorrectGetRequest implements ArgumentMatcher<HttpGet> {

    String url;

    public CorrectGetRequest(String url) {
      this.url = url;
    }

    @Override
    public boolean matches(HttpGet get) {
      return url.equals(get.getURI().toString());
    }
  }

  /**
   * Test of sendBulkRequest method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Send Bulk Request _ Iterable")
  void testSendBulkRequest_Iterable() {
    List<PiwikRequest> requests = new ArrayList<>();
    HttpResponse response = mock(HttpResponse.class);
    doReturn(response).when(piwikTracker).sendBulkRequest(requests, null);
    assertThat(piwikTracker.sendBulkRequest(requests)).isEqualTo(response);
  }

  /**
   * Test of sendBulkRequest method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Send Bulk Request _ Iterable _ String TT")
  void testSendBulkRequest_Iterable_StringTT() {
    try {
      List<PiwikRequest> requests = new ArrayList<>();
      HttpClient client = mock(HttpClient.class);
      PiwikRequest request = mock(PiwikRequest.class);
      doReturn(PARAMETERS).when(request).getParameters();
      requests.add(request);
      doReturn(client).when(piwikTracker).getHttpClient();
      piwikTracker.sendBulkRequest(requests, "1");
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("1 is not 32 characters long.");
    }
  }

  @Test
  @DisplayName("Test Send Bulk Request _ Iterable _ String FF")
  void testSendBulkRequest_Iterable_StringFF() throws Exception {
    List<PiwikRequest> requests = new ArrayList<>();
    HttpClient client = mock(HttpClient.class);
    PiwikRequest request = mock(PiwikRequest.class);
    HttpResponse response = mock(HttpResponse.class);
    doReturn(PARAMETERS).when(request).getParameters();
    requests.add(request);
    doReturn(client).when(piwikTracker).getHttpClient();
    doReturn(response).when(client).execute(argThat(new CorrectPostRequest("{\"requests\":[\"?parameterName=parameterValue\"]}")));
    assertThat(piwikTracker.sendBulkRequest(requests, null)).isEqualTo(response);
  }

  @Test
  @DisplayName("Test Send Bulk Request _ Iterable _ String FT")
  void testSendBulkRequest_Iterable_StringFT() throws Exception {
    List<PiwikRequest> requests = new ArrayList<>();
    HttpClient client = mock(HttpClient.class);
    PiwikRequest request = mock(PiwikRequest.class);
    HttpResponse response = mock(HttpResponse.class);
    doReturn(PARAMETERS).when(request).getParameters();
    requests.add(request);
    doReturn(client).when(piwikTracker).getHttpClient();
    doReturn(response).when(client).execute(argThat(new CorrectPostRequest("{\"requests\":[\"?parameterName=parameterValue\"],\"token_auth\":\"12345678901234567890123456789012\"}")));
    assertThat(piwikTracker.sendBulkRequest(requests, "12345678901234567890123456789012")).isEqualTo(response);
  }

  /**
   * Test of sendBulkRequestAsync method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Send Bulk Request Async _ Iterable")
  void testSendBulkRequestAsync_Iterable() throws Exception {
    List<PiwikRequest> requests = new ArrayList<>();
    HttpResponse response = mock(HttpResponse.class);
    Future<HttpResponse> future = mock(Future.class);
    doReturn(response).when(future).get();
    doReturn(true).when(future).isDone();
    doReturn(future).when(piwikTracker).sendBulkRequestAsync(requests);
    assertThat(piwikTracker.sendBulkRequestAsync(requests).get()).isEqualTo(response);
  }

  /**
   * Test of sendBulkRequestAsync method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Send Bulk Request Async _ Iterable _ String TT")
  void testSendBulkRequestAsync_Iterable_StringTT() {
    try {
      List<PiwikRequest> requests = new ArrayList<>();
      CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
      PiwikRequest request = mock(PiwikRequest.class);
      doReturn(PARAMETERS).when(request).getParameters();
      requests.add(request);
      doReturn(client).when(piwikTracker).getHttpAsyncClient();
      piwikTracker.sendBulkRequestAsync(requests, "1");
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("1 is not 32 characters long.");
    }
  }

  @Test
  @DisplayName("Test Send Bulk Request Async _ Iterable _ String FF")
  void testSendBulkRequestAsync_Iterable_StringFF() throws Exception {
    List<PiwikRequest> requests = new ArrayList<>();
    CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
    PiwikRequest request = mock(PiwikRequest.class);
    HttpResponse response = mock(HttpResponse.class);
    Future<HttpResponse> future = mock(Future.class);
    doReturn(response).when(future).get();
    doReturn(true).when(future).isDone();
    doReturn(PARAMETERS).when(request).getParameters();
    requests.add(request);
    doReturn(client).when(piwikTracker).getHttpAsyncClient();
    doReturn(future).when(client).execute(argThat(new CorrectPostRequest("{\"requests\":[\"?parameterName=parameterValue\"]}")), any());
    assertThat(piwikTracker.sendBulkRequestAsync(requests).get()).isEqualTo(response);
  }

  @Test
  @DisplayName("Test Send Bulk Request Async _ Iterable _ String FT")
  void testSendBulkRequestAsync_Iterable_StringFT() throws Exception {
    List<PiwikRequest> requests = new ArrayList<>();
    CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
    PiwikRequest request = mock(PiwikRequest.class);
    HttpResponse response = mock(HttpResponse.class);
    Future<HttpResponse> future = mock(Future.class);
    doReturn(response).when(future).get();
    doReturn(true).when(future).isDone();
    doReturn(PARAMETERS).when(request).getParameters();
    requests.add(request);
    doReturn(client).when(piwikTracker).getHttpAsyncClient();
    doReturn(future).when(client).execute(argThat(new CorrectPostRequest("{\"requests\":[\"?parameterName=parameterValue\"],\"token_auth\":\"12345678901234567890123456789012\"}")), any());
    assertThat(piwikTracker.sendBulkRequestAsync(requests, "12345678901234567890123456789012").get()).isEqualTo(response);
  }

  @DisplayName("Correct Post Request")
  static class CorrectPostRequest implements ArgumentMatcher<HttpPost> {

    String body;

    public CorrectPostRequest(String body) {
      this.body = body;
    }

    @Override
    public boolean matches(HttpPost post) {
      try {
        InputStream bais = post.getEntity().getContent();
        byte[] bytes = new byte[bais.available()];
        bais.read(bytes);
        String str = new String(bytes);
        return body.equals(str);
      } catch (IOException e) {
        fail("Exception should not have been throw.");
      }
      return false;
    }
  }

  /**
   * Test of getHttpClient method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Get Http Client")
  void testGetHttpClient() {
    assertThat(piwikTracker.getHttpClient()).isNotNull();
  }

  /**
   * Test of getHttpAsyncClient method, of class PiwikTracker.
   */
  @Test
  @DisplayName("Test Get Http Async Client")
  void testGetHttpAsyncClient() {
    assertThat(piwikTracker.getHttpAsyncClient()).isNotNull();
  }

  /**
   * Test of getHttpClient method, of class PiwikTracker, with proxy.
   */
  @Test
  @DisplayName("Test Get Http Client With Proxy")
  void testGetHttpClientWithProxy() {
    piwikTracker = new PiwikTracker("http://test.com", "http://proxy", 8080);
    HttpClient httpClient = piwikTracker.getHttpClient();
    assertThat(httpClient).isNotNull();
  }
}
