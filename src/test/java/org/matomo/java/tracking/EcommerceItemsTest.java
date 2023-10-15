package org.matomo.java.tracking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Ecommerce Items Test")
class EcommerceItemsTest {

  @Test
  @DisplayName("Formats Json")
  void formatsJson() {
    EcommerceItems ecommerceItems = new EcommerceItems();
    ecommerceItems.add(new EcommerceItem("sku", "name", "category", 1.0, 1));
    assertThat(ecommerceItems.toString()).isEqualTo("[[\"sku\",\"name\",\"category\",1.0,1]]");
  }
}
