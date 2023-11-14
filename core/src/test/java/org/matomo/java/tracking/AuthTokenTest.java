package org.matomo.java.tracking;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;

class AuthTokenTest {

  @Test
  void determineAuthTokenReturnsAuthTokenFromRequest() {

    MatomoRequest request =
        MatomoRequests
            .event("Inbox", "Open", null, null)
            .authToken("bdeca231a312ab12cde124131bedfa23").build();

    String authToken = AuthToken.determineAuthToken(null, singleton(request), null);

    assertThat(authToken).isEqualTo("bdeca231a312ab12cde124131bedfa23");

  }

  @Test
  void determineAuthTokenReturnsAuthTokenFromTrackerConfiguration() {

    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://your-matomo-domain.example/matomo."))
        .defaultAuthToken("bdeca231a312ab12cde124131bedfa23")
        .build();

    String authToken = AuthToken.determineAuthToken(null, null, trackerConfiguration);

    assertThat(authToken).isEqualTo("bdeca231a312ab12cde124131bedfa23");
  }

  @Test
  void determineAuthTokenFromTrackerConfigurationIfRequestTokenIsEmpty() {

    MatomoRequest request = MatomoRequests.ping().authToken("").build();

    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://your-matomo-domain.example/matomo"))
        .defaultAuthToken("bdeca231a312ab12cde124131bedfa23")
        .build();

    String authToken = AuthToken.determineAuthToken(null, singleton(request), trackerConfiguration);

    assertThat(authToken).isEqualTo("bdeca231a312ab12cde124131bedfa23");

  }

  @Test
  void determineAuthTokenFromTrackerConfigurationIfRequestTokenIsBlank() {

    MatomoRequest request = MatomoRequests.pageView("Help").authToken(" ").build();

    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://your-matomo-domain.example/matomo"))
        .defaultAuthToken("bdeca231a312ab12cde124131bedfa23")
        .build();

    String authToken = AuthToken.determineAuthToken(null, singleton(request), trackerConfiguration);

    assertThat(authToken).isEqualTo("bdeca231a312ab12cde124131bedfa23");

  }

}
