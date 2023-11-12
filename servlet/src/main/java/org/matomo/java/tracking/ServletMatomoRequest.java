package org.matomo.java.tracking;

import static java.util.Arrays.asList;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;
import org.matomo.java.tracking.parameters.CustomVariables;
import org.matomo.java.tracking.parameters.VisitorId;


/**
 * Adds the headers from a {@link HttpServletRequest} to a {@link MatomoRequest.MatomoRequestBuilder}.
 *
 * <p>Use #fromServletRequest(HttpServletRequest) to create a new builder with the headers from the request or
 * #addServletRequestHeaders(MatomoRequest.MatomoRequestBuilder, HttpServletRequest) to add the headers to an existing
 * builder.
 */
public final class ServletMatomoRequest {

  /**
   * Please ensure these values are always lower case.
   */
  private static final Set<String> RESTRICTED_HEADERS =
      Collections.unmodifiableSet(new HashSet<>(asList("connection", "content-length", "expect", "host", "upgrade")));

  private ServletMatomoRequest() {
    // should not be instantiated
  }

  /**
   * Creates a new builder with the headers from the request.
   *
   * <p>Use #addServletRequestHeaders(MatomoRequest.MatomoRequestBuilder, HttpServletRequest) to add the headers to an
   * existing builder.
   *
   * @param request the request to get the headers from (must not be null)
   * @return a new builder with the headers from the request (never null)
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder fromServletRequest(@NonNull HttpServletRequest request) {
    return addServletRequestHeaders(MatomoRequest.request(), request);
  }

  /**
   * Adds the headers from the request to an existing builder.
   *
   * <p>Use #fromServletRequest(HttpServletRequest) to create a new builder with the headers from the request.
   *
   * @param builder the builder to add the headers to (must not be null)
   * @param request the request to get the headers from (must not be null)
   * @return the builder with the headers added (never null)
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder addServletRequestHeaders(
      @NonNull MatomoRequest.MatomoRequestBuilder builder, @NonNull HttpServletRequest request
  ) {
    return builder
        .actionUrl(request.getRequestURL() == null ? null : request.getRequestURL().toString())
        .headers(collectHeaders(request))
        .visitorIp(request.getRemoteAddr())
        .cookies(processCookies(builder, request));
  }

  @edu.umd.cs.findbugs.annotations.NonNull
  private static Map<String, String> processCookies(
      @edu.umd.cs.findbugs.annotations.NonNull MatomoRequest.MatomoRequestBuilder builder,
      @edu.umd.cs.findbugs.annotations.NonNull HttpServletRequest request
  ) {
    Map<String, String> cookies = new LinkedHashMap<>(3);
    if (request.getCookies() != null) {
      builder.supportsCookies(Boolean.TRUE);
      for (Cookie cookie : request.getCookies()) {
        if (cookie.getValue() != null && !cookie.getValue().trim().isEmpty()) {
          processCookie(builder, cookies, cookie.getName(), cookie.getValue());
        }
      }
    }
    return cookies;
  }

  private static void processCookie(
      @edu.umd.cs.findbugs.annotations.NonNull MatomoRequest.MatomoRequestBuilder builder,
      @edu.umd.cs.findbugs.annotations.NonNull Map<String, String> cookies,
      @edu.umd.cs.findbugs.annotations.NonNull String cookieName,
      @edu.umd.cs.findbugs.annotations.NonNull String cookieValue
  ) {
    if (cookieName.toLowerCase(Locale.ROOT).startsWith("_pk_id")) {
      extractVisitorId(builder, cookies, cookieValue, cookieName);
    }
    if (cookieName.toLowerCase(Locale.ROOT).equalsIgnoreCase("MATOMO_SESSID")) {
      builder.sessionId(cookieValue);
    }
    if (cookieName.toLowerCase(Locale.ROOT).startsWith("_pk_ses")
        || cookieName.toLowerCase(Locale.ROOT).startsWith("_pk_ref")
        || cookieName.toLowerCase(Locale.ROOT).startsWith("_pk_hsr")) {
      cookies.put(cookieName, cookieValue);
    }
    if (cookieName.toLowerCase(Locale.ROOT).startsWith("_pk_cvar")) {
      builder.visitCustomVariables(CustomVariables.parse(cookieValue));
    }
  }

  private static void extractVisitorId(
      @edu.umd.cs.findbugs.annotations.NonNull MatomoRequest.MatomoRequestBuilder builder,
      @edu.umd.cs.findbugs.annotations.NonNull Map<String, String> cookies,
      @edu.umd.cs.findbugs.annotations.NonNull String cookieValue,
      @edu.umd.cs.findbugs.annotations.NonNull String cookieName
  ) {
    String[] cookieValues = cookieValue.split("\\.");
    if (cookieValues.length > 0) {
      builder.visitorId(VisitorId.fromHex(cookieValues[0])).newVisitor(Boolean.FALSE);
      cookies.put(cookieName, cookieValue);
    }
  }

  @edu.umd.cs.findbugs.annotations.NonNull
  private static Map<String, String> collectHeaders(
      @edu.umd.cs.findbugs.annotations.NonNull HttpServletRequest request
  ) {
    Map<String, String> headers = new HashMap<>(10);
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      if (headerName != null && !headerName.trim().isEmpty() && !RESTRICTED_HEADERS.contains(headerName.toLowerCase(
          Locale.ROOT))) {
        headers.put(headerName, request.getHeader(headerName));
      }
    }
    return headers;
  }
}