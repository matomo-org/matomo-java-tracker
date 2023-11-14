package org.matomo.java.tracking.test;

import java.net.URI;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * Example for sending multiple requests in one bulk request.
 */
public class BulkExample {

  /**
   * Example for sending multiple requests in one bulk request.
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

    MatomoTracker tracker = new MatomoTracker(configuration);

    VisitorId visitorId = VisitorId.fromString("customer@mail.com");
    tracker.sendBulkRequestAsync(
        MatomoRequests.siteSearch("Running shoes", "Running", 120L)
                      .visitorId(visitorId).build(),
        MatomoRequests.pageView("VelocityStride ProX Running Shoes")
                      .visitorId(visitorId).build(),
        MatomoRequests.ecommerceOrder("QXZ-789LMP", 100.0, 124.0, 19.0, 10.0, 5.0)
                      .visitorId(visitorId)
                      .build()
    );

  }

}
