package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import org.junit.jupiter.api.Test;

class ServiceLoaderSenderFactoryTest {

  @Test
  void failsIfNoImplementationFound() {
    ServiceLoaderSenderFactory serviceLoaderSenderFactory = new ServiceLoaderSenderFactory();

    TrackerConfiguration trackerConfiguration =
        TrackerConfiguration.builder().apiEndpoint(URI.create("http://localhost/matomo.php")).build();

    assertThatThrownBy(() -> serviceLoaderSenderFactory.createSender(trackerConfiguration,
        new QueryCreator(trackerConfiguration)
    ))
        .isInstanceOf(MatomoException.class)
        .hasMessage("No SenderProvider found");
  }

}