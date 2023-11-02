/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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

    assertThatThrownBy(() -> Country.fromCode("invalid")).isInstanceOf(
      IllegalArgumentException.class).hasMessage(
      "Invalid country code");

  }

  @Test
  void failsOnInvalidCountryCodeLength() {

    assertThatThrownBy(() -> Country.fromCode("invalid")).isInstanceOf(
      IllegalArgumentException.class).hasMessage(
      "Invalid country code");

  }

  @Test
  void returnsNullOnNullCode() {

    Country country = Country.fromCode(null);

    assertThat(country).isNull();

  }

  @Test
  void returnsNullOnEmptyCode() {

    Country country = Country.fromCode("");

    assertThat(country).isNull();

  }

  @Test
  void returnsNullOnBlankCode() {

    Country country = Country.fromCode(" ");

    assertThat(country).isNull();

  }

  @Test
  void returnsNullOnNullRanges() {

    Country country = Country.fromLanguageRanges(null);

    assertThat(country).isNull();

  }

  @Test
  void returnsNullOnEmptyRanges() {

    Country country = Country.fromLanguageRanges("");

    assertThat(country).isNull();

  }

  @Test
  void returnsNullOnBlankRanges() {

    Country country = Country.fromLanguageRanges(" ");

    assertThat(country).isNull();

  }

  @Test
  void failsOnInvalidRanges() {

    assertThatThrownBy(() -> Country.fromLanguageRanges("invalid")).isInstanceOf(
      IllegalArgumentException.class).hasMessage(
      "Invalid country code");

  }

  @Test
  void failsOnLocaleWithoutCountryCode() {

    assertThatThrownBy(() -> new Country(Locale.forLanguageTag("de"))).isInstanceOf(
      IllegalArgumentException.class).hasMessage(
      "Invalid locale");

  }

  @Test
  void setLocaleFailsOnNullLocale() {

    assertThatThrownBy(() -> new Country(Locale.forLanguageTag("de")).setLocale(null)).isInstanceOf(
      IllegalArgumentException.class).hasMessage(
      "Invalid locale");

  }

  @Test
  void setLocaleFailsOnNullCountryCode() {

    assertThatThrownBy(() -> new Country(Locale.forLanguageTag("de")).setLocale(Locale.forLanguageTag("de"))).isInstanceOf(
      IllegalArgumentException.class).hasMessage(
      "Invalid locale");

  }

  @Test
  void setLocaleFailsOnEmptyCountryCode() {

    assertThatThrownBy(() -> new Country(Locale.forLanguageTag("de")).setLocale(Locale.forLanguageTag("de"))).isInstanceOf(
      IllegalArgumentException.class).hasMessage(
      "Invalid locale");

  }

}
