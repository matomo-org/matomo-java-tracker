package org.matomo.java.tracking;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Java8SenderIT {

  private static final WireMockServer wireMockServer =
      new WireMockServer(WireMockConfiguration.options().dynamicPort().dynamicHttpsPort());

  private Sender sender;

  private TrackerConfiguration trackerConfiguration;

  @BeforeAll
  static void beforeAll() {
    wireMockServer.start();
  }

  @Test
  void sendSingleFailsIfQueryIsMalformed() {
    trackerConfiguration = TrackerConfiguration.builder().apiEndpoint(URI.create("telnet://localhost")).build();
    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(InvalidUrlException.class)
        .hasRootCause(new MalformedURLException("unknown protocol: telnet"));
  }


  @Test
  void failsIfEndpointReturnsNotFound() {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockServer.baseUrl()))
        .disableSslHostVerification(true)
        .disableSslCertValidation(true)
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 404");
  }

  @Test
  void failsIfCouldNotConnectToEndpoint() {
    trackerConfiguration = TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost:1234")).build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not send request via GET");
  }

  @Test
  void connectsViaProxy() {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockServer.baseUrl()))
        .disableSslCertValidation(true)
        .disableSslHostVerification(true)
        .proxyHost("localhost")
        .proxyPort(wireMockServer.port())
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not send request via GET");
  }

  @Test
  void connectsViaProxyWithProxyUserNameAndPassword() {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockServer.baseUrl()))
        .disableSslCertValidation(true)
        .disableSslHostVerification(true)
        .proxyHost("localhost")
        .proxyPort(wireMockServer.port())
        .proxyUsername("user")
        .proxyPassword("password")
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not send request via GET");
  }

  @Test
  void logsFailedTracking() {
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockServer.baseUrl()))
        .disableSslCertValidation(true)
        .disableSslHostVerification(true)
        .logFailedTracking(true)
        .build();

    givenSender();

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 404");
  }

  @Test
  void skipSslCertificationValidation() {
    wireMockServer.stubFor(get(urlPathEqualTo("/matomo_ssl.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(String.format("https://localhost:%d/matomo_ssl.php", wireMockServer.httpsPort())))
        .disableSslCertValidation(true)
        .disableSslHostVerification(true)
        .build();

    givenSender();

    sender.sendSingle(new MatomoRequest());

    wireMockServer.verify(getRequestedFor(urlPathEqualTo("/matomo_ssl.php")));

  }

  @Test
  void addsHeadersToSingleRequest() {
    wireMockServer.stubFor(get(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(String.format("http://localhost:%d/matomo.php", wireMockServer.port())))
        .disableSslCertValidation(true)
        .disableSslHostVerification(true)
        .build();

    givenSender();

    sender.sendSingle(MatomoRequest.request().headers(singletonMap("headerName", "headerValue")).build());

    wireMockServer.verify(getRequestedFor(urlPathEqualTo("/matomo.php")).withHeader(
        "headerName",
        equalTo("headerValue")
    ));

  }

  @Test
  void addsHeadersToBulkRequest() {
    wireMockServer.stubFor(post(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(String.format("http://localhost:%d/matomo.php", wireMockServer.port())))
        .disableSslCertValidation(true)
        .disableSslHostVerification(true)
        .build();

    givenSender();

    sender.sendBulk(
        singleton(MatomoRequest.request().headers(singletonMap("headerName", "headerValue")).build()),
        null
    );

    wireMockServer.verify(postRequestedFor(urlPathEqualTo("/matomo.php")).withHeader(
        "headerName",
        equalTo("headerValue")
    ));

  }


  @Test
  void addsHeadersToBulkAsyncRequest() {
    wireMockServer.stubFor(post(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(String.format("http://localhost:%d/matomo.php", wireMockServer.port())))
        .disableSslCertValidation(true)
        .disableSslHostVerification(true)
        .build();

    givenSender();

    CompletableFuture<Void> future = sender.sendBulkAsync(singleton(MatomoRequest
        .request()
        .headers(singletonMap("headerName", "headerValue"))
        .build()), null);

    future.join();
    wireMockServer.verify(postRequestedFor(urlPathEqualTo("/matomo.php")).withHeader(
        "headerName",
        equalTo("headerValue")
    ));

  }


  private void givenSender() {
    sender = new Java8Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), Runnable::run);
  }

}
