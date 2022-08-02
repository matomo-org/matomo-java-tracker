/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import org.matomo.java.tracking.MatomoTracker;

/**
 * @author brettcsorba
 * @deprecated Use {@link MatomoTracker} instead.
 */
@Deprecated
public class PiwikTracker extends MatomoTracker {

  /**
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(final String hostUrl) {
    super(hostUrl);
  }

  /**
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(final String hostUrl, final int timeout) {
    super(hostUrl, timeout);
  }

  /**
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(final String hostUrl, final String proxyHost, final int proxyPort) {
    super(hostUrl, proxyHost, proxyPort);
  }

  /**
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(final String hostUrl, final String proxyHost, final int proxyPort, final int timeout) {
    super(hostUrl, proxyHost, proxyPort, timeout);
  }

}
