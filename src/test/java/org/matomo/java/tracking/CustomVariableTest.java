package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CustomVariableTest {

  @Test
  void createsCustomVariable() {
    CustomVariable customVariable = new CustomVariable("key", "value");

    assertThat(customVariable.getKey()).isEqualTo("key");
    assertThat(customVariable.getValue()).isEqualTo("value");
  }

  @Test
  void failsOnNullKey() {
    assertThatThrownBy(() -> new CustomVariable(null, "value")).isInstanceOf(
      NullPointerException.class);
  }

  @Test
  void failsOnNullValue() {
    assertThatThrownBy(() -> new CustomVariable("key", null)).isInstanceOf(
      NullPointerException.class);
  }

  @Test
  void failsOnNullKeyAndValue() {
    assertThatThrownBy(() -> new CustomVariable(null, null)).isInstanceOf(
      NullPointerException.class);
  }


}
