package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.net.MalformedURLException;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SenderIT {

  private static final WireMockServer wireMockServer =
      new WireMockServer(WireMockConfiguration.options().dynamicPort());

  @BeforeAll
  static void beforeAll() {
    wireMockServer.start();
  }

  @Test
  void sendSingleFailsIfQueryIsMalformed() {
    TrackerConfiguration trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("telnet://localhost")).build();
    Sender sender =
        new Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), Runnable::run);

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(InvalidUrlException.class)
        .hasRootCause(new MalformedURLException("unknown protocol: telnet"));
  }

  @Test
  void failsIfEndpointReturnsNotFound() {
    TrackerConfiguration trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create(wireMockServer.baseUrl())).build();

    Sender sender =
        new Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), Runnable::run);

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 404");
  }

  @Test
  void failsIfCouldNotConnectToEndpoint() {
    TrackerConfiguration trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost:1234")).build();

    Sender sender =
        new Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), Runnable::run);

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not send request via GET");
  }

  @Test
  void connectsViaProxy() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockServer.baseUrl()))
        .proxyHost("localhost")
        .proxyPort(wireMockServer.port())
        .build();

    Sender sender =
        new Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), Runnable::run);

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 400");
  }

  @Test
  void connectsViaProxyWithProxyUserNameAndPassword() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockServer.baseUrl()))
        .proxyHost("localhost")
        .proxyPort(wireMockServer.port())
        .proxyUserName("user")
        .proxyPassword("password")
        .build();

    Sender sender =
        new Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), Runnable::run);

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 400");
  }

  @Test
  void logsFailedTracking() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create(wireMockServer.baseUrl()))
        .logFailedTracking(true)
        .build();

    Sender sender =
        new Sender(trackerConfiguration, new QueryCreator(trackerConfiguration), Runnable::run);

    assertThatThrownBy(() -> sender.sendSingle(new MatomoRequest()))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Tracking endpoint responded with code 404");
  }

}
