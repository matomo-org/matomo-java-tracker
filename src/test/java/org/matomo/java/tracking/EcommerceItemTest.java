package org.matomo.java.tracking;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EcommerceItemTest {

  private EcommerceItem ecommerceItem = new EcommerceItem(null, null, null, null, null);

  /**
   * Test of constructor, of class EcommerceItem.
   */
  @Test
  void testConstructor() {
    EcommerceItem ecommerceItem = new EcommerceItem("sku", "name", "category", 2.0, 2);
    assertThat(ecommerceItem.getSku()).isEqualTo("sku");
    assertThat(ecommerceItem.getName()).isEqualTo("name");
    assertThat(ecommerceItem.getCategory()).isEqualTo("category");
    assertThat(ecommerceItem.getPrice()).isEqualTo(2.0);
    assertThat(ecommerceItem.getQuantity()).isEqualTo(2);
  }

  /**
   * Test of getSku method, of class EcommerceItem.
   */
  @Test
  void testGetSku() {
    ecommerceItem.setSku("sku");
    assertThat(ecommerceItem.getSku()).isEqualTo("sku");
  }

  /**
   * Test of getName method, of class EcommerceItem.
   */
  @Test
  void testGetName() {
    ecommerceItem.setName("name");
    assertThat(ecommerceItem.getName()).isEqualTo("name");
  }

  /**
   * Test of getCategory method, of class EcommerceItem.
   */
  @Test
  void testGetCategory() {
    ecommerceItem.setCategory("category");
    assertThat(ecommerceItem.getCategory()).isEqualTo("category");
  }

  /**
   * Test of getPrice method, of class EcommerceItem.
   */
  @Test
  void testGetPrice() {
    ecommerceItem.setPrice(2.0);
    assertThat(ecommerceItem.getPrice()).isEqualTo(2.0);
  }

  /**
   * Test of getQuantity method, of class EcommerceItem.
   */
  @Test
  void testGetQuantity() {
    ecommerceItem.setQuantity(2);
    assertThat(ecommerceItem.getQuantity()).isEqualTo(2);
  }
}
