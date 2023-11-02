/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

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
    if (variable.getKey().isEmpty()) {
      throw new IllegalArgumentException("Custom variable key must not be null or empty");
    }
    if (variable.getValue().isEmpty()) {
      throw new IllegalArgumentException("Custom variable value must not be null or empty");
    }
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

  /**
   * Returns the custom variable at the given index.
   *
   * @param index The index of the custom variable
   * @return The custom variable at the given index
   */
  @Nullable
  public CustomVariable get(int index) {
    validateIndex(index);
    return variables.get(index);
  }

  /**
   * Returns the value of the custom variable with the given key. If there are multiple custom variables with the same
   * key, the first one is returned. If there is no custom variable with the given key, null is returned.
   *
   * @param key The key of the custom variable. Must not be null.
   * @return The value of the custom variable with the given key. null if there is no variable with the given key.
   */
  @Nullable
  public String get(@NonNull String key) {
    if (key.isEmpty()) {
      throw new IllegalArgumentException("key must not be null or empty");
    }
    return variables.values().stream().filter(variable -> variable.getKey().equals(key)).findFirst()
      .map(CustomVariable::getValue).orElse(null);
  }

  /**
   * Removes the custom variable at the given index. If there is no custom variable at the given index, nothing happens.
   *
   * @param index The index of the custom variable to remove. Must be greater than 0.
   */
  public void remove(int index) {
    validateIndex(index);
    variables.remove(index);
  }

  /**
   * Removes the custom variable with the given key. If there is no custom variable with the given key, nothing happens.
   *
   * @param key The key of the custom variable to remove. Must not be null.
   */
  public void remove(@NonNull String key) {
    variables.entrySet().removeIf(entry -> entry.getValue().getKey().equals(key));
  }

  boolean isEmpty() {
    return variables.isEmpty();
  }

  /**
   * Creates a JSON representation of the custom variables. The format is as follows:
   * {@code {"1":["key1","value1"],"2":["key2","value2"]}}
   *
   * @return A JSON representation of the custom variables
   */
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
