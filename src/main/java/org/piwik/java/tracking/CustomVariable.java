/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import org.matomo.java.tracking.MatomoRequest;

/**
 * @author brettcsorba
 * @deprecated Use {@link org.matomo.java.tracking.CustomVariable} instead.
 */
@Deprecated
public class CustomVariable extends org.matomo.java.tracking.CustomVariable {

  /**
   * @deprecated Use {@link MatomoRequest} instead.
   */
  @Deprecated
  public CustomVariable(String key, String value) {
    super(key, value);
  }
}
