/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A bunch of key-value pairs that represent custom information. See <a href="https://matomo.org/faq/how-to/guide-to-using-custom-variables-deprecated/">How do I use Custom Variables?</a>
 *
 * @deprecated Should not be used according to the Matomo FAQ: <a href="https://matomo.org/faq/how-to/guide-to-using-custom-variables-deprecated/">How do I use Custom Variables?</a>
 */
@EqualsAndHashCode
@Deprecated
public class CustomVariables {

  private final Map<Integer, CustomVariable> variables = new LinkedHashMap<>();

  /**
   * Adds a custom variable to the list with the next available index.
   *
   * @param variable The custom variable to add
   * @return This object for method chaining
   */
  public CustomVariables add(@NonNull CustomVariable variable) {
    boolean found = false;
    for (Entry<Integer, CustomVariable> entry : variables.entrySet()) {
      CustomVariable customVariable = entry.getValue();
      if (customVariable.getKey().equals(variable.getKey())) {
        variables.put(entry.getKey(), variable);
        found = true;
      }
    }
    if (!found) {
      int i = 1;
      while (variables.putIfAbsent(i, variable) != null) {
        i++;
      }
    }
    return this;
  }

  /**
   * Adds a custom variable to the list with the given index.
   *
   * @param cv    The custom variable to add
   * @param index The index to add the custom variable at
   * @return This object for method chaining
   */
  public CustomVariables add(@NonNull CustomVariable cv, int index) {
    validateIndex(index);
    variables.put(index, cv);
    return this;
  }

  private static void validateIndex(int index) {
    if (index <= 0) {
      throw new IllegalArgumentException("Index must be greater than 0");
    }
  }

  @Nullable
  public CustomVariable get(int index) {
    validateIndex(index);
    return variables.get(index);
  }

  @Nullable
  public String get(@NonNull String key) {
    return variables.values().stream().filter(variable -> variable.getKey().equals(key)).findFirst()
      .map(CustomVariable::getValue).orElse(null);
  }

  public void remove(int index) {
    variables.remove(index);
  }

  public void remove(@NonNull String key) {
    variables.entrySet().removeIf(entry -> entry.getValue().getKey().equals(key));
  }

  boolean isEmpty() {
    return variables.isEmpty();
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("{");
    Iterator<Entry<Integer, CustomVariable>> iterator = variables.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<Integer, CustomVariable> entry = iterator.next();
      stringBuilder.append('"').append(entry.getKey()).append("\":[\"").append(entry.getValue().getKey())
        .append("\",\"").append(entry.getValue().getValue()).append("\"]");
      if (iterator.hasNext()) {
        stringBuilder.append(',');
      }
    }
    stringBuilder.append('}');
    return stringBuilder.toString();
  }

}
