/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import lombok.NonNull;

final class Hex {

  private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

  private Hex() {
    // utility class
  }

  static String fromBytes(@NonNull byte[] bytes) {
    StringBuilder result = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      result.append(HEX_CHARS[(b >> 4) & 0xF]).append(HEX_CHARS[b & 0xF]);
    }
    return result.toString();
  }
}
