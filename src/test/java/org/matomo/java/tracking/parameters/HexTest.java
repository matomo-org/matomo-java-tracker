package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HexTest {



  @Test
  void failsIfBytesAreNull() {
    assertThatThrownBy(() -> Hex.fromBytes(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("bytes is marked non-null but is null");
  }

  private static Stream<Arguments> testBytes() {
    return Stream.of(
        Arguments.of(new byte[] {0x00, 0x01, 0x02, 0x03}, "00010203"),
        Arguments.of(new byte[] {(byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC}, "fffefdfc"),
        Arguments.of(new byte[0], "")
    );
  }

  @ParameterizedTest
  @MethodSource("testBytes")
  void convertsBytesIntoHex(byte[] bytes, String expected) {
    assertThat(Hex.fromBytes(bytes)).isEqualTo(expected);
  }

}