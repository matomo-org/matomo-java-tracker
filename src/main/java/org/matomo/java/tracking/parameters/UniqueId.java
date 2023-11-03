/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * A six character unique ID consisting of the characters [0-9a-Z].
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class UniqueId {

  private static final String CHARS =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  private static final Random RANDOM = new SecureRandom();

  private final long value;

  /**
   * Static factory to generate a random unique id.
   *
   * @return A randomly generated unique id
   */
  public static UniqueId random() {
    return fromValue(RANDOM.nextLong());
  }

  /**
   * Creates a unique id from a number.
   *
   * @param value A number to create this unique id from
   * @return The unique id for the given value
   */
  public static UniqueId fromValue(long value) {
    return new UniqueId(value);
  }

  @Override
  public String toString() {
    return IntStream
        .range(0, 6)
        .map(i -> (int) (value >> i * 8))
        .mapToObj(codePoint -> String.valueOf(CHARS.charAt(Math.abs(codePoint % CHARS.length()))))
        .collect(Collectors.joining());
  }

}
