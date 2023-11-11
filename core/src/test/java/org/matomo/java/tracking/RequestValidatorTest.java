package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.piwik.java.tracking.PiwikDate;
import org.piwik.java.tracking.PiwikLocale;

class RequestValidatorTest {

  private final MatomoRequest request = new MatomoRequest();

  @Test
  void testEcommerceRevenue() {

    request.setEcommerceRevenue(20.0);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");

  }

  @Test
  void testEcommerceDiscount() {
    request.setEcommerceDiscount(1.0);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");

  }

  @Test
  void testEcommerceId() {
    request.setEcommerceId("1");

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");
  }

  @Test
  void testEcommerceSubtotal() {
    request.setEcommerceSubtotal(20.0);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");
  }

  @Test
  void testEcommerceShippingCost() {
    request.setEcommerceShippingCost(20.0);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");
  }

  @Test
  void testEcommerceLastOrderTimestamp() {
    request.setEcommerceLastOrderTimestamp(Instant.ofEpochSecond(1000L));

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");
  }

  @Test
  void testEcommerceTax() {
    request.setEcommerceTax(20.0);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");
  }

  @Test
  void testEcommerceItemE() {

    request.addEcommerceItem(new EcommerceItem("sku", "name", "category", 1.0, 2));

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Goal ID must be set if ecommerce parameters are used");
  }

  @Test
  void testSearchResultsCount() {

    request.setSearchResultsCount(100L);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Search query must be set if search results count is set");

  }

  @Test
  void testVisitorLongitude() {
    request.setVisitorLongitude(20.5);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if longitude, latitude, region, city or country are set");
  }

  @Test
  void testVisitorLatitude() {
    request.setVisitorLatitude(10.5);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if longitude, latitude, region, city or country are set");
  }

  @Test
  void testVisitorCity() {
    request.setVisitorCity("city");

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if longitude, latitude, region, city or country are set");
  }

  @Test
  void testVisitorRegion() {
    request.setVisitorRegion("region");

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if longitude, latitude, region, city or country are set");
  }

  @Test
  void testVisitorCountryTE() {
    PiwikLocale country = new PiwikLocale(Locale.US);
    request.setVisitorCountry(country);


    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if longitude, latitude, region, city or country are set");
  }

  @Test
  void testRequestDatetime() {

    PiwikDate date = new PiwikDate(1000L);
    request.setRequestDatetime(date);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Auth token must be present if request timestamp is more than four hours ago");

  }

  @Test
  void failsIfSiteIdIsNegative() {
    request.setSiteId(-1);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Site ID must not be negative");
  }

  @Test
  void failsIfAuthTokenIsNot32CharactersLong() {
    assertThatThrownBy(() -> RequestValidator.validate(request, "123456789012345678901234567890"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }

}
