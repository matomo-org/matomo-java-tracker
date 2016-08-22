/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.TypeConstraintException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author brettcsorba
 */
public class PiwikRequestTest{
    private PiwikRequest request;
    
    @Before
    public void setUp() throws Exception{
        request = new PiwikRequest(3, new URL("http://test.com"));
    }
    
    @After
    public void tearDown(){
    }
    
    @Test
    public void testConstructor() throws Exception{
        request = new PiwikRequest(3, new URL("http://test.com"));
        assertEquals(new Integer(3), request.getSiteId());
        assertTrue(request.getRequired());
        assertEquals(new URL("http://test.com"), request.getActionUrl());
        assertNotNull(request.getVisitorId());
        assertNotNull(request.getRandomValue());
        assertEquals("1", request.getApiVersion());
        assertFalse(request.getResponseAsImage());
    }

    /**
     * Test of getActionName method, of class PiwikRequest.
     */
    @Test
    public void testActionName(){
        request.setActionName("action");
        assertEquals("action", request.getActionName());
        request.setActionName(null);
        assertNull(request.getActionName());
    }

    /**
     * Test of getActionTime method, of class PiwikRequest.
     */
    @Test
    public void testActionTime(){
        request.setActionTime(1000L);
        assertEquals(new Long(1000L), request.getActionTime());
    }

    /**
     * Test of getActionUrl method, of class PiwikRequest.
     */
    @Test
    public void testActionUrl() throws Exception{
        request.setActionUrl(null);
        assertNull(request.getActionUrl());
        assertNull(request.getActionUrlAsString());
        
        URL url = new URL("http://action.com");
        request.setActionUrl(url);
        assertEquals(url, request.getActionUrl());
        try{
            request.getActionUrlAsString();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Action URL is a URL, not a String.  Use \"getActionUrl\" instead.", e.getLocalizedMessage());
        }
        
        request.setActionUrlWithString(null);
        assertNull(request.getActionUrl());
        assertNull(request.getActionUrlAsString());
        
        request.setActionUrlWithString("actionUrl");
        assertEquals("actionUrl", request.getActionUrlAsString());
        try{
            request.getActionUrl();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Action URL is a String, not a URL.  Use \"getActionUrlAsString\" instead.", e.getLocalizedMessage());
        }
    }

    /**
     * Test of getApiVersion method, of class PiwikRequest.
     */
    @Test
    public void testApiVersion(){
        request.setApiVersion("2");
        assertEquals("2", request.getApiVersion());
    }

    /**
     * Test of getAuthToken method, of class PiwikRequest.
     */
    @Test
    public void testAuthTokenTT(){
        try{
            request.setAuthToken("1234");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1234 is not 32 characters long.", e.getLocalizedMessage());            
        }
    }
    @Test
    public void testAuthTokenTF(){
        request.setAuthToken("12345678901234567890123456789012");
        assertEquals("12345678901234567890123456789012", request.getAuthToken());
    }
    @Test
    public void testAuthTokenF(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setAuthToken(null);
        assertNull(request.getAuthToken());
    }

    /**
     * Test of verifyAuthTokenSet method, of class PiwikRequest.
     */
    @Test
    public void testVerifyAuthTokenSet(){
        try{
            request.verifyAuthTokenSet();
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("AuthToken must be set before this value can be set.", e.getLocalizedMessage());            
        }
    }

    /**
     * Test of getCampaignKeyword method, of class PiwikRequest.
     */
    @Test
    public void testCampaignKeyword(){
        request.setCampaignKeyword("keyword");
        assertEquals("keyword", request.getCampaignKeyword());
    }

    /**
     * Test of getCampaignName method, of class PiwikRequest.
     */
    @Test
    public void testCampaignName(){
        request.setCampaignName("name");
        assertEquals("name", request.getCampaignName());
    }

    /**
     * Test of getCharacterSet method, of class PiwikRequest.
     */
    @Test
    public void testCharacterSet(){
        Charset charset = Charset.defaultCharset();
        request.setCharacterSet(charset);
        assertEquals(charset, request.getCharacterSet());
    }

    /**
     * Test of getContentInteraction method, of class PiwikRequest.
     */
    @Test
    public void testContentInteraction(){
        request.setContentInteraction("interaction");
        assertEquals("interaction", request.getContentInteraction());
    }

    /**
     * Test of getContentName method, of class PiwikRequest.
     */
    @Test
    public void testContentName(){
        request.setContentName("name");
        assertEquals("name", request.getContentName());
    }

    /**
     * Test of getContentPiece method, of class PiwikRequest.
     */
    @Test
    public void testContentPiece(){
        request.setContentPiece("piece");
        assertEquals("piece", request.getContentPiece());
    }

    /**
     * Test of getContentTarget method, of class PiwikRequest.
     */
    @Test
    public void testContentTarget() throws Exception{
        URL url = new URL("http://target.com");
        request.setContentTarget(url);
        assertEquals(url, request.getContentTarget());
        
        try{
            request.getContentTargetAsString();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Content Target is a URL, not a String.  Use \"getContentTarget\" instead.", e.getLocalizedMessage());
        }
        
        request.setContentTargetWithString("contentTarget");
        assertEquals("contentTarget", request.getContentTargetAsString());
        
        try{
            request.getContentTarget();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Content Target is a String, not a URL.  Use \"getContentTargetAsString\" instead.", e.getLocalizedMessage());
        }
    }

    /**
     * Test of getCurrentHour method, of class PiwikRequest.
     */
    @Test
    public void testCurrentHour(){
        request.setCurrentHour(1);
        assertEquals(new Integer(1), request.getCurrentHour());
    }

    /**
     * Test of getCurrentMinute method, of class PiwikRequest.
     */
    @Test
    public void testCurrentMinute(){
        request.setCurrentMinute(2);
        assertEquals(new Integer(2), request.getCurrentMinute());
    }

    /**
     * Test of getCurrentSecond method, of class PiwikRequest.
     */
    @Test
    public void testCurrentSecond(){
        request.setCurrentSecond(3);
        assertEquals(new Integer(3), request.getCurrentSecond());
    }
    
    /**
     * Test of getCustomTrackingParameter method, of class PiwikRequest.
     */
    @Test
    public void testGetCustomTrackingParameter_T(){
        try{
            request.getCustomTrackingParameter(null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("Key cannot be null.", e.getLocalizedMessage());
        }        
    }
    @Test
    public void testGetCustomTrackingParameter_FT(){
        assertTrue(request.getCustomTrackingParameter("key").isEmpty());
    }
    @Test
    public void testSetCustomTrackingParameter_T(){
        try{
            request.setCustomTrackingParameter(null, null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("Key cannot be null.", e.getLocalizedMessage());
        }        
    }
    @Test
    public void testSetCustomTrackingParameter_F(){
        request.setCustomTrackingParameter("key", "value");
        List l = request.getCustomTrackingParameter("key");
        assertEquals(1, l.size());
        assertEquals("value", l.get(0));
        
        request.setCustomTrackingParameter("key", "value2");
        l = request.getCustomTrackingParameter("key");
        assertEquals(1, l.size());
        assertEquals("value2", l.get(0));
        
        request.setCustomTrackingParameter("key", null);
        l = request.getCustomTrackingParameter("key");
        assertTrue(l.isEmpty());
    }
    @Test
    public void testAddCustomTrackingParameter_T(){
        try{
            request.addCustomTrackingParameter(null, null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("Key cannot be null.", e.getLocalizedMessage());
        }        
    }
    @Test
    public void testAddCustomTrackingParameter_FT(){
        try{
            request.addCustomTrackingParameter("key", null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("Cannot add a null custom tracking parameter.", e.getLocalizedMessage());
        }        
    }
    @Test
    public void testAddCustomTrackingParameter_FF(){
        request.addCustomTrackingParameter("key", "value");
        List l = request.getCustomTrackingParameter("key");
        assertEquals(1, l.size());
        assertEquals("value", l.get(0));
        
        request.addCustomTrackingParameter("key", "value2");
        l = request.getCustomTrackingParameter("key");
        assertEquals(2, l.size());
        assertTrue(l.contains("value"));
        assertTrue(l.contains("value2"));
    }
    @Test
    public void testClearCustomTrackingParameter(){
        request.setCustomTrackingParameter("key", "value");
        request.clearCustomTrackingParameter();
        List l = request.getCustomTrackingParameter("key");
        assertTrue(l.isEmpty());
    }

    /**
     * Test of getDeviceResolution method, of class PiwikRequest.
     */
    @Test
    public void testDeviceResolution(){
        request.setDeviceResolution("1x2");
        assertEquals("1x2", request.getDeviceResolution());
    }

    /**
     * Test of getDownloadUrl method, of class PiwikRequest.
     */
    @Test
    public void testDownloadUrl() throws Exception{
        URL url = new URL("http://download.com");
        request.setDownloadUrl(url);
        assertEquals(url, request.getDownloadUrl());
        
        try{
            request.getDownloadUrlAsString();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Download URL is a URL, not a String.  Use \"getDownloadUrl\" instead.", e.getLocalizedMessage());
        }
        
        request.setDownloadUrlWithString("downloadUrl");
        assertEquals("downloadUrl", request.getDownloadUrlAsString());
        
        try{
            request.getDownloadUrl();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Download URL is a String, not a URL.  Use \"getDownloadUrlAsString\" instead.", e.getLocalizedMessage());
        }
    }

    /**
     * Test of enableEcommerce method, of class PiwikRequest.
     */
    @Test
    public void testEnableEcommerce(){
        request.enableEcommerce();
        assertEquals(new Integer(0), request.getGoalId());
    }

    /**
     * Test of verifyEcommerceEnabled method, of class PiwikRequest.
     */
    @Test
    public void testVerifyEcommerceEnabledT(){
        try{
            request.verifyEcommerceEnabled();
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVerifyEcommerceEnabledFT(){
        try{
            request.setGoalId(1);
            request.verifyEcommerceEnabled();
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVerifyEcommerceEnabledFF(){
        request.enableEcommerce();
        request.verifyEcommerceEnabled();
    }

    /**
     * Test of verifyEcommerceState method, of class PiwikRequest.
     */
    @Test
    public void testVerifyEcommerceStateE(){
        try{
            request.verifyEcommerceState();
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVerifyEcommerceStateT(){
        try{
            request.enableEcommerce();
            request.verifyEcommerceState();
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("EcommerceId must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVerifyEcommerceStateFT(){
        try{
            request.enableEcommerce();
            request.setEcommerceId("1");
            request.verifyEcommerceState();
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("EcommerceRevenue must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVerifyEcommerceStateFF(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.verifyEcommerceState();
    }

    /**
     * Test of getEcommerceDiscount method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceDiscountT(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceDiscount(1.0);
        
        assertEquals(new Double(1.0), request.getEcommerceDiscount());
    }
    @Test
    public void testEcommerceDiscountTE(){
        try{
            request.setEcommerceDiscount(1.0);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceDiscountF(){
        request.setEcommerceDiscount(null);
        
        assertNull(request.getEcommerceDiscount());
    }

    /**
     * Test of getEcommerceId method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceIdT(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        
        assertEquals("1", request.getEcommerceId());
    }
    @Test
    public void testEcommerceIdTE(){
        try{
            request.setEcommerceId("1");
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceIdF(){
        request.setEcommerceId(null);
        
        assertNull(request.getEcommerceId());
    }

    /**
     * Test of getEcommerceItem method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceItemE(){
        try{
            EcommerceItem item = new EcommerceItem("sku", "name", "category", 1.0, 2);
            request.addEcommerceItem(item);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceItemE2(){
        try{
            request.enableEcommerce();
            request.setEcommerceId("1");
            request.setEcommerceRevenue(2.0);
            request.addEcommerceItem(null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("Value cannot be null.", e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceItem(){
        assertNull(request.getEcommerceItem(0));
        
        EcommerceItem item = new EcommerceItem("sku", "name", "category", 1.0, 2);
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.addEcommerceItem(item);
        
        assertEquals(item, request.getEcommerceItem(0));
        
        request.clearEcommerceItems();
        assertNull(request.getEcommerceItem(0));
    }

    /**
     * Test of getEcommerceLastOrderTimestamp method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceLastOrderTimestampT(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceLastOrderTimestamp(1000L);
        
        assertEquals(new Long(1000L), request.getEcommerceLastOrderTimestamp());
    }
    @Test
    public void testEcommerceLastOrderTimestampTE(){
        try{
            request.setEcommerceLastOrderTimestamp(1000L);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceLastOrderTimestampF(){
        request.setEcommerceLastOrderTimestamp(null);
        
        assertNull(request.getEcommerceLastOrderTimestamp());
    }

    /**
     * Test of getEcommerceRevenue method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceRevenueT(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceRevenue());
    }
    @Test
    public void testEcommerceRevenueTE(){
        try{
            request.setEcommerceRevenue(20.0);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceRevenueF(){
        request.setEcommerceRevenue(null);
        
        assertNull(request.getEcommerceRevenue());
    }

    /**
     * Test of getEcommerceShippingCost method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceShippingCostT(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceShippingCost(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceShippingCost());
    }
    @Test
    public void testEcommerceShippingCostTE(){
        try{
            request.setEcommerceShippingCost(20.0);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceShippingCostF(){
        request.setEcommerceShippingCost(null);
        
        assertNull(request.getEcommerceShippingCost());
    }

    /**
     * Test of getEcommerceSubtotal method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceSubtotalT(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceSubtotal(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceSubtotal());
    }
    @Test
    public void testEcommerceSubtotalTE(){
        try{
            request.setEcommerceSubtotal(20.0);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceSubtotalF(){
        request.setEcommerceSubtotal(null);
        
        assertNull(request.getEcommerceSubtotal());
    }

    /**
     * Test of getEcommerceTax method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceTaxT(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceTax(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceTax());
    }
    @Test
    public void testEcommerceTaxTE(){
        try{
            request.setEcommerceTax(20.0);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testEcommerceTaxF(){
        request.setEcommerceTax(null);
        
        assertNull(request.getEcommerceTax());
    }

    /**
     * Test of getEventAction method, of class PiwikRequest.
     */
    @Test
    public void testEventAction(){
        request.setEventAction("action");
        assertEquals("action", request.getEventAction());
        request.setEventAction(null);
        assertNull(request.getEventAction());
    }
    @Test
    public void testEventActionException(){
        try{
            request.setEventAction("");
            fail("Exception should have been thrown");
        }
        catch(IllegalArgumentException e){
            assertEquals("Value cannot be empty.", e.getLocalizedMessage());
        }
    }

    /**
     * Test of getEventCategory method, of class PiwikRequest.
     */
    @Test
    public void testEventCategory(){
        request.setEventCategory("category");
        assertEquals("category", request.getEventCategory());
    }

    /**
     * Test of getEventName method, of class PiwikRequest.
     */
    @Test
    public void testEventName(){
        request.setEventName("name");
        assertEquals("name", request.getEventName());
    }


    /**
     * Test of getEventValue method, of class PiwikRequest.
     */
    @Test
    public void testEventValue(){
        request.setEventValue(1);
        assertEquals(1, request.getEventValue());
    }

    /**
     * Test of getGoalId method, of class PiwikRequest.
     */
    @Test
    public void testGoalId(){
        request.setGoalId(1);
        assertEquals(new Integer(1), request.getGoalId());
    }

    /**
     * Test of getGoalRevenue method, of class PiwikRequest.
     */
    @Test
    public void testGoalRevenueTT(){
        try{
            request.setGoalRevenue(20.0);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be set before GoalRevenue can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testGoalRevenueTF(){
        request.setGoalId(1);
        request.setGoalRevenue(20.0);
        
        assertEquals(new Double(20.0), request.getGoalRevenue());
    }
    @Test
    public void testGoalRevenueF(){
        request.setGoalRevenue(null);
        
        assertNull(request.getGoalRevenue());
    }

    /**
     * Test of getHeaderAcceptLanguage method, of class PiwikRequest.
     */
    @Test
    public void testHeaderAcceptLanguage(){
        request.setHeaderAcceptLanguage("language");        
        assertEquals("language", request.getHeaderAcceptLanguage());
    }

    /**
     * Test of getHeaderUserAgent method, of class PiwikRequest.
     */
    @Test
    public void testHeaderUserAgent(){
        request.setHeaderUserAgent("agent");
        assertEquals("agent", request.getHeaderUserAgent());
    }

    /**
     * Test of getNewVisit method, of class PiwikRequest.
     */
    @Test
    public void testNewVisit(){
        request.setNewVisit(true);
        assertEquals(true, request.getNewVisit());
        request.setNewVisit(null);
        assertNull(request.getNewVisit());
    }

    /**
     * Test of getOutlinkUrl method, of class PiwikRequest.
     */
    @Test
    public void testOutlinkUrl() throws Exception{
        URL url = new URL("http://outlink.com");
        request.setOutlinkUrl(url);
        assertEquals(url, request.getOutlinkUrl());
        
        try{
            request.getOutlinkUrlAsString();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Outlink URL is a URL, not a String.  Use \"getOutlinkUrl\" instead.", e.getLocalizedMessage());
        }
        
        request.setOutlinkUrlWithString("outlinkUrl");
        assertEquals("outlinkUrl", request.getOutlinkUrlAsString());
        
        try{
            request.getOutlinkUrl();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Outlink URL is a String, not a URL.  Use \"getOutlinkUrlAsString\" instead.", e.getLocalizedMessage());
        }
    }

    /**
     * Test of getPageCustomVariable method, of class PiwikRequest.
     */
    @Test
    public void testPageCustomVariableStringStringE(){
        try{
            request.setPageCustomVariable(null, null);
            fail("Exception should have been thrown");
        }
        catch(NullPointerException e){
            assertEquals("Key cannot be null.", e.getLocalizedMessage());
        }        
    }
    @Test
    public void testPageCustomVariableStringStringE2(){
        try{
            request.setPageCustomVariable(null, "pageVal");
            fail("Exception should have been thrown");
        }
        catch(NullPointerException e){
            assertEquals("Key cannot be null.", e.getLocalizedMessage());
        }        
    }
    @Test
    public void testPageCustomVariableStringStringE3(){
        try{
            request.getPageCustomVariable(null);
            fail("Exception should have been thrown");
        }
        catch(NullPointerException e){
            assertEquals("Key cannot be null.", e.getLocalizedMessage());
        }        
    }
    @Test
    public void testPageCustomVariableStringString(){
        assertNull(request.getPageCustomVariable("pageKey"));
        request.setPageCustomVariable("pageKey", "pageVal");
        assertEquals("pageVal", request.getPageCustomVariable("pageKey"));
        request.setPageCustomVariable("pageKey", null);
        assertNull(request.getPageCustomVariable("pageKey"));
        request.setPageCustomVariable("pageKey", "pageVal");
        assertEquals("pageVal", request.getPageCustomVariable("pageKey"));
    }
    @Test
    public void testPageCustomVariableCustomVariable(){
        assertNull(request.getPageCustomVariable(1));
        CustomVariable cv = new CustomVariable("pageKey", "pageVal");
        request.setPageCustomVariable(cv, 1);
        assertEquals(cv, request.getPageCustomVariable(1));
        request.setPageCustomVariable(null, 1);
        assertNull(request.getPageCustomVariable(1));
        request.setPageCustomVariable(cv, 2);
        assertEquals(cv, request.getPageCustomVariable(2));
    }

    /**
     * Test of getPluginDirector method, of class PiwikRequest.
     */
    @Test
    public void testPluginDirector(){
        request.setPluginDirector(true);
        assertEquals(true, request.getPluginDirector());
    }

    /**
     * Test of getPluginFlash method, of class PiwikRequest.
     */
    @Test
    public void testPluginFlash(){
        request.setPluginFlash(true);
        assertEquals(true, request.getPluginFlash());
    }

    /**
     * Test of getPluginGears method, of class PiwikRequest.
     */
    @Test
    public void testPluginGears(){
        request.setPluginGears(true);
        assertEquals(true, request.getPluginGears());
    }

    /**
     * Test of getPluginJava method, of class PiwikRequest.
     */
    @Test
    public void testPluginJava(){
        request.setPluginJava(true);
        assertEquals(true, request.getPluginJava());
    }

    /**
     * Test of getPluginPDF method, of class PiwikRequest.
     */
    @Test
    public void testPluginPDF(){
        request.setPluginPDF(true);
        assertEquals(true, request.getPluginPDF());
    }

    /**
     * Test of getPluginQuicktime method, of class PiwikRequest.
     */
    @Test
    public void testPluginQuicktime(){
        request.setPluginQuicktime(true);
        assertEquals(true, request.getPluginQuicktime());
    }

    /**
     * Test of getPluginRealPlayer method, of class PiwikRequest.
     */
    @Test
    public void testPluginRealPlayer(){
        request.setPluginRealPlayer(true);
        assertEquals(true, request.getPluginRealPlayer());
    }

    /**
     * Test of getPluginSilverlight method, of class PiwikRequest.
     */
    @Test
    public void testPluginSilverlight(){
        request.setPluginSilverlight(true);
        assertEquals(true, request.getPluginSilverlight());
    }

    /**
     * Test of getPluginWindowsMedia method, of class PiwikRequest.
     */
    @Test
    public void testPluginWindowsMedia(){
        request.setPluginWindowsMedia(true);
        assertEquals(true, request.getPluginWindowsMedia());
    }

    /**
     * Test of getRandomValue method, of class PiwikRequest.
     */
    @Test
    public void testRandomValue(){
        request.setRandomValue("value");
        assertEquals("value", request.getRandomValue());
    }

    /**
     * Test of setReferrerUrl method, of class PiwikRequest.
     */
    @Test
    public void testReferrerUrl() throws Exception{
        URL url = new URL("http://referrer.com");
        request.setReferrerUrl(url);
        assertEquals(url, request.getReferrerUrl());
        
        try{
            request.getReferrerUrlAsString();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Referrer URL is a URL, not a String.  Use \"getReferrerUrl\" instead.", e.getLocalizedMessage());
        }
        
        request.setReferrerUrlWithString("referrerUrl");
        assertEquals("referrerUrl", request.getReferrerUrlAsString());
        
        try{
            request.getReferrerUrl();
            fail("Exception should have been thrown.");
        }
        catch(TypeConstraintException e){
            assertEquals("The stored Referrer URL is a String, not a URL.  Use \"getReferrerUrlAsString\" instead.", e.getLocalizedMessage());
        }
    }

    /**
     * Test of getRequestDatetime method, of class PiwikRequest.
     */
    @Test
    public void testRequestDatetimeTTT(){
        request.setAuthToken("12345678901234567890123456789012");
        PiwikDate date = new PiwikDate(1000L);
        request.setRequestDatetime(date);
        
        assertEquals(date, request.getRequestDatetime());
    }
    @Test
    public void testRequestDatetimeTTF(){
        try{
            PiwikDate date = new PiwikDate(1000L);
            request.setRequestDatetime(date);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("Because you are trying to set RequestDatetime for a time greater than 4 hours ago, AuthToken must be set first.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testRequestDatetimeTF(){
        PiwikDate date = new PiwikDate();
        request.setRequestDatetime(date);
        assertEquals(date, request.getRequestDatetime());
    }
    @Test
    public void testRequestDatetimeF(){
        PiwikDate date = new PiwikDate();
        request.setRequestDatetime(date);
        request.setRequestDatetime(null);
        assertNull(request.getRequestDatetime());
    }

    /**
     * Test of getRequired method, of class PiwikRequest.
     */
    @Test
    public void testRequired(){
        request.setRequired(false);
        assertEquals(false, request.getRequired());
    }

    /**
     * Test of getResponseAsImage method, of class PiwikRequest.
     */
    @Test
    public void testResponseAsImage(){
        request.setResponseAsImage(true);
        assertEquals(true, request.getResponseAsImage());
    }

    /**
     * Test of getSearchCategory method, of class PiwikRequest.
     */
    @Test
    public void testSearchCategoryTT(){
        try{
            request.setSearchCategory("category");
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("SearchQuery must be set before SearchCategory can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testSearchCategoryTF(){
        request.setSearchQuery("query");
        request.setSearchCategory("category");
        assertEquals("category", request.getSearchCategory());
    }
    @Test
    public void testSearchCategoryF(){
        request.setSearchCategory(null);
        assertNull(request.getSearchCategory());
    }

    /**
     * Test of getSearchQuery method, of class PiwikRequest.
     */
    @Test
    public void testSearchQuery(){
        request.setSearchQuery("query");
        assertEquals("query", request.getSearchQuery());
    }

    /**
     * Test of getSearchResultsCount method, of class PiwikRequest.
     */
    @Test
    public void testSearchResultsCountTT(){
        try{
            request.setSearchResultsCount(100L);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("SearchQuery must be set before SearchResultsCount can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testSearchResultsCountTF(){
        request.setSearchQuery("query");
        request.setSearchResultsCount(100L);
        assertEquals(new Long(100L), request.getSearchResultsCount());
    }
    @Test
    public void testSearchResultsCountF(){
        request.setSearchResultsCount(null);
        assertNull(request.getSearchResultsCount());
    }

    /**
     * Test of getSiteId method, of class PiwikRequest.
     */
    @Test
    public void testSiteId(){
        request.setSiteId(2);
        assertEquals(new Integer(2), request.getSiteId());
    }

    /**
     * Test of setTrackBotRequest method, of class PiwikRequest.
     */
    @Test
    public void testTrackBotRequests(){
        request.setTrackBotRequests(true);
        assertEquals(true, request.getTrackBotRequests());
    }


    /**
     * Test of getUserrCustomVariable method, of class PiwikRequest.
     */
    @Test
    public void testUserCustomVariableStringString(){
        request.setUserCustomVariable("userKey", "userValue");
        assertEquals("userValue", request.getUserCustomVariable("userKey"));
    }
    @Test
    public void testVisitCustomVariableCustomVariable(){
        request.setRandomValue("random");
        request.setVisitorId("1234567890123456");
        
        assertNull(request.getVisitCustomVariable(1));
        CustomVariable cv = new CustomVariable("visitKey", "visitVal");
        request.setVisitCustomVariable(cv, 1);
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_cvar={\"1\":[\"visitKey\",\"visitVal\"]}&_id=1234567890123456&url=http://test.com", request.getQueryString());
        
        request.setUserCustomVariable("key", "val");
        assertEquals(cv, request.getVisitCustomVariable(1));
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_cvar={\"1\":[\"visitKey\",\"visitVal\"],\"2\":[\"key\",\"val\"]}&_id=1234567890123456&url=http://test.com", request.getQueryString());
        
        request.setVisitCustomVariable(null, 1);
        assertNull(request.getVisitCustomVariable(1));
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_cvar={\"2\":[\"key\",\"val\"]}&_id=1234567890123456&url=http://test.com", request.getQueryString());
        
        request.setVisitCustomVariable(cv, 2);
        assertEquals(cv, request.getVisitCustomVariable(2));
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_cvar={\"2\":[\"visitKey\",\"visitVal\"]}&_id=1234567890123456&url=http://test.com", request.getQueryString());
        
        request.setUserCustomVariable("visitKey", null);
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com", request.getQueryString());
    }

    /**
     * Test of getUserId method, of class PiwikRequest.
     */
    @Test
    public void testUserId(){
        request.setUserId("id");
        assertEquals("id", request.getUserId());
    }

    /**
     * Test of getVisitorCity method, of class PiwikRequest.
     */
    @Test
    public void testVisitorCityT(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorCity("city");
        assertEquals("city", request.getVisitorCity());
    }
    @Test
    public void testVisitorCityTE(){
        try{
            request.setVisitorCity("city");
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("AuthToken must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorCityF(){
        request.setVisitorCity(null);
        assertNull(request.getVisitorCity());
    }

    /**
     * Test of getVisitorCountry method, of class PiwikRequest.
     */
    @Test
    public void testVisitorCountryT(){
        PiwikLocale country = new PiwikLocale(Locale.US);
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorCountry(country);
        
        assertEquals(country, request.getVisitorCountry());
    }
    @Test
    public void testVisitorCountryTE(){
        try{
            PiwikLocale country = new PiwikLocale(Locale.US);
            request.setVisitorCountry(country);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("AuthToken must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorCountryF(){
        request.setVisitorCountry(null);
        
        assertNull(request.getVisitorCountry());
    }

    /**
     * Test of getVisitorCustomId method, of class PiwikRequest.
     */
    @Test
    public void testVisitorCustomTT(){
        try{
            request.setVisitorCustomId("1");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1 is not 16 characters long.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorCustomTFT(){
        try{
            request.setVisitorCustomId("1234567890abcdeg");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1234567890abcdeg is not a hexadecimal string.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorCustomIdTFF(){
        request.setVisitorCustomId("1234567890abcdef");
        assertEquals("1234567890abcdef", request.getVisitorCustomId());
    }
    @Test
    public void testVisitorCustomIdF(){
        request.setVisitorCustomId("1234567890abcdef");
        request.setVisitorCustomId(null);
        assertNull(request.getVisitorCustomId());
    }

    /**
     * Test of getVisitorFirstVisitTimestamp method, of class PiwikRequest.
     */
    @Test
    public void testVisitorFirstVisitTimestamp(){
        request.setVisitorFirstVisitTimestamp(1000L);
        assertEquals(new Long(1000L), request.getVisitorFirstVisitTimestamp());
    }

    /**
     * Test of getVisitorId method, of class PiwikRequest.
     */
    @Test
    public void testVisitorIdTT(){
        try{
            request.setVisitorId("1");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1 is not 16 characters long.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorIdTFT(){
        try{
            request.setVisitorId("1234567890abcdeg");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1234567890abcdeg is not a hexadecimal string.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorIdTFF(){
        request.setVisitorId("1234567890abcdef");
        assertEquals("1234567890abcdef", request.getVisitorId());
    }
    @Test
    public void testVisitorIdF(){
        request.setVisitorId("1234567890abcdef");
        request.setVisitorId(null);
        assertNull(request.getVisitorId());
    }

    /**
     * Test of getVisitorIp method, of class PiwikRequest.
     */
    @Test
    public void testVisitorIpT(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorIp("ip");
        assertEquals("ip", request.getVisitorIp());
    }
    @Test
    public void testVisitorIpTE(){
        try{
            request.setVisitorIp("ip");
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("AuthToken must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorIpF(){
        request.setVisitorIp(null);
        assertNull(request.getVisitorIp());
    }

    /**
     * Test of getVisitorLatitude method, of class PiwikRequest.
     */
    @Test
    public void testVisitorLatitudeT(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorLatitude(10.5);
        assertEquals(new Double(10.5), request.getVisitorLatitude());
    }
    @Test
    public void testVisitorLatitudeTE(){
        try{
            request.setVisitorLatitude(10.5);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("AuthToken must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorLatitudeF(){
        request.setVisitorLatitude(null);
        assertNull(request.getVisitorLatitude());
    }

    /**
     * Test of getVisitorLongitude method, of class PiwikRequest.
     */
    @Test
    public void testVisitorLongitudeT(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorLongitude(20.5);
        assertEquals(new Double(20.5), request.getVisitorLongitude());
    }
    @Test
    public void testVisitorLongitudeTE(){
        try{
            request.setVisitorLongitude(20.5);
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("AuthToken must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorLongitudeF(){
        request.setVisitorLongitude(null);
        assertNull(request.getVisitorLongitude());
    }

    /**
     * Test of getVisitorPreviousVisitTimestamp method, of class PiwikRequest.
     */
    @Test
    public void testVisitorPreviousVisitTimestamp(){
        request.setVisitorPreviousVisitTimestamp(1000L);
        assertEquals(new Long(1000L), request.getVisitorPreviousVisitTimestamp());
    }

    /**
     * Test of getVisitorRegion method, of class PiwikRequest.
     */
    @Test
    public void testVisitorRegionT(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorRegion("region");
        
        assertEquals("region", request.getVisitorRegion());
    }
    @Test
    public void testGetVisitorRegionTE(){
        try{
            request.setVisitorRegion("region");
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("AuthToken must be set before this value can be set.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorRegionF(){
        request.setVisitorRegion(null);
        
        assertNull(request.getVisitorRegion());
    }

    /**
     * Test of getVisitorVisitCount method, of class PiwikRequest.
     */
    @Test
    public void testVisitorVisitCount(){
        request.setVisitorVisitCount(100);
        assertEquals(new Integer(100), request.getVisitorVisitCount());
    }

    /**
     * Test of getQueryString method, of class PiwikRequest.
     */
    @Test
    public void testGetQueryString(){
        request.setRandomValue("random");
        request.setVisitorId("1234567890123456");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com", request.getQueryString());
        request.setPageCustomVariable("key", "val");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&cvar={\"1\":[\"key\",\"val\"]}&_id=1234567890123456&url=http://test.com",
                request.getQueryString());
        request.setPageCustomVariable("key", null);
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com", request.getQueryString());
        request.addCustomTrackingParameter("key", "test");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com&key=test", request.getQueryString());
        request.addCustomTrackingParameter("key", "test2");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com&key=test&key=test2", request.getQueryString());
        request.setCustomTrackingParameter("key2", "test3");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com&key2=test3&key=test&key=test2", request.getQueryString());
        request.setCustomTrackingParameter("key", "test4");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com&key2=test3&key=test4", request.getQueryString());
        request.setRandomValue(null);
        request.setSiteId(null);
        request.setRequired(null);
        request.setApiVersion(null);
        request.setResponseAsImage(null);
        request.setVisitorId(null);
        request.setActionUrl(null);
        assertEquals("key2=test3&key=test4", request.getQueryString());
        request.clearCustomTrackingParameter();
        assertEquals("", request.getQueryString());
    }
    @Test
    public void testGetQueryString2(){
        request.setActionUrlWithString("http://test.com");
        request.setRandomValue("random");
        request.setVisitorId("1234567890123456");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http://test.com", request.getQueryString());
    }

    /**
     * Test of getUrlEncodedQueryString method, of class PiwikRequest.
     */
    @Test
    public void testGetUrlEncodedQueryString(){
        request.setRandomValue("random");
        request.setVisitorId("1234567890123456");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http%3A%2F%2Ftest.com", request.getUrlEncodedQueryString());
        request.addCustomTrackingParameter("ke/y", "te:st");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http%3A%2F%2Ftest.com&ke%2Fy=te%3Ast", request.getUrlEncodedQueryString());
        request.addCustomTrackingParameter("ke/y", "te:st2");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http%3A%2F%2Ftest.com&ke%2Fy=te%3Ast&ke%2Fy=te%3Ast2", request.getUrlEncodedQueryString());
        request.setCustomTrackingParameter("ke/y2", "te:st3");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http%3A%2F%2Ftest.com&ke%2Fy=te%3Ast&ke%2Fy=te%3Ast2&ke%2Fy2=te%3Ast3", request.getUrlEncodedQueryString());
        request.setCustomTrackingParameter("ke/y", "te:st4");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http%3A%2F%2Ftest.com&ke%2Fy=te%3Ast4&ke%2Fy2=te%3Ast3", request.getUrlEncodedQueryString());
        request.setRandomValue(null);
        request.setSiteId(null);
        request.setRequired(null);
        request.setApiVersion(null);
        request.setResponseAsImage(null);
        request.setVisitorId(null);
        request.setActionUrl(null);
        assertEquals("ke%2Fy=te%3Ast4&ke%2Fy2=te%3Ast3", request.getUrlEncodedQueryString());
        request.clearCustomTrackingParameter();
        assertEquals("", request.getUrlEncodedQueryString());
    }
    @Test
    public void testGetUrlEncodedQueryString2(){
        request.setActionUrlWithString("http://test.com");
        request.setRandomValue("random");
        request.setVisitorId("1234567890123456");
        assertEquals("rand=random&idsite=3&rec=1&apiv=1&send_image=0&_id=1234567890123456&url=http%3A%2F%2Ftest.com", request.getUrlEncodedQueryString());
    }

    /**
     * Test of getRandomHexString method, of class PiwikRequest.
     */
    @Test
    public void testGetRandomHexString(){
        String s = PiwikRequest.getRandomHexString(10);
        
        assertEquals(10, s.length());
        Long.parseLong(s, 16);
    }
}
