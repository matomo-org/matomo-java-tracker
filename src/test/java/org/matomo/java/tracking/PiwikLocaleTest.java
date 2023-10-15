package org.matomo.java.tracking;

import org.junit.jupiter.api.*;
import org.piwik.java.tracking.PiwikLocale;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author brettcsorba
 */
@DisplayName("Piwik Locale Test")
class PiwikLocaleTest {

  PiwikLocale locale;

  public PiwikLocaleTest() {
  }

  @BeforeAll
  static void setUpClass() {
  }

  @AfterAll
  static void tearDownClass() {
  }

  @BeforeEach
  void setUp() {
    locale = new PiwikLocale(Locale.US);
  }

  @AfterEach
  void tearDown() {
  }

  /**
   * Test of getLocale method, of class PiwikLocale.
   */
  @Test
  @DisplayName("Test Constructor")
  void testConstructor() {
    assertThat(locale.getLocale()).isEqualTo(Locale.US);
  }

  /**
   * Test of setLocale method, of class PiwikLocale.
   */
  @Test
  @DisplayName("Test Locale")
  void testLocale() {
    locale.setLocale(Locale.GERMANY);
    assertThat(locale.getLocale()).isEqualTo(Locale.GERMANY);
  }

  /**
   * Test of toString method, of class PiwikLocale.
   */
  @Test
  @DisplayName("Test To String")
  void testToString() {
    assertThat(locale.toString()).isEqualTo("us");
  }
}
