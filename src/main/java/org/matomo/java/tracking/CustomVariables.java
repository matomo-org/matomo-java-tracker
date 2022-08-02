/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author brettcsorba
 */
@EqualsAndHashCode
class CustomVariables {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final Map<Integer, CustomVariable> variables = new HashMap<>();

  void add(@NonNull CustomVariable variable) {
    boolean found = false;
    for (Map.Entry<Integer, CustomVariable> entry : variables.entrySet()) {
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
  }

  void add(@NonNull CustomVariable cv, int index) {
    if (index <= 0) {
      throw new IllegalArgumentException("Index must be greater than 0.");
    }
    variables.put(index, cv);
  }

  @Nullable
  CustomVariable get(int index) {
    if (index <= 0) {
      throw new IllegalArgumentException("Index must be greater than 0.");
    }
    return variables.get(index);
  }

  @Nullable
  String get(@NonNull String key) {
    return variables.values().stream().filter(variable -> variable.getKey().equals(key)).findFirst().map(CustomVariable::getValue).orElse(null);
  }

  void remove(int index) {
    variables.remove(index);
  }

  void remove(@NonNull String key) {
    variables.entrySet().removeIf(entry -> entry.getValue().getKey().equals(key));
  }

  boolean isEmpty() {
    return variables.isEmpty();
  }

  @Override
  public String toString() {
    ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
    for (Map.Entry<Integer, CustomVariable> entry : variables.entrySet()) {
      CustomVariable variable = entry.getValue();
      objectNode.putArray(entry.getKey().toString()).add(variable.getKey()).add(variable.getValue());
    }
    return objectNode.toString();
  }
}
