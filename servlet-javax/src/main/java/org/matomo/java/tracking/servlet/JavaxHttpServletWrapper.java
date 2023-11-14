package org.matomo.java.tracking.servlet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;

/**
 * Converts a javax {@link HttpServletRequest} to a {@link HttpServletRequestWrapper}.
 */
public final class JavaxHttpServletWrapper {

  private JavaxHttpServletWrapper() {
    // utility
  }

  /**
   * Takes a javax {@link HttpServletRequest} and converts it to a
   * {@link HttpServletRequestWrapper}.
   *
   * @param request the request to convert to a wrapper object (must not be {@code null}).
   * @return the wrapper object (never {@code null}).
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static HttpServletRequestWrapper fromHttpServletRequest(@NonNull HttpServletRequest request) {
    Map<String, String> headers = new LinkedHashMap<>();
    request.getHeaderNames()
           .asIterator()
           .forEachRemaining(name -> headers.put(name.toLowerCase(Locale.ROOT), request.getHeader(name)));
    List<CookieWrapper> cookies = null;
    if (request.getCookies() != null) {
      cookies = Stream.of(request.getCookies())
                      .map(cookie -> new CookieWrapper(cookie.getName(), cookie.getValue()))
                      .collect(Collectors.toList());
    }
    return HttpServletRequestWrapper
        .builder()
        .requestURL(request.getRequestURL())
        .remoteAddr(request.getRemoteAddr())
        .remoteUser(request.getRemoteUser())
        .headers(headers)
        .cookies(cookies == null ? null : cookies.toArray(new CookieWrapper[0]))
        .build();
  }

}
