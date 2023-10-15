package org.matomo.java.tracking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.piwik.java.tracking.PiwikDate;
import org.piwik.java.tracking.PiwikLocale;
import org.piwik.java.tracking.PiwikRequest;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author brettcsorba
 */
@DisplayName("Piwik Request Test")
class PiwikRequestTest {

  private PiwikRequest request;

  @BeforeEach
  void setUp() throws Exception {
    request = new PiwikRequest(3, new URL("http://test.com"));
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  @DisplayName("Test Constructor")
  void testConstructor() throws Exception {
    request = new PiwikRequest(3, new URL("http://test.com"));
    assertThat(request.getSiteId()).isEqualTo(Integer.valueOf(3));
    assertThat(request.getRequired()).isTrue();
    assertThat(request.getActionUrl()).isEqualTo(new URL("http://test.com"));
    assertThat(request.getVisitorId()).isNotNull();
    assertThat(request.getRandomValue()).isNotNull();
    assertThat(request.getApiVersion()).isEqualTo("1");
    assertThat(request.getResponseAsImage()).isFalse();
  }

  /**
   * Test of getActionName method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Action Name")
  void testActionName() {
    request.setActionName("action");
    assertThat(request.getActionName()).isEqualTo("action");
    request.setActionName(null);
    assertThat(request.getActionName()).isNull();
  }

  /**
   * Test of getActionTime method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Action Time")
  void testActionTime() {
    request.setActionTime(1000L);
    assertThat(request.getActionTime()).isEqualTo(Long.valueOf(1000L));
  }

  /**
   * Test of getActionUrl method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Action Url")
  void testActionUrl() throws Exception {
    request.setActionUrl((String) null);
    assertThat(request.getActionUrl()).isNull();
    assertThat(request.getActionUrlAsString()).isNull();
    URL url = new URL("http://action.com");
    request.setActionUrl(url);
    assertThat(request.getActionUrl()).isEqualTo(url);
    assertThat(request.getActionUrlAsString()).isEqualTo("http://action.com");
    request.setActionUrlWithString(null);
    assertThat(request.getActionUrl()).isNull();
    assertThat(request.getActionUrlAsString()).isNull();
    request.setActionUrlWithString("http://actionstring.com");
    assertThat(request.getActionUrlAsString()).isEqualTo("http://actionstring.com");
    assertThat(request.getActionUrl()).isEqualTo(new URL("http://actionstring.com"));
  }

  /**
   * Test of getApiVersion method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Api Version")
  void testApiVersion() {
    request.setApiVersion("2");
    assertThat(request.getApiVersion()).isEqualTo("2");
  }

  /**
   * Test of getAuthToken method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Auth Token TT")
  void testAuthTokenTT() {
    try {
      request.setAuthToken("1234");
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("1234 is not 32 characters long.");
    }
  }

  @Test
  @DisplayName("Test Auth Token TF")
  void testAuthTokenTF() {
    request.setAuthToken("12345678901234567890123456789012");
    assertThat(request.getAuthToken()).isEqualTo("12345678901234567890123456789012");
  }

  @Test
  @DisplayName("Test Auth Token F")
  void testAuthTokenF() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setAuthToken(null);
    assertThat(request.getAuthToken()).isNull();
  }

  /**
   * Test of verifyAuthTokenSet method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Verify Auth Token Set")
  void testVerifyAuthTokenSet() {
    try {
      request.verifyAuthTokenSet();
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("AuthToken must be set before this value can be set.");
    }
  }

  /**
   * Test of getCampaignKeyword method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Campaign Keyword")
  void testCampaignKeyword() {
    request.setCampaignKeyword("keyword");
    assertThat(request.getCampaignKeyword()).isEqualTo("keyword");
  }

  /**
   * Test of getCampaignName method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Campaign Name")
  void testCampaignName() {
    request.setCampaignName("name");
    assertThat(request.getCampaignName()).isEqualTo("name");
  }

  /**
   * Test of getCharacterSet method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Character Set")
  void testCharacterSet() {
    Charset charset = Charset.defaultCharset();
    request.setCharacterSet(charset);
    assertThat(request.getCharacterSet()).isEqualTo(charset);
  }

  /**
   * Test of getContentInteraction method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Content Interaction")
  void testContentInteraction() {
    request.setContentInteraction("interaction");
    assertThat(request.getContentInteraction()).isEqualTo("interaction");
  }

  /**
   * Test of getContentName method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Content Name")
  void testContentName() {
    request.setContentName("name");
    assertThat(request.getContentName()).isEqualTo("name");
  }

  /**
   * Test of getContentPiece method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Content Piece")
  void testContentPiece() {
    request.setContentPiece("piece");
    assertThat(request.getContentPiece()).isEqualTo("piece");
  }

  /**
   * Test of getContentTarget method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Content Target")
  void testContentTarget() throws Exception {
    URL url = new URL("http://target.com");
    request.setContentTarget(url);
    assertThat(request.getContentTarget()).isEqualTo(url);
    assertThat(request.getContentTargetAsString()).isEqualTo("http://target.com");
    request.setContentTargetWithString("http://targetstring.com");
    assertThat(request.getContentTargetAsString()).isEqualTo("http://targetstring.com");
    assertThat(request.getContentTarget()).isEqualTo(new URL("http://targetstring.com"));
  }

  /**
   * Test of getCurrentHour method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Current Hour")
  void testCurrentHour() {
    request.setCurrentHour(1);
    assertThat(request.getCurrentHour()).isEqualTo(Integer.valueOf(1));
  }

  /**
   * Test of getCurrentMinute method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Current Minute")
  void testCurrentMinute() {
    request.setCurrentMinute(2);
    assertThat(request.getCurrentMinute()).isEqualTo(Integer.valueOf(2));
  }

  /**
   * Test of getCurrentSecond method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Current Second")
  void testCurrentSecond() {
    request.setCurrentSecond(3);
    assertThat(request.getCurrentSecond()).isEqualTo(Integer.valueOf(3));
  }

  /**
   * Test of getCustomTrackingParameter method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Get Custom Tracking Parameter _ T")
  void testGetCustomTrackingParameter_T() {
    try {
      request.getCustomTrackingParameter(null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Get Custom Tracking Parameter _ FT")
  void testGetCustomTrackingParameter_FT() {
    assertThat(request.getCustomTrackingParameter("key").isEmpty()).isTrue();
  }

  @Test
  @DisplayName("Test Set Custom Tracking Parameter _ T")
  void testSetCustomTrackingParameter_T() {
    try {
      request.setCustomTrackingParameter(null, null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Set Custom Tracking Parameter _ F")
  void testSetCustomTrackingParameter_F() {
    request.setCustomTrackingParameter("key", "value");
    List l = request.getCustomTrackingParameter("key");
    assertThat(l.size()).isEqualTo(1);
    assertThat(l.get(0)).isEqualTo("value");
    request.setCustomTrackingParameter("key", "value2");
    l = request.getCustomTrackingParameter("key");
    assertThat(l.size()).isEqualTo(1);
    assertThat(l.get(0)).isEqualTo("value2");
    request.setCustomTrackingParameter("key", null);
    l = request.getCustomTrackingParameter("key");
    assertThat(l.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("Test Add Custom Tracking Parameter _ T")
  void testAddCustomTrackingParameter_T() {
    try {
      request.addCustomTrackingParameter(null, null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Add Custom Tracking Parameter _ FT")
  void testAddCustomTrackingParameter_FT() {
    try {
      request.addCustomTrackingParameter("key", null);
      fail("Exception should have been thrown.");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("value is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Add Custom Tracking Parameter _ FF")
  void testAddCustomTrackingParameter_FF() {
    request.addCustomTrackingParameter("key", "value");
    List l = request.getCustomTrackingParameter("key");
    assertThat(l.size()).isEqualTo(1);
    assertThat(l.get(0)).isEqualTo("value");
    request.addCustomTrackingParameter("key", "value2");
    l = request.getCustomTrackingParameter("key");
    assertThat(l.size()).isEqualTo(2);
    assertThat(l.contains("value")).isTrue();
    assertThat(l.contains("value2")).isTrue();
  }

  @Test
  @DisplayName("Test Clear Custom Tracking Parameter")
  void testClearCustomTrackingParameter() {
    request.setCustomTrackingParameter("key", "value");
    request.clearCustomTrackingParameter();
    List l = request.getCustomTrackingParameter("key");
    assertThat(l.isEmpty()).isTrue();
  }

  /**
   * Test of getDeviceResolution method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Device Resolution")
  void testDeviceResolution() {
    request.setDeviceResolution("1x2");
    assertThat(request.getDeviceResolution()).isEqualTo("1x2");
  }

  /**
   * Test of getDownloadUrl method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Download Url")
  void testDownloadUrl() throws Exception {
    URL url = new URL("http://download.com");
    request.setDownloadUrl(url);
    assertThat(request.getDownloadUrl()).isEqualTo(url);
    assertThat(request.getDownloadUrlAsString()).isEqualTo("http://download.com");
    request.setDownloadUrlWithString("http://downloadstring.com");
    assertThat(request.getDownloadUrlAsString()).isEqualTo("http://downloadstring.com");
    assertThat(request.getDownloadUrl()).isEqualTo(new URL("http://downloadstring.com"));
  }

  /**
   * Test of enableEcommerce method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Enable Ecommerce")
  void testEnableEcommerce() {
    request.enableEcommerce();
    assertThat(request.getGoalId()).isEqualTo(Integer.valueOf(0));
  }

  /**
   * Test of verifyEcommerceEnabled method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Verify Ecommerce Enabled T")
  void testVerifyEcommerceEnabledT() {
    try {
      request.verifyEcommerceEnabled();
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Verify Ecommerce Enabled FT")
  void testVerifyEcommerceEnabledFT() {
    try {
      request.setGoalId(1);
      request.verifyEcommerceEnabled();
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Verify Ecommerce Enabled FF")
  void testVerifyEcommerceEnabledFF() {
    request.enableEcommerce();
    request.verifyEcommerceEnabled();
  }

  /**
   * Test of verifyEcommerceState method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Verify Ecommerce State E")
  void testVerifyEcommerceStateE() {
    try {
      request.verifyEcommerceState();
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Verify Ecommerce State T")
  void testVerifyEcommerceStateT() {
    try {
      request.enableEcommerce();
      request.verifyEcommerceState();
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("EcommerceId must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Verify Ecommerce State FT")
  void testVerifyEcommerceStateFT() {
    try {
      request.enableEcommerce();
      request.setEcommerceId("1");
      request.verifyEcommerceState();
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("EcommerceRevenue must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Verify Ecommerce State FF")
  void testVerifyEcommerceStateFF() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.verifyEcommerceState();
  }

  /**
   * Test of getEcommerceDiscount method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Ecommerce Discount T")
  void testEcommerceDiscountT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceDiscount(1.0);
    assertThat(request.getEcommerceDiscount()).isEqualTo(Double.valueOf(1.0));
  }

  @Test
  @DisplayName("Test Ecommerce Discount TE")
  void testEcommerceDiscountTE() {
    try {
      request.setEcommerceDiscount(1.0);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Discount F")
  void testEcommerceDiscountF() {
    request.setEcommerceDiscount(null);
    assertThat(request.getEcommerceDiscount()).isNull();
  }

  /**
   * Test of getEcommerceId method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Ecommerce Id T")
  void testEcommerceIdT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    assertThat(request.getEcommerceId()).isEqualTo("1");
  }

  @Test
  @DisplayName("Test Ecommerce Id TE")
  void testEcommerceIdTE() {
    try {
      request.setEcommerceId("1");
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Id F")
  void testEcommerceIdF() {
    request.setEcommerceId(null);
    assertThat(request.getEcommerceId()).isNull();
  }

  /**
   * Test of getEcommerceItem method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Ecommerce Item E")
  void testEcommerceItemE() {
    try {
      EcommerceItem item = new EcommerceItem("sku", "name", "category", 1.0, 2);
      request.addEcommerceItem(item);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Item E 2")
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
  @DisplayName("Test Ecommerce Item")
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
  @DisplayName("Test Ecommerce Last Order Timestamp T")
  void testEcommerceLastOrderTimestampT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceLastOrderTimestamp(1000L);
    assertThat(request.getEcommerceLastOrderTimestamp()).isEqualTo(Long.valueOf(1000L));
  }

  @Test
  @DisplayName("Test Ecommerce Last Order Timestamp TE")
  void testEcommerceLastOrderTimestampTE() {
    try {
      request.setEcommerceLastOrderTimestamp(1000L);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Last Order Timestamp F")
  void testEcommerceLastOrderTimestampF() {
    request.setEcommerceLastOrderTimestamp(null);
    assertThat(request.getEcommerceLastOrderTimestamp()).isNull();
  }

  /**
   * Test of getEcommerceRevenue method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Ecommerce Revenue T")
  void testEcommerceRevenueT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(20.0);
    assertThat(request.getEcommerceRevenue()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  @DisplayName("Test Ecommerce Revenue TE")
  void testEcommerceRevenueTE() {
    try {
      request.setEcommerceRevenue(20.0);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Revenue F")
  void testEcommerceRevenueF() {
    request.setEcommerceRevenue(null);
    assertThat(request.getEcommerceRevenue()).isNull();
  }

  /**
   * Test of getEcommerceShippingCost method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Ecommerce Shipping Cost T")
  void testEcommerceShippingCostT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceShippingCost(20.0);
    assertThat(request.getEcommerceShippingCost()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  @DisplayName("Test Ecommerce Shipping Cost TE")
  void testEcommerceShippingCostTE() {
    try {
      request.setEcommerceShippingCost(20.0);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Shipping Cost F")
  void testEcommerceShippingCostF() {
    request.setEcommerceShippingCost(null);
    assertThat(request.getEcommerceShippingCost()).isNull();
  }

  /**
   * Test of getEcommerceSubtotal method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Ecommerce Subtotal T")
  void testEcommerceSubtotalT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceSubtotal(20.0);
    assertThat(request.getEcommerceSubtotal()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  @DisplayName("Test Ecommerce Subtotal TE")
  void testEcommerceSubtotalTE() {
    try {
      request.setEcommerceSubtotal(20.0);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Subtotal F")
  void testEcommerceSubtotalF() {
    request.setEcommerceSubtotal(null);
    assertThat(request.getEcommerceSubtotal()).isNull();
  }

  /**
   * Test of getEcommerceTax method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Ecommerce Tax T")
  void testEcommerceTaxT() {
    request.enableEcommerce();
    request.setEcommerceId("1");
    request.setEcommerceRevenue(2.0);
    request.setEcommerceTax(20.0);
    assertThat(request.getEcommerceTax()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  @DisplayName("Test Ecommerce Tax TE")
  void testEcommerceTaxTE() {
    try {
      request.setEcommerceTax(20.0);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  @Test
  @DisplayName("Test Ecommerce Tax F")
  void testEcommerceTaxF() {
    request.setEcommerceTax(null);
    assertThat(request.getEcommerceTax()).isNull();
  }

  /**
   * Test of getEventAction method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Event Action")
  void testEventAction() {
    request.setEventAction("action");
    assertThat(request.getEventAction()).isEqualTo("action");
    request.setEventAction(null);
    assertThat(request.getEventAction()).isNull();
  }

  @Test
  @DisplayName("Test Event Action Exception")
  void testEventActionException() {
    try {
      request.setEventAction("");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("Value cannot be empty.");
    }
  }

  /**
   * Test of getEventCategory method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Event Category")
  void testEventCategory() {
    request.setEventCategory("category");
    assertThat(request.getEventCategory()).isEqualTo("category");
  }

  /**
   * Test of getEventName method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Event Name")
  void testEventName() {
    request.setEventName("name");
    assertThat(request.getEventName()).isEqualTo("name");
  }

  /**
   * Test of getEventValue method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Event Value")
  void testEventValue() {
    request.setEventValue(1);
    assertThat(request.getEventValue()).isEqualTo(1);
  }

  /**
   * Test of getGoalId method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Goal Id")
  void testGoalId() {
    request.setGoalId(1);
    assertThat(request.getGoalId()).isEqualTo(Integer.valueOf(1));
  }

  /**
   * Test of getGoalRevenue method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Goal Revenue TT")
  void testGoalRevenueTT() {
    try {
      request.setGoalRevenue(20.0);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("GoalId must be set before GoalRevenue can be set.");
    }
  }

  @Test
  @DisplayName("Test Goal Revenue TF")
  void testGoalRevenueTF() {
    request.setGoalId(1);
    request.setGoalRevenue(20.0);
    assertThat(request.getGoalRevenue()).isEqualTo(Double.valueOf(20.0));
  }

  @Test
  @DisplayName("Test Goal Revenue F")
  void testGoalRevenueF() {
    request.setGoalRevenue(null);
    assertThat(request.getGoalRevenue()).isNull();
  }

  /**
   * Test of getHeaderAcceptLanguage method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Header Accept Language")
  void testHeaderAcceptLanguage() {
    request.setHeaderAcceptLanguage("language");
    assertThat(request.getHeaderAcceptLanguage()).isEqualTo("language");
  }

  /**
   * Test of getHeaderUserAgent method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Header User Agent")
  void testHeaderUserAgent() {
    request.setHeaderUserAgent("agent");
    assertThat(request.getHeaderUserAgent()).isEqualTo("agent");
  }

  /**
   * Test of getNewVisit method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test New Visit")
  void testNewVisit() {
    request.setNewVisit(true);
    assertThat(request.getNewVisit()).isEqualTo(true);
    request.setNewVisit(null);
    assertThat(request.getNewVisit()).isNull();
  }

  /**
   * Test of getOutlinkUrl method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Outlink Url")
  void testOutlinkUrl() throws Exception {
    URL url = new URL("http://outlink.com");
    request.setOutlinkUrl(url);
    assertThat(request.getOutlinkUrl()).isEqualTo(url);
    assertThat(request.getOutlinkUrlAsString()).isEqualTo("http://outlink.com");
    request.setOutlinkUrlWithString("http://outlinkstring.com");
    assertThat(request.getOutlinkUrlAsString()).isEqualTo("http://outlinkstring.com");
    assertThat(request.getOutlinkUrl()).isEqualTo(new URL("http://outlinkstring.com"));
  }

  /**
   * Test of getPageCustomVariable method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Page Custom Variable String String E")
  void testPageCustomVariableStringStringE() {
    try {
      request.setPageCustomVariable(null, null);
      fail("Exception should have been thrown");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Page Custom Variable String String E 2")
  void testPageCustomVariableStringStringE2() {
    try {
      request.setPageCustomVariable(null, "pageVal");
      fail("Exception should have been thrown");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Page Custom Variable String String E 3")
  void testPageCustomVariableStringStringE3() {
    try {
      request.getPageCustomVariable(null);
      fail("Exception should have been thrown");
    } catch (NullPointerException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("key is marked non-null but is null");
    }
  }

  @Test
  @DisplayName("Test Page Custom Variable String String")
  void testPageCustomVariableStringString() {
    assertThat(request.getPageCustomVariable("pageKey")).isNull();
    request.setPageCustomVariable("pageKey", "pageVal");
    assertThat(request.getPageCustomVariable("pageKey")).isEqualTo("pageVal");
    request.setPageCustomVariable("pageKey", null);
    assertThat(request.getPageCustomVariable("pageKey")).isNull();
    request.setPageCustomVariable("pageKey", "pageVal");
    assertThat(request.getPageCustomVariable("pageKey")).isEqualTo("pageVal");
  }

  @Test
  @DisplayName("Test Page Custom Variable Custom Variable")
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
  @DisplayName("Test Plugin Director")
  void testPluginDirector() {
    request.setPluginDirector(true);
    assertThat(request.getPluginDirector()).isEqualTo(true);
  }

  /**
   * Test of getPluginFlash method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin Flash")
  void testPluginFlash() {
    request.setPluginFlash(true);
    assertThat(request.getPluginFlash()).isEqualTo(true);
  }

  /**
   * Test of getPluginGears method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin Gears")
  void testPluginGears() {
    request.setPluginGears(true);
    assertThat(request.getPluginGears()).isEqualTo(true);
  }

  /**
   * Test of getPluginJava method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin Java")
  void testPluginJava() {
    request.setPluginJava(true);
    assertThat(request.getPluginJava()).isEqualTo(true);
  }

  /**
   * Test of getPluginPDF method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin PDF")
  void testPluginPDF() {
    request.setPluginPDF(true);
    assertThat(request.getPluginPDF()).isEqualTo(true);
  }

  /**
   * Test of getPluginQuicktime method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin Quicktime")
  void testPluginQuicktime() {
    request.setPluginQuicktime(true);
    assertThat(request.getPluginQuicktime()).isEqualTo(true);
  }

  /**
   * Test of getPluginRealPlayer method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin Real Player")
  void testPluginRealPlayer() {
    request.setPluginRealPlayer(true);
    assertThat(request.getPluginRealPlayer()).isEqualTo(true);
  }

  /**
   * Test of getPluginSilverlight method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin Silverlight")
  void testPluginSilverlight() {
    request.setPluginSilverlight(true);
    assertThat(request.getPluginSilverlight()).isEqualTo(true);
  }

  /**
   * Test of getPluginWindowsMedia method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Plugin Windows Media")
  void testPluginWindowsMedia() {
    request.setPluginWindowsMedia(true);
    assertThat(request.getPluginWindowsMedia()).isEqualTo(true);
  }

  /**
   * Test of getRandomValue method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Random Value")
  void testRandomValue() {
    request.setRandomValue("value");
    assertThat(request.getRandomValue()).isEqualTo("value");
  }

  /**
   * Test of setReferrerUrl method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Referrer Url")
  void testReferrerUrl() throws Exception {
    URL url = new URL("http://referrer.com");
    request.setReferrerUrl(url);
    assertThat(request.getReferrerUrl()).isEqualTo(url);
    assertThat(request.getReferrerUrlAsString()).isEqualTo("http://referrer.com");
    request.setReferrerUrlWithString("http://referrerstring.com");
    assertThat(request.getReferrerUrlAsString()).isEqualTo("http://referrerstring.com");
    assertThat(request.getReferrerUrl()).isEqualTo(new URL("http://referrerstring.com"));
  }

  /**
   * Test of getRequestDatetime method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Request Datetime TTT")
  void testRequestDatetimeTTT() {
    request.setAuthToken("12345678901234567890123456789012");
    PiwikDate date = new PiwikDate(1000L);
    request.setRequestDatetime(date);
    assertThat(request.getRequestDatetime()).isEqualTo(date);
  }

  @Test
  @DisplayName("Test Request Datetime TTF")
  void testRequestDatetimeTTF() {
    try {
      PiwikDate date = new PiwikDate(1000L);
      request.setRequestDatetime(date);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("Because you are trying to set RequestDatetime for a time greater than 4 hours ago, AuthToken must be set first.");
    }
  }

  @Test
  @DisplayName("Test Request Datetime TF")
  void testRequestDatetimeTF() {
    PiwikDate date = new PiwikDate();
    request.setRequestDatetime(date);
    assertThat(request.getRequestDatetime()).isEqualTo(date);
  }

  @Test
  @DisplayName("Test Request Datetime F")
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
  @DisplayName("Test Required")
  void testRequired() {
    request.setRequired(false);
    assertThat(request.getRequired()).isEqualTo(false);
  }

  /**
   * Test of getResponseAsImage method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Response As Image")
  void testResponseAsImage() {
    request.setResponseAsImage(true);
    assertThat(request.getResponseAsImage()).isEqualTo(true);
  }

  /**
   * Test of getSearchCategory method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Search Category TT")
  void testSearchCategoryTT() {
    try {
      request.setSearchCategory("category");
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("SearchQuery must be set before SearchCategory can be set.");
    }
  }

  @Test
  @DisplayName("Test Search Category TF")
  void testSearchCategoryTF() {
    request.setSearchQuery("query");
    request.setSearchCategory("category");
    assertThat(request.getSearchCategory()).isEqualTo("category");
  }

  @Test
  @DisplayName("Test Search Category F")
  void testSearchCategoryF() {
    request.setSearchCategory(null);
    assertThat(request.getSearchCategory()).isNull();
  }

  /**
   * Test of getSearchQuery method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Search Query")
  void testSearchQuery() {
    request.setSearchQuery("query");
    assertThat(request.getSearchQuery()).isEqualTo("query");
  }

  /**
   * Test of getSearchResultsCount method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Search Results Count TT")
  void testSearchResultsCountTT() {
    try {
      request.setSearchResultsCount(100L);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("SearchQuery must be set before SearchResultsCount can be set.");
    }
  }

  @Test
  @DisplayName("Test Search Results Count TF")
  void testSearchResultsCountTF() {
    request.setSearchQuery("query");
    request.setSearchResultsCount(100L);
    assertThat(request.getSearchResultsCount()).isEqualTo(Long.valueOf(100L));
  }

  @Test
  @DisplayName("Test Search Results Count F")
  void testSearchResultsCountF() {
    request.setSearchResultsCount(null);
    assertThat(request.getSearchResultsCount()).isNull();
  }

  /**
   * Test of getSiteId method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Site Id")
  void testSiteId() {
    request.setSiteId(2);
    assertThat(request.getSiteId()).isEqualTo(Integer.valueOf(2));
  }

  /**
   * Test of setTrackBotRequest method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Track Bot Requests")
  void testTrackBotRequests() {
    request.setTrackBotRequests(true);
    assertThat(request.getTrackBotRequests()).isEqualTo(true);
  }

  /**
   * Test of getUserrCustomVariable method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test User Custom Variable String String")
  void testUserCustomVariableStringString() {
    request.setUserCustomVariable("userKey", "userValue");
    assertThat(request.getUserCustomVariable("userKey")).isEqualTo("userValue");
  }

  @Test
  @DisplayName("Test Visit Custom Variable Custom Variable")
  void testVisitCustomVariableCustomVariable() {
    request.setRandomValue("random");
    request.setVisitorId("1234567890123456");
    assertThat(request.getVisitCustomVariable(1)).isNull();
    CustomVariable cv = new CustomVariable("visitKey", "visitVal");
    request.setVisitCustomVariable(cv, 1);
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&_cvar={\"1\":[\"visitKey\",\"visitVal\"]}");
    request.setUserCustomVariable("key", "val");
    assertThat(request.getVisitCustomVariable(1)).isEqualTo(cv);
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&_cvar={\"1\":[\"visitKey\",\"visitVal\"],\"2\":[\"key\",\"val\"]}");
    request.setVisitCustomVariable(null, 1);
    assertThat(request.getVisitCustomVariable(1)).isNull();
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&_cvar={\"2\":[\"key\",\"val\"]}");
    request.setVisitCustomVariable(cv, 2);
    assertThat(request.getVisitCustomVariable(2)).isEqualTo(cv);
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&_cvar={\"2\":[\"visitKey\",\"visitVal\"]}");
    request.setUserCustomVariable("visitKey", null);
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456");
  }

  /**
   * Test of getUserId method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test User Id")
  void testUserId() {
    request.setUserId("id");
    assertThat(request.getUserId()).isEqualTo("id");
  }

  /**
   * Test of getVisitorCity method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor City T")
  void testVisitorCityT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorCity("city");
    assertThat(request.getVisitorCity()).isEqualTo("city");
  }

  @Test
  @DisplayName("Test Visitor City TE")
  void testVisitorCityTE() {
    try {
      request.setVisitorCity("city");
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("AuthToken must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Visitor City F")
  void testVisitorCityF() {
    request.setVisitorCity(null);
    assertThat(request.getVisitorCity()).isNull();
  }

  /**
   * Test of getVisitorCountry method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Country T")
  void testVisitorCountryT() {
    PiwikLocale country = new PiwikLocale(Locale.US);
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorCountry(country);
    assertThat(request.getVisitorCountry()).isEqualTo(country);
  }

  @Test
  @DisplayName("Test Visitor Country TE")
  void testVisitorCountryTE() {
    try {
      PiwikLocale country = new PiwikLocale(Locale.US);
      request.setVisitorCountry(country);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("AuthToken must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Visitor Country F")
  void testVisitorCountryF() {
    request.setVisitorCountry(null);
    assertThat(request.getVisitorCountry()).isNull();
  }

  /**
   * Test of getVisitorCustomId method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Custom TT")
  void testVisitorCustomTT() {
    try {
      request.setVisitorCustomId("1");
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("1 is not 16 characters long.");
    }
  }

  @Test
  @DisplayName("Test Visitor Custom TFT")
  void testVisitorCustomTFT() {
    try {
      request.setVisitorCustomId("1234567890abcdeg");
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("1234567890abcdeg is not a hexadecimal string.");
    }
  }

  @Test
  @DisplayName("Test Visitor Custom Id TFF")
  void testVisitorCustomIdTFF() {
    request.setVisitorCustomId("1234567890abcdef");
    assertThat(request.getVisitorCustomId()).isEqualTo("1234567890abcdef");
  }

  @Test
  @DisplayName("Test Visitor Custom Id F")
  void testVisitorCustomIdF() {
    request.setVisitorCustomId("1234567890abcdef");
    request.setVisitorCustomId(null);
    assertThat(request.getVisitorCustomId()).isNull();
  }

  /**
   * Test of getVisitorFirstVisitTimestamp method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor First Visit Timestamp")
  void testVisitorFirstVisitTimestamp() {
    request.setVisitorFirstVisitTimestamp(1000L);
    assertThat(request.getVisitorFirstVisitTimestamp()).isEqualTo(Long.valueOf(1000L));
  }

  /**
   * Test of getVisitorId method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Id TT")
  void testVisitorIdTT() {
    try {
      request.setVisitorId("1");
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("1 is not 16 characters long.");
    }
  }

  @Test
  @DisplayName("Test Visitor Id TFT")
  void testVisitorIdTFT() {
    try {
      request.setVisitorId("1234567890abcdeg");
      fail("Exception should have been thrown.");
    } catch (IllegalArgumentException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("1234567890abcdeg is not a hexadecimal string.");
    }
  }

  @Test
  @DisplayName("Test Visitor Id TFF")
  void testVisitorIdTFF() {
    request.setVisitorId("1234567890abcdef");
    assertThat(request.getVisitorId()).isEqualTo("1234567890abcdef");
  }

  @Test
  @DisplayName("Test Visitor Id F")
  void testVisitorIdF() {
    request.setVisitorId("1234567890abcdef");
    request.setVisitorId(null);
    assertThat(request.getVisitorId()).isNull();
  }

  /**
   * Test of getVisitorIp method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Ip T")
  void testVisitorIpT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorIp("ip");
    assertThat(request.getVisitorIp()).isEqualTo("ip");
  }

  @Test
  @DisplayName("Test Visitor Ip TE")
  void testVisitorIpTE() {
    try {
      request.setVisitorIp("ip");
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("AuthToken must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Visitor Ip F")
  void testVisitorIpF() {
    request.setVisitorIp(null);
    assertThat(request.getVisitorIp()).isNull();
  }

  /**
   * Test of getVisitorLatitude method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Latitude T")
  void testVisitorLatitudeT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorLatitude(10.5);
    assertThat(request.getVisitorLatitude()).isEqualTo(Double.valueOf(10.5));
  }

  @Test
  @DisplayName("Test Visitor Latitude TE")
  void testVisitorLatitudeTE() {
    try {
      request.setVisitorLatitude(10.5);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("AuthToken must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Visitor Latitude F")
  void testVisitorLatitudeF() {
    request.setVisitorLatitude(null);
    assertThat(request.getVisitorLatitude()).isNull();
  }

  /**
   * Test of getVisitorLongitude method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Longitude T")
  void testVisitorLongitudeT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorLongitude(20.5);
    assertThat(request.getVisitorLongitude()).isEqualTo(Double.valueOf(20.5));
  }

  @Test
  @DisplayName("Test Visitor Longitude TE")
  void testVisitorLongitudeTE() {
    try {
      request.setVisitorLongitude(20.5);
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("AuthToken must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Visitor Longitude F")
  void testVisitorLongitudeF() {
    request.setVisitorLongitude(null);
    assertThat(request.getVisitorLongitude()).isNull();
  }

  /**
   * Test of getVisitorPreviousVisitTimestamp method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Previous Visit Timestamp")
  void testVisitorPreviousVisitTimestamp() {
    request.setVisitorPreviousVisitTimestamp(1000L);
    assertThat(request.getVisitorPreviousVisitTimestamp()).isEqualTo(Long.valueOf(1000L));
  }

  /**
   * Test of getVisitorRegion method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Region T")
  void testVisitorRegionT() {
    request.setAuthToken("12345678901234567890123456789012");
    request.setVisitorRegion("region");
    assertThat(request.getVisitorRegion()).isEqualTo("region");
  }

  @Test
  @DisplayName("Test Get Visitor Region TE")
  void testGetVisitorRegionTE() {
    try {
      request.setVisitorRegion("region");
      fail("Exception should have been thrown.");
    } catch (IllegalStateException e) {
      assertThat(e.getLocalizedMessage()).isEqualTo("AuthToken must be set before this value can be set.");
    }
  }

  @Test
  @DisplayName("Test Visitor Region F")
  void testVisitorRegionF() {
    request.setVisitorRegion(null);
    assertThat(request.getVisitorRegion()).isNull();
  }

  /**
   * Test of getVisitorVisitCount method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Visitor Visit Count")
  void testVisitorVisitCount() {
    request.setVisitorVisitCount(100);
    assertThat(request.getVisitorVisitCount()).isEqualTo(Integer.valueOf(100));
  }

  /**
   * Test of getQueryString method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Get Query String")
  void testGetQueryString() {
    request.setRandomValue("random");
    request.setVisitorId("1234567890123456");
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456");
    request.setPageCustomVariable("key", "val");
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&cvar={\"1\":[\"key\",\"val\"]}");
    request.setPageCustomVariable("key", null);
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456");
    request.addCustomTrackingParameter("key", "test");
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&key=test");
    request.addCustomTrackingParameter("key", "test2");
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&key=test&key=test2");
    request.setCustomTrackingParameter("key2", "test3");
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&key=test&key=test2&key2=test3");
    request.setCustomTrackingParameter("key", "test4");
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&url=http://test.com&apiv=1&send_image=0&rand=random&_id=1234567890123456&key2=test3&key=test4");
    request.setRandomValue(null);
    request.setSiteId(null);
    request.setRequired(null);
    request.setApiVersion(null);
    request.setResponseAsImage(null);
    request.setVisitorId(null);
    request.setActionUrl((String) null);
    assertThat(request.getQueryString()).isEqualTo("key2=test3&key=test4");
    request.clearCustomTrackingParameter();
    assertThat(request.getQueryString()).isEqualTo("");
  }

  @Test
  @DisplayName("Test Get Query String 2")
  void testGetQueryString2() {
    request.setActionUrlWithString("http://test.com");
    request.setRandomValue("random");
    request.setVisitorId("1234567890123456");
    assertThat(request.getQueryString()).isEqualTo("idsite=3&rec=1&apiv=1&send_image=0&url=http://test.com&rand=random&_id=1234567890123456");
  }

  /**
   * Test of getUrlEncodedQueryString method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Get Url Encoded Query String")
  void testGetUrlEncodedQueryString() {
    request.setRandomValue("random");
    request.setVisitorId("1234567890123456");
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("_id=1234567890123456&apiv=1&idsite=3&rand=random&rec=1&send_image=0&url=http%3A%2F%2Ftest.com");
    request.addCustomTrackingParameter("ke/y", "te:st");
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("_id=1234567890123456&apiv=1&idsite=3&ke%2Fy=te%3Ast&rand=random&rec=1&send_image=0&url=http%3A%2F%2Ftest.com");
    request.addCustomTrackingParameter("ke/y", "te:st2");
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("_id=1234567890123456&apiv=1&idsite=3&ke%2Fy=te%3Ast&ke%2Fy=te%3Ast2&rand=random&rec=1&send_image=0&url=http%3A%2F%2Ftest.com");
    request.setCustomTrackingParameter("ke/y2", "te:st3");
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("_id=1234567890123456&apiv=1&idsite=3&ke%2Fy=te%3Ast&ke%2Fy=te%3Ast2&ke%2Fy2=te%3Ast3&rand=random&rec=1&send_image=0&url=http%3A%2F%2Ftest.com");
    request.setCustomTrackingParameter("ke/y", "te:st4");
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("_id=1234567890123456&apiv=1&idsite=3&ke%2Fy=te%3Ast4&ke%2Fy2=te%3Ast3&rand=random&rec=1&send_image=0&url=http%3A%2F%2Ftest.com");
    request.setRandomValue(null);
    request.setSiteId(null);
    request.setRequired(null);
    request.setApiVersion(null);
    request.setResponseAsImage(null);
    request.setVisitorId(null);
    request.setActionUrl((String) null);
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("ke%2Fy=te%3Ast4&ke%2Fy2=te%3Ast3");
    request.clearCustomTrackingParameter();
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("");
  }

  @Test
  @DisplayName("Test Get Url Encoded Query String 2")
  void testGetUrlEncodedQueryString2() {
    request.setActionUrlWithString("http://test.com");
    request.setRandomValue("random");
    request.setVisitorId("1234567890123456");
    assertThat(request.getUrlEncodedQueryString()).isEqualTo("_id=1234567890123456&apiv=1&idsite=3&rand=random&rec=1&send_image=0&url=http%3A%2F%2Ftest.com");
  }

  /**
   * Test of getRandomHexString method, of class PiwikRequest.
   */
  @Test
  @DisplayName("Test Get Random Hex String")
  void testGetRandomHexString() {
    String s = PiwikRequest.getRandomHexString(10);
    assertThat(s.length()).isEqualTo(10);
    Long.parseLong(s, 16);
  }
}
