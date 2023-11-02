/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import lombok.Builder;
import lombok.Value;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Builder
@Value
class TrackingParameterMethod {

  String parameterName;

  Method method;

  Pattern pattern;

  void validateParameterValue(Object parameterValue) {
    if (pattern != null && parameterValue instanceof CharSequence && !pattern.matcher((CharSequence) parameterValue)
      .matches()) {
      throw new IllegalArgumentException(String.format(
        "Invalid value for %s. Must match regex %s",
        parameterName,
        pattern
      ));
    }
  }

}
