/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.piwik.java.tracking;

/**
 * A user defined custom variable.
 *
 * <p>Renamed to {@link org.matomo.java.tracking.parameters.CustomVariable} in 3.0.0.
 *
 * @author brettcsorba
 * @deprecated Use {@link org.matomo.java.tracking.parameters.CustomVariable} instead.
 */
@Deprecated
public class CustomVariable extends org.matomo.java.tracking.parameters.CustomVariable {

  /**
   * Instantiates a new custom variable.
   *
   * @param key   the key of the custom variable (required)
   * @param value the value of the custom variable (required)
   * @deprecated Use {@link org.matomo.java.tracking.parameters.CustomVariable} instead.
   */
  @Deprecated
  public CustomVariable(String key, String value) {
    super(key, value);
  }
}
