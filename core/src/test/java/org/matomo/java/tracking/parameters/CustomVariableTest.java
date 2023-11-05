/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomVariableTest {

  private CustomVariable customVariable;

  @BeforeEach
  void setUp() {
    customVariable = new CustomVariable("key", "value");
  }

  @Test
  void testConstructorNullKey() {
    try {
      new CustomVariable(null, null);
      fail("Exception should have been throw.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  void testConstructorNullValue() {
    try {
      new CustomVariable("key", null);
      fail("Exception should have been throw.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("value is marked non-null but is null");
    }
  }

  @Test
  void testGetKey() {
    assertThat(customVariable.getKey()).isEqualTo("key");
  }

  @Test
  void testGetValue() {
    assertThat(customVariable.getValue()).isEqualTo("value");
  }

  @Test
  void equalsCustomVariable() {
    CustomVariable variableA = new CustomVariable("a", "b");
    CustomVariable variableB = new CustomVariable("a", "b");
    assertThat(variableA).isEqualTo(variableB);
    assertThat(variableA.hashCode()).isEqualTo(variableB.hashCode());
    CustomVariable c = new CustomVariable("a", "c");
    assertThat(variableA).isNotEqualTo(c);
    assertThat(variableA.hashCode()).isNotEqualTo(c.hashCode());
    CustomVariable d = new CustomVariable("d", "b");
    assertThat(variableA).isNotEqualTo(d);
    assertThat(variableA.hashCode()).isNotEqualTo(d.hashCode());
  }

}
