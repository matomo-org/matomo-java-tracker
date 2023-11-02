/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceResolutionTest {

  @Test
  void formatsDeviceResolution() {

    DeviceResolution deviceResolution = DeviceResolution.builder().width(1280).height(1080).build();

    assertThat(deviceResolution).hasToString("1280x1080");

  }

}
