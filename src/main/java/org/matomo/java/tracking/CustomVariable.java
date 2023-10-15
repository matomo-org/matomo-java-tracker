/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A user defined custom variable.
 *
 * @author brettcsorba
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomVariable {

  @NonNull
  String key;

  @NonNull
  String value;

}
