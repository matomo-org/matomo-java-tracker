package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MatomoRequestTest {

  private MatomoRequest request = new MatomoRequest();

  @Test
  void returnsEmptyListWhenCustomTrackingParametersDoesNotContainKey() {

    request.setCustomTrackingParameter("foo", "bar");

    assertThat(request.getCustomTrackingParameter("baz")).isEmpty();
    assertThat(request.getCustomTrackingParameters()).isNotEmpty();
    assertThat(request.getCustomTrackingParameter("foo")).isNotEmpty();
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

}
