package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.piwik.java.tracking.PiwikLocale;

class PiwikLocaleTest {

  private final PiwikLocale locale = new PiwikLocale(Locale.US);

  /**
   * Test of setLocale method, of class PiwikLocale.
   */
  @Test
  void testLocale() {
    locale.setLocale(Locale.GERMANY);
    assertThat(locale.getLocale()).isEqualTo(Locale.GERMAN);
  }

  /**
   * Test of toString method, of class PiwikLocale.
   */
  @Test
  void testToString() {
    assertThat(locale).hasToString("us");
  }
}
