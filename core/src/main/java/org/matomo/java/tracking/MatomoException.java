/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

/**
 * Thrown when an error occurs while communicating with the Matomo server or when the request is invalid.
 */
public class MatomoException extends RuntimeException {

  private static final long serialVersionUID = 4592083764365938934L;

  MatomoException(String message) {
    super(message);
  }

  MatomoException(String message, Throwable cause) {
    super(message, cause);
  }

}
