package org.piwik.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PiwikTrackerIT {

  private PiwikTracker piwikTracker;

  @Test
  void createsNewPiwikTrackerInstanceWithHostUrl() {

    piwikTracker = new PiwikTracker("http://localhost:8080");

    assertThat(piwikTracker).isNotNull();

  }

  @Test
  void createsNewPiwikTrackerInstanceWithHostUrlAndTimeout() {

    piwikTracker = new PiwikTracker("http://localhost:8080", 1000);

    assertThat(piwikTracker).isNotNull();

  }

  @Test
  void createsNewPiwikTrackerInstanceWithHostUrlAndProxySettings() {

    piwikTracker = new PiwikTracker("http://localhost:8080", "localhost", 8080);

    assertThat(piwikTracker).isNotNull();

  }

  @Test
  void createsNewPiwikTrackerInstanceWithHostUrlAndProxySettingsAndTimeout() {

    piwikTracker = new PiwikTracker("http://localhost:8080", "localhost", 8080, 1000);

    assertThat(piwikTracker).isNotNull();

  }

}