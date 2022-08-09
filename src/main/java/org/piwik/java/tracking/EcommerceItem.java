/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import org.matomo.java.tracking.MatomoRequest;

/**
 * @author brettcsorba
 * @deprecated Use {@link org.matomo.java.tracking.EcommerceItem} instead.
 */
@Deprecated
public class EcommerceItem extends org.matomo.java.tracking.EcommerceItem {

  /**
   * @deprecated Use {@link MatomoRequest} instead.
   */
  @Deprecated
  public EcommerceItem(String sku, String name, String category, Double price, Integer quantity) {
    super(sku, name, category, price, quantity);
  }
}
