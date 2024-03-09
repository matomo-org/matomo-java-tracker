package org.matomo.java.tracking;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WireMockTest(httpsEnabled = true)
class Java11SenderIT {

  private Sender sender;

  private TrackerConfiguration trackerConfiguration;

  @BeforeEach
  void disableSslHostnameVerification() {
    System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
  }

  @Test
  void failsIfTrackerConfigurationIsNotSet() {
    CookieManager cookieManager = new CookieManager();
    assertThatThrownBy(() -> new Java11Sender(
        null,
        new QueryCreator(TrackerConfiguration.builder()
                                             .apiEndpoint(URI.create("http://localhost"))
                                             .build()),
        HttpClient.newBuilder().cookieHandler(cookieManager).build(),
        cookieManager.getCookieStore(),
        Executors.newFixedThreadPool(2, new DaemonThreadFactory())
    )).isInstanceOf(NullPointerException.class)
      .hasMessage("trackerConfiguration is marked non-null but is null");
  }

  @Test
  void failsIfQueryCreatorIsNotSet() {
    CookieManager cookieManager = new CookieManager();
    assertThatThrownBy(() -> new Java11Sender(
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost")).build(),
        null,
        HttpClient.newBuilder().cookieHandler(cookieManager).build(),
        cookieManager.getCookieStore(),
        Executors.newFixedThreadPool(2, new DaemonThreadFactory())
    )).isInstanceOf(NullPointerException.class)
      .hasMessage("queryCreator is marked non-null but is null");
  }

  @Test
  void failsIfHttpClientIsNotSet() {
    CookieManager cookieManager = new CookieManager();
    assertThatThrownBy(() -> new Java11Sender(
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost")).build(),
        new QueryCreator(TrackerConfiguration.builder()
                                             .apiEndpoint(URI.create("http://localhost"))
                                             .build()),
        null,
        cookieManager.getCookieStore(),
        Executors.newFixedThreadPool(2, new DaemonThreadFactory())
    )).isInstanceOf(NullPointerException.class)
      .hasMessage("httpClient is marked non-null but is null");
  }

  @Test
  void failsIfCookieStoreIsNotSet() {
    CookieManager cookieManager = new CookieManager();
    assertThatThrownBy(() -> new Java11Sender(
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost")).build(),
        new QueryCreator(TrackerConfiguration.builder()
                                             .apiEndpoint(URI.create("http://localhost"))
                                             .build()),
        HttpClient.newBuilder().cookieHandler(cookieManager).build(),
        null,
        Executors.newFixedThreadPool(2, new DaemonThreadFactory())
    )).isInstanceOf(NullPointerException.class)
      .hasMessage("cookieStore is marked non-null but is null");
  }

  @Test
  void sendSingleFailsIfQueryIsMalformedWithSocketTimeout() {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("telnet://localhost"))
        .socketTimeout(Duration.ofSeconds(30L))
        .build();
    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(MatomoRequests.ecommerceCartUpdate(50.0)
                                                             .goalId(0).build()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("invalid URI scheme telnet");
  }

  @Test
  void sendSingleFailsIfQueryIsMalformedWithoutSocketTimeout() {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("telnet://localhost"))
        .build();
    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("invalid URI scheme telnet");
  }

  private void givenSender() {
    sender = new Java11SenderProvider().provideSender(
        trackerConfiguration,
        new QueryCreator(trackerConfiguration)
    );
  }

  @Test
  void failsIfEndpointReturnsNotFound(WireMockRuntimeInfo wireMockRuntimeInfo) {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()))
        .disableSslCertValidation(true)
        .socketTimeout(Duration.ofSeconds(0L))
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 404");
  }

  @Test
  void failsAndLogsIfCouldNotConnectToEndpoint() {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("http://localhost:1234"))
        .userAgent("")
        .logFailedTracking(true)
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not send request to Matomo");
  }

  @Test
  void failsAndDoesNotLogIfCouldNotConnectToEndpoint() {
    trackerConfiguration =
        TrackerConfiguration.builder()
                            .apiEndpoint(URI.create("http://localhost:1234"))
                            .userAgent("")
                            .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not send request to Matomo");
  }

  @Test
  void connectsViaProxy(WireMockRuntimeInfo wireMockRuntimeInfo) {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()))
        .disableSslCertValidation(true)
        .proxyHost("localhost")
        .proxyPort(wireMockRuntimeInfo.getHttpPort())
        .userAgent(null)
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 400");
  }

  @Test
  void connectsViaProxyWithProxyUserNameAndPassword(WireMockRuntimeInfo wireMockRuntimeInfo) {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()))
        .disableSslCertValidation(true)
        .proxyHost("localhost")
        .proxyPort(wireMockRuntimeInfo.getHttpPort())
        .proxyUsername("user")
        .proxyPassword("password")
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 400");
  }

  @Test
  void logsFailedTracking(WireMockRuntimeInfo wireMockRuntimeInfo) {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpsBaseUrl()))
        .disableSslCertValidation(true)
        .logFailedTracking(true)
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 404");
  }

  @Test
  void skipSslCertificationValidation(WireMockRuntimeInfo wireMockRuntimeInfo) {
    stubFor(get(urlPathEqualTo("/matomo_ssl.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(String.format(
            "https://localhost:%d/matomo_ssl.php",
            wireMockRuntimeInfo.getHttpsPort()
        )))
        .disableSslCertValidation(true)
        .build();

    givenSender();

    sender.sendSingle(MatomoRequests.goal(2, 60.0).build());

    verify(getRequestedFor(urlPathEqualTo("/matomo_ssl.php")));

  }

  @Test
  void addsHeadersToSingleRequest(WireMockRuntimeInfo wireMockRuntimeInfo) {
    stubFor(get(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php"))
        .build();

    givenSender();

    sender.sendSingle(MatomoRequests.ping()
                                    .headers(singletonMap("headerName", "headerValue"))
                                    .build());

    verify(getRequestedFor(urlPathEqualTo("/matomo.php")).withHeader(
        "headerName",
        equalTo("headerValue")
    ));
  }

  @Test
  void addsHeadersToBulkRequest(WireMockRuntimeInfo wireMockRuntimeInfo) {
    stubFor(post(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php"))
        .build();

    givenSender();

    sender.sendBulk(List.of(MatomoRequests.goal(1, 23.50).headers(singletonMap(
        "headerName",
        "headerValue"
    )).build()), null);

    verify(postRequestedFor(urlPathEqualTo("/matomo.php")).withHeader(
        "headerName",
        equalTo("headerValue")
    ));
  }

  @Test
  void doesNotAddEmptyHeaders(WireMockRuntimeInfo wireMockRuntimeInfo) {
    stubFor(post(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php"))
        .build();

    givenSender();

    sender.sendBulk(List.of(MatomoRequests.pageView("Contact").headers(emptyMap()).build()), null);

    verify(postRequestedFor(urlPathEqualTo("/matomo.php")).withoutHeader("headerName"));
  }

  @Test
  void addsHeadersToBulkAsyncRequest(WireMockRuntimeInfo wireMockRuntimeInfo) {
    stubFor(post(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockRuntimeInfo.getHttpBaseUrl() + "/matomo.php"))
        .build();

    givenSender();

    CompletableFuture<Void> future = sender.sendBulkAsync(List.of(MatomoRequest
        .request()
        .headers(singletonMap("headerName", "headerValue"))
        .build()), null);

    future.join();
    verify(postRequestedFor(urlPathEqualTo("/matomo.php")).withHeader(
        "headerName",
        equalTo("headerValue")
    ));
  }

  @Test
  void failsOnSendSingleAsyncIfRequestIsNull() {
    trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost:1234")).build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingleAsync(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("request is marked non-null but is null");
  }

  @Test
  void failsOnSendSingleIfRequestIsNull() {
    trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost:1234")).build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("request is marked non-null but is null");
  }

  @Test
  void failsOnSendBulkAsyncIfRequestsIsNull() {
    trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost:1234")).build();

    givenSender();

    assertThatThrownBy(() -> sender.sendBulkAsync(null, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("requests is marked non-null but is null");
  }

  @Test
  void failsOnSendBulkAsyncIfRequestIsNull() {
    trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost:1234")).build();

    givenSender();

    assertThatThrownBy(() -> sender.sendBulk(null, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("requests is marked non-null but is null");
  }

  @Test
  void failsOnSendBulkAsyncIfOverrideAuthTokenIsMalformed() {
    trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost:1234")).build();

    givenSender();

    assertThatThrownBy(() -> sender.sendBulkAsync(
        List.of(MatomoRequests
            .siteSearch("Special offers", "Products", 5L).build()),
        "telnet://localhost"
    ))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }


}
