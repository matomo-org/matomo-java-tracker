/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import lombok.Value;

/**
 * Object representing a locale required by some Matomo query parameters.
 *
 * @author brettcsorba
 */
@Value
public class MatomoBoolean {
  boolean value;

  /**
   * Returns the locale's lowercase country code.
   *
   * @return the locale's lowercase country code
   */
  @Override
  public String toString() {
    return value ? "1" : "0";
  }
}
