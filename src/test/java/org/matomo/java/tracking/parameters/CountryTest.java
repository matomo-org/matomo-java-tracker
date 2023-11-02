/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CountryTest {

  @Test
  void createsCountryFromCode() {

    Country country = Country.fromCode("DE");

    assertThat(country).hasToString("de");

  }

  @Test
  void createsCountryFromAcceptLanguageHeader() {

    Country country = Country.fromLanguageRanges("en-GB;q=0.7,de,de-DE;q=0.9,en;q=0.8,en-US;q=0.6");

    assertThat(country).hasToString("de");

  }

  @ParameterizedTest
  @NullAndEmptySource
  void returnsNullOnEmptyRanges(String ranges) {

    Country country = Country.fromLanguageRanges(ranges);

    assertThat(country).isNull();

  }

  @Test
  void failsOnInvalidCountryCode() {

    assertThatThrownBy(() -> Country.fromCode("invalid")).isInstanceOf(IllegalArgumentException.class).hasMessage(
      "Invalid country code");

  }

}
