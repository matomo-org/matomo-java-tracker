package org.matomo.java.tracking;

import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.parameters.CustomVariables;

import java.util.Collections;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;


class MatomoRequestBuilderTest {

  @Test
  void buildsRequest() {
    CustomVariable pageCustomVariable = new CustomVariable("pageCustomVariableName", "pageCustomVariableValue");
    CustomVariable visitCustomVariable = new CustomVariable("visitCustomVariableName", "visitCustomVariableValue");

    MatomoRequest matomoRequest = MatomoRequest.builder().siteId(42).actionName("ACTION_NAME")
      .actionUrl("https://www.your-domain.tld/some/page?query=foo").referrerUrl("https://referrer.com")
      .customTrackingParameters(Collections.singletonMap("trackingParameterName", singleton("trackingParameterValue")))
      .pageCustomVariables(new CustomVariables().add(pageCustomVariable, 2))
      .visitCustomVariables(new CustomVariables().add(visitCustomVariable, 3)).customAction(true).build();

    assertThat(matomoRequest.getSiteId()).isEqualTo(42);
    assertThat(matomoRequest.getActionName()).isEqualTo("ACTION_NAME");
    assertThat(matomoRequest.getApiVersion()).isEqualTo("1");
    assertThat(matomoRequest.getActionUrl()).isEqualTo("https://www.your-domain.tld/some/page?query=foo");
    assertThat(matomoRequest.getVisitorId().toString()).hasSize(16).isHexadecimal();
    assertThat(matomoRequest.getRandomValue().toString()).hasSize(20).isHexadecimal();
    assertThat(matomoRequest.getResponseAsImage()).isFalse();
    assertThat(matomoRequest.getRequired()).isTrue();
    assertThat(matomoRequest.getReferrerUrl()).isEqualTo("https://referrer.com");
    assertThat(matomoRequest.getCustomTrackingParameter("trackingParameterName"))
      .containsExactly("trackingParameterValue");
    assertThat(matomoRequest.getPageCustomVariables())
      .hasToString("{\"2\":[\"pageCustomVariableName\",\"pageCustomVariableValue\"]}");
    assertThat(matomoRequest.getVisitCustomVariables())
      .hasToString("{\"3\":[\"visitCustomVariableName\",\"visitCustomVariableValue\"]}");
    assertThat(matomoRequest.getCustomAction()).isTrue();

  }
}
