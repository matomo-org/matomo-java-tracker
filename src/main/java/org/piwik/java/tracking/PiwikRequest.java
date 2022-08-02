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
 * @author brettcsorba
 * @deprecated Use {@link MatomoRequest} instead.
 */
@Deprecated
public class PiwikRequest extends MatomoRequest {

  /**
   * @deprecated Use {@link MatomoRequest} instead.
   */
  @Deprecated
  public PiwikRequest(int siteId, @NonNull URL actionUrl) {
    super(siteId, actionUrl.toString());
  }
}
