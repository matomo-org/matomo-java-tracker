/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AcceptLanguageTest {

  @Test
  void fromHeader() {

    AcceptLanguage acceptLanguage = AcceptLanguage.fromHeader(
      "de,de-DE;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");

    assertThat(acceptLanguage).hasToString(
      "de,de-de;q=0.9,de-dd;q=0.9,en;q=0.8,en-gb;q=0.7,en-us;q=0.6");

  }

  @ParameterizedTest
  @NullAndEmptySource
  void fromHeaderToleratesNull(String header) {

    AcceptLanguage acceptLanguage = AcceptLanguage.fromHeader(header);

    assertThat(acceptLanguage).isNull();

  }

  @Test
  void failsOnNullLanguageRange() {
    assertThat(AcceptLanguage.builder().languageRanges(singletonList(null)).build()).hasToString(
      "");
  }

}
