package org.matomo.java.tracking.test;

import jakarta.servlet.http.HttpServletRequest;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.ServletMatomoRequest;

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
   * @param req the servlet request
   */
  public void someControllerMethod(HttpServletRequest req) {
    MatomoRequest matomoRequest = ServletMatomoRequest
        .fromServletRequest(req)
        .actionName("Some Controller Action")
        // ...
        .build();
    tracker.sendRequestAsync(matomoRequest);
    // ...
  }

}
