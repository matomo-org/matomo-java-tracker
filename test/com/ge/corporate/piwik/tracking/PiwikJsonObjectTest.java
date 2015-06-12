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
import static org.junit.Assert.assertNull;
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
    public void testPutGetRemove(){
        assertNull(obj.put("key", "value"));
        assertEquals("value", obj.get("key"));
        assertEquals("value", obj.remove("key"));
        assertNull(obj.remove("key"));
    }

    /**
     * Test of toString method, of class PiwikJsonObject.
     */
    @Test
    public void testToString(){
        obj.put("key", "value");
        obj.put("key2", "value2");
        
        assertEquals("{\"1\":[\"key2\",\"value2\"],\"2\":[\"key\",\"value\"]}", obj.toString());
    }
    
}
