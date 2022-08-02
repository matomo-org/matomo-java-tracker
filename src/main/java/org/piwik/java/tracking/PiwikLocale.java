/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import org.matomo.java.tracking.MatomoLocale;

import java.util.Locale;

/**
 * @author brettcsorba
 * @deprecated Use {@link org.matomo.java.tracking.MatomoLocale} instead.
 */
@Deprecated
public class PiwikLocale extends MatomoLocale {

  /**
   * @deprecated Use {@link MatomoLocale} instead.
   */
  @Deprecated
  public PiwikLocale(Locale locale) {
    super(locale);
  }
}
