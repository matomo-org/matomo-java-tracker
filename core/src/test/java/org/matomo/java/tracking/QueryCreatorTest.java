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

  private final MatomoRequest.MatomoRequestBuilder matomoRequestBuilder =
      MatomoRequest.request()
          .visitorId(VisitorId.fromHash(1234567890123456789L))
          .randomValue(RandomValue.fromString("random-value"));

  private String defaultAuthToken = "876de1876fb2cda2816c362a61bfc712";

  private String query;

  private MatomoRequest request;

  @Test
  void usesDefaultSiteId() {

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");
  }

  private void whenCreatesQuery() {
    request = matomoRequestBuilder.build();
    TrackerConfiguration trackerConfiguration =
        TrackerConfiguration.builder()
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

    assertThat(query)
        .isEqualTo(
            "token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&idsite=123&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");
  }

  @Test
  void usesDefaultTokenAuth() {

    defaultAuthToken = "f123bfc9a46de0bb5453afdab6f93200";

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=f123bfc9a46de0bb5453afdab6f93200&rec=1&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");
  }

  @Test
  void overridesDefaultTokenAuth() {

    defaultAuthToken = "f123bfc9a46de0bb5453afdab6f93200";
    matomoRequestBuilder.authToken("e456bfc9a46de0bb5453afdab6f93200");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
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

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&fla=1&send_image=0&rand=random-value");
  }

  @Test
  void convertsFalseBooleanTo0() {

    matomoRequestBuilder.pluginJava(false);

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&java=0&send_image=0&rand=random-value");
  }

  @Test
  void encodesUrl() {

    matomoRequestBuilder.actionUrl("https://www.daniel-heid.de/some/page?foo=bar");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&url=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fpage%3Ffoo%3Dbar&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value");
  }

  @Test
  void encodesReferrerUrl() {

    matomoRequestBuilder.referrerUrl("https://www.daniel-heid.de/some/referrer?foo2=bar2");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&urlref=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Freferrer%3Ffoo2%3Dbar2&send_image=0&rand=random-value");
  }

  @Test
  void encodesLink() {

    matomoRequestBuilder.outlinkUrl("https://www.daniel-heid.de/some/external/link#");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&link=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fexternal%2Flink%23&send_image=0&rand=random-value");
  }

  @Test
  void encodesDownloadUrl() {

    matomoRequestBuilder.downloadUrl("https://www.daniel-heid.de/some/download.pdf");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&download=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fdownload.pdf&send_image=0&rand=random-value");
  }

  @Test
  void tracksMinimalRequest() {

    matomoRequestBuilder
        .actionName("Help / Feedback")
        .actionUrl("https://www.daniel-heid.de/portfolio")
        .visitorId(VisitorId.fromHash(3434343434343434343L))
        .referrerUrl("https://www.daniel-heid.de/referrer")
        .visitCustomVariables(
            new CustomVariables()
                .add(new CustomVariable("customVariable1Key", "customVariable1Value"), 5)
                .add(new CustomVariable("customVariable2Key", "customVariable2Value"), 6))
        .visitorVisitCount(2)
        .visitorPreviousVisitTimestamp(Instant.parse("2022-08-09T18:34:12Z"))
        .deviceResolution(DeviceResolution.builder().width(1024).height(768).build())
        .headerAcceptLanguage(
            AcceptLanguage.builder()
                .languageRange(new LanguageRange("de"))
                .languageRange(new LanguageRange("de-DE", 0.9))
                .languageRange(new LanguageRange("en", 0.8))
                .build())
        .pageViewId(UniqueId.fromValue(999999999999999999L))
        .goalId(0)
        .ecommerceRevenue(12.34)
        .ecommerceItems(
            EcommerceItems.builder()
                .item(EcommerceItem.builder().sku("SKU").build())
                .item(
                    EcommerceItem.builder()
                        .sku("SKU")
                        .name("NAME")
                        .category("CATEGORY")
                        .price(123.4)
                        .build())
                .build())
        .authToken("fdf6e8461ea9de33176b222519627f78")
        .visitorCountry(
            Country.fromLanguageRanges("en-GB;q=0.7,de,de-DE;q=0.9,en;q=0.8,en-US;q=0.6"));

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
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
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random");
    matomoRequestBuilder.pageCustomVariables(
        new CustomVariables().add(new CustomVariable("key", "val"), 7));
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random");
    matomoRequestBuilder.additionalParameters(singletonMap("key", singleton("test")));
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key=%5Btest%5D");
    matomoRequestBuilder.additionalParameters(singletonMap("key", asList("test", "test2")));
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key=%5Btest%2C+test2%5D");
    Map<String, Object> customTrackingParameters = new HashMap<>();
    customTrackingParameters.put("key", "test2");
    customTrackingParameters.put("key2", "test3");
    matomoRequestBuilder.additionalParameters(customTrackingParameters);
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key2=test3&key=test2");
    customTrackingParameters.put("key", "test4");
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&cvar=%7B%227%22%3A%5B%22key%22%2C%22val%22%5D%7D&send_image=0&rand=random&key2=test3&key=test4");
    matomoRequestBuilder.randomValue(null);
    matomoRequestBuilder.siteId(null);
    matomoRequestBuilder.required(null);
    matomoRequestBuilder.apiVersion(null);
    matomoRequestBuilder.responseAsImage(null);
    matomoRequestBuilder.visitorId(null);
    matomoRequestBuilder.actionUrl(null);
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
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

    assertThat(query)
        .isEqualTo(
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
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random");
    Map<String, Object> customTrackingParameters = new HashMap<>();
    customTrackingParameters.put("ke/y", "te:st");
    matomoRequestBuilder.additionalParameters(customTrackingParameters);
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random&ke%2Fy=te%3Ast");
    customTrackingParameters.put("ke/y", "te:st2");
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random&ke%2Fy=te%3Ast2");
    customTrackingParameters.put("ke/y2", "te:st3");
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&url=http%3A%2F%2Ftest.com&apiv=1&_id=1234567890123456&send_image=0&rand=random&ke%2Fy=te%3Ast2&ke%2Fy2=te%3Ast3");
    customTrackingParameters.put("ke/y", "te:st3");
    whenCreatesQuery();
    assertThat(query)
        .isEqualTo(
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

    assertThat(query)
        .isEqualTo(
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
    assertThat(query)
        .isEqualTo(
            "rec=1&idsite=3&apiv=1&_id=1234567890123456&_cvar=%7B%228%22%3A%5B%22visitKey%22%2C%22visitVal%22%5D%7D&send_image=0&rand=random");
  }

  @Test
  void doesNotAppendEmptyString() {

    matomoRequestBuilder.eventAction("");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
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

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&send_image=0&rand=random-value&dimension1=firstDimension&dimension3=thirdDimension");
  }

  @Test
  void appendsCharsetParameters() {
    matomoRequestBuilder.characterSet(StandardCharsets.ISO_8859_1);

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
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
    matomoRequestBuilder
        .eventName("Event Name")
        .eventValue(23.456)
        .eventAction("Event Action")
        .eventCategory("Event Category");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&token_auth=876de1876fb2cda2816c362a61bfc712&rec=1&apiv=1&_id=112210f47de98115&e_c=Event+Category&e_a=Event+Action&e_n=Event+Name&e_v=23.456&send_image=0&rand=random-value");
  }

  @Test
  void allowsZeroForEventValue() {
    matomoRequestBuilder
        .eventName("Event Name")
        .eventValue(0.0)
        .eventAction("Event Action")
        .eventCategory("Event Category");

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&"
                + "token_auth=876de1876fb2cda2816c362a61bfc712&"
                + "rec=1&"
                + "apiv=1&"
                + "_id=112210f47de98115&"
                + "e_c=Event+Category&"
                + "e_a=Event+Action&"
                + "e_n=Event+Name&"
                + "e_v=0.0&"
                + "send_image=0&"
                + "rand=random-value");
  }

  @Test
  void allowsZeroForEcommerceValues() {
    matomoRequestBuilder
        .ecommerceRevenue(0.0)
        .ecommerceSubtotal(0.0)
        .ecommerceTax(0.0)
        .ecommerceShippingCost(0.0)
        .ecommerceDiscount(0.0);

    whenCreatesQuery();

    assertThat(query)
        .isEqualTo(
            "idsite=42&"
                + "token_auth=876de1876fb2cda2816c362a61bfc712&"
                + "rec=1&"
                + "apiv=1&"
                + "_id=112210f47de98115&"
                + "revenue=0.0&"
                + "ec_st=0.0&"
                + "ec_tx=0.0&"
                + "ec_sh=0.0&"
                + "ec_dt=0.0&"
                + "send_image=0&"
                + "rand=random-value");
  }

  @Test
  void includesClientHints() {
    matomoRequestBuilder.clientHints(
        "{\"brands\":[{\"brand\":\"Chromium\",\"version\":\"110\"}],\"mobile\":false}");

    whenCreatesQuery();

    assertThat(query)
        .contains(
            "uadata=%7B%22brands%22%3A%5B%7B%22brand%22%3A%22Chromium%22%2C%22version%22%3A%22110%22%7D%5D%2C%22mobile%22%3Afalse%7D");
  }

  @Test
  void includesEcommerceProductView() {
    matomoRequestBuilder
        .ecommerceProductSku("SKU-123")
        .ecommerceProductName("Blue Widget")
        .ecommerceProductCategory("Widgets")
        .ecommerceProductPrice(9.99);

    whenCreatesQuery();

    assertThat(query)
        .contains("_pks=SKU-123")
        .contains("_pkn=Blue+Widget")
        .contains("_pkc=Widgets")
        .contains("_pkp=9.99");
  }

  @Test
  void allowsZeroForEcommerceProductPrice() {
    matomoRequestBuilder.ecommerceProductSku("SKU-FREE").ecommerceProductPrice(0.0);

    whenCreatesQuery();

    assertThat(query).contains("_pks=SKU-FREE").contains("_pkp=0.0");
  }

  @Test
  void failsIfEcommerceProductPriceIsNegative() {
    matomoRequestBuilder.ecommerceProductPrice(-1.0);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for _pkp. Must be greater or equal than 0");
  }

  @Test
  void includesBotTrackingParameters() {
    matomoRequestBuilder
        .trackBotRequests(true)
        .botRecordingMode(1)
        .httpStatusCode(200)
        .bandwidthBytes(1024L)
        .sourceLabel("backend");

    whenCreatesQuery();

    assertThat(query)
        .contains("bots=1")
        .contains("recMode=1")
        .contains("http_status=200")
        .contains("bw_bytes=1024")
        .contains("source=backend");
  }

  @Test
  void allowsZeroForBandwidthBytes() {
    matomoRequestBuilder.bandwidthBytes(0L);

    whenCreatesQuery();

    assertThat(query).contains("bw_bytes=0");
  }

  @Test
  void failsIfBandwidthBytesIsNegative() {
    matomoRequestBuilder.bandwidthBytes(-1L);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for bw_bytes. Must be greater or equal than 0");
  }

  @Test
  void includesMediaAnalyticsParameters() {
    matomoRequestBuilder
        .mediaId("media-abc123")
        .mediaTitle("My Video")
        .mediaResource("https://example.com/video.mp4")
        .mediaType("video")
        .mediaPlayerName("html5")
        .mediaTimeSpent(42)
        .mediaLength(120)
        .mediaProgressPercent(35)
        .mediaTimeToPlay(3)
        .mediaWidth(1280)
        .mediaHeight(720)
        .mediaFullscreen(false)
        .mediaSegmentsViewed("[[0,42]]");

    whenCreatesQuery();

    assertThat(query)
        .contains("ma_id=media-abc123")
        .contains("ma_ti=My+Video")
        .contains("ma_re=https%3A%2F%2Fexample.com%2Fvideo.mp4")
        .contains("ma_mt=video")
        .contains("ma_pn=html5")
        .contains("ma_st=42")
        .contains("ma_le=120")
        .contains("ma_ps=35")
        .contains("ma_ttp=3")
        .contains("ma_w=1280")
        .contains("ma_h=720")
        .contains("ma_fs=0")
        .contains("ma_se=%5B%5B0%2C42%5D%5D");
  }

  @Test
  void allowsZeroForMediaTimeValues() {
    matomoRequestBuilder.mediaTimeSpent(0).mediaLength(0).mediaTimeToPlay(0);

    whenCreatesQuery();

    assertThat(query).contains("ma_st=0").contains("ma_le=0").contains("ma_ttp=0");
  }

  @Test
  void failsIfMediaTimeSpentIsNegative() {
    matomoRequestBuilder.mediaTimeSpent(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for ma_st. Must be greater or equal than 0");
  }

  @Test
  void failsIfMediaLengthIsNegative() {
    matomoRequestBuilder.mediaLength(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for ma_le. Must be greater or equal than 0");
  }

  @Test
  void failsIfMediaProgressPercentIsNegative() {
    matomoRequestBuilder.mediaProgressPercent(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for ma_ps. Must be greater or equal than 0");
  }

  @Test
  void failsIfMediaProgressPercentExceeds100() {
    matomoRequestBuilder.mediaProgressPercent(101);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for ma_ps. Must be less or equal than 100");
  }

  @Test
  void allowsZeroAndMaxForMediaProgressPercent() {
    matomoRequestBuilder.mediaProgressPercent(0);
    whenCreatesQuery();
    assertThat(query).contains("ma_ps=0");

    matomoRequestBuilder.mediaProgressPercent(100);
    whenCreatesQuery();
    assertThat(query).contains("ma_ps=100");
  }

  @Test
  void failsIfMediaWidthIsNegative() {
    matomoRequestBuilder.mediaWidth(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for ma_w. Must be greater or equal than 0");
  }

  @Test
  void failsIfMediaHeightIsNegative() {
    matomoRequestBuilder.mediaHeight(-1);

    assertThatThrownBy(this::whenCreatesQuery)
        .isInstanceOf(MatomoException.class)
        .hasMessage("Could not append parameter")
        .hasRootCauseMessage("Invalid value for ma_h. Must be greater or equal than 0");
  }
}
