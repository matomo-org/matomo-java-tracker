/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.matomo.java.tracking;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Katie
 */
public class CustomVariablesTest {
  private final CustomVariables customVariables = new CustomVariables();

  @Test
  public void testAdd_CustomVariable() {
    CustomVariable a = new CustomVariable("a", "b");
    CustomVariable b = new CustomVariable("c", "d");
    CustomVariable c = new CustomVariable("a", "e");
    CustomVariable d = new CustomVariable("a", "f");

    assertTrue(customVariables.isEmpty());
    customVariables.add(a);
    assertFalse(customVariables.isEmpty());
    assertEquals("b", customVariables.get("a"));
    assertEquals(a, customVariables.get(1));
    assertEquals("{\"1\":[\"a\",\"b\"]}", customVariables.toString());

    customVariables.add(b);
    assertEquals("d", customVariables.get("c"));
    assertEquals(b, customVariables.get(2));
    assertEquals("{\"1\":[\"a\",\"b\"],\"2\":[\"c\",\"d\"]}", customVariables.toString());

    customVariables.add(c, 5);
    assertEquals("b", customVariables.get("a"));
    assertEquals(c, customVariables.get(5));
    assertNull(customVariables.get(3));
    assertEquals("{\"1\":[\"a\",\"b\"],\"2\":[\"c\",\"d\"],\"5\":[\"a\",\"e\"]}", customVariables.toString());

    customVariables.add(d);
    assertEquals("f", customVariables.get("a"));
    assertEquals(d, customVariables.get(1));
    assertEquals(d, customVariables.get(5));
    assertEquals("{\"1\":[\"a\",\"f\"],\"2\":[\"c\",\"d\"],\"5\":[\"a\",\"f\"]}", customVariables.toString());

    customVariables.remove("a");
    assertNull(customVariables.get("a"));
    assertNull(customVariables.get(1));
    assertNull(customVariables.get(5));
    assertEquals("{\"2\":[\"c\",\"d\"]}", customVariables.toString());

    customVariables.remove(2);
    assertNull(customVariables.get("c"));
    assertNull(customVariables.get(2));
    assertTrue(customVariables.isEmpty());
    assertEquals("{}", customVariables.toString());
  }

  @Test
  public void testAddCustomVariableIndexLessThan1() {
    try {
      customVariables.add(new CustomVariable("a", "b"), 0);
      fail("Exception should have been throw.");
    } catch (IllegalArgumentException e) {
      assertEquals("Index must be greater than 0.", e.getLocalizedMessage());
    }
  }

  @Test
  public void testGetCustomVariableIntegerLessThan1() {
    try {
      customVariables.get(0);
      fail("Exception should have been throw.");
    } catch (IllegalArgumentException e) {
      assertEquals("Index must be greater than 0.", e.getLocalizedMessage());
    }
  }
}
