package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class VisitorIdTest {

  private static Stream<Arguments> validHexStrings() {
    return Stream.of(
        Arguments.of("0", "0000000000000000"),
        Arguments.of("0000", "0000000000000000"),
        Arguments.of("1", "0000000000000001"),
        Arguments.of("a", "000000000000000a"),
        Arguments.of("1a", "000000000000001a"),
        Arguments.of("01a", "000000000000001a"),
        Arguments.of("1a2b", "0000000000001a2b"),
        Arguments.of("1a2b3c", "00000000001a2b3c"),
        Arguments.of("1a2b3c4d", "000000001a2b3c4d"),
        Arguments.of("1a2b3c4d5e", "0000001a2b3c4d5e"),
        Arguments.of("1A2B3C4D5E", "0000001a2b3c4d5e"),
        Arguments.of("1a2b3c4d5e6f", "00001a2b3c4d5e6f"),
        Arguments.of("1a2b3c4d5e6f7a", "001a2b3c4d5e6f7a")
    );
  }

  @Test
  void hasCorrectFormat() {

    VisitorId visitorId = VisitorId.random();

    assertThat(visitorId.toString()).matches("^[a-z0-9]{16}$");

  }

  @Test
  void createsRandomVisitorId() {

    VisitorId first = VisitorId.random();
    VisitorId second = VisitorId.random();

    assertThat(first).doesNotHaveToString(second.toString());

  }

  @Test
  void fixedVisitorIdForLongHash() {

    VisitorId visitorId = VisitorId.fromHash(987654321098765432L);

    assertThat(visitorId).hasToString("0db4da5f49f8b478");

  }

  @Test
  void fixedVisitorIdForIntHash() {

    VisitorId visitorId = VisitorId.fromHash(777777777);

    assertThat(visitorId).hasToString("000000002e5bf271");

  }

  @Test
  void sameVisitorIdForSameHash() {

    VisitorId first = VisitorId.fromHash(1234567890L);
    VisitorId second = VisitorId.fromHash(1234567890);

    assertThat(first).hasToString(second.toString());

  }

  @Test
  void alwaysTheSameToString() {

    VisitorId visitorId = VisitorId.random();

    assertThat(visitorId).hasToString(visitorId.toString());

  }

  @Test
  void createsVisitorIdFrom16CharacterHex() {

    VisitorId visitorId = VisitorId.fromHex("1234567890abcdef");

    assertThat(visitorId).hasToString("1234567890abcdef");

  }

  @Test
  void createsVisitorIdFrom1CharacterHex() {

    VisitorId visitorId = VisitorId.fromHex("a");

    assertThat(visitorId).hasToString("000000000000000a");

  }

  @Test
  void createsVisitorIdFrom2CharacterHex() {

    VisitorId visitorId = VisitorId.fromHex("12");

    assertThat(visitorId).hasToString("0000000000000012");

  }

  @Test
  void failsOnInvalidHexString() {

    assertThatThrownBy(() -> VisitorId.fromHex("invalid123456789"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Input must be a valid hex string");

  }

  @ParameterizedTest
  @ValueSource(strings =
      {"g", "gh", "ghi", "ghij", "ghijk", "ghijkl", "ghijklm", "ghijklmn", "ghijklmn", "-1"})
  void failsOnInvalidHexString(String hex) {
    assertThatThrownBy(() -> VisitorId.fromHex(hex))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Input must be a valid hex string");
  }

  @ParameterizedTest
  @MethodSource("validHexStrings")
  void createsVisitorIdFromHex(
      String hex, String expected
  ) {

    VisitorId visitorId = VisitorId.fromHex(hex);

    assertThat(visitorId).hasToString(expected);

  }

  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  void failsOnEmptyStrings(String hex) {
    assertThatThrownBy(() -> VisitorId.fromHex(hex))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Hex string must not be null or empty");
  }

  @ParameterizedTest
  @ValueSource(strings = {"1234567890abcdefg", "1234567890abcdeff"})
  void failsOnInvalidHexStringLength(String hex) {
    assertThatThrownBy(() -> VisitorId.fromHex(hex))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Hex string must not be longer than 16 characters");
  }

  @Test
  void createsVisitorIdFromUUID() {

    VisitorId visitorId = VisitorId.fromUUID(
        java.util.UUID.fromString("12345678-90ab-cdef-1234-567890abcdef")
    );

    assertThat(visitorId).hasToString("1234567890abcdef");

  }

  @Test
  void failsOnNullUUID() {
    assertThatThrownBy(() -> VisitorId.fromUUID(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("uuid is marked non-null but is null");
  }

  @Test
  void createsVisitorIdFromString() {

    VisitorId visitorId = VisitorId.fromString("test");

    assertThat(visitorId).hasToString("0000000000364492");

  }

}
