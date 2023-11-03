/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;


import static java.util.Objects.requireNonNull;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
    requireNonNull(request, "Request must not be null");
    if (request.getSiteId() != null && request.getSiteId() < 0) {
      throw new IllegalArgumentException("Site ID must not be negative");
    }
    if (request.getGoalId() == null && (
        request.getEcommerceId() != null || request.getEcommerceRevenue() != null
            || request.getEcommerceDiscount() != null || request.getEcommerceItems() != null
            || request.getEcommerceLastOrderTimestamp() != null
            || request.getEcommerceShippingCost() != null || request.getEcommerceSubtotal() != null
            || request.getEcommerceTax() != null)) {
      throw new MatomoException("Goal ID must be set if ecommerce parameters are used");
    }
    if (request.getSearchResultsCount() != null && request.getSearchQuery() == null) {
      throw new MatomoException("Search query must be set if search results count is set");
    }
    if (authToken == null) {
      if (request.getVisitorLongitude() != null || request.getVisitorLatitude() != null
          || request.getVisitorRegion() != null || request.getVisitorCity() != null
          || request.getVisitorCountry() != null) {
        throw new MatomoException(
            "Auth token must be present if longitude, latitude, region, city or country are set");
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

