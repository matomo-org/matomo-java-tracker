package org.matomo.java.tracking.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoTracker;

/**
 * This filter can be used to automatically send a request to the Matomo server for every request
 * that is received by the servlet container.
 */
@RequiredArgsConstructor
@Slf4j
public class MatomoTrackerFilter extends HttpFilter {

  private final MatomoTracker tracker;

  @Override
  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException {
    MatomoRequest matomoRequest = ServletMatomoRequest
        .fromServletRequest(JakartaHttpServletWrapper.fromHttpServletRequest(req)).build();
    log.debug("Sending request {}", matomoRequest);
    tracker.sendRequestAsync(matomoRequest);
    super.doFilter(req, res, chain);
  }
}
