package org.matomo.java.tracking;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.parameters.RandomValue;
import org.matomo.java.tracking.parameters.VisitorId;

class MatomoTrackerIT {

  private static final String HOST_URL = "http://localhost:8080/matomo.php";
  public static final String QUERY =
      "rec=1&idsite=1&action_name=test&apiv=1&_id=00000000343efaf5&send_image=0&rand=test-random";
  private MatomoTracker matomoTracker;
  private final TestSenderFactory senderFactory = new TestSenderFactory();
  private final MatomoRequest request = MatomoRequest
      .request()
      .siteId(1)
      .visitorId(VisitorId.fromString("test-visitor-id"))
      .randomValue(RandomValue.fromString("test-random"))
      .actionName("test")
      .build();

  @Test
  void sendsRequest() {

    matomoTracker = new MatomoTracker(HOST_URL);
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendRequest(request);

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, QUERY);
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);

  }

  @Test
  void validatesRequest() {

    matomoTracker = new MatomoTracker(HOST_URL);
    matomoTracker.setSenderFactory(senderFactory);
    request.setSiteId(null);

    assertThatThrownBy(() -> matomoTracker.sendRequest(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("No default site ID and no request site ID is given");

    assertThat(senderFactory.getTestSender()).isNull();

  }

  @Test
  void doesNotSendRequestIfNotEnabled() {

    matomoTracker =
        new MatomoTracker(TrackerConfiguration.builder().apiEndpoint(URI.create(HOST_URL)).enabled(false).build());
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendRequest(request);

    assertThat(senderFactory.getTestSender()).isNull();

  }

  @Test
  void sendsRequestUsingProxy() {

    matomoTracker = new MatomoTracker(HOST_URL, "localhost", 8081);
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendRequest(request);

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, QUERY);
    TrackerConfiguration trackerConfiguration = testSender.getTrackerConfiguration();
    assertThat(trackerConfiguration.getProxyHost()).isEqualTo("localhost");
    assertThat(trackerConfiguration.getProxyPort()).isEqualTo(8081);

  }

  @Test
  void sendsRequestAsync() {

    matomoTracker = new MatomoTracker(HOST_URL, 1000);
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendRequestAsync(request);

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, QUERY);
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);

  }

  @Test
  void sendsRequestAsyncWithCallback() {

    matomoTracker = new MatomoTracker(HOST_URL, 1000);
    matomoTracker.setSenderFactory(senderFactory);
    AtomicBoolean callbackCalled = new AtomicBoolean();
    matomoTracker.sendRequestAsync(request, request -> {
      assertThat(request).isEqualTo(request);
      callbackCalled.set(true);
      return null;
    });

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, QUERY);
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);
    assertThat(callbackCalled).isTrue();

  }

  @Test
  void doesNotSendRequestAsyncIfNotEnabled() {

    matomoTracker =
        new MatomoTracker(TrackerConfiguration.builder().apiEndpoint(URI.create(HOST_URL)).enabled(false).build());
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendRequestAsync(request);

    assertThat(senderFactory.getTestSender()).isNull();

  }

  @Test
  void sendsBulkRequests() {

    matomoTracker = new MatomoTracker(HOST_URL);
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendBulkRequest(request);

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, QUERY);
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);

  }

  @Test
  void doesNotSendBulkRequestsIfNotEnabled() {

    matomoTracker =
        new MatomoTracker(TrackerConfiguration.builder().apiEndpoint(URI.create(HOST_URL)).enabled(false).build());
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendBulkRequest(request);

    assertThat(senderFactory.getTestSender()).isNull();

  }

  @Test
  void sendsBulkRequestsAsync() {

    matomoTracker = new MatomoTracker(HOST_URL, 1000);
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendBulkRequestAsync(request);

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, QUERY);
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);

  }

  @Test
  void doesNotSendBulkRequestsAsyncIfNotEnabled() {

    matomoTracker =
        new MatomoTracker(TrackerConfiguration.builder().apiEndpoint(URI.create(HOST_URL)).enabled(false).build());
    matomoTracker.setSenderFactory(senderFactory);

    matomoTracker.sendBulkRequestAsync(request);

    assertThat(senderFactory.getTestSender()).isNull();

  }

  @Test
  void sendsBulkRequestAsyncWithCallback() {

    matomoTracker = new MatomoTracker(HOST_URL, 1000);
    matomoTracker.setSenderFactory(senderFactory);
    AtomicBoolean callbackCalled = new AtomicBoolean();
    matomoTracker.sendBulkRequestAsync(singleton(request), v -> callbackCalled.set(true));

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, QUERY);
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);
    assertThat(callbackCalled).isTrue();

  }

  @Test
  void sendsBulkRequestAsyncWithAuthToken() {

    matomoTracker = new MatomoTracker(HOST_URL, 1000);
    matomoTracker.setSenderFactory(senderFactory);
    matomoTracker.sendBulkRequestAsync(singleton(request), "abc123def456abc123def456abc123de");

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, "token_auth=abc123def456abc123def456abc123de&rec=1&idsite=1&action_name=test&apiv=1&_id=00000000343efaf5&send_image=0&rand=test-random");
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);

  }

  @Test
  void appliesGoalId() {

    matomoTracker = new MatomoTracker(HOST_URL);
    matomoTracker.setSenderFactory(senderFactory);
    request.setEcommerceId("some-id");

    matomoTracker.sendRequest(request);

    TestSender testSender = senderFactory.getTestSender();
    thenContainsRequest(testSender, "rec=1&idsite=1&action_name=test&apiv=1&_id=00000000343efaf5&idgoal=0&ec_id=some-id&send_image=0&rand=test-random");
    assertThat(testSender.getTrackerConfiguration().getApiEndpoint()).hasToString(HOST_URL);

  }

  private void thenContainsRequest(TestSender testSender, String query) {
    assertThat(testSender.getRequests()).containsExactly(request);
    assertThat(testSender.getQueries()).containsExactly(query);
  }

}