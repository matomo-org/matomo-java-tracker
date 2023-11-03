/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;

final class AuthToken {

  private AuthToken() {
    // utility
  }

  @Nullable
  static String determineAuthToken(
      @Nullable
      String overrideAuthToken,
      @Nullable
      Iterable<? extends MatomoRequest> requests,
      @Nullable
      TrackerConfiguration trackerConfiguration
  ) {
    if (isNotBlank(overrideAuthToken)) {
      return overrideAuthToken;
    }
    if (requests != null) {
      for (MatomoRequest request : requests) {
        if (request != null && isNotBlank(request.getAuthToken())) {
          return request.getAuthToken();
        }
      }
    }
    if (trackerConfiguration != null && isNotBlank(trackerConfiguration.getDefaultAuthToken())) {
      return trackerConfiguration.getDefaultAuthToken();
    }
    return null;
  }

  private static boolean isNotBlank(
      @Nullable
      String str
  ) {
    return str != null && !str.isEmpty() && !str.trim().isEmpty();
  }
}
