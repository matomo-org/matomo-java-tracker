/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import org.matomo.java.tracking.MatomoDate;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @author brettcsorba
 * @deprecated Use {@link MatomoDate} instead.
 */
@Deprecated
public class PiwikDate extends MatomoDate {

  /**
   * @author brettcsorba
   * @deprecated Use {@link MatomoDate} instead.
   */
  public PiwikDate() {
    super();
  }

  /**
   * @author brettcsorba
   * @deprecated Use {@link MatomoDate} instead.
   */
  public PiwikDate(long epochMilli) {
    super(epochMilli);
  }

  /**
   * @author brettcsorba
   * @deprecated Use {@link MatomoDate#setTimeZone(ZoneId)} instead.
   */
  @Deprecated
  public void setTimeZone(TimeZone zone) {
    setTimeZone(zone.toZoneId());
  }

}
