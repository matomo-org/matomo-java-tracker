/* 
 * Piwik Java Tracking API
 * 
 * @link https://github.com/piwik/piwik-java-tracking-api
 * @license https://github.com/piwik/piwik-java-tracking-api/blob/master/LICENSE BSD 3-Clause
 */
package org.piwik.java;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author brettcsorba
 */
public class PiwikDateTest{
    
    public PiwikDateTest(){
    }
    
    @BeforeClass
    public static void setUpClass(){
    }
    
    @AfterClass
    public static void tearDownClass(){
    }
    
    @Before
    public void setUp(){
    }
    
    @After
    public void tearDown(){
    }

    /**
     * Test of constructor, of class PiwikDate.
     */
    @Test
    public void testConstructor0(){
        PiwikDate date = new PiwikDate();
        
        assertNotNull(date);
    }
    @Test
    public void testConstructor1(){
        PiwikDate date = new PiwikDate(1433186085092L);
        
        assertNotNull(date);
    }
    /**
     * Test of toString method, of class PiwikDate.
     */
    @Test
    public void testToString(){
        PiwikDate date = new PiwikDate(1433186085092L);
        
        assertEquals("2015-06-01 03:14:45", date.toString());
    }
    
}
