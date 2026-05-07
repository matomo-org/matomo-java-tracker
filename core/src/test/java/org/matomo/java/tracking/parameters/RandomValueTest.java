package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RandomValueTest {

  @Test
  void createsRandomValueWith20HexChars() {

    RandomValue random = RandomValue.random();

    assertThat(random.toString()).matches("[0-9a-f]{20}");
  }

  @Test
  void createsRandomValue() {

    RandomValue random1 = RandomValue.random();
    RandomValue random2 = RandomValue.random();

    assertThat(random1.toString()).isNotEqualTo(random2.toString());
  }
}
