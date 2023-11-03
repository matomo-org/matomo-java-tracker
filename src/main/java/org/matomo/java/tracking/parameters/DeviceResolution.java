/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * The resolution (width and height) of the user's output device (monitor / phone).
 */
@Builder
@RequiredArgsConstructor
public class DeviceResolution {

  private final int width;

  private final int height;

  /**
   * Creates a device resolution from a string representation.
   *
   * <p>The string must be in the format "widthxheight", e.g. "1920x1080".
   *
   * @param deviceResolution The string representation of the device resolution, e.g. "1920x1080"
   * @return The device resolution representation
   */
  @Nullable
  public static DeviceResolution fromString(
      @Nullable
      String deviceResolution
  ) {
    if (deviceResolution == null) {
      return null;
    }
    String[] dimensions = deviceResolution.split("x");
    if (dimensions.length != 2) {
      throw new IllegalArgumentException("Wrong dimension size");
    }
    return builder()
        .width(Integer.parseInt(dimensions[0]))
        .height(Integer.parseInt(dimensions[1]))
        .build();
  }

  @Override
  public String toString() {
    return String.format("%dx%d", width, height);
  }

}
