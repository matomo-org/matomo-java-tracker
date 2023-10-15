package org.matomo.java.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Katie
 */
@DisplayName("Custom Variable Test")
class CustomVariableTest {

  private CustomVariable customVariable;

  @BeforeEach
  void setUp() {
    customVariable = new CustomVariable("key", "value");
  }

  @Test
  @DisplayName("Test Constructor Null Key")
  void testConstructorNullKey() {
    try {
      new CustomVariable(null, null);
      fail("Exception should have been throw.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Constructor Null Value")
  void testConstructorNullValue() {
    try {
      new CustomVariable("key", null);
      fail("Exception should have been throw.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("value is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Get Key")
  void testGetKey() {
    assertThat(customVariable.getKey()).isEqualTo("key");
  }

  @Test
  @DisplayName("Test Get Value")
  void testGetValue() {
    assertThat(customVariable.getValue()).isEqualTo("value");
  }
}
