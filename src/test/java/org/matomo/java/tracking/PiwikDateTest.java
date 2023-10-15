package org.matomo.java.tracking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.piwik.java.tracking.PiwikDate;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author brettcsorba
 */
@DisplayName("Piwik Date Test")
class PiwikDateTest {

  /**
   * Test of constructor, of class PiwikDate.
   */
  @Test
  @DisplayName("Test Constructor 0")
  void testConstructor0() {
    PiwikDate date = new PiwikDate();
    assertThat(date).isNotNull();
  }

  @Test
  @DisplayName("Test Constructor 1")
  void testConstructor1() {
    PiwikDate date = new PiwikDate(1433186085092L);
    assertThat(date).isNotNull();
    assertThat(date.toString()).isEqualTo("2015-06-01 19:14:45");
    date = new PiwikDate(1467437553000L);
    assertThat(date.toString()).isEqualTo("2016-07-02 05:32:33");
  }

  /**
   * Test of setTimeZone method, of class PiwikDate.
   */
  @Test
  @DisplayName("Test Set Time Zone")
  void testSetTimeZone() {
    PiwikDate date = new PiwikDate(1433186085092L);
    date.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    assertThat(date.toString()).isEqualTo("2015-06-01 15:14:45");
  }
}
