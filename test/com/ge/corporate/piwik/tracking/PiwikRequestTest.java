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

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author brettcsorba
 */
public class PiwikRequestTest{
    PiwikRequest request;
    
    public PiwikRequestTest(){
    }
    
    @BeforeClass
    public static void setUpClass(){
    }
    
    @AfterClass
    public static void tearDownClass(){
    }
    
    @Before
    public void setUp() throws Exception{
        request = new PiwikRequest(3, new URL("http://test.com"));
    }
    
    @After
    public void tearDown(){
    }

    /**
     * Test of getActionName method, of class PiwikRequest.
     */
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
        URL url = new URL("http://action.com");
        request.setActionUrl(url);
        assertEquals(url, request.getActionUrl());
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
    public void testAuthTokenT(){
        try{
            request.setAuthToken(null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("AuthToken cannot be null.", e.getLocalizedMessage());
        }
    }
    @Test
    public void testAuthTokenFT(){
        try{
            request.setAuthToken("1234");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1234 is not 32 characters long.", e.getLocalizedMessage());            
        }
    }
    @Test
    public void testAuthTokenFF(){
        request.setAuthToken("12345678901234567890123456789012");
        assertEquals("12345678901234567890123456789012", request.getAuthToken());
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
    }

    /**
     * Test of enableEcommerce method, of class PiwikRequest.
     */
    @Test
    public void testEnableEcommerce(){
        request.enableEcommerce();
        assertEquals("0", request.getGoalId());
    }

    /**
     * Test of verifyEcommerceEnabled method, of class PiwikRequest.
     */
    @Test
    public void testVerifyEcommerceEnabled(){
        try{
            request.verifyEcommerceEnabled();
            fail("Exception should have been thrown.");
        }
        catch(IllegalStateException e){
            assertEquals("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.",
                    e.getLocalizedMessage());
        }
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
    public void testVerifyEcommerceStateTF(){
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

    /**
     * Test of getEcommerceDiscount method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceDiscountE(){
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
    public void testEcommerceDiscount(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceDiscount(1.0);
        
        assertEquals(new Double(1.0), request.getEcommerceDiscount());
    }

    /**
     * Test of getEcommerceId method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceIdE(){
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
    public void testEcommerceId(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        
        assertEquals("1", request.getEcommerceId());
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
    public void testEcommerceItem(){
        assertNull(request.getEcommerceItem(0));
        
        EcommerceItem item = new EcommerceItem("sku", "name", "category", 1.0, 2);
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.addEcommerceItem(item);
        
        assertEquals(item, request.getEcommerceItem(0));
    }

    /**
     * Test of getEcommerceLastOrderTimestamp method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceLastOrderTimestampE(){
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
    public void testEcommerceLastOrderTimestamp(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceLastOrderTimestamp(1000L);
        
        assertEquals(new Long(1000L), request.getEcommerceLastOrderTimestamp());
    }

    /**
     * Test of getEcommerceRevenue method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceRevenueE(){
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
    public void testEcommerceRevenue(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceRevenue());
    }

    /**
     * Test of getEcommerceShippingCost method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceShippingCostE(){
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
    public void testEcommerceShippingCost(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceShippingCost(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceShippingCost());
    }

    /**
     * Test of getEcommerceSubtotal method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceSubtotalE(){
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
    public void testEcommerceSubtotal(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceSubtotal(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceSubtotal());
    }

    /**
     * Test of getEcommerceTax method, of class PiwikRequest.
     */
    @Test
    public void testEcommerceTaxE(){
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
    public void testEcommerceTax(){
        request.enableEcommerce();
        request.setEcommerceId("1");
        request.setEcommerceRevenue(2.0);
        request.setEcommerceTax(20.0);
        
        assertEquals(new Double(20.0), request.getEcommerceTax());
    }

    /**
     * Test of getEventAction method, of class PiwikRequest.
     */
    @Test
    public void testEventAction(){
        request.setEventAction("action");
        assertEquals("action", request.getEventAction());
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
        assertEquals(new Integer(1), request.getEventValue());
    }

    /**
     * Test of getGoalId method, of class PiwikRequest.
     */
    @Test
    public void testGoalId(){
        request.setGoalId("id");
        assertEquals("id", request.getGoalId());
    }

    /**
     * Test of getGoalRevenue method, of class PiwikRequest.
     */
    @Test
    public void testGoalRevenueT(){
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
    public void testGoalRevenue(){
        request.setGoalId("id");
        request.setGoalRevenue(20.0);
        
        assertEquals(new Double(20.0), request.getGoalRevenue());
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
    }

    /**
     * Test of getOutlinkUrl method, of class PiwikRequest.
     */
    @Test
    public void testOutlinkUrl() throws Exception{
        URL url = new URL("http://outlink.com");
        request.setOutlinkUrl(url);
        assertEquals(url, request.getOutlinkUrl());
    }

    /**
     * Test of getPageCustomVariable method, of class PiwikRequest.
     */
    @Test
    public void testPageCustomVariable(){
        assertNull(request.getPageCustomVariable("pageKey"));
        request.setPageCustomVariable("pageKey", "pageVal");
        assertEquals("pageVal", request.getPageCustomVariable("pageKey"));
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
    }

    /**
     * Test of getRequestDatetime method, of class PiwikRequest.
     */
    @Test
    public void testRequestDatetimeT(){
        try{
            request.setRequestDatetime(null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("Datetime cannot be null.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testRequestDatetimeFT(){
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
    public void testRequestDatetimeFF(){
        request.setAuthToken("12345678901234567890123456789012");
        PiwikDate date = new PiwikDate(1000L);
        request.setRequestDatetime(date);
        
        assertEquals(date, request.getRequestDatetime());
    }
    @Test
    public void testRequestDatetime(){
        PiwikDate date = new PiwikDate();
        request.setRequestDatetime(date);
        assertEquals(date, request.getRequestDatetime());
    }

    /**
     * Test of getRequired method, of class PiwikRequest.
     */
    @Test
    public void testRequired(){
        request.setRequired(true);
        assertEquals(true, request.getRequired());
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
    public void testSearchCategoryT(){
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
    public void testSearchCategory(){
        request.setSearchQuery("query");
        request.setSearchCategory("category");
        assertEquals("category", request.getSearchCategory());
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
    public void testSearchResultsCountT(){
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
    public void testSearchResultsCount(){
        request.setSearchQuery("query");
        request.setSearchResultsCount(100L);
        assertEquals(new Long(100L), request.getSearchResultsCount());
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
     * Test of getUserCustomVariable method, of class PiwikRequest.
     */
    @Test
    public void testUserCustomVariable(){
        request.setUserCustomVariable("userKey", "userValue");
        assertEquals("userValue", request.getUserCustomVariable("userKey"));
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
    public void testVisitorCityE(){
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
    public void testVisitorCity(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorCity("city");
        assertEquals("city", request.getVisitorCity());
    }

    /**
     * Test of getVisitorCountry method, of class PiwikRequest.
     */
    @Test
    public void testVisitorCountryE(){
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
    public void testVisitorCountry(){
        PiwikLocale country = new PiwikLocale(Locale.US);
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorCountry(country);
        
        assertEquals(country, request.getVisitorCountry());
    }

    /**
     * Test of getVisitorCustomId method, of class PiwikRequest.
     */
    @Test
    public void testVisitorCustomIdT(){
        try{
            request.setVisitorCustomId(null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("VisitorCustomId cannot be null.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorCustomFT(){
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
    public void testVisitorCustomFFT(){
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
    public void testVisitorCustomIdFFF(){
        request.setVisitorCustomId("1234567890abcdef");
        assertEquals("1234567890abcdef", request.getVisitorCustomId());
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
    public void testVisitorIdT(){
        try{
            request.setVisitorId(null);
            fail("Exception should have been thrown.");
        }
        catch(NullPointerException e){
            assertEquals("VisitorId cannot be null.",
                    e.getLocalizedMessage());
        }
    }
    @Test
    public void testVisitorIdFT(){
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
    public void testVisitorIdFFT(){
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
    public void testVisitorIdFFF(){
        request.setVisitorId("1234567890abcdef");
        assertEquals("1234567890abcdef", request.getVisitorId());
    }

    /**
     * Test of getVisitorIp method, of class PiwikRequest.
     */
    @Test
    public void testVisitorIpE(){
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
    public void testVisitorIp(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorIp("ip");
        assertEquals("ip", request.getVisitorIp());
    }

    /**
     * Test of getVisitorLatitude method, of class PiwikRequest.
     */
    @Test
    public void testVisitorLatitudeE(){
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
    public void testVisitorLatitude(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorLatitude(10.5);
        assertEquals(new Double(10.5), request.getVisitorLatitude());
    }

    /**
     * Test of getVisitorLongitude method, of class PiwikRequest.
     */
    @Test
    public void testVisitorLongitudeE(){
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
    public void testVisitorLongitude(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorLongitude(20.5);
        assertEquals(new Double(20.5), request.getVisitorLongitude());
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
    public void testGetVisitorRegionE(){
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
    public void testVisitorRegion(){
        request.setAuthToken("12345678901234567890123456789012");
        request.setVisitorRegion("region");
        
        assertEquals("region", request.getVisitorRegion());
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
    }

    /**
     * Test of getUrlEncodedQueryString method, of class PiwikRequest.
     */
    @Test
    public void testGetUrlEncodedQueryString(){
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
