package org.piwik.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CustomVariableTest {

  @Test
  void createsCustomVariable() {
    CustomVariable customVariable = new CustomVariable("key", "value");

    assertThat(customVariable.getKey()).isEqualTo("key");
    assertThat(customVariable.getValue()).isEqualTo("value");
  }

}
