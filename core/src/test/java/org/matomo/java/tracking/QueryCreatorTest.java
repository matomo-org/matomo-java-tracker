package org.matomo.java.tracking;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale.LanguageRange;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.parameters.AcceptLanguage;
import org.matomo.java.tracking.parameters.Country;
import org.matomo.java.tracking.parameters.CustomVariable;
import org.matomo.java.tracking.parameters.CustomVariables;
import org.matomo.java.tracking.parameters.DeviceResolution;
import org.matomo.java.tracking.parameters.EcommerceItem;
import org.matomo.java.tracking.parameters.EcommerceItems;
import org.matomo.java.tracking.parameters.RandomValue;
import org.matomo.java.tracking.parameters.UniqueId;
import org.matomo.java.tracking.parameters.VisitorId;

class QueryCreatorTest {

  private final MatomoRequest.MatomoRequestBuilder matomoRequestBuilder = MatomoRequest
      .request()
      .visitorId(VisitorId.fromHash(1234567890123456789L))
      .randomValue(RandomValue.fromString("random-value"));

  private String defaultAuthToken = "876de1876fb2cda2816c362a61bfc712";

  private String query;

  private MatomoRequest request;

  @Test
  void usesDefaultSiteId() {

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");

  }

  private void whenCreatesQuery() {
    request = matomoRequestBuilder.build();
    TrackerConfiguration trackerConfiguration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("http://localhost"))
        .defaultSiteId(42)
        .defaultAuthToken(defaultAuthToken)
        .build();
    String authToken = AuthToken.determineAuthToken(null, singleton(request), trackerConfiguration);
    query = new QueryCreator(trackerConfiguration).createQuery(request, authToken);
  }

  @Test
  void overridesDefaultSiteId() {

    matomoRequestBuilder.siteId(123);

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&idsite=123&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");

  }

  @Test
  void usesDefaultTokenAuth() {

    defaultAuthToken = "f123bfc9a46de0bb5453afdab6f93200";

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=f123bfc9a46de0bb5453afdab6f93200&rec=1&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");

  }

  @Test
  void overridesDefaultTokenAuth() {

    defaultAuthToken = "f123bfc9a46de0bb5453afdab6f93200";
    matomoRequestBuilder.authToken("e456bfc9a46de0bb5453afdab6f93200");

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=e456bfc9a46de0bb5453afdab6f93200&rec=1&apiv=1&_id=112210f47de98115&token_auth=e456bfc9a46de0bb5453afdab6f93200&send_image=0&rand=random-value");

  }

  @Test
  void validatesTokenAuth() {

    matomoRequestBuilder.authToken("invalid-token-auth");

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");

  }

  @Test
  void convertsTrueBooleanTo1() {

    matomoRequestBuilder.pluginFlash(true);

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&fla=1&send_image=0&rand=random-value");

  }

  @Test
  void convertsFalseBooleanTo0() {

    matomoRequestBuilder.pluginJava(false);

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&java=0&send_image=0&rand=random-value");

  }

  @Test
  void encodesUrl() {

    matomoRequestBuilder.actionUrl("https://www.daniel-heid.de/some/page?foo=bar");

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&url=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fpage%3Ffoo%3Dbar&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");

  }

  @Test
  void encodesReferrerUrl() {

    matomoRequestBuilder.referrerUrl("https://www.daniel-heid.de/some/referrer?foo2=bar2");

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&urlref=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Freferrer%3Ffoo2%3Dbar2&send_image=0&rand=random-value");

  }

  @Test
  void encodesLink() {

    matomoRequestBuilder.outlinkUrl("https://www.daniel-heid.de/some/external/link#");

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&link=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fexternal%2Flink%23&send_image=0&rand=random-value");

  }

  @Test
  void encodesDownloadUrl() {

    matomoRequestBuilder.downloadUrl("https://www.daniel-heid.de/some/download.pdf");

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&download=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fdownload.pdf&send_image=0&rand=random-value");

  }

  @Test
  void tracksMinimalRequest() {

    matomoRequestBuilder
        .actionName("Help / Feedback")
        .actionUrl("https://www.daniel-heid.de/portfolio")
        .visitorId(VisitorId.fromHash(3434343434343434343L))
        .referrerUrl("https://www.daniel-heid.de/referrer")
        .visitCustomVariables(new CustomVariables()
            .add(new CustomVariable("customVariable1Key", "customVariable1Value"), 5)
            .add(new CustomVariable("customVariable2Key", "customVariable2Value"), 6))
        .visitorVisitCount(2)
        .visitorPreviousVisitTimestamp(Instant.parse("2022-08-09T18:34:12Z"))
        .deviceResolution(DeviceResolution.builder().width(1024).height(768).build())
        .headerAcceptLanguage(AcceptLanguage
            .builder()
            .languageRange(new LanguageRange("de"))
            .languageRange(new LanguageRange("de-DE", 0.9))
            .languageRange(new LanguageRange("en", 0.8))
            .build())
        .pageViewId(UniqueId.fromValue(999999999999999999L))
        .goalId(0)
        .ecommerceRevenue(12.34)
        .ecommerceItems(EcommerceItems
            .builder()
            .item(EcommerceItem.builder().sku("SKU").build())
            .item(EcommerceItem
                .builder()
                .sku("SKU")
                .name("NAME")
                .category("CATEGORY")
                .price(123.4)
                .build())
            .build())
        .authToken("fdf6e8461ea9de33176b222519627f78")
        .visitorCountry(Country.fromLanguageRanges("en-GB;q=0.7,de,de-DE;q=0.9,en;q=0.8,en-US;q=0.6"));

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=fdf6e8461ea9de33176b222519627f78&rec=1&action_name=Help+%2F+Feedback&url=https%3A%2F%2Fwww.daniel-heid.de%2Fportfolio&apiv=1&_id=2fa93d2858bc4867&urlref=https%3A%2F%2Fwww.daniel-heid.de%2Freferrer&_cvar=%7B%225%22%3A%5B%22customVariable1Key%22%2C%22customVariable1Value%22%5D%2C%226%22%3A%5B%22customVariable2Key%22%2C%22customVariable2Value%22%5D%7D&_idvc=2&_viewts=1660070052&res=1024x768&lang=de%2Cde-de%3Bq%3D0.9%2Cen%3Bq%3D0.8&pv_id=lbBbxG&idgoal=0&revenue=12.34&ec_items=%5B%5B%22SKU%22%2C%22%22%2C%22%22%2C0.0%2C0%5D%2C%5B%22SKU%22%2C%22NAME%22%2C%22CATEGORY%22%2C123.4%2C0%5D%5D&token_auth=fdf6e8461ea9de33176b222519627f78&country=de&send_image=0&rand=random-value");

  }

  @Test
  void testGetQueryString() {
    matomoRequestBuilder
        .siteId(3)
        .actionUrl("http://test.com")
        .randomValue(RandomValue.fromString("random"))
        .visitorId(VisitorId.fromHex("1234567890123456"));
    defaultAuthToken = null;
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random");
    matomoRequestBuilder.pageCustomVariables(new CustomVariables().add(new CustomVariable(
        "key",
        "val"
    ), 7));
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random");
    matomoRequestBuilder.additionalParameters(singletonMap("key", singleton("test")));
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key=%5Btest%5D");
    matomoRequestBuilder.additionalParameters(singletonMap("key", asList("test", "test2")));
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key=%5Btest%2C+test2%5D");
    Map<String, Object> customTrackingParameters = new HashMap<>();
    customTrackingParameters.put("key", "test2");
    customTrackingParameters.put("key2", "test3");
    matomoRequestBuilder.additionalParameters(customTrackingParameters);
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key2=test3&key=test2");
    customTrackingParameters.put("key", "test4");
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key2=test3&key=test4");
    matomoRequestBuilder.randomValue(null);
    matomoRequestBuilder.siteId(null);
    matomoRequestBuilder.required(null);
    matomoRequestBuilder.apiVersion(null);
    matomoRequestBuilder.responseAsImage(null);
    matomoRequestBuilder.visitorId(null);
    matomoRequestBuilder.actionUrl(null);
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "idsite=42&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&key2=test3&key=test4");
  }

  @Test
  void testGetQueryString2() {
    matomoRequestBuilder
        .actionUrl("http://test.com")
        .randomValue(RandomValue.fromString("random"))
        .visitorId(VisitorId.fromHex("1234567890123456"))
        .siteId(3);
    defaultAuthToken = null;

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random");
  }

  @Test
  void testGetUrlEncodedQueryString() {
    defaultAuthToken = null;
    matomoRequestBuilder
        .actionUrl("http://test.com")
        .randomValue(RandomValue.fromString("random"))
        .visitorId(VisitorId.fromHex("1234567890123456"))
        .siteId(3);
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random");
    Map<String, Object> customTrackingParameters = new HashMap<>();
    customTrackingParameters.put("ke/y", "te:st");
    matomoRequestBuilder.additionalParameters(customTrackingParameters);
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random&ke%2Fy=te%3Ast");
    customTrackingParameters.put("ke/y", "te:st2");
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random&ke%2Fy=te%3Ast2");
    customTrackingParameters.put("ke/y2", "te:st3");
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random&ke%2Fy=te%3Ast2&ke%2Fy2=te%3Ast3");
    customTrackingParameters.put("ke/y", "te:st3");
    whenCreatesQuery();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random&ke%2Fy=te%3Ast3&ke%2Fy2=te%3Ast3");
    matomoRequestBuilder
        .randomValue(null)
        .siteId(null)
        .required(null)
        .apiVersion(null)
        .responseAsImage(null)
        .visitorId(null)
        .actionUrl(null);
    whenCreatesQuery();
    assertThat(query).isEqualTo("idsite=42&ke%2Fy=te%3Ast3&ke%2Fy2=te%3Ast3");

  }

  @Test
  void testGetUrlEncodedQueryString2() {
    matomoRequestBuilder
        .actionUrl("http://test.com")
        .randomValue(RandomValue.fromString("random"))
        .visitorId(VisitorId.fromHex("1234567890123456"));
    defaultAuthToken = null;

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&rec=1&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random");

  }

  @Test
  void testVisitCustomVariableCustomVariable() {
    matomoRequestBuilder
        .randomValue(RandomValue.fromString("random"))
        .visitorId(VisitorId.fromHex("1234567890123456"))
        .siteId(3);
    org.matomo.java.tracking.CustomVariable cv =
        new org.matomo.java.tracking.CustomVariable("visitKey", "visitVal");
    matomoRequestBuilder.visitCustomVariables(new CustomVariables().add(cv, 8));
    defaultAuthToken = null;

    whenCreatesQuery();

    assertThat(request.getVisitCustomVariable(1)).isNull();
    assertThat(query).isEqualTo(
        "rec=1&idsite=3&apiv=1&_id=1234567890123456&_cvar=%7B%228%22%3A%5B%22visitKey%22%2C%22visitVal%22%5D%7D&send_image=0&rand=random");
  }

  @Test
  void doesNotAppendEmptyString() {

    matomoRequestBuilder.eventAction("");

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&e_a=&send_image=0&rand=random-value");

  }

  @Test
  void testAuthTokenTT() {

    matomoRequestBuilder.authToken("1234");

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Auth token must be exactly 32 characters long");
  }

  @Test
  void createsQueryWithDimensions() {
    Map<Long, Object> dimensions = new LinkedHashMap<>();
    dimensions.put(1L, "firstDimension");
    dimensions.put(3L, "thirdDimension");
    matomoRequestBuilder.dimensions(dimensions);

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value&dimension1=firstDimension&dimension3=thirdDimension");
  }

  @Test
  void appendsCharsetParameters() {
    matomoRequestBuilder.characterSet(StandardCharsets.ISO_8859_1);

    whenCreatesQuery();

    assertThat(query).isEqualTo(
        "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&cs=ISO-8859-1&send_image=0&rand=random-value");
  }

  @Test
  void failsIfIdSiteIsNegative() {
    matomoRequestBuilder.siteId(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for idsite. Must be greater or equal than 1");
  }

  @Test
  void failsIfIdSiteIsZero() {
    matomoRequestBuilder.siteId(0);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for idsite. Must be greater or equal than 1");
  }

  @Test
  void failsIfCurrentHourIsNegative() {
    matomoRequestBuilder.currentHour(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for h. Must be greater or equal than 0");
  }

  @Test
  void failsIfCurrentHourIsGreaterThan23() {
    matomoRequestBuilder.currentHour(24);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for h. Must be less or equal than 23");
  }

  @Test
  void failsIfCurrentMinuteIsNegative() {
    matomoRequestBuilder.currentMinute(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for m. Must be greater or equal than 0");
  }

  @Test
  void failsIfCurrentMinuteIsGreaterThan59() {
    matomoRequestBuilder.currentMinute(60);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for m. Must be less or equal than 59");
  }

  @Test
  void failsIfCurrentSecondIsNegative() {
    matomoRequestBuilder.currentSecond(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for s. Must be greater or equal than 0");
  }

  @Test
  void failsIfCurrentSecondIsGreaterThan59() {
    matomoRequestBuilder.currentSecond(60);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for s. Must be less or equal than 59");
  }

  @Test
  void failsIfLatitudeIsLessThanMinus90() {
    matomoRequestBuilder.visitorLatitude(-90.1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for lat. Must be greater or equal than -90");
  }

  @Test
  void failsIfLatitudeIsGreaterThan90() {
    matomoRequestBuilder.visitorLatitude(90.1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseInstanceOf(MatomoException.class)
        .hasRootCauseMessage("Invalid value for lat. Must be less or equal than 90");
  }

  @Test
  void failsIfLongitudeIsLessThanMinus180() {
    matomoRequestBuilder.visitorLongitude(-180.1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for long. Must be greater or equal than -180");
  }

  @Test
  void failsIfLongitudeIsGreaterThan180() {
    matomoRequestBuilder.visitorLongitude(180.1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for long. Must be less or equal than 180");
  }

  @Test
  void tracksEvent() {
    matomoRequestBuilder.eventName("Event Name")
                        .eventValue(23.456)
                        .eventAction("Event Action")
                        .eventCategory("Event Category");

    whenCreatesQuery();

    assertThat(query).isEqualTo("idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&e_c=Event+Category&e_a=Event+Action&e_n=Event+Name&e_v=23.456&send_image=0&rand=random-value");
  }

}
