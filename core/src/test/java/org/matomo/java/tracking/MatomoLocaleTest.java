package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import org.junit.jupiter.api.Test;

class MatomoLocaleTest {

  @Test
  void createsMatomoLocaleFromLocale() {
    MatomoLocale locale = new MatomoLocale(Locale.US);
    assertThat(locale.toString()).isEqualTo("us");
  }

  @Test
  void failsIfLocaleIsNull() {
    assertThatThrownBy(() -> new MatomoLocale(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("Locale must not be null");
  }

}
