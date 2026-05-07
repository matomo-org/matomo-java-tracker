package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.parameters.Country;

class RequestValidatorTest {

  private final MatomoRequest request = new MatomoRequest();

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
            "Auth token must be present if visitor longitude, latitude, region, city, country or IP"
                + " are set");
  }

  @Test
  void testVisitorLatitude() {
    request.setVisitorLatitude(10.5);

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if visitor longitude, latitude, region, city, country or IP"
                + " are set");
  }

  @Test
  void testVisitorCity() {
    request.setVisitorCity("city");

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if visitor longitude, latitude, region, city, country or IP"
                + " are set");
  }

  @Test
  void testVisitorRegion() {
    request.setVisitorRegion("region");

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if visitor longitude, latitude, region, city, country or IP"
                + " are set");
  }

  @Test
  void testVisitorCountry() {
    request.setVisitorCountry(Country.fromCode("us"));

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if visitor longitude, latitude, region, city, country or IP"
                + " are set");
  }

  @Test
  void testVisitorIp() {
    request.setVisitorIp("192.168.0.1");

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage(
            "Auth token must be present if visitor longitude, latitude, region, city, country or IP"
                + " are set");
  }

  @Test
  void testRequestTimestampOlderThanFourHours() {
    request.setRequestTimestamp(Instant.now().minus(5, ChronoUnit.HOURS));

    assertThatThrownBy(() -> RequestValidator.validate(request, null))
        .isInstanceOf(MatomoException.class)
        .hasMessage("Auth token must be present if request timestamp is more than four hours ago");
  }

  @Test
  void doesNotFailIfRequestTimestampIsWithinFourHours() {
    request.setRequestTimestamp(Instant.now().minus(1, ChronoUnit.HOURS));

    assertThatCode(() -> RequestValidator.validate(request, null)).doesNotThrowAnyException();
  }

  @Test
  void doesNotFailWithValid32CharAuthToken() {
    assertThatCode(() -> RequestValidator.validate(request, "12345678901234567890123456789012"))
        .doesNotThrowAnyException();
  }

  @Test
  void failsIfAuthTokenIsNot32CharactersLong() {
    assertThatThrownBy(() -> RequestValidator.validate(request, "123456789012345678901234567890"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }
}
