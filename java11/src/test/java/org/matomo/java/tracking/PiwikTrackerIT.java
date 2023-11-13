package org.matomo.java.tracking;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.resetAllRequests;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.parameters.RandomValue;
import org.matomo.java.tracking.parameters.VisitorId;
import org.piwik.java.tracking.PiwikRequest;
import org.piwik.java.tracking.PiwikTracker;

@WireMockTest
class PiwikTrackerIT {

  private static final int SITE_ID = 42;

  private PiwikTracker piwikTracker;

  private PiwikRequest request;

  @BeforeEach
  void setUp(WireMockRuntimeInfo wireMockRuntimeInfo) throws MalformedURLException {
    piwikTracker = new PiwikTracker(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php", -1);
    resetAllRequests();
    stubFor(post(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    stubFor(get(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    request = new PiwikRequest(SITE_ID, new URL("https://test.local/test/path?id=123"));
    request.setRandomValue(RandomValue.fromString("rand"));
    request.setVisitorId(VisitorId.fromHash(999999999999999999L));
  }

  /**
   * Test of sendRequest method, of class PiwikTracker.
   */
  @Test
  void testSendRequest() {
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    piwikTracker.sendRequest(request);

    verify(getRequestedFor(urlEqualTo(
        "/matomo.php?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand&parameterName=parameterValue")).withHeader("User-Agent",
        equalTo("MatomoJavaClient")
    ));
  }

  /**
   * Test of sendRequestAsync method, of class PiwikTracker.
   */
  @Test
  void testSendRequestAsync() {
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    CompletableFuture<?> future = piwikTracker.sendRequestAsync(request);

    assertThat(future).isNotCompletedExceptionally();
    assertThat(future).succeedsWithin(1, MINUTES).satisfies(v -> {
      verify(getRequestedFor(urlEqualTo(
          "/matomo.php?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand&parameterName=parameterValue"))
          .withHeader("User-Agent", equalTo("MatomoJavaClient")
      ));
    });

  }


  /**
   * Test of sendBulkRequest method, of class PiwikTracker.
   */
  @Test
  void testSendBulkRequest_Iterable() {
    List<PiwikRequest> requests = Collections.singletonList(request);
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    piwikTracker.sendBulkRequest(requests);

    verify(postRequestedFor(urlEqualTo("/matomo.php"))
        .withHeader("Content-Length", equalTo("167"))
        .withHeader("Accept", equalTo("*/*"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("User-Agent", equalTo("MatomoJavaClient"))
        .withRequestBody(equalToJson(
            "{ \"requests\" : [ \"?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand&parameterName=parameterValue\" ]}")));

  }

  /**
   * Test of sendBulkRequest method, of class PiwikTracker.
   */
  @Test
  void testSendBulkRequest_Iterable_StringTT() {
    List<PiwikRequest> requests = Collections.singletonList(request);
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    assertThatThrownBy(() -> piwikTracker.sendBulkRequest(requests, "1"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }

  @Test
  void testSendBulkRequest_Iterable_StringFF() {
    List<PiwikRequest> requests = Collections.singletonList(request);
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    piwikTracker.sendBulkRequest(requests, null);

    verify(postRequestedFor(urlEqualTo("/matomo.php"))
        .withHeader("Content-Length", equalTo("167"))
        .withHeader("Accept", equalTo("*/*"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("User-Agent", equalTo("MatomoJavaClient"))
        .withRequestBody(equalToJson(
            "{\"requests\":[\"?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand&parameterName=parameterValue\"]}")));

  }

  @Test
  void testSendBulkRequest_Iterable_StringFT() {
    List<PiwikRequest> requests = Collections.singletonList(request);
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    piwikTracker.sendBulkRequest(requests, "12345678901234567890123456789012");

    verify(postRequestedFor(urlEqualTo("/matomo.php"))
        .withHeader("Content-Length", equalTo("215"))
        .withHeader("Accept", equalTo("*/*"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("User-Agent", equalTo("MatomoJavaClient"))
        .withRequestBody(equalToJson(
            "{\"requests\":[\"?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand&parameterName=parameterValue\" ],\"token_auth\":\"12345678901234567890123456789012\"}")));

  }

  /**
   * Test of sendBulkRequestAsync method, of class PiwikTracker.
   */
  @Test
  void testSendBulkRequestAsync_Iterable() {
    List<PiwikRequest> requests = Collections.singletonList(request);
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    CompletableFuture<Void> future = piwikTracker.sendBulkRequestAsync(requests);

    assertThat(future).succeedsWithin(1, MINUTES).satisfies(v -> {
      verify(postRequestedFor(urlEqualTo("/matomo.php"))
          .withHeader("Content-Length", equalTo("167"))
          .withHeader("Accept", equalTo("*/*"))
          .withHeader("Content-Type", equalTo("application/json"))
          .withHeader("User-Agent", equalTo("MatomoJavaClient"))
          .withRequestBody(equalToJson(
              "{\"requests\" : [ \"?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand&parameterName=parameterValue\" ]}")));

    });

  }

  /**
   * Test of sendBulkRequestAsync method, of class PiwikTracker.
   */
  @Test
  void testSendBulkRequestAsync_Iterable_StringTT() {

    List<PiwikRequest> requests = Collections.singletonList(request);
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    assertThatThrownBy(() -> piwikTracker.sendBulkRequestAsync(requests, "1").get())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");

  }


  @Test
  void testSendBulkRequestAsync_Iterable_String() {

    List<PiwikRequest> requests = Collections.singletonList(request);
    request.setCustomTrackingParameter("parameterName", "parameterValue");

    CompletableFuture<Void> future =
        piwikTracker.sendBulkRequestAsync(requests, "12345678901234567890123456789012");

    assertThat(future).isNotCompletedExceptionally();
    assertThat(future).succeedsWithin(1, MINUTES).satisfies(v -> {
      verify(postRequestedFor(urlEqualTo("/matomo.php"))
          .withHeader("Content-Length", equalTo("215"))
          .withHeader("Accept", equalTo("*/*"))
          .withHeader("Content-Type", equalTo("application/json"))
          .withHeader("User-Agent", equalTo("MatomoJavaClient"))
          .withRequestBody(equalToJson(
              "{\"requests\":[ \"?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand&parameterName=parameterValue\"],\"token_auth\":\"12345678901234567890123456789012\"}")));

    });
  }

  @Test
  void createsPiwikTrackerWithHostUrl(WireMockRuntimeInfo wireMockRuntimeInfo) {
    PiwikTracker piwikTracker = new PiwikTracker(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php");

    piwikTracker.sendRequest(request);

    verify(getRequestedFor(urlEqualTo(
        "/matomo.php?rec=1&idsite=42&url=https%3A%2F%2Ftest.local%2Ftest%2Fpath%3Fid%3D123&apiv=1&_id=0de0b6b3a763ffff&send_image=0&rand=rand")).withHeader("User-Agent",
        equalTo("MatomoJavaClient")
    ));
  }

  @Test
  void createPiwikTrackerWithHostUrlAndProxyHostAndPort(WireMockRuntimeInfo wireMockRuntimeInfo) {
    PiwikTracker piwikTracker =
        new PiwikTracker(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php", "localhost", 8080);

    assertThatThrownBy(() -> piwikTracker.sendRequest(request))
        .isInstanceOf(MatomoException.class)
    ;

  }

  @Test
  void createPiwikTrackerWithHostUrlAndProxyHostAndPortAndTimeout(WireMockRuntimeInfo wireMockRuntimeInfo) {
    PiwikTracker piwikTracker =
        new PiwikTracker(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php", "localhost", 8080, 1000);

    assertThatThrownBy(() -> piwikTracker.sendRequest(request))
        .isInstanceOf(MatomoException.class)
    ;
  }

}
