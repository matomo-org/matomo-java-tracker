package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import org.junit.jupiter.api.Test;

class TrackerConfigurationTest {

  @Test
  void validateDoesNotFailIfDefaultAuthTokenIsNull() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken(null)
        .build();
    trackerConfiguration.validate();
  }

  @Test
  void validateFailsIfDefaultAuthTokenIsEmpty() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("")
        .build();

    assertThatThrownBy(trackerConfiguration::validate)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }

  @Test
  void validateFailsIfDefaultAuthTokenIsTooLong() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("123456789012345678901234567890123")
        .build();

    assertThatThrownBy(trackerConfiguration::validate)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }

  @Test
  void validateFailsIfDefaultAuthTokenIsTooShort() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("1234567890123456789012345678901")
        .build();

    assertThatThrownBy(trackerConfiguration::validate)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }

  @Test
  void validateFailsIfDefaultAuthTokenContainsInvalidCharacters() throws Exception {
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("1234567890123456789012345678901!")
        .build();

    assertThatThrownBy(trackerConfiguration::validate)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must contain only lowercase letters and numbers");
  }

}
