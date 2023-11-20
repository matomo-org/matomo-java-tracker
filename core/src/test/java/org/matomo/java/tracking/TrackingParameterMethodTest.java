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
        .isInstanceOf(MatomoException.class)
        .hasMessage("Invalid value for foo. Must match regex bar");
  }

  @Test
  void doNothingIfPatternIsNull() {
    TrackingParameterMethod trackingParameterMethod =
        TrackingParameterMethod.builder().parameterName("foo").maxLength(255).build();

    trackingParameterMethod.validateParameterValue("baz");
  }

  @Test
  void doNothingIfParameterValueIsNotCharSequence() {
    TrackingParameterMethod trackingParameterMethod = TrackingParameterMethod
        .builder()
        .parameterName("foo")
        .pattern(Pattern.compile("bar"))
        .maxLength(255)
        .min(1)
        .max(1)
        .build();

    trackingParameterMethod.validateParameterValue(1);
  }

  @Test
  void failIfParameterValueIsNull() {
    TrackingParameterMethod trackingParameterMethod = TrackingParameterMethod
        .builder()
        .parameterName("foo")
        .pattern(Pattern.compile("bar"))
        .maxLength(255)
        .build();

    assertThatThrownBy(() -> trackingParameterMethod.validateParameterValue(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("parameterValue is marked non-null but is null");
  }

  @Test
  void validateParameterValueFailsIfMaxLengthIsExceeded() {
    TrackingParameterMethod trackingParameterMethod =
        TrackingParameterMethod.builder().parameterName("foo").maxLength(3).build();

    assertThatThrownBy(() -> trackingParameterMethod.validateParameterValue("foobar"))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Invalid value for foo. Must be less or equal than 3 characters");
  }

  @Test
  void failIfParameterValueIsLessThanMin() {
    TrackingParameterMethod trackingParameterMethod =
        TrackingParameterMethod.builder().parameterName("foo").min(3.0).build();

    assertThatThrownBy(() -> trackingParameterMethod.validateParameterValue(1))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Invalid value for foo. Must be greater or equal than 3");
  }

  @Test
  void failIfParameterValueIsGreaterThanMax() {
    TrackingParameterMethod trackingParameterMethod =
        TrackingParameterMethod.builder().parameterName("foo").max(3.0).build();

    assertThatThrownBy(() -> trackingParameterMethod.validateParameterValue(4))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Invalid value for foo. Must be less or equal than 3");
  }

}
