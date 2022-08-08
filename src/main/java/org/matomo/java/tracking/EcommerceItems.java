/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
class EcommerceItems {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final List<EcommerceItem> ecommerceItems = new ArrayList<>();

  public void add(@NonNull EcommerceItem ecommerceItem) {
    ecommerceItems.add(ecommerceItem);
  }

  @Nonnull
  public EcommerceItem get(int index) {
    return ecommerceItems.get(index);
  }

  @Override
  public String toString() {
    ArrayNode arrayNode = OBJECT_MAPPER.createArrayNode();
    for (EcommerceItem ecommerceItem : ecommerceItems) {
      arrayNode.add(OBJECT_MAPPER.createArrayNode()
        .add(ecommerceItem.getSku())
        .add(ecommerceItem.getName())
        .add(ecommerceItem.getCategory())
        .add(ecommerceItem.getPrice())
        .add(ecommerceItem.getQuantity())
      );
    }
    return arrayNode.toString();
  }
}
