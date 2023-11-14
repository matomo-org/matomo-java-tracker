package org.matomo.java.tracking.servlet;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Wraps a HttpServletRequest to be compatible with both the Jakarta and the Java EE API.
 */
@Builder
@Value
public class HttpServletRequestWrapper {

  @Nullable
  StringBuffer requestURL;

  @Nullable
  String remoteAddr;

  @Nullable
  String remoteUser;

  @Nullable
  Map<String, String> headers;

  @Nullable
  CookieWrapper[] cookies;

  /**
   * Returns an enumeration of all the header names this request contains. If the request has no
   * headers, this method returns an empty enumeration.
   *
   * @return an enumeration of all the header names sent with this request
   */
  public Enumeration<String> getHeaderNames() {
    return headers == null ? Collections.emptyEnumeration() :
        Collections.enumeration(headers.keySet());
  }

  /**
   * Returns the value of the specified request header as a String. If the request did not include a
   * header of the specified name, this method returns null. If there are multiple headers with the
   * same name, this method returns the last header in the request. The header name is case
   * insensitive. You can use this method with any request header.
   *
   * @param name a String specifying the header name (case insensitive) - must not be {@code null}.
   * @return a String containing the value of the requested header, or null if the request does not
   *        have a header of that name
   */
  @Nullable
  public String getHeader(@NonNull String name) {
    return headers == null ? null : headers.get(name.toLowerCase(Locale.ROOT));
  }

}
