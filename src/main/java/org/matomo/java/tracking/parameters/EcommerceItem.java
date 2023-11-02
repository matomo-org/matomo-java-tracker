/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an item in an ecommerce order.
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class EcommerceItem {

  private String sku;

  @Builder.Default
  private String name = "";

  @Builder.Default
  private String category = "";

  @Builder.Default
  private Double price = 0.0;

  @Builder.Default
  private Integer quantity = 0;

  public String toString() {
    return String.format("[\"%s\",\"%s\",\"%s\",%s,%d]", sku, name, category, price, quantity);
  }
}
