package org.matomo.java.tracking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Matomo Request Builder Test")
class MatomoRequestBuilderTest {

  @Test
  @DisplayName("Builds Request")
  void buildsRequest() {
    CustomVariable customVariable = new CustomVariable("pageCustomVariableName", "pageCustomVariableValue");
    MatomoRequest matomoRequest = MatomoRequest.builder().siteId(42).actionName("ACTION_NAME").actionUrl("https://www.your-domain.tld/some/page?query=foo").referrerUrl("https://referrer.com").customTrackingParameters(Collections.singletonMap("trackingParameterName", "trackingParameterValue")).pageCustomVariables(Collections.singletonList(customVariable)).visitCustomVariables(Collections.singletonList(customVariable)).customAction(true).build();
    Map<String, Collection<Object>> parameters = matomoRequest.getParameters();
    assertThat(parameters.get("idsite")).contains(42);
    assertThat(parameters.get("action_name")).contains("ACTION_NAME");
    assertThat(parameters.get("apiv")).contains("1");
    assertThat(parameters.get("url")).contains("https://www.your-domain.tld/some/page?query=foo");
    assertThat(parameters.get("_id").isEmpty()).isFalse();
    assertThat(parameters.get("rand").isEmpty()).isFalse();
    assertThat(parameters.get("send_image")).contains(new MatomoBoolean(false));
    assertThat(parameters.get("rec")).contains(new MatomoBoolean(true));
    assertThat(parameters.get("urlref")).contains("https://referrer.com");
    assertThat(parameters.get("trackingParameterName")).contains("trackingParameterValue");
    CustomVariables customVariables = new CustomVariables();
    customVariables.add(customVariable);
    assertThat(parameters.get("cvar")).contains(customVariables);
    assertThat(parameters.get("_cvar")).contains(customVariables);
    assertThat(parameters.get("ca")).contains(new MatomoBoolean(true));
  }
}
