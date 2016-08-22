/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.piwik.java.tracking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Katie
 */
public class CustomVariableListTest {
    private CustomVariableList cvl;
    
    @Before
    public void setUp() {
        cvl = new CustomVariableList();
    }

    @Test
    public void testAdd_CustomVariable() {
        CustomVariable a = new CustomVariable("a", "b");
        CustomVariable b = new CustomVariable("c", "d");
        CustomVariable c = new CustomVariable("a", "e");
        CustomVariable d = new CustomVariable("a", "f");
        
        assertTrue(cvl.isEmpty());
        cvl.add(a);
        assertFalse(cvl.isEmpty());
        assertEquals("b", cvl.get("a"));
        assertEquals(a, cvl.get(1));
        assertEquals("{\"1\":[\"a\",\"b\"]}", cvl.toString());
        
        cvl.add(b);
        assertEquals("d", cvl.get("c"));
        assertEquals(b, cvl.get(2));
        assertEquals("{\"1\":[\"a\",\"b\"],\"2\":[\"c\",\"d\"]}", cvl.toString());
        
        cvl.add(c, 5);
        assertEquals("b", cvl.get("a"));
        assertEquals(c, cvl.get(5));
        assertNull(cvl.get(3));
        assertEquals("{\"1\":[\"a\",\"b\"],\"2\":[\"c\",\"d\"],\"5\":[\"a\",\"e\"]}", cvl.toString());
        
        cvl.add(d);
        assertEquals("f", cvl.get("a"));
        assertEquals(d, cvl.get(1));
        assertEquals(d, cvl.get(5));
        assertEquals("{\"1\":[\"a\",\"f\"],\"2\":[\"c\",\"d\"],\"5\":[\"a\",\"f\"]}", cvl.toString());
        
        cvl.remove("a");
        assertNull(cvl.get("a"));
        assertNull(cvl.get(1));
        assertNull(cvl.get(5));
        assertEquals("{\"2\":[\"c\",\"d\"]}", cvl.toString());

        cvl.remove(2);
        assertNull(cvl.get("c"));
        assertNull(cvl.get(2));
        assertTrue(cvl.isEmpty());
        assertEquals("{}", cvl.toString());
    }
    
    @Test
    public void testAddCustomVariableIndexLessThan1(){
        try{
            cvl.add(new CustomVariable("a", "b"), 0);
            fail("Exception should have been throw.");
        }
        catch(IllegalArgumentException e){
            assertEquals("Index must be greater than 0.", e.getLocalizedMessage());
        }
    }
    
    @Test
    public void testGetCustomVariableIntegerLessThan1(){
        try{
            cvl.get(0);
            fail("Exception should have been throw.");
        }
        catch(IllegalArgumentException e){
            assertEquals("Index must be greater than 0.", e.getLocalizedMessage());
        }
    }
}
