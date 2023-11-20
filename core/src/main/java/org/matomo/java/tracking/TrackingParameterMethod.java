/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
class TrackingParameterMethod {

  String parameterName;

  Method method;

  Pattern pattern;

  double min;

  double max;

  int maxLength;

  void validateParameterValue(@NonNull Object parameterValue) {
    if (pattern != null && parameterValue instanceof CharSequence && !pattern
        .matcher((CharSequence) parameterValue)
        .matches()) {
      throw new MatomoException(String.format("Invalid value for %s. Must match regex %s",
          parameterName,
          pattern
      ));
    }
    if (maxLength != 0 && parameterValue.toString().length() > maxLength) {
      throw new MatomoException(String.format("Invalid value for %s. Must be less or equal than %d characters",
          parameterName,
          maxLength
      ));
    }
    if (parameterValue instanceof Number) {
      Number number = (Number) parameterValue;
      if (number.doubleValue() < min) {
        throw new MatomoException(String.format(
            "Invalid value for %s. Must be greater or equal than %s",
            parameterName,
            min % 1 == 0 ? Long.toString((long) min) : min
        ));

      }
      if (number.doubleValue() > max) {
        throw new MatomoException(String.format(
            "Invalid value for %s. Must be less or equal than %s",
            parameterName,
            max % 1 == 0 ? Long.toString((long) max) : max
        ));
      }
    }
  }

}
