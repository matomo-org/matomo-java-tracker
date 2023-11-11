package org.matomo.java.tracking;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ServletMatomoRequestCustomizerTest {

  @Test
  void addsServletRequestHeaders() {

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setHeaders(singletonMap("headerName", "headerValue"));

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequestCustomizer.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getHeaders()).hasSize(1).containsEntry("headerName", "headerValue");
  }

  @Test
  void failsIfServletRequestIsNull() {
    assertThatThrownBy(() -> ServletMatomoRequestCustomizer.fromServletRequest(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("request is marked non-null but is null");
  }

  @Test
  void failsIfBuilderIsNull() {
    assertThatThrownBy(() -> ServletMatomoRequestCustomizer.addServletRequestHeaders(
        null,
        new MockHttpServletRequest()
    )).isInstanceOf(NullPointerException.class).hasMessage("builder is marked non-null but is null");
  }

}