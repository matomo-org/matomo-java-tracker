package org.matomo.java.tracking.servlet;

import static java.util.Arrays.asList;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.parameters.CustomVariables;
import org.matomo.java.tracking.parameters.VisitorId;


/**
 * Adds the headers from a {@link HttpServletRequestWrapper} to a
 * {@link MatomoRequest.MatomoRequestBuilder}.
 *
 * <p>Use #fromServletRequest(HttpServletRequestWrapper) to create a new builder with the headers from the
 * request or #addServletRequestHeaders(MatomoRequest.MatomoRequestBuilder, HttpServletRequestWrapper) to
 * add the headers to an existing builder.
 */
public final class ServletMatomoRequest {

  /**
   * Please ensure these values are always lower case.
   */
  private static final Set<String> RESTRICTED_HEADERS =
      Collections.unmodifiableSet(new HashSet<>(asList(
          "connection",
          "content-length",
          "expect",
          "host",
          "upgrade"
      )));

  private ServletMatomoRequest() {
    // should not be instantiated
  }

  /**
   * Creates a new builder with the headers from the request.
   *
   * <p>Use #addServletRequestHeaders(MatomoRequest.MatomoRequestBuilder, HttpServletRequestWrapper) to
   * add the headers to an existing builder.
   *
   * @param request the request to get the headers from (must not be null)
   *
   * @return a new builder with the headers from the request (never null)
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder fromServletRequest(@NonNull HttpServletRequestWrapper request) {
    return addServletRequestHeaders(MatomoRequest.request(), request);
  }

  /**
   * Adds the headers from the request to an existing builder.
   *
   * <p>Use #fromServletRequest(HttpServletRequestWrapper) to create a new builder with the headers from
   * the request.
   *
   * @param builder the builder to add the headers to (must not be null)
   * @param request the request to get the headers from (must not be null)
   *
   * @return the builder with the headers added (never null)
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder addServletRequestHeaders(
      @NonNull MatomoRequest.MatomoRequestBuilder builder, @NonNull HttpServletRequestWrapper request
  ) {
    return builder
        .actionUrl(request.getRequestURL() == null ? null : request.getRequestURL().toString())
        .headers(collectHeaders(request))
        .visitorIp(determineVisitorIp(request))
        .cookies(processCookies(builder, request));
  }

  @edu.umd.cs.findbugs.annotations.NonNull
  private static Map<String, String> collectHeaders(
      @edu.umd.cs.findbugs.annotations.NonNull HttpServletRequestWrapper request
  ) {
    Map<String, String> headers = new HashMap<>(10);
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      if (headerName != null && !headerName.trim().isEmpty() && !RESTRICTED_HEADERS.contains(
          headerName.toLowerCase(
              Locale.ROOT))) {
        headers.put(headerName, request.getHeader(headerName));
      }
    }
    return headers;
  }

  @Nullable
  private static String determineVisitorIp(
      @edu.umd.cs.findbugs.annotations.NonNull HttpServletRequestWrapper request
  ) {
    String realIpHeader = request.getHeader("X-Real-Ip");
    if (isNotEmpty(realIpHeader)) {
      return realIpHeader;
    }
    String forwardedForHeader = request.getHeader("X-Forwarded-For");
    if (isNotEmpty(forwardedForHeader)) {
      return forwardedForHeader;
    }
    if (isNotEmpty(request.getRemoteAddr())) {
      return request.getRemoteAddr();
    }
    return null;
  }

  @edu.umd.cs.findbugs.annotations.NonNull
  private static Map<String, String> processCookies(
      @edu.umd.cs.findbugs.annotations.NonNull MatomoRequest.MatomoRequestBuilder builder,
      @edu.umd.cs.findbugs.annotations.NonNull HttpServletRequestWrapper request
  ) {
    Map<String, String> cookies = new LinkedHashMap<>(3);
    if (request.getCookies() != null) {
      builder.supportsCookies(Boolean.TRUE);
      for (CookieWrapper cookie : request.getCookies()) {
        if (isNotEmpty(cookie.getValue())) {
          processCookie(builder, cookies, cookie.getName(), cookie.getValue());
        }
      }
    }
    return cookies;
  }

  private static boolean isNotEmpty(String forwardedForHeader) {
    return forwardedForHeader != null && !forwardedForHeader.trim().isEmpty();
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
}