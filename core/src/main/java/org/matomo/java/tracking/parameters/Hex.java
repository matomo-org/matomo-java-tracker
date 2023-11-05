/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import lombok.NonNull;

final class Hex {

  private Hex() {
    // utility class
  }

  static String fromBytes(@NonNull byte[] bytes) {
    StringBuilder result = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      result.append(String.format("%02x", b));
    }
    return result.toString();
  }

}
