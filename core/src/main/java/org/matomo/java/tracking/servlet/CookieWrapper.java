package org.matomo.java.tracking.servlet;

import lombok.Value;

/**
 * Wrapper for the cookie name and value.
 */
@Value
public class CookieWrapper {

  String name;

  String value;

}
