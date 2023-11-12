/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class CustomVariablesTest {

  private final CustomVariables customVariables = new CustomVariables();

  @Test
  void testAdd_CustomVariable() {
    CustomVariable a = new CustomVariable("a", "b");
    assertThat(customVariables.isEmpty()).isTrue();
    customVariables.add(a);
    assertThat(customVariables.isEmpty()).isFalse();
    assertThat(customVariables.get("a")).isEqualTo("b");
    assertThat(customVariables.get(1)).isEqualTo(a);
    assertThat(customVariables).hasToString("{\"1\":[\"a\",\"b\"]}");
    CustomVariable b = new CustomVariable("c", "d");
    customVariables.add(b);
    assertThat(customVariables.get("c")).isEqualTo("d");
    assertThat(customVariables.get(2)).isEqualTo(b);
    assertThat(customVariables).hasToString("{\"1\":[\"a\",\"b\"],\"2\":[\"c\",\"d\"]}");
    CustomVariable c = new CustomVariable("a", "e");
    customVariables.add(c, 5);
    assertThat(customVariables.get("a")).isEqualTo("b");
    assertThat(customVariables.get(5)).isEqualTo(c);
    assertThat(customVariables.get(3)).isNull();
    assertThat(customVariables).hasToString("{\"1\":[\"a\",\"b\"],\"2\":[\"c\",\"d\"],\"5\":[\"a\",\"e\"]}");
    CustomVariable d = new CustomVariable("a", "f");
    customVariables.add(d);
    assertThat(customVariables.get("a")).isEqualTo("f");
    assertThat(customVariables.get(1)).isEqualTo(d);
    assertThat(customVariables.get(5)).isEqualTo(d);
    assertThat(customVariables).hasToString("{\"1\":[\"a\",\"f\"],\"2\":[\"c\",\"d\"],\"5\":[\"a\",\"f\"]}");
    customVariables.remove("a");
    assertThat(customVariables.get("a")).isNull();
    assertThat(customVariables.get(1)).isNull();
    assertThat(customVariables.get(5)).isNull();
    assertThat(customVariables).hasToString("{\"2\":[\"c\",\"d\"]}");
    customVariables.remove(2);
    assertThat(customVariables.get("c")).isNull();
    assertThat(customVariables.get(2)).isNull();
    assertThat(customVariables.isEmpty()).isTrue();
    assertThat(customVariables).hasToString("{}");
  }

  @Test
  void testAddCustomVariableIndexLessThan1() {
    try {
      customVariables.add(new CustomVariable("a", "b"), 0);
      fail("Exception should have been throw.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("Index must be greater than 0");
    }
  }

  @Test
  void equalCustomVariables() {
    CustomVariables customVariables = new CustomVariables();
    customVariables.add(new CustomVariable("a", "b"));
    customVariables.add(new CustomVariable("c", "d"));
    customVariables.add(new CustomVariable("a", "e"));
    customVariables.add(new CustomVariable("a", "f"));
    assertThat(customVariables).isEqualTo(customVariables);
    assertThat(customVariables).hasSameHashCodeAs(customVariables);
  }

  @Test
  void notEqualCustomVariables() {
    CustomVariables customVariablesA = new CustomVariables();
    customVariablesA.add(new CustomVariable("a", "b"));
    customVariablesA.add(new CustomVariable("c", "d"));
    customVariablesA.add(new CustomVariable("a", "e"));
    customVariablesA.add(new CustomVariable("a", "f"));
    CustomVariables customVariablesB = new CustomVariables();
    customVariablesB.add(new CustomVariable("a", "b"));
    customVariablesB.add(new CustomVariable("c", "d"));
    customVariablesB.add(new CustomVariable("a", "e"));
    assertThat(customVariablesA).isNotEqualTo(customVariablesB);
    assertThat(customVariablesA).doesNotHaveSameHashCodeAs(customVariablesB);
  }

  @Test
  void testAddCustomVariableNull() {
    assertThatThrownBy(() -> customVariables.add(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("variable" + " is marked non-null but is null")
        .hasNoCause();
  }

  @Test
  void testAddCustomVariableKeyEmpty() {
    assertThatThrownBy(() -> customVariables.add(new CustomVariable("", "b")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Custom variable key must not be null or empty")
        .hasNoCause();
  }

  @Test
  void testAddCustomVariableValueEmpty() {
    assertThatThrownBy(() -> customVariables.add(new CustomVariable("a", "")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Custom variable value must not be null or empty")
        .hasNoCause();
  }

  @Test
  void testAddCustomVariableNullIndex() {
    assertThatThrownBy(() -> customVariables.add(new CustomVariable("a", "b"), 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Index must be greater than 0")
        .hasNoCause();
  }


  @Test
  void testAddNullCustomVariableIndex() {
    assertThatThrownBy(() -> customVariables.add(null, 1))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("cv is marked non-null but is null")
        .hasNoCause();
  }

  @Test
  void testGetCustomVariableIntegerNull() {
    assertThatThrownBy(() -> customVariables.get(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Index must be greater than 0")
        .hasNoCause();
  }

  @Test
  void testGetCustomVariableKeyNull() {
    assertThatThrownBy(() -> customVariables.get(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("key is marked non-null but is null")
        .hasNoCause();
  }

  @Test
  void testGetCustomVariableKeyEmpty() {
    assertThatThrownBy(() -> customVariables.get(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("key must not be null or empty")
        .hasNoCause();
  }

  @Test
  void testRemoveCustomVariableKeyNull() {
    assertThatThrownBy(() -> customVariables.remove(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("key is marked non-null but is null")
        .hasNoCause();
  }

  @Test
  void parseCustomVariables() {
    CustomVariables customVariables =
        CustomVariables.parse(
            "{\"1\":[\"VAR 1 set, var 2 not set\",\"yes\"],\"3\":[\"var 3 set\",\"yes!!!!\"]}");
    assertThat(customVariables.get(1).getKey()).isEqualTo("VAR 1 set, var 2 not set");
    assertThat(customVariables.get(1).getValue()).isEqualTo("yes");
    assertThat(customVariables.get(3).getKey()).isEqualTo("var 3 set");
    assertThat(customVariables.get(3).getValue()).isEqualTo("yes!!!!");
  }

}
