package org.matomo.java.tracking.test;

import jakarta.servlet.http.HttpServletRequest;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.parameters.VisitorId;
import org.matomo.java.tracking.servlet.JakartaHttpServletWrapper;
import org.matomo.java.tracking.servlet.ServletMatomoRequest;

/**
 * This is an example of how to use the ServletMatomoRequest class.
 */
public class ServletMatomoRequestExample {

  private final MatomoTracker tracker;

  public ServletMatomoRequestExample(MatomoTracker tracker) {
    this.tracker = tracker;
  }

  /**
   * Example for sending a request from a servlet request.
   *
   * @param request the servlet request
   */
  public void someControllerMethod(HttpServletRequest request) {
    MatomoRequest matomoRequest = ServletMatomoRequest
        .addServletRequestHeaders(
            MatomoRequests.contentImpression(
                "Latest Product Announced",
                "Main Blog Text",
                "https://www.yourdomain.com/blog/2018/10/01/new-product-launches"
            ),
            JakartaHttpServletWrapper.fromHttpServletRequest(request)
        ).visitorId(VisitorId.fromString("customer@mail.com"))
        // ...
        .build();
    tracker.sendRequestAsync(matomoRequest);
    // ...
  }

}
