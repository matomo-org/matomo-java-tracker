/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import java.security.SecureRandom;
import java.util.Random;

/**
 * A random value to avoid the tracking request being cached by the browser or a proxy.
 */
public class RandomValue {

  private static final Random RANDOM = new SecureRandom();

  private final byte[] representation = new byte[10];

  private String override;

  /**
   * Static factory to generate a random value.
   *
   * @return A randomly generated value
   */
  public static RandomValue random() {
    RandomValue randomValue = new RandomValue();
    RANDOM.nextBytes(randomValue.representation);
    return randomValue;
  }

  /**
   * Static factory to generate a random value from a given string. The string will be used as is and not hashed.
   *
   * @param override The string to use as random value
   * @return A random value from the given string
   */
  public static RandomValue fromString(String override) {
    RandomValue randomValue = new RandomValue();
    randomValue.override = override;
    return randomValue;
  }

  @Override
  public String toString() {
    if (override != null) {
      return override;
    }
    return Hex.fromBytes(representation);
  }

}
