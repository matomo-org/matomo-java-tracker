/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import java.util.TimeZone;
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
        
        assertEquals("2015-06-01 07:14:45", date.toString());
    }
    
    /**
     * Test of setTimeZone method, of class PiwikDate.
     */
    @Test
    public void testSetTimeZone(){
        PiwikDate date = new PiwikDate(1433186085092L);
        
        date.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        
        assertEquals("2015-06-01 03:14:45", date.toString());
    }
    
}
