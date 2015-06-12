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
        array.add(JsonValue.FALSE);
        
        assertEquals("[true,false]", array.toString());
    }
    
}
