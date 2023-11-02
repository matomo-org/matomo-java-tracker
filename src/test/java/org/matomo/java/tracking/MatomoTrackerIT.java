package org.matomo.java.tracking;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Locale.LanguageRange;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.MatomoRequest.MatomoRequestBuilder;
import org.matomo.java.tracking.TrackerConfiguration.TrackerConfigurationBuilder;
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

class MatomoTrackerIT {

  private static final WireMockServer wireMockServer = new WireMockServer(
    WireMockConfiguration.options().dynamicPort());

  private static final int SITE_ID = 42;

  private final TrackerConfigurationBuilder trackerConfigurationBuilder = TrackerConfiguration.builder();

  private final MatomoRequestBuilder requestBuilder =
    MatomoRequest.builder().visitorId(VisitorId.fromHex("bbccddeeff1122"))
      .randomValue(RandomValue.fromString("someRandom"));

  private CompletableFuture<Void> future;

  @BeforeAll
  static void beforeAll() {
    wireMockServer.start();
  }

  @BeforeEach
  void givenStub() {
    wireMockServer.resetRequests();
    wireMockServer.stubFor(post(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
    wireMockServer.stubFor(get(urlPathEqualTo("/matomo.php")).willReturn(status(204)));
  }

  @Test
  void requiresApiEndpoint() {

    assertThatThrownBy(() -> trackerConfigurationBuilder.defaultSiteId(SITE_ID).build()).isInstanceOf(
      NullPointerException.class).hasMessage("apiEndpoint is marked non-null but is null");

  }

  @Test
  void requiresSiteId() {

    trackerConfigurationBuilder.apiEndpoint(URI.create("http://localhost:8099/matomo.php")).build();

    assertThatThrownBy(this::whenSendsRequestAsync).isInstanceOf(IllegalArgumentException.class)
      .hasMessage("No default site ID and no request site ID is given");

  }

  private void whenSendsRequestAsync() {
    future = new MatomoTracker(trackerConfigurationBuilder.build()).sendRequestAsync(requestBuilder.build());
    try {
      future.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void usesDefaultSiteId() {

    givenTrackerConfigurationWithDefaultSiteId();

    whenSendsRequestAsync();

    thenGetsRequest("idsite=42&rec=1&apiv=1&_id=00bbccddeeff1122&send_image=0&rand=someRandom");

  }

  private void givenTrackerConfigurationWithDefaultSiteId() {
    trackerConfigurationBuilder.apiEndpoint(URI.create(String.format(
      "http://localhost:%s/matomo.php", wireMockServer.port()))).defaultSiteId(SITE_ID);
  }

  private void thenGetsRequest(String expectedQuery) {
    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(
      getRequestedFor(urlEqualTo(String.format("/matomo.php?%s", expectedQuery)))
        .withHeader("User-Agent", equalTo("MatomoJavaClient")));
  }

  @Test
  void overridesDefaultSiteId() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.siteId(123);

    whenSendsRequestAsync();

    thenGetsRequest("rec=1&idsite=123&apiv=1&_id=00bbccddeeff1122&send_image=0&rand=someRandom");

  }

  @Test
  void validatesTokenAuth() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.authToken("invalid-token-auth");

    assertThatThrownBy(this::whenSendsRequestAsync).hasRootCauseInstanceOf(IllegalArgumentException.class)
      .hasRootCauseMessage("Auth token must be exactly 32 characters long");

  }

  @Test
  void convertsTrueBooleanTo1() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.pluginFlash(true);

    whenSendsRequestAsync();

    thenGetsRequest(
      "idsite=42&rec=1&apiv=1&_id=00bbccddeeff1122&fla=1&send_image=0&rand=someRandom"
    );

  }

  @Test
  void convertsFalseBooleanTo0() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.pluginJava(false);

    whenSendsRequestAsync();

    thenGetsRequest(
      "idsite=42&rec=1&apiv=1&_id=00bbccddeeff1122&java=0&send_image=0&rand=someRandom"
    );

  }

  @Test
  void encodesUrl() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.actionUrl("https://www.daniel-heid.de/some/page?foo=bar");

    whenSendsRequestAsync();

    thenGetsRequest(
      "idsite=42&rec=1&url=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fpage%3Ffoo%3Dbar&apiv=1&_id=00bbccddeeff1122&send_image=0&rand=someRandom"
    );

  }

  @Test
  void encodesReferrerUrl() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.referrerUrl("https://www.daniel-heid.de/some/referrer?foo2=bar2");

    whenSendsRequestAsync();

    thenGetsRequest(
      "idsite=42&rec=1&apiv=1&_id=00bbccddeeff1122&urlref=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Freferrer%3Ffoo2%3Dbar2&send_image=0&rand=someRandom"
    );

  }

  @Test
  void encodesLink() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.outlinkUrl("https://www.daniel-heid.de/some/external/link#");

    whenSendsBulkRequestAsync();

    thenPostsRequestWithoutAuthToken(
      "idsite=42&rec=1&apiv=1&_id=00bbccddeeff1122&link=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fexternal%2Flink%23&send_image=0&rand=someRandom",
      "156"
    );

  }

  private void whenSendsBulkRequestAsync() {
    future =
      new MatomoTracker(trackerConfigurationBuilder.build()).sendBulkRequestAsync(singleton(requestBuilder.build()));
    try {
      future.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void thenPostsRequestWithoutAuthToken(String expectedQuery, String contentLength) {
    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(postRequestedFor(urlEqualTo("/matomo.php"))
      .withHeader("Content-Length", equalTo(contentLength))
      .withHeader("Accept", equalTo("*/*"))
      .withHeader("Content-Type", equalTo("application/json"))
      .withHeader("User-Agent", equalTo("MatomoJavaClient"))
      .withRequestBody(
        equalToJson("{\"requests\":[\"?" + expectedQuery + "\"]}")));
  }

  @Test
  void encodesDownloadUrl() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.downloadUrl("https://www.daniel-heid.de/some/download.pdf");

    whenSendsBulkRequestAsync();

    thenPostsRequestWithoutAuthToken(
      "idsite=42&rec=1&apiv=1&_id=00bbccddeeff1122&download=https%3A%2F%2Fwww.daniel-heid.de%2Fsome%2Fdownload.pdf&send_image=0&rand=someRandom",
      "154"
    );

  }

  @Test
  void getContainsHeaders() {

    givenTrackerConfigurationWithDefaultSiteId();

    whenSendsRequestAsync();

    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(getRequestedFor(urlPathEqualTo("/matomo.php"))
      .withHeader("User-Agent", equalTo("MatomoJavaClient")));

  }

  @Test
  void postContainsHeaders() {

    givenTrackerConfigurationWithDefaultSiteId();

    whenSendsBulkRequestAsync();

    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(postRequestedFor(urlPathEqualTo("/matomo.php"))
      .withHeader("Accept", equalTo("*/*"))
      .withHeader("Content-Length", equalTo("90"))
      .withHeader("Content-Type", equalTo("application/json"))
      .withHeader("User-Agent", equalTo("MatomoJavaClient")));

  }

  @Test
  void allowsToOverrideUserAgent() {

    givenTrackerConfigurationWithDefaultSiteId();
    trackerConfigurationBuilder.userAgent("Mozilla/5.0");

    whenSendsRequestAsync();

    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(getRequestedFor(urlPathEqualTo("/matomo.php"))
      .withHeader("User-Agent", equalTo("Mozilla/5.0")));

  }

  @Test
  void tracksMinimalRequest() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.actionName("Help / Feedback").actionUrl("https://www.daniel-heid.de/portfolio")
      .visitorId(VisitorId.fromHash(3434343434343434343L)).referrerUrl("https://www.daniel-heid.de/referrer")
      .visitCustomVariables(
        new CustomVariables().add(new CustomVariable("customVariable1Key", "customVariable1Value"), 4)
          .add(new CustomVariable("customVariable2Key", "customVariable2Value"), 5)).visitorVisitCount(2)
      .visitorFirstVisitTimestamp(LocalDateTime.of(2022, 8, 9, 18, 34, 12).toInstant(ZoneOffset.UTC))
      .deviceResolution(DeviceResolution.builder().width(1024).height(768).build()).headerAcceptLanguage(
        AcceptLanguage.builder().languageRange(new LanguageRange("de")).languageRange(new LanguageRange("de-DE", 0.9))
          .languageRange(new LanguageRange("en", 0.8)).build()).pageViewId(UniqueId.fromValue(999999999999999999L))
      .goalId(0).ecommerceRevenue(12.34).ecommerceItems(
        EcommerceItems.builder().item(org.matomo.java.tracking.parameters.EcommerceItem.builder().sku("SKU").build())
          .item(EcommerceItem.builder().sku("SKU").name("NAME").category("CATEGORY").price(123.4).build()).build())
      .authToken("fdf6e8461ea9de33176b222519627f78")
      .visitorCountry(Country.fromLanguageRanges("en-GB;q=0.7,de,de-DE;q=0.9,en;q=0.8,en-US;q=0.6"));

    whenSendsBulkRequestAsync();

    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(postRequestedFor(urlEqualTo("/matomo.php")).withHeader("Content-Length", equalTo("711"))
      .withHeader("Accept", equalTo("*/*")).withHeader("Content-Type", equalTo("application/json"))
      .withHeader("User-Agent", equalTo("MatomoJavaClient"))
      .withRequestBody(
        equalToJson("{\"requests\":[\"?" + "idsite=42&rec=1&action_name=Help+%2F+Feedback&url=https%3A%2F%2Fwww.daniel-heid.de%2Fportfolio&apiv=1&_id=2fa93d2858bc4867&urlref=https%3A%2F%2Fwww.daniel-heid.de%2Freferrer&_cvar=%7B%224%22%3A%5B%22customVariable1Key%22%2C%22customVariable1Value%22%5D%2C%225%22%3A%5B%22customVariable2Key%22%2C%22customVariable2Value%22%5D%7D&_idvc=2&_idts=1660070052&res=1024x768&lang=de%2Cde-de%3Bq%3D0.9%2Cen%3Bq%3D0.8&pv_id=lbBbxG&idgoal=0&revenue=12.34&ec_items=%5B%5B%22SKU%22%2C%22%22%2C%22%22%2C0.0%2C0%5D%2C%5B%22SKU%22%2C%22NAME%22%2C%22CATEGORY%22%2C123.4%2C0%5D%5D&token_auth=fdf6e8461ea9de33176b222519627f78&country=de&send_image=0&rand=someRandom"
          + "\"],\"token_auth\" : \"" + "fdf6e8461ea9de33176b222519627f78" + "\"}")));

  }

  @Test
  void doesNothingIfNotEnabled() {

    wireMockServer.resetRequests();
    givenTrackerConfigurationWithDefaultSiteId();
    trackerConfigurationBuilder.enabled(false);

    whenSendsRequestAsync();

    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(0, postRequestedFor(urlPathEqualTo("/matomo.php")));

  }

  @Test
  void exampleWorks() {

    TrackerConfiguration config =
      TrackerConfiguration.builder().apiEndpoint(URI.create("https://your-domain.net/matomo/matomo.php"))
        .defaultSiteId(42) // if not explicitly specified by action
        .build();

    // Prepare the tracker (stateless - can be used for multiple actions)
    MatomoTracker tracker = new MatomoTracker(config);

    // Track an action
    CompletableFuture<Void> future = tracker.sendRequestAsync(
      MatomoRequest.builder().actionName("User Profile / Upload Profile Picture")
        .actionUrl("https://your-domain.net/user/profile/picture")
        .visitorId(VisitorId.fromHash("some@email-adress.org".hashCode()))
        // ...
        .build());

    // If you want to ensure the request has been handled:
    if (future.isCompletedExceptionally()) {
      // log, throw, ...
    }
  }

  @Test
  void reportsErrors() {

    wireMockServer.stubFor(get(urlPathEqualTo("/failing")).willReturn(status(500)));
    trackerConfigurationBuilder.apiEndpoint(URI.create(String.format("http://localhost:%d/failing",
      wireMockServer.port()))).defaultSiteId(SITE_ID);

    assertThatThrownBy(this::whenSendsRequestAsync).hasRootCauseInstanceOf(MatomoException.class)
      .hasRootCauseMessage("Tracking endpoint responded with code 500");

    assertThat(future).isCompletedExceptionally();

  }

  @Test
  void includesDefaultTokenAuth() {

    givenTrackerConfigurationWithDefaultSiteId();
    trackerConfigurationBuilder.defaultAuthToken("fdf6e8461ea9de33176b222519627f78");

    whenSendsRequestAsync();

    assertThat(future).isNotCompletedExceptionally();
    wireMockServer.verify(
      getRequestedFor(
        urlEqualTo(
          "/matomo.php?idsite=42token_auth=fdf6e8461ea9de33176b222519627f78&rec=1&apiv=1&_id=00bbccddeeff1122&send_image=0&rand=someRandom"))
        .withHeader("User-Agent", equalTo("MatomoJavaClient")));

  }

  @Test
  void includesMultipleQueriesInBulkRequest() throws Exception {

    givenTrackerConfigurationWithDefaultSiteId();
    MatomoTracker tracker = new MatomoTracker(trackerConfigurationBuilder.build());

    CompletableFuture<Void> future1 = tracker.sendBulkRequestAsync(
      Arrays.asList(requestBuilder.actionName("First").build(), requestBuilder.actionName("Second").build(),
        requestBuilder.actionName("Third").build()
      ));
    future1.get();

    assertThat(future1).isNotCompletedExceptionally();
    wireMockServer.verify(postRequestedFor(urlEqualTo("/matomo.php")).withHeader("Content-Length", equalTo("297"))
      .withHeader("Accept", equalTo("*/*")).withHeader("Content-Type", equalTo("application/json"))
      .withHeader("User-Agent", equalTo("MatomoJavaClient")).withRequestBody(equalToJson(
        "{\"requests\" : [ \"?idsite=42&rec=1&action_name=First&apiv=1&_id=00bbccddeeff1122&send_image=0&rand=someRandom\", \"?idsite=42&rec=1&action_name=Second&apiv=1&_id=00bbccddeeff1122&send_image=0&rand=someRandom\", \"?idsite=42&rec=1&action_name=Third&apiv=1&_id=00bbccddeeff1122&send_image=0&rand=someRandom\" ]}")));

  }

  @Test
  void failsOnNegativeSiteId() {

    givenTrackerConfigurationWithDefaultSiteId();
    requestBuilder.siteId(-1);

    assertThatThrownBy(this::whenSendsRequestAsync).hasRootCauseInstanceOf(IllegalArgumentException.class)
      .hasRootCauseMessage("Site ID must not be negative");
  }

}
