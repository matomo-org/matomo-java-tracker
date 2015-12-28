/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author brettcsorba
 */
public class PiwikJsonObjectTest{
    PiwikJsonObject obj;
    
    public PiwikJsonObjectTest(){
    }
    
    @BeforeClass
    public static void setUpClass(){
    }
    
    @AfterClass
    public static void tearDownClass(){
    }
    
    @Before
    public void setUp(){
        obj = new PiwikJsonObject();
    }
    
    @After
    public void tearDown(){
    }

    /**
     * Test of get method, of class PiwikJsonObject.
     */
    @Test
    public void testMethods(){
        assertTrue(obj.isEmpty());
        assertEquals(0, obj.size());
        assertNull(obj.put("key", "value"));
        assertFalse(obj.isEmpty());
        assertEquals(1, obj.size());
        assertEquals("value", obj.get("key"));
        assertEquals("value", obj.remove("key"));
        assertNull(obj.remove("key"));
        assertTrue(obj.isEmpty());
        assertEquals(0, obj.size());
    }

    /**
     * Test of toString method, of class PiwikJsonObject.
     */
    @Test
    public void testToString(){
        obj.put("key", "value");
        obj.put("key2", "value2");
        obj.put("key3", "value3");
        obj.remove("key2");
        
        assertEquals("{\"1\":[\"key\",\"value\"],\"2\":[\"key3\",\"value3\"]}", obj.toString());
    }
    
}
