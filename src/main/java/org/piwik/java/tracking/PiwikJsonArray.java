/*
 * Piwik Java Tracker
 *
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;

/**
 * Object representing a JSON array required by some Piwik query parameters.
 *
 * @author brettcsorba
 */
public class PiwikJsonArray {
  List<JsonValue> list = new ArrayList<>();

  /**
   * Get the value stored at the specified index.
   *
   * @param index the index of the value to return
   * @return the value stored at the specified index
   */
  public JsonValue get(int index) {
    return list.get(index);
  }

  /**
   * Add a value to the end of this array.
   *
   * @param value value to add to the end of the array
   */
  public void add(JsonValue value) {
    list.add(value);
  }

  /**
   * Set the value at the specified index to the specified value.
   *
   * @param index the index of the value to set
   * @param value the value to set at the specified index
   */
  public void set(int index, JsonValue value) {
    list.set(index, value);
  }

  /**
   * Returns a JSON encoded array string representing this object.
   *
   * @return returns the current array as a JSON encode string
   */
  @Override
  public String toString() {
    JsonArrayBuilder ab = Json.createArrayBuilder();

    for (int x = 0; x < list.size(); ++x) {
      JsonValue value = list.get(x);
      if (value instanceof EcommerceItem) {
        ab.add(((EcommerceItem) value).toJsonArray());
      } else {
        ab.add(value);
      }
    }

    return ab.build().toString();
  }
}
