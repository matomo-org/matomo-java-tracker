package org.matomo.java.tracking.servlet;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.matomo.java.tracking.MatomoRequest;

class ServletMatomoRequestTest {

  @Test
  void addsServletRequestHeaders() {

    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .headers(singletonMap("headername", "headerValue"))
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getHeaders()).hasSize(1).containsEntry("headername", "headerValue");
  }

  @Test
  void skipsEmptyHeaderNames() {

    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .headers(singletonMap("", "headerValue"))
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getHeaders()).isEmpty();

  }

  @Test
  void skipsBlankHeaderNames() {

    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .headers(singletonMap(" ", "headerValue"))
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getHeaders()).isEmpty();

  }

  @ParameterizedTest
  @ValueSource(strings = {"connection", "content-length", "expect", "host", "upgrade"})
  void doesNotAddRestrictedHeaders(String restrictedHeader) {
    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .headers(singletonMap(restrictedHeader, "headerValue"))
        .build();

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
        HttpServletRequestWrapper.builder().build()
    ))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("builder is marked non-null but is null");
  }

  @Test
  void extractsVisitorIdFromCookie() {
    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .cookies(new CookieWrapper[] {
          new CookieWrapper("_pk_id.1.1fff", "be40d677d6c7270b.1699801331.")
        })
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getVisitorId()).hasToString("be40d677d6c7270b");
    assertThat(matomoRequest.getCookies())
        .hasSize(1)
        .containsEntry("_pk_id.1.1fff", "be40d677d6c7270b.1699801331.");
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"_pk_ses.1.1fff", "_pk_ref.1.1fff", "_pk_hsr.1.1fff"}
  )
  void extractsMatomoCookies(String cookieName) {
    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .cookies(new CookieWrapper[] {new CookieWrapper(cookieName, "anything")})
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getCookies()).hasSize(1).containsEntry(cookieName, "anything");
  }

  @Test
  void extractsSessionIdFromMatomoSessIdCookie() {
    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .cookies(new CookieWrapper[] {
          new CookieWrapper(
              "MATOMO_SESSID",
              "2cbf8b5ba00fbf9ba70853308cd0944a"
          )
        })
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getSessionId()).isEqualTo("2cbf8b5ba00fbf9ba70853308cd0944a");
  }

  @Test
  void parsesVisitCustomVariablesFromCookie() {
    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .cookies(new CookieWrapper[] {
          new CookieWrapper(
              "_pk_cvar.1.1fff",
              "{\"1\":[\"VAR 1 set, var 2 not set\",\"yes\"],\"3\":[\"var 3 set\",\"yes!!!!\"]}"
          )
        })
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getVisitCustomVariables().get(1).getKey()).isEqualTo(
        "VAR 1 set, var 2 not set");
    assertThat(matomoRequest.getVisitCustomVariables().get(1).getValue()).isEqualTo("yes");
    assertThat(matomoRequest.getVisitCustomVariables().get(3).getKey()).isEqualTo("var 3 set");
    assertThat(matomoRequest.getVisitCustomVariables().get(3).getValue()).isEqualTo("yes!!!!");

  }

  @Test
  void determinerVisitorIpFromXForwardedForHeader() {
    HttpServletRequestWrapper request = HttpServletRequestWrapper
        .builder()
        .headers(singletonMap("x-forwarded-for", "44.55.66.77"))
        .build();

    MatomoRequest.MatomoRequestBuilder builder = ServletMatomoRequest.fromServletRequest(request);

    MatomoRequest matomoRequest = builder.build();
    assertThat(matomoRequest.getVisitorIp()).isEqualTo("44.55.66.77");
  }

}