package org.matomo.java.tracking;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EcommerceItemsTest {

  @Test
  public void formatsJson() {

    EcommerceItems ecommerceItems = new EcommerceItems();
    ecommerceItems.add(new EcommerceItem("sku", "name", "category", 1.0, 1));

    assertEquals("[[\"sku\",\"name\",\"category\",1.0,1]]", ecommerceItems.toString());

  }
}
