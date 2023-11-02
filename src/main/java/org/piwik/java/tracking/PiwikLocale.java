/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.piwik.java.tracking;

import org.matomo.java.tracking.parameters.Country;

import java.util.Locale;

/**
 * A locale object that can be used to send visitor country to Matomo. This class is deprecated and will be removed in
 * the future.
 *
 * @author brettcsorba
 * @deprecated Use {@link Country} instead.
 */
@Deprecated
public class PiwikLocale extends Country {

  /**
   * Creates a new Piwik locale object with the specified locale.
   *
   * @deprecated Use {@link Country} instead.
   */
  @Deprecated
  public PiwikLocale(Locale locale) {
    super(locale);
  }
}
