/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.NonNull;

/**
 * The unique visitor ID, must be a 16 characters hexadecimal string. Every unique visitor must be assigned a different
 * ID and this ID must not change after it is assigned. If this value is not set Matomo will still track visits, but the
 * unique visitors metric might be less accurate.
 */
public class VisitorId {

  private static final Random RANDOM = new SecureRandom();
  private static final Pattern HEX_DIGITS = Pattern.compile("[0-9a-fA-F]+");

  private final byte[] representation = new byte[8];

  /**
   * Static factory to generate a random visitor id.
   *
   * <p>Please consider creating a fixed id for each visitor by getting a hash code from e.g. the username and
   * using {@link #fromHash(long)} or {@link #fromString(String)} instead of using this method.
   *
   * @return A randomly generated visitor id
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static VisitorId random() {
    VisitorId visitorId = new VisitorId();
    RANDOM.nextBytes(visitorId.representation);
    return visitorId;
  }

  /**
   * Creates always the same visitor id for the given input.
   *
   * <p>You can use e.g. {@link Object#hashCode()} to generate a hash code for an object, e.g. a username
   * string as input.
   *
   * @param hash A number (a hash code) to create the visitor id from
   * @return Always the same visitor id for the same input
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static VisitorId fromHash(long hash) {
    VisitorId visitorId = new VisitorId();
    long remainingHash = hash;
    for (int i = visitorId.representation.length - 1; i >= 0; i--) {
      visitorId.representation[i] = (byte) (remainingHash & 0xFF);
      remainingHash >>= Byte.SIZE;
    }
    return visitorId;
  }

  /**
   * Creates a visitor id from a UUID.
   *
   * <p>Uses the most significant bits of the UUID to create the visitor id.
   *
   * @param uuid A UUID to create the visitor id from
   * @return The visitor id for the given UUID
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static VisitorId fromUUID(@NonNull UUID uuid) {
    return fromHash(uuid.getMostSignificantBits());
  }

  /**
   * Creates a visitor id from a hexadecimal string.
   *
   * <p>The input must be a valid hexadecimal string with a maximum length of 16 characters. If the input is shorter
   * than 16 characters it will be padded with zeros.</p>
   *
   * @param inputHex A hexadecimal string to create the visitor id from
   * @return The visitor id for the given input
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static VisitorId fromHex(@NonNull String inputHex) {
    if (inputHex.trim().isEmpty()) {
      throw new IllegalArgumentException("Hex string must not be null or empty");
    }
    if (inputHex.length() > 16) {
      throw new IllegalArgumentException("Hex string must not be longer than 16 characters");
    }
    if (!HEX_DIGITS.matcher(inputHex).matches()) {
      throw new IllegalArgumentException("Input must be a valid hex string");
    }
    VisitorId visitorId = new VisitorId();
    for (int charIndex = inputHex.length() - 1, representationIndex =
         visitorId.representation.length - 1;
         charIndex >= 0;
         charIndex -= 2, representationIndex--) {
      String hex = inputHex.substring(Math.max(0, charIndex - 1), charIndex + 1);
      try {
        visitorId.representation[representationIndex] = (byte) Integer.parseInt(hex, 16);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Input must be a valid hex string", e);
      }
    }
    return visitorId;
  }

  /**
   * Creates a visitor id from a string. The string will be hashed to create the visitor id.
   *
   * @param str A string to create the visitor id from
   * @return The visitor id for the given string or null if the string is null or empty
   */
  @Nullable
  public static VisitorId fromString(@Nullable String str) {
    if (str == null || str.trim().isEmpty()) {
      return null;
    }
    return fromHash(str.hashCode());
  }

  @Override
  @edu.umd.cs.findbugs.annotations.NonNull
  public String toString() {
    return Hex.fromBytes(representation);
  }

}
