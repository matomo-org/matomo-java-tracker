/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DeviceResolutionTest {

  @Test
  void formatsDeviceResolution() {

    DeviceResolution deviceResolution = DeviceResolution.builder().width(1280).height(1080).build();

    assertThat(deviceResolution).hasToString("1280x1080");

  }

  @Test
  void returnsNullOnNull() {

    DeviceResolution deviceResolution = DeviceResolution.fromString(null);

    assertThat(deviceResolution).isNull();

  }

  @Test
  void failsOnWrongDimensionSize() {
    assertThatThrownBy(() -> DeviceResolution.fromString("1920x1080x720"))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Wrong dimension size");
  }

}
