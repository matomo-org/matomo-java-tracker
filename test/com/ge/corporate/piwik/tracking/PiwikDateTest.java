/*
 * Copyright (c) 2015 General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.ge.corporate.piwik.tracking;

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
