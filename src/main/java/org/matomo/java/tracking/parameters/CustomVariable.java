/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * A key-value pair that represents custom information. See
 * <a href="https://matomo.org/faq/how-to/guide-to-using-custom-variables-deprecated/">How do I use Custom Variables?</a>
 *
 * <p>If you are not already using Custom Variables to measure your custom data, Matomo recommends to use the
 * <a href="https://matomo.org/docs/custom-dimensions/">Custom Dimensions feature</a> instead.
 * There are many <a href="https://matomo.org/faq/general/faq_21117/">advantages of Custom Dimensions over Custom
 * variables</a>. Custom variables will be deprecated in the future.
 *
 * @deprecated Should not be used according to the Matomo FAQ: <a href="https://matomo.org/faq/how-to/guide-to-using-custom-variables-deprecated/">How do I use Custom Variables?</a>
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "index")
@Deprecated
public class CustomVariable {

  private int index;

  @NonNull
  private String key;

  @NonNull
  private String value;

  /**
   * Instantiates a new custom variable.
   *
   * @param key   the key of the custom variable (required)
   * @param value the value of the custom variable (required)
   */
  public CustomVariable(@NonNull String key, @NonNull String value) {
    this.key = key;
    this.value = value;
  }
}



