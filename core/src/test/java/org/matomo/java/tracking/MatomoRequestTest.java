package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MatomoRequestTest {

  private MatomoRequest request = new MatomoRequest();

  @Test
  void returnsEmptyListWhenCustomTrackingParametersDoesNotContainKey() {

    request.setCustomTrackingParameter("foo", "bar");

    assertThat(request.getCustomTrackingParameter("baz")).isNull();
    assertThat(request.getAdditionalParameters()).isNotEmpty();
    assertThat(request.getCustomTrackingParameter("foo")).isEqualTo("bar");
  }

  @Test
  void getPageCustomVariableReturnsNullIfPageCustomVariablesIsNull() {
    assertThat(request.getPageCustomVariable("foo")).isNull();
  }

  @Test
  void getPageCustomVariableReturnsValueIfPageCustomVariablesIsNotNull() {
    request.setPageCustomVariable("foo", "bar");
    assertThat(request.getPageCustomVariable("foo")).isEqualTo("bar");
  }

  @Test
  void setPageCustomVariableRequiresNonNullKey() {
    assertThatThrownBy(() -> request.setPageCustomVariable(null, "bar")).isInstanceOf(
        NullPointerException.class);
  }

  @Test
  void setPageCustomVariableDoesNothingIfValueIsNull() {
    request.setPageCustomVariable("foo", null);
    assertThat(request.getPageCustomVariable("foo")).isNull();
  }

  @Test
  void setPageCustomVariableRemovesValueIfValueIsNull() {
    request.setPageCustomVariable("foo", "bar");
    request.setPageCustomVariable("foo", null);
    assertThat(request.getPageCustomVariable("foo")).isNull();
  }

  @Test
  void setPageCustomVariableAddsCustomVariableIfValueIsNotNull() {
    request.setPageCustomVariable("foo", "bar");
    assertThat(request.getPageCustomVariable("foo")).isEqualTo("bar");
  }

  @Test
  void setPageCustomVariableDoesNothingIfCustomVariableParameterIsNullAndIndexIsPositive() {
    request.setPageCustomVariable(null, 1);
    assertThat(request.getPageCustomVariable(1)).isNull();
  }

  @Test
  void setPageCustomVariableInitializesPageCustomVariablesIfCustomVariableParameterIsNullAndIndexIsPositive() {
    request.setPageCustomVariable(new CustomVariable("key", "value"), 1);
    assertThat(request.getPageCustomVariables()).isNotNull();
  }

  @Test
  void setUserCustomVariableDoesNothingIfValueIsNull() {
    request.setUserCustomVariable("foo", null);
    assertThat(request.getUserCustomVariable("foo")).isNull();
  }

  @Test
  void setUserCustomVariableRemovesValueIfValueIsNull() {
    request.setUserCustomVariable("foo", "bar");
    request.setUserCustomVariable("foo", null);
    assertThat(request.getUserCustomVariable("foo")).isNull();
  }

  @Test
  void setVisitCustomVariableDoesNothingIfCustomVariableParameterIsNullAndIndexIsPositive() {
    request.setVisitCustomVariable(null, 1);
    assertThat(request.getVisitCustomVariable(1)).isNull();
  }

  @Test
  void setVisitCustomVariableInitializesVisitCustomVariablesIfCustomVariableParameterIsNullAndIndexIsPositive() {
    request.setVisitCustomVariable(new CustomVariable("key", "value"), 1);
    assertThat(request.getVisitCustomVariables()).isNotNull();
  }

  @Test
  void setsCustomParameter() {
    request.setParameter("foo", 1);
    assertThat(request.getCustomTrackingParameter("foo")).isEqualTo(1);
  }

  @Test
  void failsToSetCustomParameterIfKeyIsNull() {
    assertThatThrownBy(() -> request.setParameter(
        null,
        1
    )).isInstanceOf(NullPointerException.class);
  }

  @Test
  void doesNothingWhenSettingCustomParameterIfValueIsNull() {
    request.setParameter("foo", null);
    assertThat(request.getAdditionalParameters()).isNull();
  }

  @Test
  void removesCustomParameter() {
    request.setParameter("foo", 1);
    request.setParameter("foo", null);
    assertThat(request.getAdditionalParameters()).isEmpty();
  }

  @Test
  void setsDeviceResolutionString() {
    request.setDeviceResolution("1920x1080");
    assertThat(request.getDeviceResolution().toString()).isEqualTo("1920x1080");
  }

  @Test
  void failsIfSetParameterParameterNameIsBlank() {
    assertThatThrownBy(() -> request.setParameter(" ", "bar")).isInstanceOf(
        IllegalArgumentException.class);
  }

}
