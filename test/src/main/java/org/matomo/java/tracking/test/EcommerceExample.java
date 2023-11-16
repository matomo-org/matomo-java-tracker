package org.matomo.java.tracking.test;

import java.net.URI;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.EcommerceItem;
import org.matomo.java.tracking.parameters.EcommerceItems;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * Example for sending an ecommerce request.
 */
public class EcommerceExample {

  /**
   * Example for sending an ecommerce request.
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

    tracker.sendBulkRequestAsync(MatomoRequests
        .ecommerceCartUpdate(50.0)
        .ecommerceItems(EcommerceItems
            .builder()
            .item(EcommerceItem
                .builder()
                .sku("XYZ12345")
                .name("Matomo - The big book about web analytics")
                .category("Education & Teaching")
                .price(23.1)
                .quantity(2)
                .build())
            .item(EcommerceItem
                .builder()
                .sku("B0C2WV3MRJ")
                .name("Matomo for data visualization")
                .category("Education & Teaching")
                .price(15.0)
                .quantity(1)
                .build())
            .build())
        .visitorId(VisitorId.fromString("customer@mail.com"))
        .build()
    );

  }

}
