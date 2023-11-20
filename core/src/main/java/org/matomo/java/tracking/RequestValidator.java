/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;


import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.NonNull;

final class RequestValidator {

  private RequestValidator() {
    // utility
  }

  static void validate(
      @NonNull
      MatomoRequest request,
      @Nullable
      CharSequence authToken
  ) {

    if (request.getSearchResultsCount() != null && request.getSearchQuery() == null) {
      throw new MatomoException("Search query must be set if search results count is set");
    }
    if (authToken == null) {
      if (request.getVisitorLongitude() != null || request.getVisitorLatitude() != null
          || request.getVisitorRegion() != null || request.getVisitorCity() != null
          || request.getVisitorCountry() != null || request.getVisitorIp() != null) {
        throw new MatomoException(
            "Auth token must be present if visitor longitude, latitude, region, city, country or IP are set");
      }
      if (request.getRequestTimestamp() != null && request
          .getRequestTimestamp()
          .isBefore(Instant.now().minus(4, ChronoUnit.HOURS))) {
        throw new MatomoException(
            "Auth token must be present if request timestamp is more than four hours ago");
      }
    } else {
      if (authToken.length() != 32) {
        throw new IllegalArgumentException("Auth token must be exactly 32 characters long");
      }
    }
  }
}

