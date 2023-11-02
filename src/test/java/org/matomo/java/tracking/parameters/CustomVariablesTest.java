/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
  void testGetCustomVariableIntegerLessThan1() {
    try {
      customVariables.get(0);
      fail("Exception should have been throw.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("Index must be greater than 0");
    }
  }
}
