package org.matomo.java.tracking;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;


/**
 * Adds the headers from a {@link HttpServletRequest} to a {@link MatomoRequest.MatomoRequestBuilder}.
 *
 * <p>Use #fromServletRequest(HttpServletRequest) to create a new builder with the headers from the request or
 * #addServletRequestHeaders(MatomoRequest.MatomoRequestBuilder, HttpServletRequest) to add the headers to an existing
 * builder.
 */
public final class ServletMatomoRequestCustomizer {

  private ServletMatomoRequestCustomizer() {
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
    Map<String, String> headers = new HashMap<>(10);
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      headers.put(headerName, request.getHeader(headerName));
    }
    return builder.headers(headers);
  }
}