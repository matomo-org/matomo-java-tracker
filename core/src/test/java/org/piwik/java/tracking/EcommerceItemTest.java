package org.piwik.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EcommerceItemTest {

  @Test
  void createsEcItem() {
    EcommerceItem item = new EcommerceItem("sku", "name", "category", 1.0, 1);

    assertThat(item.getSku()).isEqualTo("sku");
    assertThat(item.getName()).isEqualTo("name");
    assertThat(item.getCategory()).isEqualTo("category");
    assertThat(item.getPrice()).isEqualTo(1.0);
    assertThat(item.getQuantity()).isEqualTo(1);
  }
}
