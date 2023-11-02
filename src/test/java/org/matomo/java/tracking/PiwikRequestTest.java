package org.matomo.java.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.parameters.AcceptLanguage;
import org.matomo.java.tracking.parameters.DeviceResolution;
import org.matomo.java.tracking.parameters.RandomValue;
import org.matomo.java.tracking.parameters.VisitorId;
import org.piwik.java.tracking.PiwikDate;
import org.piwik.java.tracking.PiwikLocale;
import org.piwik.java.tracking.PiwikRequest;

import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.within;

class PiwikRequestTest {

  private PiwikRequest request;

  @BeforeEach
  void setUp() throws Exception {
    request = new PiwikRequest(3, new URL("http://test.com"));
  }

  @Test
  void testConstructor() throws Exception {
    request = new PiwikRequest(3, new URL("http://test.com"));
    assertThat(request.getSiteId()).isEqualTo(Integer.valueOf(3));
    assertThat(request.getRequired()).isTrue();
    assertThat(request.getActionUrl()).isEqualTo("http://test.com");
    assertThat(request.getVisitorId()).isNotNull();
    assertThat(request.getRandomValue()).isNotNull();
    assertThat(request.getApiVersion()).isEqualTo("1");
    assertThat(request.getResponseAsImage()).isFalse();
  }

  /**
   * Test of getActionName method, of class PiwikRequest.
   */
  @Test
  void testActionName() {
    request.setActionName("action");
    assertThat(request.getActionName()).isEqualTo("action");
    request.setActionName(null);
    assertThat(request.getActionName()).isNull();
  }

  /**
   * Test of getActionUrl method, of class PiwikRequest.
   */
  @Test
  void testActionUrl() {
    request.setActionUrl(null);
    assertThat(request.getActionUrl()).isNull();
    request.setActionUrl("http://action.com");
    assertThat(request.getActionUrl()).isEqualTo("http://action.com");
  }

  /**
   * Test of getApiVersion method, of class PiwikRequest.
   */
  @Test
  void testApiVersion() {
    request.setApiVersion("2");
    assertThat(request.getApiVersion()).isEqualTo("2");
  }

  @Test
  void testAuthTokenTF() {
    request.setAuthToken("12345678901234567890123456789012");
    assertThat(request.getAuthToken()).isEqualTo("12345678901234567890123456789012");
  }

  @Test
  void testAuthTokenF() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setAuthToken(null);
    assertThat(request.getAuthToken()).isNull();
  }

  /**
   * Test of getCampaignKeyword method, of class PiwikRequest.
   */
  @Test
  void testCampaignKeyword() {
    request.setCampaignKeyword("keyword");
    assertThat(request.getCampaignKeyword()).isEqualTo("keyword");
  }

  /**
   * Test of getCampaignName method, of class PiwikRequest.
   */
  @Test
  void testCampaignName() {
    request.setCampaignName("name");
    assertThat(request.getCampaignName()).isEqualTo("name");
  }

  /**
   * Test of getCharacterSet method, of class PiwikRequest.
   */
  @Test
  void testCharacterSet() {
    Charset charset = Charset.defaultCharset();
    request.setCharacterSet(charset);
    assertThat(request.getCharacterSet()).isEqualTo(charset);
  }

  /**
   * Test of getContentInteraction method, of class PiwikRequest.
   */
  @Test
  void testContentInteraction() {
    request.setContentInteraction("interaction");
    assertThat(request.getContentInteraction()).isEqualTo("interaction");
  }

  /**
   * Test of getContentName method, of class PiwikRequest.
   */
  @Test
  void testContentName() {
    request.setContentName("name");
    assertThat(request.getContentName()).isEqualTo("name");
  }

  /**
   * Test of getContentPiece method, of class PiwikRequest.
   */
  @Test
  void testContentPiece() {
    request.setContentPiece("piece");
    assertThat(request.getContentPiece()).isEqualTo("piece");
  }

  /**
   * Test of getContentTarget method, of class PiwikRequest.
   */
  @Test
  void testContentTarget() {
    request.setContentTarget("http://target.com");
    assertThat(request.getContentTarget()).isEqualTo("http://target.com");
  }

  /**
   * Test of getCurrentHour method, of class PiwikRequest.
   */
  @Test
  void testCurrentHour() {
    request.setCurrentHour(1);
    assertThat(request.getCurrentHour()).isEqualTo(Integer.valueOf(1));
  }

  /**
   * Test of getCurrentMinute method, of class PiwikRequest.
   */
  @Test
  void testCurrentMinute() {
    request.setCurrentMinute(2);
    assertThat(request.getCurrentMinute()).isEqualTo(Integer.valueOf(2));
  }

  /**
   * Test of getCurrentSecond method, of class PiwikRequest.
   */
  @Test
  void testCurrentSecond() {
    request.setCurrentSecond(3);
    assertThat(request.getCurrentSecond()).isEqualTo(Integer.valueOf(3));
  }

  /**
   * Test of getCustomTrackingParameter method, of class PiwikRequest.
   */
  @Test
  void testGetCustomTrackingParameter_T() {
    try {
      request.getCustomTrackingParameter(null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  void testGetCustomTrackingParameter_FT() {
    assertThat(request.getCustomTrackingParameter("key")).isEmpty();
  }

  @Test
  void testSetCustomTrackingParameter_T() {
    try {
      request.setCustomTrackingParameter(null, null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  void testSetCustomTrackingParameter1() {
    request.setCustomTrackingParameter("key", "value");
    List<Object> l = request.getCustomTrackingParameter("key");
    assertThat(l).hasSize(1);
    assertThat(l.get(0)).isEqualTo("value");
    request.setCustomTrackingParameter("key", "value2");
  }

  @Test
  void testSetCustomTrackingParameter2() {
    request.setCustomTrackingParameter("key", "value2");
    List<Object> l = request.getCustomTrackingParameter("key");
    assertThat(l).hasSize(1);
    assertThat(l.get(0)).isEqualTo("value2");
    request.setCustomTrackingParameter("key", null);
    l = request.getCustomTrackingParameter("key");
    assertThat(l).isEmpty();
  }

  @Test
  void testSetCustomTrackingParameter3() {
    request.setCustomTrackingParameter("key", null);
    List<Object> l = request.getCustomTrackingParameter("key");
    assertThat(l).isEmpty();
  }

  @Test
  void testAddCustomTrackingParameter_T() {
    try {
      request.addCustomTrackingParameter(null, null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  void testAddCustomTrackingParameter_FT() {
    try {
      request.addCustomTrackingParameter("key", null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("value is marked non-null but is null");
    }
  }

  @Test
  void testAddCustomTrackingParameter1() {
    request.addCustomTrackingParameter("key", "value");
    List<Object> l = request.getCustomTrackingParameter("key");
    assertThat(l).hasSize(1);
    assertThat(l.get(0)).isEqualTo("value");
  }

  @Test
  void testAddCustomTrackingParameter2() {
    request.addCustomTrackingParameter("key", "value");
    request.addCustomTrackingParameter("key", "value2");
    List<Object> l = request.getCustomTrackingParameter("key");
    assertThat(l).hasSize(2)
      .contains(new String[] {"value"})
      .contains(new String[] {"value2"});
  }

  @Test
  void testClearCustomTrackingParameter() {
    request.setCustomTrackingParameter("key", "value");
    request.clearCustomTrackingParameter();
    List<Object> l = request.getCustomTrackingParameter("key");
    assertThat(l).isEmpty();
  }

  /**
   * Test of getDeviceResolution method, of class PiwikRequest.
   */
  @Test
  void testDeviceResolution() {
    request.setDeviceResolution(DeviceResolution.fromString("100x200"));
    assertThat(request.getDeviceResolution()).hasToString("100x200");
  }

  /**
   * Test of getDownloadUrl method, of class PiwikRequest.
   */
  @Test
  void testDownloadUrl() {

    request.setDownloadUrl("http://download.com");
    assertThat(request.getDownloadUrl()).isEqualTo("http://download.com");
  }

  /**
   * Test of enableEcommerce method, of class PiwikRequest.
   */
  @Test
  void testEnableEcommerce() {
    request.enableEcommerce();
    assertThat(request.getGoalId()).isEqualTo(Integer.valueOf(0));
  }

  /**
   * Test of getEcommerceDiscount method, of class PiwikRequest.
   */
  @Test
  void testEcommerceDiscountT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceDiscount(1.0);
    assertThat(request.getEcommerceDiscount()).isEqualTo(Double.valueOf(1.0));
  }


  @Test
  void testEcommerceDiscountF() {
    request.setEcommerceDiscount(null);
    assertThat(request.getEcommerceDiscount()).isNull();
  }

  /**
   * Test of getEcommerceId method, of class PiwikRequest.
   */
  @Test
  void testEcommerceIdT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    assertThat(request.getEcommerceId()).isEqualTo("1");
  }

  @Test
  void testEcommerceIdF() {
    request.setEcommerceId(null);
    assertThat(request.getEcommerceId()).isNull();
  }

  @Test
  void testEcommerceItemE2() {
    try {
      request.enableEcommerce();
      request.setEcommerceId("1");
      request.setEcommerceRevenue(2.0);
      request.addEcommerceItem(null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("item is marked non-null but is null");
    }
  }

  @Test
  void testEcommerceItem() {
    assertThat(request.getEcommerceItem(0)).isNull();
    EcommerceItem item = new EcommerceItem("sku", "name", "category", 1.0, 2);
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.addEcommerceItem(item);
    assertThat(request.getEcommerceItem(0)).isEqualTo(item);
    request.clearEcommerceItems();
    assertThat(request.getEcommerceItem(0)).isNull();
  }

  /**
   * Test of getEcommerceLastOrderTimestamp method, of class PiwikRequest.
   */
  @Test
  void testEcommerceLastOrderTimestampT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceLastOrderTimestamp(Instant.ofEpochSecond(1000L));
    assertThat(request.getEcommerceLastOrderTimestamp()).isEqualTo("1970-01-01T00:16:40Z");
  }

  @Test
  void testEcommerceLastOrderTimestampF() {
    request.setEcommerceLastOrderTimestamp(null);
    assertThat(request.getEcommerceLastOrderTimestamp()).isNull();
  }

  /**
   * Test of getEcommerceRevenue method, of class PiwikRequest.
   */
  @Test
  void testEcommerceRevenueT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(20.0);
    assertThat(request.getEcommerceRevenue()).isEqualTo(Double.valueOf(20.0));
  }


  @Test
  void testEcommerceRevenueF() {
    request.setEcommerceRevenue(null);
    assertThat(request.getEcommerceRevenue()).isNull();
  }

  /**
   * Test of getEcommerceShippingCost method, of class PiwikRequest.
   */
  @Test
  void testEcommerceShippingCostT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceShippingCost(20.0);
    assertThat(request.getEcommerceShippingCost()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  void testEcommerceShippingCostF() {
    request.setEcommerceShippingCost(null);
    assertThat(request.getEcommerceShippingCost()).isNull();
  }

  /**
   * Test of getEcommerceSubtotal method, of class PiwikRequest.
   */
  @Test
  void testEcommerceSubtotalT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceSubtotal(20.0);
    assertThat(request.getEcommerceSubtotal()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  void testEcommerceSubtotalF() {
    request.setEcommerceSubtotal(null);
    assertThat(request.getEcommerceSubtotal()).isNull();
  }

  /**
   * Test of getEcommerceTax method, of class PiwikRequest.
   */
  @Test
  void testEcommerceTaxT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceTax(20.0);
    assertThat(request.getEcommerceTax()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  void testEcommerceTaxF() {
    request.setEcommerceTax(null);
    assertThat(request.getEcommerceTax()).isNull();
  }

  /**
   * Test of getEventAction method, of class PiwikRequest.
   */
  @Test
  void testEventAction() {
    request.setEventAction("action");
    assertThat(request.getEventAction()).isEqualTo("action");
    request.setEventAction(null);
    assertThat(request.getEventAction()).isNull();
  }

  /**
   * Test of getEventCategory method, of class PiwikRequest.
   */
  @Test
  void testEventCategory() {
    request.setEventCategory("category");
    assertThat(request.getEventCategory()).isEqualTo("category");
  }

  /**
   * Test of getEventName method, of class PiwikRequest.
   */
  @Test
  void testEventName() {
    request.setEventName("name");
    assertThat(request.getEventName()).isEqualTo("name");
  }

  /**
   * Test of getEventValue method, of class PiwikRequest.
   */
  @Test
  void testEventValue() {
    request.setEventValue(1.0);
    assertThat(request.getEventValue()).isOne();
  }

  /**
   * Test of getGoalId method, of class PiwikRequest.
   */
  @Test
  void testGoalId() {
    request.setGoalId(1);
    assertThat(request.getGoalId()).isEqualTo(Integer.valueOf(1));
  }

  /**
   * Test of getHeaderAcceptLanguage method, of class PiwikRequest.
   */
  @Test
  void testHeaderAcceptLanguage() {
    request.setHeaderAcceptLanguage(AcceptLanguage.fromHeader("en"));
    assertThat(request.getHeaderAcceptLanguage()).hasToString("en");
  }

  /**
   * Test of getHeaderUserAgent method, of class PiwikRequest.
   */
  @Test
  void testHeaderUserAgent() {
    request.setHeaderUserAgent("agent");
    assertThat(request.getHeaderUserAgent()).isEqualTo("agent");
  }

  /**
   * Test of getNewVisit method, of class PiwikRequest.
   */
  @Test
  void testNewVisit() {
    request.setNewVisit(true);
    assertThat(request.getNewVisit()).isTrue();
    request.setNewVisit(null);
    assertThat(request.getNewVisit()).isNull();
  }

  /**
   * Test of getOutlinkUrl method, of class PiwikRequest.
   */
  @Test
  void testOutlinkUrl() {
    request.setOutlinkUrl("http://outlink.com");
    assertThat(request.getOutlinkUrl()).isEqualTo("http://outlink.com");
  }

  /**
   * Test of getPageCustomVariable method, of class PiwikRequest.
   */
  @Test
  void testPageCustomVariableStringStringE() {
    assertThatThrownBy(() -> request.setPageCustomVariable(null, null)).isInstanceOf(
      IllegalArgumentException.class);
  }

  @Test
  void testPageCustomVariableStringStringE2() {
    assertThatThrownBy(() -> request.setPageCustomVariable(null, "pageVal")).isInstanceOf(
      IllegalArgumentException.class);
  }

  @Test
  void testPageCustomVariableCustomVariable() {
    assertThat(request.getPageCustomVariable(1)).isNull();
    CustomVariable cv = new CustomVariable("pageKey", "pageVal");
    request.setPageCustomVariable(cv, 1);
    assertThat(request.getPageCustomVariable(1)).isEqualTo(cv);
    request.setPageCustomVariable(null, 1);
    assertThat(request.getPageCustomVariable(1)).isNull();
    request.setPageCustomVariable(cv, 2);
    assertThat(request.getPageCustomVariable(2)).isEqualTo(cv);
  }

  /**
   * Test of getPluginDirector method, of class PiwikRequest.
   */
  @Test
  void testPluginDirector() {
    request.setPluginDirector(true);
    assertThat(request.getPluginDirector()).isTrue();
  }

  /**
   * Test of getPluginFlash method, of class PiwikRequest.
   */
  @Test
  void testPluginFlash() {
    request.setPluginFlash(true);
    assertThat(request.getPluginFlash()).isTrue();
  }

  /**
   * Test of getPluginGears method, of class PiwikRequest.
   */
  @Test
  void testPluginGears() {
    request.setPluginGears(true);
    assertThat(request.getPluginGears()).isTrue();
  }

  /**
   * Test of getPluginJava method, of class PiwikRequest.
   */
  @Test
  void testPluginJava() {
    request.setPluginJava(true);
    assertThat(request.getPluginJava()).isTrue();
  }

  /**
   * Test of getPluginPDF method, of class PiwikRequest.
   */
  @Test
  void testPluginPDF() {
    request.setPluginPDF(true);
    assertThat(request.getPluginPDF()).isTrue();
  }

  /**
   * Test of getPluginQuicktime method, of class PiwikRequest.
   */
  @Test
  void testPluginQuicktime() {
    request.setPluginQuicktime(true);
    assertThat(request.getPluginQuicktime()).isTrue();
  }

  /**
   * Test of getPluginRealPlayer method, of class PiwikRequest.
   */
  @Test
  void testPluginRealPlayer() {
    request.setPluginRealPlayer(true);
    assertThat(request.getPluginRealPlayer()).isTrue();
  }

  /**
   * Test of getPluginSilverlight method, of class PiwikRequest.
   */
  @Test
  void testPluginSilverlight() {
    request.setPluginSilverlight(true);
    assertThat(request.getPluginSilverlight()).isTrue();
  }

  /**
   * Test of getPluginWindowsMedia method, of class PiwikRequest.
   */
  @Test
  void testPluginWindowsMedia() {
    request.setPluginWindowsMedia(true);
    assertThat(request.getPluginWindowsMedia()).isTrue();
  }

  /**
   * Test of getRandomValue method, of class PiwikRequest.
   */
  @Test
  void testRandomValue() {
    request.setRandomValue(RandomValue.fromString("value"));
    assertThat(request.getRandomValue()).hasToString("value");
  }

  /**
   * Test of setReferrerUrl method, of class PiwikRequest.
   */
  @Test
  void testReferrerUrl() {
    request.setReferrerUrl("http://referrer.com");
    assertThat(request.getReferrerUrl()).isEqualTo("http://referrer.com");
  }

  /**
   * Test of getRequestDatetime method, of class PiwikRequest.
   */
  @Test
  void testRequestDatetimeTTT() {
    request.setAuthToken("12345678901234567890123456789012");
    PiwikDate date = new PiwikDate(1000L);
    request.setRequestDatetime(date);
    assertThat(request.getRequestDatetime().getTime()).isEqualTo(1000L);
  }


  @Test
  void testRequestDatetimeTF() {
    request.setRequestDatetime(new PiwikDate());
    assertThat(request.getRequestDatetime().getZonedDateTime()).isCloseTo(ZonedDateTime.now(), within(2, MINUTES));
  }

  @Test
  void testRequestDatetimeF() {
    PiwikDate date = new PiwikDate();
    request.setRequestDatetime(date);
    request.setRequestDatetime(null);
    assertThat(request.getRequestDatetime()).isNull();
  }

  /**
   * Test of getRequired method, of class PiwikRequest.
   */
  @Test
  void testRequired() {
    request.setRequired(false);
    assertThat(request.getRequired()).isFalse();
  }

  /**
   * Test of getResponseAsImage method, of class PiwikRequest.
   */
  @Test
  void testResponseAsImage() {
    request.setResponseAsImage(true);
    assertThat(request.getResponseAsImage()).isTrue();
  }

  @Test
  void testSearchCategoryTF() {
    request.setSearchQuery("query");
    request.setSearchCategory("category");
    assertThat(request.getSearchCategory()).isEqualTo("category");
  }

  @Test
  void testSearchCategoryF() {
    request.setSearchCategory(null);
    assertThat(request.getSearchCategory()).isNull();
  }

  /**
   * Test of getSearchQuery method, of class PiwikRequest.
   */
  @Test
  void testSearchQuery() {
    request.setSearchQuery("query");
    assertThat(request.getSearchQuery()).isEqualTo("query");
  }

  @Test
  void testSearchResultsCountTF() {
    request.setSearchQuery("query");
    request.setSearchResultsCount(100L);
    assertThat(request.getSearchResultsCount()).isEqualTo(Long.valueOf(100L));
  }

  @Test
  void testSearchResultsCountF() {
    request.setSearchResultsCount(null);
    assertThat(request.getSearchResultsCount()).isNull();
  }

  /**
   * Test of getSiteId method, of class PiwikRequest.
   */
  @Test
  void testSiteId() {
    request.setSiteId(2);
    assertThat(request.getSiteId()).isEqualTo(Integer.valueOf(2));
  }

  /**
   * Test of setTrackBotRequest method, of class PiwikRequest.
   */
  @Test
  void testTrackBotRequests() {
    request.setTrackBotRequests(true);
    assertThat(request.getTrackBotRequests()).isTrue();
  }

  /**
   * Test of getUserCustomVariable method, of class PiwikRequest.
   */
  @Test
  void testUserCustomVariableStringString() {
    request.setUserCustomVariable("userKey", "userValue");
    assertThat(request.getUserCustomVariable("userKey")).isEqualTo("userValue");
  }


  /**
   * Test of getUserId method, of class PiwikRequest.
   */
  @Test
  void testUserId() {
    request.setUserId("id");
    assertThat(request.getUserId()).isEqualTo("id");
  }

  /**
   * Test of getVisitorCity method, of class PiwikRequest.
   */
  @Test
  void testVisitorCityT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorCity("city");
    assertThat(request.getVisitorCity()).isEqualTo("city");
  }

  @Test
  void testVisitorCityF() {
    request.setVisitorCity(null);
    assertThat(request.getVisitorCity()).isNull();
  }

  /**
   * Test of getVisitorCountry method, of class PiwikRequest.
   */
  @Test
  void testVisitorCountryT() {
    PiwikLocale country = new PiwikLocale(Locale.US);
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorCountry(country);
    assertThat(request.getVisitorCountry()).isEqualTo(country);
  }

  @Test
  void testVisitorCountryF() {
    request.setVisitorCountry(null);
    assertThat(request.getVisitorCountry()).isNull();
  }

  @Test
  void testVisitorCustomTF() {
    request.setVisitorCustomId(VisitorId.fromHex("1234567890abcdef"));
    assertThat(request.getVisitorCustomId()).hasToString("1234567890abcdef");
  }

  @Test
  void testVisitorCustomIdF() {
    request.setVisitorCustomId(VisitorId.fromHex("1234567890abcdef"));
    request.setVisitorCustomId(null);
    assertThat(request.getVisitorCustomId()).isNull();
  }

  /**
   * Test of getVisitorFirstVisitTimestamp method, of class PiwikRequest.
   */
  @Test
  void testVisitorFirstVisitTimestamp() {
    request.setVisitorFirstVisitTimestamp(Instant.parse("2021-03-10T10:22:22.123Z"));
    assertThat(request.getVisitorFirstVisitTimestamp()).isEqualTo("2021-03-10T10:22:22.123Z");
  }

  @Test
  void testVisitorIdTFT() {
    try {
      request.setVisitorId(VisitorId.fromHex("1234567890abcdeg"));
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("Input must be a valid hex string");
    }
  }

  @Test
  void testVisitorIdTFF() {
    request.setVisitorId(VisitorId.fromHex("1234567890abcdef"));
    assertThat(request.getVisitorId()).hasToString("1234567890abcdef");
  }

  @Test
  void testVisitorIdF() {
    request.setVisitorId(VisitorId.fromHex("1234567890abcdef"));
    request.setVisitorId(null);
    assertThat(request.getVisitorId()).isNull();
  }

  /**
   * Test of getVisitorIp method, of class PiwikRequest.
   */
  @Test
  void testVisitorIpT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorIp("ip");
    assertThat(request.getVisitorIp()).isEqualTo("ip");
  }

  @Test
  void testVisitorIpF() {
    request.setVisitorIp(null);
    assertThat(request.getVisitorIp()).isNull();
  }

  /**
   * Test of getVisitorLatitude method, of class PiwikRequest.
   */
  @Test
  void testVisitorLatitudeT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorLatitude(10.5);
    assertThat(request.getVisitorLatitude()).isEqualTo(Double.valueOf(10.5));
  }

  @Test
  void testVisitorLatitudeF() {
    request.setVisitorLatitude(null);
    assertThat(request.getVisitorLatitude()).isNull();
  }

  /**
   * Test of getVisitorLongitude method, of class PiwikRequest.
   */
  @Test
  void testVisitorLongitudeT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorLongitude(20.5);
    assertThat(request.getVisitorLongitude()).isEqualTo(Double.valueOf(20.5));
  }

  @Test
  void testVisitorLongitudeF() {
    request.setVisitorLongitude(null);
    assertThat(request.getVisitorLongitude()).isNull();
  }

  /**
   * Test of getVisitorPreviousVisitTimestamp method, of class PiwikRequest.
   */
  @Test
  void testVisitorPreviousVisitTimestamp() {
    request.setVisitorPreviousVisitTimestamp(Instant.ofEpochSecond(1000L));
    assertThat(request.getVisitorPreviousVisitTimestamp()).isEqualTo("1970-01-01T00:16:40Z");
  }

  /**
   * Test of getVisitorRegion method, of class PiwikRequest.
   */
  @Test
  void testVisitorRegionT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorRegion("region");
    assertThat(request.getVisitorRegion()).isEqualTo("region");
  }

  @Test
  void testVisitorRegionF() {
    request.setVisitorRegion(null);
    assertThat(request.getVisitorRegion()).isNull();
  }

  /**
   * Test of getVisitorVisitCount method, of class PiwikRequest.
   */
  @Test
  void testVisitorVisitCount() {
    request.setVisitorVisitCount(100);
    assertThat(request.getVisitorVisitCount()).isEqualTo(Integer.valueOf(100));
  }

}
