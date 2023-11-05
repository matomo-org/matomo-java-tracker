package org.matomo.java.tracking.parameters;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class EcommerceItemsTest {

  @Test
  void formatsJson() {
    EcommerceItems ecommerceItems = EcommerceItems
        .builder()
        .item(EcommerceItem
            .builder()
            .sku("XYZ12345")
            .name("Matomo - The big book about web analytics")
            .category("Education & Teaching")
            .price(23.1)
            .quantity(2)
            .build())
        .item(EcommerceItem
            .builder()
            .sku("B0C2WV3MRJ")
            .name("Matomo for data visualization")
            .category("Education & Teaching")
            .price(15.1)
            .quantity(1)
            .build())
        .build();
    assertThat(ecommerceItems).hasToString("[[\"XYZ12345\",\"Matomo - The big book about web analytics\",\"Education & Teaching\",23.1,2],[\"B0C2WV3MRJ\",\"Matomo for data visualization\",\"Education & Teaching\",15.1,1]]");
  }


}
