/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.piwik.java.tracking;

/**
 * Describes an item in an ecommerce transaction.
 *
 * @author brettcsorba
 * @deprecated Use {@link org.matomo.java.tracking.parameters.EcommerceItem} instead.
 */
@Deprecated
public class EcommerceItem extends org.matomo.java.tracking.parameters.EcommerceItem {

  /**
   * Creates a new ecommerce item.
   *
   * @deprecated Use {@link org.matomo.java.tracking.parameters.EcommerceItem} instead.
   */
  @Deprecated
  public EcommerceItem(String sku, String name, String category, Double price, Integer quantity) {
    super(sku, name, category, price, quantity);
  }

}
