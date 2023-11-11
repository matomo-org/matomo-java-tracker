package org.matomo.java.tracking.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.TrackerConfiguration;

class StandardTrackerConfigurationBuilderCustomizerIT {

  @Test
  void createsStandardTrackerConfigurationBuilderCustomizer() {
    MatomoTrackerProperties properties = new MatomoTrackerProperties();
    properties.setApiEndpoint("https://test.com/matomo.php");
    properties.setDefaultSiteId(1);
    properties.setDefaultAuthToken("abc123def4563123abc123def4563123");
    properties.setEnabled(true);
    properties.setConnectTimeout(Duration.ofMinutes(1L));
    properties.setSocketTimeout(Duration.ofMinutes(2L));
    properties.setProxyHost("proxy.example.com");
    properties.setProxyPort(8080);
    properties.setProxyUsername("user");
    properties.setProxyPassword("password");
    properties.setUserAgent("Mozilla/5.0 (compatible; AcmeInc/1.0; +https://example.com/bot.html)");
    properties.setLogFailedTracking(true);
    properties.setDisableSslCertValidation(true);
    properties.setDisableSslHostVerification(true);
    properties.setThreadPoolSize(10);
    StandardTrackerConfigurationBuilderCustomizer customizer =
        new StandardTrackerConfigurationBuilderCustomizer(properties);
    TrackerConfiguration.TrackerConfigurationBuilder builder = TrackerConfiguration.builder();

    customizer.customize(builder);

    assertThat(customizer.getOrder()).isZero();
    TrackerConfiguration configuration = builder.build();
    assertThat(configuration.getApiEndpoint()).hasToString("https://test.com/matomo.php");
    assertThat(configuration.getDefaultSiteId()).isEqualTo(1);
    assertThat(configuration.getDefaultAuthToken()).isEqualTo("abc123def4563123abc123def4563123");
    assertThat(configuration.isEnabled()).isTrue();
    assertThat(configuration.getConnectTimeout()).hasSeconds(60L);
    assertThat(configuration.getSocketTimeout()).hasSeconds(120L);
    assertThat(configuration.getProxyHost()).isEqualTo("proxy.example.com");
    assertThat(configuration.getProxyPort()).isEqualTo(8080);
    assertThat(configuration.getProxyUsername()).isEqualTo("user");
    assertThat(configuration.getProxyPassword()).isEqualTo("password");
    assertThat(configuration.getUserAgent()).isEqualTo(
        "Mozilla/5.0 (compatible; AcmeInc/1.0; +https://example.com/bot.html)");
    assertThat(configuration.isLogFailedTracking()).isTrue();
    assertThat(configuration.isDisableSslCertValidation()).isTrue();
    assertThat(configuration.isDisableSslHostVerification()).isTrue();
    assertThat(configuration.getThreadPoolSize()).isEqualTo(10);
  }

}