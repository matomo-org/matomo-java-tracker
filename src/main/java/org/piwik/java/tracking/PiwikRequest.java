/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.piwik.java.tracking;

import lombok.NonNull;
import org.matomo.java.tracking.MatomoRequest;

import java.net.URL;

/**
 * A request object that can be used to send requests to Matomo. This class is deprecated and will be removed in the
 * future.
 *
 * @author brettcsorba
 * @deprecated Use {@link MatomoRequest} instead.
 */
@Deprecated
public class PiwikRequest extends MatomoRequest {

  /**
   * Creates a new request object with the specified site ID and action URL.
   *
   * @param siteId    the site ID
   * @param actionUrl the action URL. Must not be null.
   * @deprecated Use {@link MatomoRequest} instead.
   */
  @Deprecated
  public PiwikRequest(int siteId, @NonNull URL actionUrl) {
    super(siteId, actionUrl.toString());
  }
}
