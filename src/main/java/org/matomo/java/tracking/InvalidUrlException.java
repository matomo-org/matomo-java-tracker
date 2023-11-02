/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

/**
 * Thrown when an invalid URL is passed to the tracker.
 */
public class InvalidUrlException extends RuntimeException {

  InvalidUrlException(Throwable cause) {
    super(cause);
  }
}
