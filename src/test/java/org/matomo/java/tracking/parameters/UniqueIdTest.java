package org.matomo.java.tracking.parameters;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UniqueIdTest {

  @Test
  void createsRandomUniqueId() {

    UniqueId uniqueId = UniqueId.random();

    assertThat(uniqueId.toString()).matches("[0-9a-zA-Z]{6}");

  }

  @Test
  void createsSameUniqueIds() {

    UniqueId uniqueId1 = UniqueId.fromValue(868686868L);
    UniqueId uniqueId2 = UniqueId.fromValue(868686868);

    assertThat(uniqueId1).hasToString(uniqueId2.toString());

  }

}
