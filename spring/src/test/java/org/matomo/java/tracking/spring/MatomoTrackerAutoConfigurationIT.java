package org.matomo.java.tracking.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class MatomoTrackerAutoConfigurationIT {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(MatomoTrackerAutoConfiguration.class));


  @Test
  void matomoTrackerRegistration() {
    contextRunner.withPropertyValues("matomo.tracker.api-endpoint:https://test.com/matomo.php").run(context -> {
      assertThat(context).hasSingleBean(MatomoTracker.class).hasBean("matomoTracker");
    });
  }

  @Test
  void additionalTrackerConfigurationBuilderCustomization() {
    this.contextRunner
        .withPropertyValues("matomo.tracker.api-endpoint:https://test.com/matomo.php")
        .withUserConfiguration(TrackerConfigurationBuilderCustomizerConfig.class)
        .run(context -> {
          TrackerConfiguration trackerConfiguration = context.getBean(TrackerConfiguration.class);
          assertThat(trackerConfiguration.getConnectTimeout()).isEqualTo(Duration.ofMinutes(1L));
        });
  }

  @Test
  void customTrackerConfigurationBuilder() {
    this.contextRunner
        .withPropertyValues("matomo.tracker.api-endpoint:https://test.com/matomo.php")
        .withUserConfiguration(TrackerConfigurationBuilderConfig.class)
        .run(context -> {
          TrackerConfiguration trackerConfiguration = context.getBean(TrackerConfiguration.class);
          assertThat(trackerConfiguration.isDisableSslHostVerification()).isTrue();
        });
  }

  @Configuration
  static class TrackerConfigurationBuilderCustomizerConfig {

    @Bean
    TrackerConfigurationBuilderCustomizer customConnectTimeout() {
      return configurationBuilder -> configurationBuilder.connectTimeout(Duration.ofMinutes(1L));
    }

  }

  @Configuration
  static class TrackerConfigurationBuilderConfig {

    @Bean
    TrackerConfiguration.TrackerConfigurationBuilder customTrackerConfigurationBuilder() {
      return TrackerConfiguration.builder().apiEndpoint(URI.create("https://test.com/matomo.php")).disableSslHostVerification(true);
    }

  }

}