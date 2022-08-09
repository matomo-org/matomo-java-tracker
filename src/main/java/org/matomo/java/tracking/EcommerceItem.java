/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an item in an ecommerce order.
 *
 * @author brettcsorba
 */
@Setter
@Getter
@AllArgsConstructor
@Builder
public class EcommerceItem {

  private String sku;
  private String name;
  private String category;
  private Double price;
  private Integer quantity;

}
