package org.matomo.java.tracking;

import org.junit.jupiter.api.Test;
import org.piwik.java.tracking.PiwikDate;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;


class PiwikDateTest {

  /**
   * Test of constructor, of class PiwikDate.
   */
  @Test
  void testConstructor0() {
    PiwikDate date = new PiwikDate();
    assertThat(date).isNotNull();
  }

  @Test
  void testConstructor1() {
    PiwikDate date = new PiwikDate(1433186085092L);
    assertThat(date).isNotNull();
    assertThat(date.getTime()).isEqualTo(1433186085092L);
  }

  @Test
  void testConstructor2() {
    PiwikDate date = new PiwikDate(1467437553000L);
    assertThat(date.getTime()).isEqualTo(1467437553000L);
  }

  /**
   * Test of setTimeZone method, of class PiwikDate.
   */
  @Test
  void testSetTimeZone() {
    PiwikDate date = new PiwikDate(1433186085092L);
    date.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    assertThat(date.getTime()).isEqualTo(1433186085092L);
  }
}
