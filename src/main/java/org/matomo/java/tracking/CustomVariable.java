/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import lombok.NonNull;

/**
 * A user defined custom variable.
 *
 * @author brettcsorba
 * @deprecated Use {@link org.matomo.java.tracking.parameters.EcommerceItem} instead.
 */
@Deprecated
public class CustomVariable extends org.matomo.java.tracking.parameters.CustomVariable {

  public CustomVariable(@NonNull String key, @NonNull String value) {
    super(key, value);
  }
}
