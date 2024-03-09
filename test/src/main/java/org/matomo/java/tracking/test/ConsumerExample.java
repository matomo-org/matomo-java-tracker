package org.matomo.java.tracking.test;

import java.net.URI;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * Example for sending a request and performing an action when the request was sent successfully.
 */
public class ConsumerExample {

  /**
   * Example for sending a request and performing an action when the request was sent successfully.
   *
   * @param args ignored
   */
  public static void main(String[] args) {

    TrackerConfiguration configuration = TrackerConfiguration
        .builder()
        .apiEndpoint(URI.create("https://www.yourdomain.com/matomo.php"))
        .defaultSiteId(1)
        .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
        .logFailedTracking(true)
        .build();

    try (MatomoTracker tracker = new MatomoTracker(configuration)) {
      MatomoRequest request = MatomoRequests
          .event("Training", "Workout completed", "Bench press", 60.0)
          .visitorId(VisitorId.fromString("customer@mail.com"))
          .build();

      tracker.sendRequestAsync(request)
          .thenAccept(req -> System.out.printf("Sent request %s%n", req))
          .exceptionally(throwable -> {
            System.err.printf("Failed to send request: %s%n", throwable.getMessage());
            return null;
          });
    } catch (Exception e) {
      throw new RuntimeException("Could not close tracker", e);
    }

  }

}
