/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * Object representing a locale required by some Matomo query parameters.
 *
 * @author brettcsorba
 */
@Setter
@Getter
@AllArgsConstructor
public class MatomoLocale {
  private Locale locale;

  /**
   * Returns the locale's lowercase country code.
   *
   * @return the locale's lowercase country code
   */
  @Override
  public String toString() {
    return locale.getCountry().toLowerCase(Locale.ENGLISH);
  }
}
