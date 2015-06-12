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

import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author brettcsorba
 */
public class PiwikLocaleTest{
    PiwikLocale locale;
    
    public PiwikLocaleTest(){
    }
    
    @BeforeClass
    public static void setUpClass(){
    }
    
    @AfterClass
    public static void tearDownClass(){
    }
    
    @Before
    public void setUp(){
        locale = new PiwikLocale(Locale.US);
    }
    
    @After
    public void tearDown(){
    }

    /**
     * Test of getLocale method, of class PiwikLocale.
     */
    @Test
    public void testConstructor(){
        assertEquals(Locale.US, locale.getLocale());
    }

    /**
     * Test of setLocale method, of class PiwikLocale.
     */
    @Test
    public void testLocale(){
        locale.setLocale(Locale.GERMANY);
        assertEquals(Locale.GERMANY, locale.getLocale());
    }

    /**
     * Test of toString method, of class PiwikLocale.
     */
    @Test
    public void testToString(){
        assertEquals("us", locale.toString());
    }
    
}
