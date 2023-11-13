package org.matomo.java.tracking.servlet;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
  protected void doFilter(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res,
      @NonNull FilterChain chain)
         throws IOException, ServletException {
    MatomoRequest matomoRequest = ServletMatomoRequest
        .fromServletRequest(JavaxHttpServletWrapper.fromHttpServletRequest(req)).build();
    log.debug("Sending request {}", matomoRequest);
    tracker.sendRequestAsync(matomoRequest);
    super.doFilter(req, res, chain);
  }
}
