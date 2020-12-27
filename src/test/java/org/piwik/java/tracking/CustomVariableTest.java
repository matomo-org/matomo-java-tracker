/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.piwik.java.tracking;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Katie
 */
public class CustomVariableTest {
  private CustomVariable customVariable;

  @Before
  public void setUp() {
    customVariable = new CustomVariable("key", "value");
  }

  @Test
  public void testConstructorNullKey() {
    try {
      new CustomVariable(null, null);
      fail("Exception should have been throw.");
    } catch (NullPointerException e) {
      assertEquals("Key cannot be null.", e.getLocalizedMessage());
    }
  }

  @Test
  public void testConstructorNullValue() {
    try {
      new CustomVariable("key", null);
      fail("Exception should have been throw.");
    } catch (NullPointerException e) {
      assertEquals("Value cannot be null.", e.getLocalizedMessage());
    }
  }

  @Test
  public void testGetKey() {
    assertEquals("key", customVariable.getKey());
  }

  @Test
  public void testGetValue() {
    assertEquals("value", customVariable.getValue());
  }
}
