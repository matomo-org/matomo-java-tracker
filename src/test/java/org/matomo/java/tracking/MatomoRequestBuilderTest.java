package org.matomo.java.tracking;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatomoRequestBuilderTest {

  @Test
  public void buildsRequest() throws Exception {

    CustomVariable customVariable = new CustomVariable("pageCustomVariableName", "pageCustomVariableValue");
    MatomoRequest matomoRequest = MatomoRequest.builder()
      .siteId(42)
      .actionName("ACTION_NAME")
      .actionUrl("https://www.your-domain.tld/some/page?query=foo")
      .referrerUrl("https://referrer.com")
      .customTrackingParameters(Collections.singletonMap("trackingParameterName", "trackingParameterValue"))
      .pageCustomVariables(Collections.singletonList(customVariable))
      .visitCustomVariables(Collections.singletonList(customVariable))
      .build();

    Map<String, Collection<Object>> parameters = matomoRequest.getParameters();
    assertThat(parameters.get("idsite"), hasItem(42));
    assertThat(parameters.get("action_name"), hasItem("ACTION_NAME"));
    assertThat(parameters.get("apiv"), hasItem("1"));
    assertThat(parameters.get("url"), hasItem("https://www.your-domain.tld/some/page?query=foo"));
    assertThat(parameters.get("_id").isEmpty(), is(false));
    assertThat(parameters.get("rand").isEmpty(), is(false));
    assertThat(parameters.get("send_image"), hasItem(new MatomoBoolean(false)));
    assertThat(parameters.get("rec"), hasItem(new MatomoBoolean(true)));
    assertThat(parameters.get("urlref"), hasItem("https://referrer.com"));
    assertThat(parameters.get("trackingParameterName"), hasItem("trackingParameterValue"));
    CustomVariables customVariables = new CustomVariables();
    customVariables.add(customVariable);
    assertThat(parameters.get("cvar"), hasItem(customVariables));
    assertThat(parameters.get("_cvar"), hasItem(customVariables));

  }

}
