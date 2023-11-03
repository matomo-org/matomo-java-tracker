package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.EcommerceItem;


class EcommerceItemsTest {

  @Test
  void formatsJson() {
    EcommerceItems ecommerceItems = new EcommerceItems();
    ecommerceItems.add(new EcommerceItem("sku", "name", "category", 1.0, 1));
    assertThat(ecommerceItems).hasToString("[[\"sku\",\"name\",\"category\",1.0,1]]");
  }
}
