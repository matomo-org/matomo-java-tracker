/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.piwik.java.tracking;

import org.matomo.java.tracking.MatomoDate;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * A date object that can be used to send dates to Matomo. This class is deprecated and will be removed in a future.
 *
 * @author brettcsorba
 * @deprecated Please use {@link Instant}
 */
@Deprecated
public class PiwikDate extends MatomoDate {

  /**
   * Creates a new date object with the current time.
   *
   * @deprecated Use {@link Instant} instead.
   */
  public PiwikDate() {
  }

  /**
   * Creates a new date object with the specified time. The time is specified in milliseconds since the epoch.
   *
   * @param epochMilli The time in milliseconds since the epoch
   * @deprecated Use {@link Instant} instead.
   */
  public PiwikDate(long epochMilli) {
    super(epochMilli);
  }

  /**
   * Sets the time zone for this date object. This is used to convert the date to UTC before sending it to Matomo.
   *
   * @param zone the time zone to use
   * @deprecated Use {@link ZonedDateTime#toInstant()} instead.
   */
  @Deprecated
  public void setTimeZone(TimeZone zone) {
    setTimeZone(zone.toZoneId());
  }

}
