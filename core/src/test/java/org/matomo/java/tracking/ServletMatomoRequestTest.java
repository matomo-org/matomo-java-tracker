package org.matomo.java.tracking;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ServletMatomoRequestTest {

  @Test
  void addsServletRequestHeaders() {

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setHeaders(singletonMap("headerName", "headerValue"));

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getHeaders()).hasSize(1).containsEntry("headerName", "headerValue");
  }

  @ParameterizedTest
  @ValueSource(strings = {"connection", "content-length", "expect", "host", "upgrade"})
  void doesNotAddRestrictedHeaders(String restrictedHeader) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setHeaders(singletonMap(restrictedHeader, "headerValue"));

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getHeaders()).isEmpty();
  }

  @Test
  void failsIfServletRequestIsNull() {
    assertThatThrownBy(() -> ServletMatomoRequest.fromServletRequest(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("request is marked non-null but is null");
  }

  @Test
  void failsIfBuilderIsNull() {
    assertThatThrownBy(() -> ServletMatomoRequest.addServletRequestHeaders(
        null,
        new MockHttpServletRequest()
    )).isInstanceOf(NullPointerException.class).hasMessage("builder is marked non-null but is null");
  }

}