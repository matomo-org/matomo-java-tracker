package org.matomo.java.tracking;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author brettcsorba
 */
@DisplayName("Ecommerce Item Test")
class EcommerceItemTest {

  EcommerceItem ecommerceItem;

  public EcommerceItemTest() {
  }

  @BeforeAll
  static void setUpClass() {
  }

  @AfterAll
  static void tearDownClass() {
  }

  @BeforeEach
  void setUp() {
    ecommerceItem = new EcommerceItem(null, null, null, null, null);
  }

  @AfterEach
  void tearDown() {
  }

  /**
   * Test of constructor, of class EcommerceItem.
   */
  @Test
  @DisplayName("Test Constructor")
  void testConstructor() {
    EcommerceItem ecommerceItem = new EcommerceItem("sku", "name", "category", 1.0, 1);
    assertThat(ecommerceItem.getSku()).isEqualTo("sku");
    assertThat(ecommerceItem.getName()).isEqualTo("name");
    assertThat(ecommerceItem.getCategory()).isEqualTo("category");
    assertThat(ecommerceItem.getPrice()).isEqualTo(new Double(1.0));
    assertThat(ecommerceItem.getQuantity()).isEqualTo(new Integer(1));
  }

  /**
   * Test of getSku method, of class EcommerceItem.
   */
  @Test
  @DisplayName("Test Get Sku")
  void testGetSku() {
    ecommerceItem.setSku("sku");
    assertThat(ecommerceItem.getSku()).isEqualTo("sku");
  }

  /**
   * Test of getName method, of class EcommerceItem.
   */
  @Test
  @DisplayName("Test Get Name")
  void testGetName() {
    ecommerceItem.setName("name");
    assertThat(ecommerceItem.getName()).isEqualTo("name");
  }

  /**
   * Test of getCategory method, of class EcommerceItem.
   */
  @Test
  @DisplayName("Test Get Category")
  void testGetCategory() {
    ecommerceItem.setCategory("category");
    assertThat(ecommerceItem.getCategory()).isEqualTo("category");
  }

  /**
   * Test of getPrice method, of class EcommerceItem.
   */
  @Test
  @DisplayName("Test Get Price")
  void testGetPrice() {
    ecommerceItem.setPrice(1.0);
    assertThat(ecommerceItem.getPrice()).isEqualTo(new Double(1.0));
  }

  /**
   * Test of getQuantity method, of class EcommerceItem.
   */
  @Test
  @DisplayName("Test Get Quantity")
  void testGetQuantity() {
    ecommerceItem.setQuantity(1);
    assertThat(ecommerceItem.getQuantity()).isEqualTo(new Integer(1));
  }
}
