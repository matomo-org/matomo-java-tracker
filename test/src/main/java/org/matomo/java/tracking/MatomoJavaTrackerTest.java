package org.matomo.java.tracking;

import com.github.javafaker.Country;
import com.github.javafaker.Faker;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.matomo.java.tracking.parameters.AcceptLanguage;
import org.matomo.java.tracking.parameters.CustomVariable;
import org.matomo.java.tracking.parameters.CustomVariables;
import org.matomo.java.tracking.parameters.DeviceResolution;
import org.matomo.java.tracking.parameters.EcommerceItem;
import org.matomo.java.tracking.parameters.EcommerceItems;
import org.matomo.java.tracking.parameters.UniqueId;
import org.matomo.java.tracking.parameters.VisitorId;

class MatomoJavaTrackerTest {

  private final MatomoTracker tracker;

  private final Faker faker = new Faker();
  private final List<VisitorId> vistors = new ArrayList<>(5);

  MatomoJavaTrackerTest(TrackerConfiguration configuration) {
    tracker = new MatomoTracker(configuration);
    for (int i = 0; i < 5; i++) {
      vistors.add(VisitorId.random());
    }
  }

  public static void main(String[] args) {

    TrackerConfiguration configuration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("http://localhost:8080/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
        .logFailedTracking(true)
        .build();

    MatomoJavaTrackerTest matomoJavaTrackerTest = new MatomoJavaTrackerTest(configuration);

    matomoJavaTrackerTest.sendRequestAsync();
    matomoJavaTrackerTest.sendBulkRequestsAsync();
    matomoJavaTrackerTest.sendRequest();
    matomoJavaTrackerTest.sendBulkRequests();

  }

  private void sendRequest() {
    MatomoRequest request = randomRequest();
    tracker.sendRequest(request);
    System.out.printf("Successfully sent single request to Matomo server: %s%n", request);
  }

  private void sendBulkRequests() {
    List<MatomoRequest> requests = randomRequests();
    tracker.sendBulkRequest(requests);
    System.out.printf("Successfully sent bulk requests to Matomo server: %s%n", requests);
  }

  private void sendRequestAsync() {
    MatomoRequest request = randomRequest();
    CompletableFuture<?> future = tracker.sendRequestAsync(request);
    future.thenAccept(v -> System.out.printf("Successfully sent async single request to Matomo server: %s%n", request));
  }

  private void sendBulkRequestsAsync() {
    List<MatomoRequest> requests = randomRequests();
    tracker
        .sendBulkRequestAsync(requests)
        .thenAccept(v -> System.out.printf("Successfully sent async bulk requests to Matomo server: %s%n", requests));
  }

  private List<MatomoRequest> randomRequests() {
    return IntStream
        .range(0, 5)
        .mapToObj(i -> randomRequest())
        .collect(Collectors.toCollection(() -> new ArrayList<>(10)));
  }

  private MatomoRequest randomRequest() {
    Country country = faker.country();
    return MatomoRequest
        .request()
        .actionName(faker.funnyName().name())
        .actionUrl("https://" + faker.internet().url())
        .visitorId(vistors.get(faker.random().nextInt(vistors.size())))
        .referrerUrl("https://" + faker.internet().url())
        .visitCustomVariables(new CustomVariables()
            .add(new CustomVariable("color", faker.color().hex()))
            .add(new CustomVariable("beer", faker.beer().name())))
        .visitorVisitCount(faker.random().nextInt(10))
        .visitorPreviousVisitTimestamp(Instant.now().minusSeconds(faker.random().nextInt(10000)))
        .visitorFirstVisitTimestamp(Instant.now().minusSeconds(faker.random().nextInt(10000)))
        .campaignName(faker.dragonBall().character())
        .campaignKeyword(faker.buffy().celebrities())
        .deviceResolution(DeviceResolution
            .builder()
            .width(faker.random().nextInt(1920))
            .height(faker.random().nextInt(1280))
            .build())
        .currentHour(faker.random().nextInt(24))
        .currentMinute(faker.random().nextInt(60))
        .currentSecond(faker.random().nextInt(60))
        .pluginJava(true)
        .pluginFlash(true)
        .pluginDirector(true)
        .pluginQuicktime(true)
        .pluginPDF(true)
        .pluginWindowsMedia(true)
        .pluginGears(true)
        .pluginSilverlight(true)
        .supportsCookies(true)
        .headerUserAgent(faker.internet().userAgentAny())
        .headerAcceptLanguage(AcceptLanguage.fromHeader("de"))
        .userId(faker.random().hex())
        .visitorCustomId(VisitorId.random())
        .newVisit(true)
        .pageCustomVariables(new CustomVariables()
            .add(new CustomVariable("job", faker.job().position()))
            .add(new CustomVariable("team", faker.team().name())))
        .outlinkUrl("https://" + faker.internet().url())
        .downloadUrl("https://" + faker.internet().url())
        .searchQuery(faker.cat().name())
        .searchCategory(faker.hipster().word())
        .searchResultsCount(Long.valueOf(faker.random().nextInt(20)))
        .pageViewId(UniqueId.random())
        .goalId(0)
        .ecommerceRevenue(faker.random().nextInt(50) + faker.random().nextDouble())
        .ecommerceId(faker.random().hex())
        .ecommerceItems(EcommerceItems
            .builder()
            .item(EcommerceItem
                .builder()
                .sku(faker.random().hex())
                .name(faker.commerce().productName())
                .quantity(faker.random().nextInt(10))
                .price(faker.random().nextInt(100) + faker.random().nextDouble())
                .build())
            .item(EcommerceItem
                .builder()
                .sku(faker.random().hex())
                .name(faker.commerce().productName())
                .quantity(faker.random().nextInt(10))
                .price(faker.random().nextInt(100) + faker.random().nextDouble())
                .build())
            .build())
        .ecommerceSubtotal(faker.random().nextInt(1000) + faker.random().nextDouble())
        .ecommerceTax(faker.random().nextInt(100) + faker.random().nextDouble())
        .ecommerceDiscount(faker.random().nextInt(100) + faker.random().nextDouble())
        .ecommerceLastOrderTimestamp(Instant.now())
        .visitorIp(faker.internet().ipV4Address())
        .requestTimestamp(Instant.now())
        .visitorCountry(org.matomo.java.tracking.parameters.Country.fromCode(faker.address().countryCode()))
        .visitorCity(faker.address().cityName())
        .visitorLatitude(faker.random().nextDouble() * 180 - 90)
        .visitorLongitude(faker.random().nextDouble() * 360 - 180)
        .trackBotRequests(true)
        .characterSet(StandardCharsets.UTF_8)
        .customAction(true)
        .networkTime(Long.valueOf(faker.random().nextInt(100)))
        .serverTime(Long.valueOf(faker.random().nextInt(100)))
        .transferTime(Long.valueOf(faker.random().nextInt(100)))
        .domProcessingTime(Long.valueOf(faker.random().nextInt(100)))
        .domCompletionTime(Long.valueOf(faker.random().nextInt(100)))
        .onloadTime(Long.valueOf(faker.random().nextInt(100)))
        .eventCategory(country.name())
        .eventAction(country.capital())
        .eventName(country.currencyCode())
        .contentName(faker.aviation().aircraft())
        .contentPiece(faker.ancient().god())
        .contentTarget("https://" + faker.internet().url())
        .contentInteraction(faker.app().name())
        .dimensions(Map.of(1L, faker.artist().name(), 2L, faker.dog().name()))
        .debug(true)
        .build();
  }
}
