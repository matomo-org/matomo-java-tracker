/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import javax.json.JsonValue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 *
 * @author brettcsorba
 */
public class PiwikJsonArrayTest{
    PiwikJsonArray array;
    
    public PiwikJsonArrayTest(){
    }
    
    @BeforeClass
    public static void setUpClass(){
    }
    
    @AfterClass
    public static void tearDownClass(){
    }
    
    @Before
    public void setUp(){
        array = new PiwikJsonArray();
    }
    
    @After
    public void tearDown(){
    }

    /**
     * Test of get method, of class PiwikJsonArray.
     */
    @Test
    public void testAddGetSet(){
        JsonValue value = mock(JsonValue.class);
        JsonValue value2 = mock(JsonValue.class);
        
        array.add(value);
        assertEquals(value, array.get(0));
        
        array.set(0, value2);
        assertEquals(value2, array.get(0));
    }

    /**
     * Test of toString method, of class PiwikJsonArray.
     */
    @Test
    public void testToString(){
        array.add(JsonValue.TRUE);
        array.add(new EcommerceItem("a", "b", "c", 1.0, 2));
        array.add(JsonValue.FALSE);
        
        assertEquals("[true,[\"a\",\"b\",\"c\",1.0,2],false]", array.toString());
    }
    
}
