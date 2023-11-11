package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import org.junit.jupiter.api.Test;

class TrackerConfigurationTest {

  private final TrackerConfiguration.TrackerConfigurationBuilder trackerConfigurationBuilder
      = TrackerConfiguration.builder();

  @Test
  void validateDoesNotFailIfDefaultAuthTokenIsNull() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken(null)
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfDefaultAuthTokenIsEmpty() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("")
        .build();

    thenFailsOnValidation("Auth token must be exactly 32 characters long");
  }

  @Test
  void validateFailsIfDefaultAuthTokenIsTooLong() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("123456789012345678901234567890123")
        .build();

    thenFailsOnValidation("Auth token must be exactly 32 characters long");
  }

  @Test
  void validateFailsIfDefaultAuthTokenIsTooShort() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("1234567890123456789012345678901")
        .build();

    thenFailsOnValidation("Auth token must be exactly 32 characters long");
  }

  @Test
  void validateFailsIfDefaultAuthTokenContainsInvalidCharacters() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("1234567890123456789012345678901!")
        .build();

    thenFailsOnValidation("Auth token must contain only lowercase letters and numbers");
  }

  @Test
  void validateDoesNotFailIfDefaultSiteIdIsNull() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(null)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }


  @Test
  void validateDoesNotFailIfDefaultSiteIdIsPositive() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateDoesNotFailIfDefaultSiteIdIsZero() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(0)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    whenValidates();
  }

  @Test
  void validateFailsIfDefaultSiteIdIsNegative() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(-1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Default site ID must not be negative");
  }

  @Test
  void validateDoesNotFailIfApiEndpointIsSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfApiEndpointIsNotSet() {
    trackerConfigurationBuilder
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("API endpoint must not be null");
  }

  @Test
  void validateDoesNotFailIfProxyHostIsSetAndProxyPortIsPositive() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(1234)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfProxyPortIsSetAndProxyHostIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyPort(1234)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    thenFailsOnValidation("Proxy host must be set if port is set");
  }

  @Test
  void validateFailsIfProxyPasswordIsSetAndProxyHostIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyPassword("password")
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    thenFailsOnValidation("Proxy host must be set if password is set");
  }

  @Test
  void validateFailsIfProxyHostIsSetAndProxyPortIsZero() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(0)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Proxy port must be greater than 0");
  }

  @Test
  void validateFailsIfProxyHostIsSetAndProxyPortIsNegative() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(-1)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Proxy port must be greater than 0");
  }

  @Test
  void validateFailsIfProxyUsernameIsSetAndProxyPasswordIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(1234)
        .proxyUsername("user")
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Proxy password must be set if username is set");
  }

  @Test
  void validateFailsIfProxyPasswordIsSetAndProxyUsernameIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(1234)
        .proxyPassword("password")
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Proxy username must be set if password is set");
  }

  @Test
  void validateDoesNotFailIfProxyUsernameAndProxyPasswordAreSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(1234)
        .proxyUsername("user")
        .proxyPassword("password")
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateDoesNotFailIfProxyUsernameAndProxyPasswordAreNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(1234)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateDoesNotFailIfProxyUsernameIsSetAndProxyPasswordIsSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(1234)
        .proxyUsername("user")
        .proxyPassword("password")
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateDoesNotFailIfProxyUsernameIsNotSetAndProxyPasswordIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyHost("proxy.example")
        .proxyPort(1234)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfProxyUsernameIsSetAndProxyPasswordIsNotSetAndProxyHostIsNotSetAndProxyPortIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyUsername("user")
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    thenFailsOnValidation("Proxy host must be set if username is set");
  }

  @Test
  void validateDoesNotFailIfProxyUsernameIsNotSetAndProxyPasswordIsNotSetAndProxyHostIsNotSetAndProxyPortIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfProxyUsernameIsSetAndProxyPasswordIsSetAndProxyHostIsNotSetAndProxyPortIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .proxyUsername("user")
        .proxyPassword("password")
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    thenFailsOnValidation("Proxy host must be set if username is set");
  }

  @Test
  void validateDoesNotFailIfSocketTimeoutIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .socketTimeout(null)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfSocketTimeoutIsNegative() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .socketTimeout(java.time.Duration.ofSeconds(-1))
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Socket timeout must not be negative");
  }

  @Test
  void validateDoesNotFailIfConnectTimeoutIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .connectTimeout(null)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfConnectTimeoutIsNegative() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .connectTimeout(java.time.Duration.ofSeconds(-1))
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Connect timeout must not be negative");
  }

  @Test
  void validateDoesNotFailIfThreadPoolSizeIsOne() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .threadPoolSize(1)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateFailsIfThreadPoolSizeIsLessThanOne() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .threadPoolSize(0)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();

    thenFailsOnValidation("Thread pool size must be greater than 0");
  }

  @Test
  void validateDoesNotFailIfThreadPoolSizeIsGreaterThanOne() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .threadPoolSize(2)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateDoesNotFailIfThreadPoolSizeIsNotSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  @Test
  void validateDoesNotFailIfThreadPoolSizeIsGreaterThanOneAndSet() {
    trackerConfigurationBuilder
        .apiEndpoint(URI.create("https://matomo.example/matomo.php"))
        .threadPoolSize(2)
        .defaultSiteId(1)
        .defaultAuthToken("12345678901234567890123456789012")
        .build();
    whenValidates();
  }

  void whenValidates() {
    trackerConfigurationBuilder.build().validate();
  }

  private void thenFailsOnValidation(String message) {
    assertThatThrownBy(this::whenValidates).isInstanceOf(IllegalArgumentException.class).hasMessage(message);
  }

}