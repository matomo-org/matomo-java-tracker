package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class TrackingParameterMethodTest {

  @Test
  void validateParameterValueFailsIfPatternDoesNotMatch() {
    TrackingParameterMethod trackingParameterMethod = TrackingParameterMethod
        .builder()
        .parameterName("foo")
        .pattern(Pattern.compile("bar"))
        .build();

    assertThatThrownBy(() -> trackingParameterMethod.validateParameterValue("baz"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid value for foo. Must match regex bar");
  }

  @Test
  void doNothingIfPatternIsNull() {
    TrackingParameterMethod trackingParameterMethod =
        TrackingParameterMethod.builder().parameterName("foo").build();

    trackingParameterMethod.validateParameterValue("baz");
  }

  @Test
  void doNothingIfParameterValueIsNotCharSequence() {
    TrackingParameterMethod trackingParameterMethod = TrackingParameterMethod
        .builder()
        .parameterName("foo")
        .pattern(Pattern.compile("bar"))
        .build();

    trackingParameterMethod.validateParameterValue(1);
  }

  @Test
  void doNothingIfParameterValueIsNull() {
    TrackingParameterMethod trackingParameterMethod = TrackingParameterMethod
        .builder()
        .parameterName("foo")
        .pattern(Pattern.compile("bar"))
        .build();

    trackingParameterMethod.validateParameterValue(null);
  }

}
