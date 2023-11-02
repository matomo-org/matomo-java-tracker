/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.piwik.java.tracking;

import org.matomo.java.tracking.MatomoTracker;

/**
 * Creates a new PiwikTracker instance. This class is deprecated and will be removed in the future.
 *
 * @author brettcsorba
 * @deprecated Use {@link MatomoTracker} instead.
 */
@Deprecated
public class PiwikTracker extends MatomoTracker {

  /**
   * Creates a new PiwikTracker instance with the given host URL.
   *
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(String hostUrl) {
    super(hostUrl);
  }

  /**
   * Creates a new PiwikTracker instance with the given host URL and timeout in milliseconds. Use -1 for no timeout.
   *
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(String hostUrl, int timeout) {
    super(hostUrl, timeout);
  }

  /**
   * Creates a new PiwikTracker instance with the given host URL and proxy settings.
   *
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(String hostUrl, String proxyHost, int proxyPort) {
    super(hostUrl, proxyHost, proxyPort);
  }

  /**
   * Creates a new PiwikTracker instance with the given host URL, proxy settings and timeout in milliseconds. Use -1 for
   * no timeout.
   *
   * @deprecated Use {@link MatomoTracker} instead.
   */
  @Deprecated
  public PiwikTracker(String hostUrl, String proxyHost, int proxyPort, int timeout) {
    super(hostUrl, proxyHost, proxyPort, timeout);
  }

}
