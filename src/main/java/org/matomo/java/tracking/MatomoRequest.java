/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.matomo.java.tracking;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.BaseEncoding;
import lombok.NonNull;
import lombok.ToString;
import org.apache.http.client.utils.URIBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A class that implements the <a href="https://developer.matomo.org/api-reference/tracking-api">
 * Matomo Tracking HTTP API</a>.  These requests can be sent using {@link MatomoTracker}.
 *
 * @author brettcsorba
 */
@ToString
public class MatomoRequest {
  public static final int ID_LENGTH = 16;
  public static final int AUTH_TOKEN_LENGTH = 32;

  private static final String ACTION_NAME = "action_name";
  private static final String ACTION_TIME = "gt_ms";
  private static final String ACTION_URL = "url";
  private static final String API_VERSION = "apiv";
  private static final String AUTH_TOKEN = "token_auth";
  private static final String CAMPAIGN_KEYWORD = "_rck";
  private static final String CAMPAIGN_NAME = "_rcn";
  private static final String CHARACTER_SET = "cs";
  private static final String CONTENT_INTERACTION = "c_i";
  private static final String CONTENT_NAME = "c_n";
  private static final String CONTENT_PIECE = "c_p";
  private static final String CONTENT_TARGET = "c_t";
  private static final String CURRENT_HOUR = "h";
  private static final String CURRENT_MINUTE = "m";
  private static final String CURRENT_SECOND = "s";

  private static final String CUSTOM_ACTION = "ca";
  private static final String DEVICE_RESOLUTION = "res";
  private static final String DOWNLOAD_URL = "download";
  private static final String ECOMMERCE_DISCOUNT = "ec_dt";
  private static final String ECOMMERCE_ID = "ec_id";
  private static final String ECOMMERCE_ITEMS = "ec_items";
  private static final String ECOMMERCE_LAST_ORDER_TIMESTAMP = "_ects";
  private static final String ECOMMERCE_REVENUE = "revenue";
  private static final String ECOMMERCE_SHIPPING_COST = "ec_sh";
  private static final String ECOMMERCE_SUBTOTAL = "ec_st";
  private static final String ECOMMERCE_TAX = "ec_tx";
  private static final String EVENT_ACTION = "e_a";
  private static final String EVENT_CATEGORY = "e_c";
  private static final String EVENT_NAME = "e_n";
  private static final String EVENT_VALUE = "e_v";
  private static final String HEADER_ACCEPT_LANGUAGE = "lang";
  private static final String GOAL_ID = "idgoal";
  private static final String GOAL_REVENUE = "revenue";
  private static final String HEADER_USER_AGENT = "ua";
  private static final String NEW_VISIT = "new_visit";
  private static final String OUTLINK_URL = "link";
  private static final String PAGE_CUSTOM_VARIABLE = "cvar";
  private static final String PLUGIN_DIRECTOR = "dir";
  private static final String PLUGIN_FLASH = "fla";
  private static final String PLUGIN_GEARS = "gears";
  private static final String PLUGIN_JAVA = "java";
  private static final String PLUGIN_PDF = "pdf";
  private static final String PLUGIN_QUICKTIME = "qt";
  private static final String PLUGIN_REAL_PLAYER = "realp";
  private static final String PLUGIN_SILVERLIGHT = "ag";
  private static final String PLUGIN_WINDOWS_MEDIA = "wma";
  private static final String RANDOM_VALUE = "rand";
  private static final String REFERRER_URL = "urlref";
  private static final String REQUEST_DATETIME = "cdt";
  private static final String REQUIRED = "rec";
  private static final String RESPONSE_AS_IMAGE = "send_image";
  private static final String SEARCH_CATEGORY = "search_cat";
  private static final String SEARCH_QUERY = "search";
  private static final String SEARCH_RESULTS_COUNT = "search_count";
  private static final String SITE_ID = "idsite";
  private static final String TRACK_BOT_REQUESTS = "bots";
  private static final String VISIT_CUSTOM_VARIABLE = "_cvar";
  private static final String USER_ID = "uid";
  private static final String VISITOR_CITY = "city";
  private static final String VISITOR_COUNTRY = "country";
  private static final String VISITOR_CUSTOM_ID = "cid";
  private static final String VISITOR_FIRST_VISIT_TIMESTAMP = "_idts";
  private static final String VISITOR_ID = "_id";
  private static final String VISITOR_IP = "cip";
  private static final String VISITOR_LATITUDE = "lat";
  private static final String VISITOR_LONGITUDE = "long";
  private static final String VISITOR_PREVIOUS_VISIT_TIMESTAMP = "_viewts";
  private static final String VISITOR_REGION = "region";
  private static final String VISITOR_VISIT_COUNT = "_idvc";

  private static final int RANDOM_VALUE_LENGTH = 20;
  private static final long REQUEST_DATETIME_AUTH_LIMIT = 14400000L;
  private static final Pattern VISITOR_ID_PATTERN = Pattern.compile("[0-9A-Fa-f]+");

  private final Multimap<String, Object> parameters = LinkedHashMultimap.create(8, 1);

  private final Set<String> customTrackingParameterNames = new HashSet<>(2);

  /**
   * Create a new request from the id of the site being tracked and the full
   * url for the current action.  This constructor also sets:
   * <pre>
   * {@code
   * Required = true
   * Visior Id = random 16 character hex string
   * Random Value = random 20 character hex string
   * API version = 1
   * Response as Image = false
   * }
   * </pre>
   * Overwrite these values yourself as desired.
   *
   * @param siteId    the id of the website we're tracking a visit/action for
   * @param actionUrl the full URL for the current action
   */
  public MatomoRequest(int siteId, String actionUrl) {
    setParameter(SITE_ID, siteId);
    setBooleanParameter(REQUIRED, true);
    setParameter(ACTION_URL, actionUrl);
    setParameter(VISITOR_ID, getRandomHexString(ID_LENGTH));
    setParameter(RANDOM_VALUE, getRandomHexString(RANDOM_VALUE_LENGTH));
    setParameter(API_VERSION, "1");
    setBooleanParameter(RESPONSE_AS_IMAGE, false);
  }

  public static MatomoRequestBuilder builder() {
    return new MatomoRequestBuilder();
  }

  /**
   * Get the title of the action being tracked
   *
   * @return the title of the action being tracked
   */
  @Nullable
  public String getActionName() {
    return castOrNull(ACTION_NAME);
  }

  /**
   * Set the title of the action being tracked. It is possible to
   * <a href="http://matomo.org/faq/how-to/faq_62">use slashes /
   * to set one or several categories for this action</a>.
   * For example, <strong>Help / Feedback </strong>
   * will create the Action <strong>Feedback</strong> in the category Help.
   *
   * @param actionName the title of the action to set.  A null value will remove this parameter
   */
  public void setActionName(String actionName) {
    setParameter(ACTION_NAME, actionName);
  }

  /**
   * Get the amount of time it took the server to generate this action, in milliseconds.
   *
   * @return the amount of time
   */
  @Nullable
  public Long getActionTime() {
    return castOrNull(ACTION_TIME);
  }

  /**
   * Set the amount of time it took the server to generate this action, in milliseconds.
   * This value is used to process the
   * <a href=http://matomo.org/docs/page-speed/>Page speed report</a>
   * <strong>Avg. generation time</strong> column in the Page URL and Page Title reports,
   * as well as a site wide running average of the speed of your server.
   *
   * @param actionTime the amount of time to set.  A null value will remove this parameter
   */
  public void setActionTime(Long actionTime) {
    setParameter(ACTION_TIME, actionTime);
  }

  /**
   * Get the full URL for the current action.
   *
   * @return the full URL
   * @deprecated Please use {@link #getActionUrlAsString}
   */
  @Nullable
  public URL getActionUrl() {
    return castToUrlOrNull(ACTION_URL);
  }

  /**
   * Get the full URL for the current action.
   *
   * @return the full URL
   */
  @Deprecated
  @Nullable
  public String getActionUrlAsString() {
    return castOrNull(ACTION_URL);
  }


  /**
   * Set the full URL for the current action.
   *
   * @param actionUrl the full URL to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setActionUrl(String)}
   */
  @Deprecated
  public void setActionUrl(@NonNull URL actionUrl) {
    setActionUrl(actionUrl.toString());
  }

  /**
   * Set the full URL for the current action.
   *
   * @param actionUrl the full URL to set.  A null value will remove this parameter
   */
  public void setActionUrl(String actionUrl) {
    setParameter(ACTION_URL, actionUrl);
  }

  /**
   * Set the full URL for the current action.
   *
   * @param actionUrl the full URL to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setActionUrl(String)}
   */
  @Deprecated
  public void setActionUrlWithString(String actionUrl) {
    setActionUrl(actionUrl);
  }

  /**
   * Get the api version
   *
   * @return the api version
   */
  @Nullable
  public String getApiVersion() {
    return castOrNull(API_VERSION);
  }

  /**
   * Set the api version to use (currently always set to 1)
   *
   * @param apiVersion the api version to set.  A null value will remove this parameter
   */
  public void setApiVersion(String apiVersion) {
    setParameter(API_VERSION, apiVersion);
  }

  /**
   * Get the authorization key.
   *
   * @return the authorization key
   */
  @Nullable
  public String getAuthToken() {
    return castOrNull(AUTH_TOKEN);
  }

  /**
   * Set the {@value #AUTH_TOKEN_LENGTH} character authorization key used to authenticate the API request.
   *
   * @param authToken the authorization key to set.  A null value will remove this parameter
   */
  public void setAuthToken(String authToken) {
    if (authToken != null && authToken.length() != AUTH_TOKEN_LENGTH) {
      throw new IllegalArgumentException(authToken + " is not " + AUTH_TOKEN_LENGTH + " characters long.");
    }
    setParameter(AUTH_TOKEN, authToken);
  }

  /**
   * Verifies that AuthToken has been set for this request.  Will throw an
   * {@link IllegalStateException} if not.
   */
  public void verifyAuthTokenSet() {
    if (getAuthToken() == null) {
      throw new IllegalStateException("AuthToken must be set before this value can be set.");
    }
  }

  /**
   * Get the campaign keyword
   *
   * @return the campaign keyword
   */
  @Nullable
  public String getCampaignKeyword() {
    return castOrNull(CAMPAIGN_KEYWORD);
  }

  /**
   * Set the Campaign Keyword (see
   * <a href=http://matomo.org/docs/tracking-campaigns/>Tracking Campaigns</a>).
   * Used to populate the <em>Referrers &gt; Campaigns</em> report (clicking on a
   * campaign loads all keywords for this campaign). <em>Note: this parameter
   * will only be used for the first pageview of a visit.</em>
   *
   * @param campaignKeyword the campaign keyword to set.  A null value will remove this parameter
   */
  public void setCampaignKeyword(String campaignKeyword) {
    setParameter(CAMPAIGN_KEYWORD, campaignKeyword);
  }

  /**
   * Get the campaign name
   *
   * @return the campaign name
   */
  @Nullable
  public String getCampaignName() {
    return castOrNull(CAMPAIGN_NAME);
  }

  /**
   * Set the Campaign Name (see
   * <a href=http://matomo.org/docs/tracking-campaigns/>Tracking Campaigns</a>).
   * Used to populate the <em>Referrers &gt; Campaigns</em> report. <em>Note: this parameter
   * will only be used for the first pageview of a visit.</em>
   *
   * @param campaignName the campaign name to set.  A null value will remove this parameter
   */
  public void setCampaignName(String campaignName) {
    setParameter(CAMPAIGN_NAME, campaignName);
  }

  /**
   * Get the charset of the page being tracked
   *
   * @return the charset
   */
  @Nullable
  public Charset getCharacterSet() {
    return castOrNull(CHARACTER_SET);
  }

  /**
   * The charset of the page being tracked. Specify the charset if the data
   * you send to Matomo is encoded in a different character set than the default
   * <strong>utf-8</strong>.
   *
   * @param characterSet the charset to set.  A null value will remove this parameter
   */
  public void setCharacterSet(Charset characterSet) {
    setParameter(CHARACTER_SET, characterSet);
  }

  /**
   * Get the name of the interaction with the content
   *
   * @return the name of the interaction
   */
  @Nullable
  public String getContentInteraction() {
    return castOrNull(CONTENT_INTERACTION);
  }

  /**
   * Set the name of the interaction with the content. For instance a 'click'.
   *
   * @param contentInteraction the name of the interaction to set.  A null value will remove this parameter
   */
  public void setContentInteraction(String contentInteraction) {
    setParameter(CONTENT_INTERACTION, contentInteraction);
  }

  /**
   * Get the name of the content
   *
   * @return the name
   */
  @Nullable
  public String getContentName() {
    return castOrNull(CONTENT_NAME);
  }

  /**
   * Set the name of the content. For instance 'Ad Foo Bar'.
   *
   * @param contentName the name to set.  A null value will remove this parameter
   */
  public void setContentName(String contentName) {
    setParameter(CONTENT_NAME, contentName);
  }

  /**
   * Get the content piece.
   *
   * @return the content piece.
   */
  @Nullable
  public String getContentPiece() {
    return castOrNull(CONTENT_PIECE);
  }

  /**
   * Set the actual content piece. For instance the path to an image, video, audio, any text.
   *
   * @param contentPiece the content piece to set.  A null value will remove this parameter
   */
  public void setContentPiece(String contentPiece) {
    setParameter(CONTENT_PIECE, contentPiece);
  }

  /**
   * Get the content target
   *
   * @return the target
   */
  @Nullable
  public URL getContentTarget() {
    return castToUrlOrNull(CONTENT_TARGET);
  }

  @Nullable
  private URL castToUrlOrNull(@NonNull String key) {
    String url = castOrNull(key);
    if (url == null) {
      return null;
    }
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new InvalidUrlException(e);
    }
  }

  /**
   * Get the content target
   *
   * @return the target
   */
  @Nullable
  public String getContentTargetAsString() {
    return castOrNull(CONTENT_TARGET);
  }

  /**
   * Set the target of the content. For instance the URL of a landing page.
   *
   * @param contentTarget the target to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setContentTarget(String)}
   */
  @Deprecated
  public void setContentTarget(@NonNull URL contentTarget) {
    setContentTarget(contentTarget.toString());
  }

  /**
   * Set the target of the content. For instance the URL of a landing page.
   *
   * @param contentTarget the target to set.  A null value will remove this parameter
   */
  public void setContentTarget(String contentTarget) {
    setParameter(CONTENT_TARGET, contentTarget);
  }

  /**
   * Set the target of the content. For instance the URL of a landing page.
   *
   * @param contentTarget the target to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setContentTarget(String)}
   */
  @Deprecated
  public void setContentTargetWithString(String contentTarget) {
    setContentTarget(contentTarget);
  }

  /**
   * Get the current hour.
   *
   * @return the current hour
   */
  @Nullable
  public Integer getCurrentHour() {
    return castOrNull(CURRENT_HOUR);
  }

  /**
   * Set the current hour (local time).
   *
   * @param currentHour the hour to set.  A null value will remove this parameter
   */
  public void setCurrentHour(Integer currentHour) {
    setParameter(CURRENT_HOUR, currentHour);
  }

  /**
   * Get the current minute.
   *
   * @return the current minute
   */
  @Nullable
  public Integer getCurrentMinute() {
    return castOrNull(CURRENT_MINUTE);
  }

  /**
   * Set the current minute (local time).
   *
   * @param currentMinute the minute to set.  A null value will remove this parameter
   */
  public void setCurrentMinute(Integer currentMinute) {
    setParameter(CURRENT_MINUTE, currentMinute);
  }

  /**
   * Get the current second
   *
   * @return the current second
   */
  @Nullable
  public Integer getCurrentSecond() {
    return castOrNull(CURRENT_SECOND);
  }

  /**
   * Set the current second (local time).
   *
   * @param currentSecond the second to set.  A null value will remove this parameter
   */
  public void setCurrentSecond(Integer currentSecond) {
    setParameter(CURRENT_SECOND, currentSecond);
  }

  /**
   * Get the custom action
   *
   * @return the custom action
   */
  @Nullable
  public Boolean getCustomAction() {
    return getBooleanParameter(CUSTOM_ACTION);
  }

  /**
   * Set the custom action
   *
   * @param customAction the second to set.  A null value will remove this parameter
   */
  public void setCustomAction(Boolean customAction) {
    setBooleanParameter(CUSTOM_ACTION, customAction);
  }

  /**
   * Gets the list of objects currently stored at the specified custom tracking
   * parameter.  An empty list will be returned if there are no objects set at
   * that key.
   *
   * @param key the key of the parameter whose list of objects to get.  Cannot be null
   * @return the list of objects currently stored at the specified key
   */
  public List<Object> getCustomTrackingParameter(@NonNull String key) {
    return new ArrayList<>(parameters.get(key));
  }

  /**
   * Set a custom tracking parameter whose toString() value will be sent to
   * the Matomo server.  These parameters are stored separately from named Matomo
   * parameters, meaning it is not possible to overwrite or clear named Matomo
   * parameters with this method.  A custom parameter that has the same name
   * as a named Matomo parameter will be sent in addition to that named parameter.
   *
   * @param key   the parameter's key.  Cannot be null
   * @param value the parameter's value.  Removes the parameter if null
   */
  public <T> void setCustomTrackingParameter(@NonNull String key, @Nullable T value) {
    customTrackingParameterNames.add(key);
    setParameter(key, value);
  }

  /**
   * Add a custom tracking parameter to the specified key.  This allows users
   * to have multiple parameters with the same name and different values,
   * commonly used during situations where list parameters are needed
   *
   * @param key   the parameter's key.  Cannot be null
   * @param value the parameter's value.  Cannot be null
   */
  public void addCustomTrackingParameter(@NonNull String key, @NonNull Object value) {
    customTrackingParameterNames.add(key);
    addParameter(key, value);
  }

  /**
   * Removes all custom tracking parameters
   */
  public void clearCustomTrackingParameter() {
    for (String customTrackingParameterName : customTrackingParameterNames) {
      setParameter(customTrackingParameterName, null);
    }
  }

  /**
   * Get the resolution of the device
   *
   * @return the resolution
   */
  @Nullable
  public String getDeviceResolution() {
    return castOrNull(DEVICE_RESOLUTION);
  }

  /**
   * Set the resolution of the device the visitor is using, eg <strong>1280x1024</strong>.
   *
   * @param deviceResolution the resolution to set.  A null value will remove this parameter
   */
  public void setDeviceResolution(String deviceResolution) {
    setParameter(DEVICE_RESOLUTION, deviceResolution);
  }

  /**
   * Get the url of a file the user had downloaded
   *
   * @return the url
   */
  @Nullable
  public URL getDownloadUrl() {
    return castToUrlOrNull(DOWNLOAD_URL);
  }

  /**
   * Get the url of a file the user had downloaded
   *
   * @return the url
   */
  @Nullable
  public String getDownloadUrlAsString() {
    return castOrNull(DOWNLOAD_URL);
  }

  /**
   * Set the url of a file the user has downloaded. Used for tracking downloads.
   * We recommend to also set the <strong>url</strong> parameter to this same value.
   *
   * @param downloadUrl the url to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setDownloadUrl(String)}
   */
  @Deprecated
  public void setDownloadUrl(@NonNull URL downloadUrl) {
    setDownloadUrl(downloadUrl.toString());
  }

  /**
   * Set the url of a file the user has downloaded. Used for tracking downloads.
   * We recommend to also set the <strong>url</strong> parameter to this same value.
   *
   * @param downloadUrl the url to set.  A null value will remove this parameter
   */
  public void setDownloadUrl(String downloadUrl) {
    setParameter(DOWNLOAD_URL, downloadUrl);
  }

  /**
   * Set the url of a file the user has downloaded. Used for tracking downloads.
   * We recommend to also set the <strong>url</strong> parameter to this same value.
   *
   * @param downloadUrl the url to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setDownloadUrl(String)}
   */
  @Deprecated
  public void setDownloadUrlWithString(String downloadUrl) {
    setDownloadUrl(downloadUrl);
  }

  /**
   * Sets <em>idgoal&#61;0</em> in the request to track an ecommerce interaction:
   * cart update or an ecommerce order.
   */
  public void enableEcommerce() {
    setGoalId(0);
  }

  /**
   * Verifies that Ecommerce has been enabled for the request.  Will throw an
   * {@link IllegalStateException} if not.
   */
  public void verifyEcommerceEnabled() {
    if (getGoalId() == null || getGoalId() != 0) {
      throw new IllegalStateException("GoalId must be \"0\".  Try calling enableEcommerce first before calling this method.");
    }
  }

  /**
   * Verifies that Ecommerce has been enabled and that Ecommerce Id and
   * Ecommerce Revenue have been set for the request.  Will throw an
   * {@link IllegalStateException} if not.
   */
  public void verifyEcommerceState() {
    verifyEcommerceEnabled();
    if (getEcommerceId() == null) {
      throw new IllegalStateException("EcommerceId must be set before this value can be set.");
    }
    if (getEcommerceRevenue() == null) {
      throw new IllegalStateException("EcommerceRevenue must be set before this value can be set.");
    }
  }

  /**
   * Get the discount offered.
   *
   * @return the discount
   */
  @Nullable
  public Double getEcommerceDiscount() {
    return castOrNull(ECOMMERCE_DISCOUNT);
  }

  /**
   * Set the discount offered.  Ecommerce must be enabled, and EcommerceId and
   * EcommerceRevenue must first be set.
   *
   * @param discount the discount to set.  A null value will remove this parameter
   */
  public void setEcommerceDiscount(Double discount) {
    if (discount != null) {
      verifyEcommerceState();
    }
    setParameter(ECOMMERCE_DISCOUNT, discount);
  }

  /**
   * Get the id of this order.
   *
   * @return the id
   */
  @Nullable
  public String getEcommerceId() {
    return castOrNull(ECOMMERCE_ID);
  }

  /**
   * Set the unique string identifier for the ecommerce order (required when
   * tracking an ecommerce order).  Ecommerce must be enabled.
   *
   * @param id the id to set.  A null value will remove this parameter
   */
  public void setEcommerceId(String id) {
    if (id != null) {
      verifyEcommerceEnabled();
    }
    setParameter(ECOMMERCE_ID, id);
  }

  /**
   * Get the {@link EcommerceItem} at the specified index
   *
   * @param index the index of the {@link EcommerceItem} to return
   * @return the {@link EcommerceItem} at the specified index
   */
  @Nullable
  public EcommerceItem getEcommerceItem(int index) {
    EcommerceItems ecommerceItems = castOrNull(ECOMMERCE_ITEMS);
    if (ecommerceItems == null) {
      return null;
    }
    return ecommerceItems.get(index);
  }

  /**
   * Add an {@link EcommerceItem} to this order.  Ecommerce must be enabled,
   * and EcommerceId and EcommerceRevenue must first be set.
   *
   * @param item the {@link EcommerceItem} to add.  Cannot be null
   */
  public void addEcommerceItem(@NonNull EcommerceItem item) {
    verifyEcommerceState();
    EcommerceItems ecommerceItems = castOrNull(ECOMMERCE_ITEMS);
    if (ecommerceItems == null) {
      ecommerceItems = new EcommerceItems();
      setParameter(ECOMMERCE_ITEMS, ecommerceItems);
    }
    ecommerceItems.add(item);
  }

  /**
   * Clears all {@link EcommerceItem} from this order.
   */
  public void clearEcommerceItems() {
    setParameter(ECOMMERCE_ITEMS, null);
  }

  /**
   * Get the timestamp of the customer's last ecommerce order
   *
   * @return the timestamp
   */
  @Nullable
  public Long getEcommerceLastOrderTimestamp() {
    return castOrNull(ECOMMERCE_LAST_ORDER_TIMESTAMP);
  }

  /**
   * Set the UNUX timestamp of this customer's last ecommerce order. This value
   * is used to process the "Days since last order" report.  Ecommerce must be
   * enabled, and EcommerceId and EcommerceRevenue must first be set.
   *
   * @param timestamp the timestamp to set.  A null value will remove this parameter
   */
  public void setEcommerceLastOrderTimestamp(Long timestamp) {
    if (timestamp != null) {
      verifyEcommerceState();
    }
    setParameter(ECOMMERCE_LAST_ORDER_TIMESTAMP, timestamp);
  }

  /**
   * Get the grand total of the ecommerce order.
   *
   * @return the grand total
   */
  @Nullable
  public Double getEcommerceRevenue() {
    return castOrNull(ECOMMERCE_REVENUE);
  }

  /**
   * Set the grand total of the ecommerce order (required when tracking an
   * ecommerce order).  Ecommerce must be enabled.
   *
   * @param revenue the grand total to set.  A null value will remove this parameter
   */
  public void setEcommerceRevenue(Double revenue) {
    if (revenue != null) {
      verifyEcommerceEnabled();
    }
    setParameter(ECOMMERCE_REVENUE, revenue);
  }

  /**
   * Get the shipping cost of the ecommerce order.
   *
   * @return the shipping cost
   */
  @Nullable
  public Double getEcommerceShippingCost() {
    return castOrNull(ECOMMERCE_SHIPPING_COST);
  }

  /**
   * Set the shipping cost of the ecommerce order.  Ecommerce must be enabled,
   * and EcommerceId and EcommerceRevenue must first be set.
   *
   * @param shippingCost the shipping cost to set.  A null value will remove this parameter
   */
  public void setEcommerceShippingCost(Double shippingCost) {
    if (shippingCost != null) {
      verifyEcommerceState();
    }
    setParameter(ECOMMERCE_SHIPPING_COST, shippingCost);
  }

  /**
   * Get the subtotal of the ecommerce order; excludes shipping.
   *
   * @return the subtotal
   */
  @Nullable
  public Double getEcommerceSubtotal() {
    return castOrNull(ECOMMERCE_SUBTOTAL);
  }

  /**
   * Set the subtotal of the ecommerce order; excludes shipping.  Ecommerce
   * must be enabled and EcommerceId and EcommerceRevenue must first be set.
   *
   * @param subtotal the subtotal to set.  A null value will remove this parameter
   */
  public void setEcommerceSubtotal(Double subtotal) {
    if (subtotal != null) {
      verifyEcommerceState();
    }
    setParameter(ECOMMERCE_SUBTOTAL, subtotal);
  }

  /**
   * Get the tax amount of the ecommerce order.
   *
   * @return the tax amount
   */
  @Nullable
  public Double getEcommerceTax() {
    return castOrNull(ECOMMERCE_TAX);
  }

  /**
   * Set the tax amount of the ecommerce order.  Ecommerce must be enabled, and
   * EcommerceId and EcommerceRevenue must first be set.
   *
   * @param tax the tax amount to set.  A null value will remove this parameter
   */
  public void setEcommerceTax(Double tax) {
    if (tax != null) {
      verifyEcommerceState();
    }
    setParameter(ECOMMERCE_TAX, tax);
  }

  /**
   * Get the event action.
   *
   * @return the event action
   */
  @Nullable
  public String getEventAction() {
    return castOrNull(EVENT_ACTION);
  }

  /**
   * Set the event action. Must not be empty. (eg. Play, Pause, Duration,
   * Add Playlist, Downloaded, Clicked...).
   *
   * @param eventAction the event action to set.  A null value will remove this parameter
   */
  public void setEventAction(String eventAction) {
    setNonEmptyStringParameter(EVENT_ACTION, eventAction);
  }

  /**
   * Get the event category.
   *
   * @return the event category
   */
  @Nullable
  public String getEventCategory() {
    return castOrNull(EVENT_CATEGORY);
  }

  /**
   * Set the event category. Must not be empty. (eg. Videos, Music, Games...).
   *
   * @param eventCategory the event category to set.  A null value will remove this parameter
   */
  public void setEventCategory(String eventCategory) {
    setNonEmptyStringParameter(EVENT_CATEGORY, eventCategory);
  }

  /**
   * Get the event name.
   *
   * @return the event name
   */
  @Nullable
  public String getEventName() {
    return castOrNull(EVENT_NAME);
  }

  /**
   * Set the event name. (eg. a Movie name, or Song name, or File name...).
   *
   * @param eventName the event name to set.  A null value will remove this parameter
   */
  public void setEventName(String eventName) {
    setParameter(EVENT_NAME, eventName);
  }

  /**
   * Get the event value.
   *
   * @return the event value
   */
  @Nullable
  public Number getEventValue() {
    return castOrNull(EVENT_VALUE);
  }

  /**
   * Set the event value. Must be a float or integer value (numeric), not a string.
   *
   * @param eventValue the event value to set.  A null value will remove this parameter
   */
  public void setEventValue(Number eventValue) {
    setParameter(EVENT_VALUE, eventValue);
  }

  /**
   * Get the goal id
   *
   * @return the goal id
   */
  @Nullable
  public Integer getGoalId() {
    return castOrNull(GOAL_ID);
  }

  /**
   * Set the goal id.  If specified, the tracking request will trigger a
   * conversion for the goal of the website being tracked with this id.
   *
   * @param goalId the goal id to set.  A null value will remove this parameter
   */
  public void setGoalId(Integer goalId) {
    setParameter(GOAL_ID, goalId);
  }

  /**
   * Get the goal revenue.
   *
   * @return the goal revenue
   */
  @Nullable
  public Double getGoalRevenue() {
    return castOrNull(GOAL_REVENUE);
  }

  /**
   * Set a monetary value that was generated as revenue by this goal conversion.
   * Only used if idgoal is specified in the request.
   *
   * @param goalRevenue the goal revenue to set.  A null value will remove this parameter
   */
  public void setGoalRevenue(Double goalRevenue) {
    if (goalRevenue != null && getGoalId() == null) {
      throw new IllegalStateException("GoalId must be set before GoalRevenue can be set.");
    }
    setParameter(GOAL_REVENUE, goalRevenue);
  }

  /**
   * Get the Accept-Language HTTP header
   *
   * @return the Accept-Language HTTP header
   */
  @Nullable
  public String getHeaderAcceptLanguage() {
    return castOrNull(HEADER_ACCEPT_LANGUAGE);
  }

  /**
   * Set an override value for the <strong>Accept-Language</strong> HTTP header
   * field. This value is used to detect the visitor's country if
   * <a href="http://matomo.org/faq/troubleshooting/faq_65">GeoIP </a>is not enabled.
   *
   * @param acceptLangage the Accept-Language HTTP header to set.  A null value will remove this parameter
   */
  public void setHeaderAcceptLanguage(String acceptLangage) {
    setParameter(HEADER_ACCEPT_LANGUAGE, acceptLangage);
  }

  /**
   * Get the User-Agent HTTP header
   *
   * @return the User-Agent HTTP header
   */
  @Nullable
  public String getHeaderUserAgent() {
    return castOrNull(HEADER_USER_AGENT);
  }

  /**
   * Set an override value for the <strong>User-Agent</strong> HTTP header field.
   * The user agent is used to detect the operating system and browser used.
   *
   * @param userAgent the User-Agent HTTP header tos et
   */
  public void setHeaderUserAgent(String userAgent) {
    setParameter(HEADER_USER_AGENT, userAgent);
  }

  /**
   * Get if this request will force a new visit.
   *
   * @return true if this request will force a new visit
   */
  @Nullable
  public Boolean getNewVisit() {
    return getBooleanParameter(NEW_VISIT);
  }

  /**
   * If set to true, will force a new visit to be created for this action.
   *
   * @param newVisit if this request will force a new visit
   */
  public void setNewVisit(Boolean newVisit) {
    setBooleanParameter(NEW_VISIT, newVisit);
  }

  /**
   * Get the outlink url
   *
   * @return the outlink url
   */
  @Nullable
  public URL getOutlinkUrl() {
    return castToUrlOrNull(OUTLINK_URL);
  }

  /**
   * Get the outlink url
   *
   * @return the outlink url
   */
  @Nullable
  public String getOutlinkUrlAsString() {
    return castOrNull(OUTLINK_URL);
  }

  /**
   * Set an external URL the user has opened. Used for tracking outlink clicks.
   * We recommend to also set the <strong>url</strong> parameter to this same value.
   *
   * @param outlinkUrl the outlink url to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setOutlinkUrl(String)}
   */
  @Deprecated
  public void setOutlinkUrl(@NonNull URL outlinkUrl) {
    setOutlinkUrl(outlinkUrl.toString());
  }


  /**
   * Set an external URL the user has opened. Used for tracking outlink clicks.
   * We recommend to also set the <strong>url</strong> parameter to this same value.
   *
   * @param outlinkUrl the outlink url to set.  A null value will remove this parameter
   */
  public void setOutlinkUrl(String outlinkUrl) {
    setParameter(OUTLINK_URL, outlinkUrl);
  }

  /**
   * Set an external URL the user has opened. Used for tracking outlink clicks.
   * We recommend to also set the <strong>url</strong> parameter to this same value.
   *
   * @param outlinkUrl the outlink url to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setOutlinkUrl(String)}
   */
  @Deprecated
  public void setOutlinkUrlWithString(String outlinkUrl) {
    setOutlinkUrl(outlinkUrl);
  }

  /**
   * Get the page custom variable at the specified key.
   *
   * @param key the key of the variable to get
   * @return the variable at the specified key, null if key is not present
   * @deprecated Use the {@link #getPageCustomVariable(int)} method instead.
   */
  @Nullable
  @Deprecated
  public String getPageCustomVariable(String key) {
    return getCustomVariable(PAGE_CUSTOM_VARIABLE, key);
  }

  /**
   * Get the page custom variable at the specified index.
   *
   * @param index the index of the variable to get.  Must be greater than 0
   * @return the variable at the specified key, null if nothing at this index
   */
  @Nullable
  public CustomVariable getPageCustomVariable(int index) {
    return getCustomVariable(PAGE_CUSTOM_VARIABLE, index);
  }

  /**
   * Set a page custom variable with the specified key and value at the first available index.
   * All page custom variables with this key will be overwritten or deleted
   *
   * @param key   the key of the variable to set
   * @param value the value of the variable to set at the specified key.  A null value will remove this custom variable
   * @deprecated Use the {@link #setPageCustomVariable(CustomVariable, int)} method instead.
   */
  @Deprecated
  public void setPageCustomVariable(String key, String value) {
    if (value == null) {
      removeCustomVariable(PAGE_CUSTOM_VARIABLE, key);
    } else {
      setCustomVariable(PAGE_CUSTOM_VARIABLE, new CustomVariable(key, value), null);
    }
  }

  /**
   * Set a page custom variable at the specified index.
   *
   * @param customVariable the CustomVariable to set.  A null value will remove the CustomVariable at the specified index
   * @param index          the index of he CustomVariable to set
   */
  public void setPageCustomVariable(CustomVariable customVariable, int index) {
    setCustomVariable(PAGE_CUSTOM_VARIABLE, customVariable, index);
  }

  /**
   * Check if the visitor has the Director plugin.
   *
   * @return true if visitor has the Director plugin
   */
  @Nullable
  public Boolean getPluginDirector() {
    return getBooleanParameter(PLUGIN_DIRECTOR);
  }

  /**
   * Set if the visitor has the Director plugin.
   *
   * @param director true if the visitor has the Director plugin
   */
  public void setPluginDirector(Boolean director) {
    setBooleanParameter(PLUGIN_DIRECTOR, director);
  }

  /**
   * Check if the visitor has the Flash plugin.
   *
   * @return true if the visitor has the Flash plugin
   */
  @Nullable
  public Boolean getPluginFlash() {
    return getBooleanParameter(PLUGIN_FLASH);
  }

  /**
   * Set if the visitor has the Flash plugin.
   *
   * @param flash true if the visitor has the Flash plugin
   */
  @Nullable
  public void setPluginFlash(Boolean flash) {
    setBooleanParameter(PLUGIN_FLASH, flash);
  }

  /**
   * Check if the visitor has the Gears plugin.
   *
   * @return true if the visitor has the Gears plugin
   */
  @Nullable
  public Boolean getPluginGears() {
    return getBooleanParameter(PLUGIN_GEARS);
  }

  /**
   * Set if the visitor has the Gears plugin.
   *
   * @param gears true if the visitor has the Gears plugin
   */
  public void setPluginGears(Boolean gears) {
    setBooleanParameter(PLUGIN_GEARS, gears);
  }

  /**
   * Check if the visitor has the Java plugin.
   *
   * @return true if the visitor has the Java plugin
   */
  @Nullable
  public Boolean getPluginJava() {
    return getBooleanParameter(PLUGIN_JAVA);
  }

  /**
   * Set if the visitor has the Java plugin.
   *
   * @param java true if the visitor has the Java plugin
   */
  public void setPluginJava(Boolean java) {
    setBooleanParameter(PLUGIN_JAVA, java);
  }

  /**
   * Check if the visitor has the PDF plugin.
   *
   * @return true if the visitor has the PDF plugin
   */
  @Nullable
  public Boolean getPluginPDF() {
    return getBooleanParameter(PLUGIN_PDF);
  }

  /**
   * Set if the visitor has the PDF plugin.
   *
   * @param pdf true if the visitor has the PDF plugin
   */
  public void setPluginPDF(Boolean pdf) {
    setBooleanParameter(PLUGIN_PDF, pdf);
  }

  /**
   * Check if the visitor has the Quicktime plugin.
   *
   * @return true if the visitor has the Quicktime plugin
   */
  @Nullable
  public Boolean getPluginQuicktime() {
    return getBooleanParameter(PLUGIN_QUICKTIME);
  }

  /**
   * Set if the visitor has the Quicktime plugin.
   *
   * @param quicktime true if the visitor has the Quicktime plugin
   */
  public void setPluginQuicktime(Boolean quicktime) {
    setBooleanParameter(PLUGIN_QUICKTIME, quicktime);
  }

  /**
   * Check if the visitor has the RealPlayer plugin.
   *
   * @return true if the visitor has the RealPlayer plugin
   */
  @Nullable
  public Boolean getPluginRealPlayer() {
    return getBooleanParameter(PLUGIN_REAL_PLAYER);
  }

  /**
   * Set if the visitor has the RealPlayer plugin.
   *
   * @param realPlayer true if the visitor has the RealPlayer plugin
   */
  public void setPluginRealPlayer(Boolean realPlayer) {
    setBooleanParameter(PLUGIN_REAL_PLAYER, realPlayer);
  }

  /**
   * Check if the visitor has the Silverlight plugin.
   *
   * @return true if the visitor has the Silverlight plugin
   */
  @Nullable
  public Boolean getPluginSilverlight() {
    return getBooleanParameter(PLUGIN_SILVERLIGHT);
  }

  /**
   * Set if the visitor has the Silverlight plugin.
   *
   * @param silverlight true if the visitor has the Silverlight plugin
   */
  public void setPluginSilverlight(Boolean silverlight) {
    setBooleanParameter(PLUGIN_SILVERLIGHT, silverlight);
  }

  /**
   * Check if the visitor has the Windows Media plugin.
   *
   * @return true if the visitor has the Windows Media plugin
   */
  @Nullable
  public Boolean getPluginWindowsMedia() {
    return getBooleanParameter(PLUGIN_WINDOWS_MEDIA);
  }

  /**
   * Set if the visitor has the Windows Media plugin.
   *
   * @param windowsMedia true if the visitor has the Windows Media plugin
   */
  public void setPluginWindowsMedia(Boolean windowsMedia) {
    setBooleanParameter(PLUGIN_WINDOWS_MEDIA, windowsMedia);
  }

  /**
   * Get the random value for this request
   *
   * @return the random value
   */
  @Nullable
  public String getRandomValue() {
    return castOrNull(RANDOM_VALUE);
  }

  /**
   * Set a random value that is generated before each request. Using it helps
   * avoid the tracking request being cached by the browser or a proxy.
   *
   * @param randomValue the random value to set.  A null value will remove this parameter
   */
  public void setRandomValue(String randomValue) {
    setParameter(RANDOM_VALUE, randomValue);
  }

  /**
   * Get the referrer url
   *
   * @return the referrer url
   */
  @Nullable
  public URL getReferrerUrl() {
    return castToUrlOrNull(REFERRER_URL);
  }

  /**
   * Get the referrer url
   *
   * @return the referrer url
   */
  @Nullable
  public String getReferrerUrlAsString() {
    return castOrNull(REFERRER_URL);
  }

  /**
   * Set the full HTTP Referrer URL. This value is used to determine how someone
   * got to your website (ie, through a website, search engine or campaign).
   *
   * @param referrerUrl the referrer url to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setReferrerUrl(String)}
   */
  @Deprecated
  public void setReferrerUrl(@NonNull URL referrerUrl) {
    setReferrerUrl(referrerUrl.toString());
  }

  /**
   * Set the full HTTP Referrer URL. This value is used to determine how someone
   * got to your website (ie, through a website, search engine or campaign).
   *
   * @param referrerUrl the referrer url to set.  A null value will remove this parameter
   */
  public void setReferrerUrl(String referrerUrl) {
    setParameter(REFERRER_URL, referrerUrl);
  }

  /**
   * Set the full HTTP Referrer URL. This value is used to determine how someone
   * got to your website (ie, through a website, search engine or campaign).
   *
   * @param referrerUrl the referrer url to set.  A null value will remove this parameter
   * @deprecated Please use {@link #setReferrerUrl(String)}
   */
  @Deprecated
  public void setReferrerUrlWithString(String referrerUrl) {
    setReferrerUrl(referrerUrl);
  }

  /**
   * Get the datetime of the request
   *
   * @return the datetime of the request
   */
  @Nullable
  public MatomoDate getRequestDatetime() {
    return castOrNull(REQUEST_DATETIME);
  }

  /**
   * Set the datetime of the request (normally the current time is used).
   * This can be used to record visits and page views in the past. The datetime
   * must be sent in UTC timezone. <em>Note: if you record data in the past, you will
   * need to <a href="http://matomo.org/faq/how-to/faq_59">force Matomo to re-process
   * reports for the past dates</a>.</em> If you set the <em>Request Datetime</em> to a datetime
   * older than four hours then <em>Auth Token</em> must be set. If you set
   * <em>Request Datetime</em> with a datetime in the last four hours then you
   * don't need to pass <em>Auth Token</em>.
   *
   * @param datetime the datetime of the request to set.  A null value will remove this parameter
   */
  public void setRequestDatetime(MatomoDate datetime) {
    if (datetime != null && new Date().getTime() - datetime.getTime() > REQUEST_DATETIME_AUTH_LIMIT && getAuthToken() == null) {
      throw new IllegalStateException("Because you are trying to set RequestDatetime for a time greater than 4 hours ago, AuthToken must be set first.");
    }
    setParameter(REQUEST_DATETIME, datetime);
  }

  /**
   * Get if this request will be tracked.
   *
   * @return true if request will be tracked
   */
  @Nullable
  public Boolean getRequired() {
    return getBooleanParameter(REQUIRED);
  }

  /**
   * Set if this request will be tracked by the Matomo server.
   *
   * @param required true if request will be tracked
   */
  public void setRequired(Boolean required) {
    setBooleanParameter(REQUIRED, required);
  }

  /**
   * Get if the response will be an image.
   *
   * @return true if the response will be an an image
   */
  @Nullable
  public Boolean getResponseAsImage() {
    return getBooleanParameter(RESPONSE_AS_IMAGE);
  }

  /**
   * Set if the response will be an image.  If set to false, Matomo will respond
   * with a HTTP 204 response code instead of a GIF image. This improves performance
   * and can fix errors if images are not allowed to be obtained directly
   * (eg Chrome Apps). Available since Matomo 2.10.0.
   *
   * @param responseAsImage true if the response will be an image
   */
  public void setResponseAsImage(Boolean responseAsImage) {
    setBooleanParameter(RESPONSE_AS_IMAGE, responseAsImage);
  }

  /**
   * Get the search category
   *
   * @return the search category
   */
  @Nullable
  public String getSearchCategory() {
    return castOrNull(SEARCH_CATEGORY);
  }

  /**
   * Specify a search category with this parameter.  SearchQuery must first be
   * set.
   *
   * @param searchCategory the search category to set.  A null value will remove this parameter
   */
  public void setSearchCategory(String searchCategory) {
    if (searchCategory != null && getSearchQuery() == null) {
      throw new IllegalStateException("SearchQuery must be set before SearchCategory can be set.");
    }
    setParameter(SEARCH_CATEGORY, searchCategory);
  }

  /**
   * Get the search query.
   *
   * @return the search query
   */
  @Nullable
  public String getSearchQuery() {
    return castOrNull(SEARCH_QUERY);
  }

  /**
   * Set the search query.  When specified, the request will not be tracked as
   * a normal pageview but will instead be tracked as a Site Search request.
   *
   * @param searchQuery the search query to set.  A null value will remove this parameter
   */
  public void setSearchQuery(String searchQuery) {
    setParameter(SEARCH_QUERY, searchQuery);
  }

  /**
   * Get the search results count.
   *
   * @return the search results count
   */
  @Nullable
  public Long getSearchResultsCount() {
    return castOrNull(SEARCH_RESULTS_COUNT);
  }

  /**
   * We recommend to set the
   * search count to the number of search results displayed on the results page.
   * When keywords are tracked with {@code Search Results Count=0} they will appear in
   * the "No Result Search Keyword" report.  SearchQuery must first be set.
   *
   * @param searchResultsCount the search results count to set.  A null value will remove this parameter
   */
  public void setSearchResultsCount(Long searchResultsCount) {
    if (searchResultsCount != null && getSearchQuery() == null) {
      throw new IllegalStateException("SearchQuery must be set before SearchResultsCount can be set.");
    }
    setParameter(SEARCH_RESULTS_COUNT, searchResultsCount);
  }

  /**
   * Get the id of the website we're tracking.
   *
   * @return the id of the website
   */
  @Nullable
  public Integer getSiteId() {
    return castOrNull(SITE_ID);
  }

  /**
   * Set the ID of the website we're tracking a visit/action for.
   *
   * @param siteId the id of the website to set.  A null value will remove this parameter
   */
  public void setSiteId(Integer siteId) {
    setParameter(SITE_ID, siteId);
  }

  /**
   * Set if bot requests should be tracked
   *
   * @return true if bot requests should be tracked
   */
  @Nullable
  public Boolean getTrackBotRequests() {
    return getBooleanParameter(TRACK_BOT_REQUESTS);
  }

  /**
   * By default Matomo does not track bots. If you use the Tracking Java API,
   * you may be interested in tracking bot requests. To enable Bot Tracking in
   * Matomo, set <em>Track Bot Requests</em> to true.
   *
   * @param trackBotRequests true if bot requests should be tracked
   */
  public void setTrackBotRequests(Boolean trackBotRequests) {
    setBooleanParameter(TRACK_BOT_REQUESTS, trackBotRequests);
  }

  /**
   * Get the visit custom variable at the specified key.
   *
   * @param key the key of the variable to get
   * @return the variable at the specified key, null if key is not present
   * @deprecated Use the {@link #getVisitCustomVariable(int)} method instead.
   */
  @Nullable
  @Deprecated
  public String getUserCustomVariable(String key) {
    return getCustomVariable(VISIT_CUSTOM_VARIABLE, key);
  }

  /**
   * Get the visit custom variable at the specified index.
   *
   * @param index the index of the variable to get
   * @return the variable at the specified index, null if nothing at this index
   */
  @Nullable
  public CustomVariable getVisitCustomVariable(int index) {
    return getCustomVariable(VISIT_CUSTOM_VARIABLE, index);
  }

  /**
   * Set a visit custom variable with the specified key and value at the first available index.
   * All visit custom variables with this key will be overwritten or deleted
   *
   * @param key   the key of the variable to set
   * @param value the value of the variable to set at the specified key.  A null value will remove this parameter
   * @deprecated Use the {@link #setVisitCustomVariable(CustomVariable, int)} method instead.
   */
  @Deprecated
  public void setUserCustomVariable(String key, String value) {
    if (value == null) {
      removeCustomVariable(VISIT_CUSTOM_VARIABLE, key);
    } else {
      setCustomVariable(VISIT_CUSTOM_VARIABLE, new CustomVariable(key, value), null);
    }
  }

  /**
   * Set a user custom variable at the specified key.
   *
   * @param customVariable the CustomVariable to set.  A null value will remove the custom variable at the specified index
   * @param index          the index to set the customVariable at.
   */
  public void setVisitCustomVariable(CustomVariable customVariable, int index) {
    setCustomVariable(VISIT_CUSTOM_VARIABLE, customVariable, index);
  }

  /**
   * Get the user id for this request.
   *
   * @return the user id
   */
  @Nullable
  public String getUserId() {
    return castOrNull(USER_ID);
  }

  /**
   * Set the <a href="http://matomo.org/docs/user-id/">user id</a> for this request.
   * User id is any non empty unique string identifying the user (such as an email
   * address or a username). To access this value, users must be logged-in in your
   * system so you can fetch this user id from your system, and pass it to Matomo.
   * The user id appears in the visitor log, the Visitor profile, and you can
   * <a href="http://developer.matomo.org/api-reference/segmentation">Segment</a>
   * reports for one or several user ids. When specified, the user id will be
   * "enforced". This means that if there is no recent visit with this user id,
   * a new one will be created. If a visit is found in the last 30 minutes with
   * your specified user id, then the new action will be recorded to this existing visit.
   *
   * @param userId the user id to set.  A null value will remove this parameter
   */
  public void setUserId(String userId) {
    setNonEmptyStringParameter(USER_ID, userId);
  }

  /**
   * Get the visitor's city.
   *
   * @return the visitor's city
   */
  @Nullable
  public String getVisitorCity() {
    return castOrNull(VISITOR_CITY);
  }

  /**
   * Set an override value for the city. The name of the city the visitor is
   * located in, eg, Tokyo.  AuthToken must first be set.
   *
   * @param city the visitor's city to set.  A null value will remove this parameter
   */
  public void setVisitorCity(String city) {
    if (city != null) {
      verifyAuthTokenSet();
    }
    setParameter(VISITOR_CITY, city);
  }

  /**
   * Get the visitor's country.
   *
   * @return the visitor's country
   */
  @Nullable
  public MatomoLocale getVisitorCountry() {
    return castOrNull(VISITOR_COUNTRY);
  }

  /**
   * Set an override value for the country.  AuthToken must first be set.
   *
   * @param country the visitor's country to set.  A null value will remove this parameter
   */
  public void setVisitorCountry(MatomoLocale country) {
    if (country != null) {
      verifyAuthTokenSet();
    }
    setParameter(VISITOR_COUNTRY, country);
  }

  /**
   * Get the visitor's custom id.
   *
   * @return the visitor's custom id
   */
  @Nullable
  public String getVisitorCustomId() {
    return castOrNull(VISITOR_CUSTOM_ID);
  }

  /**
   * Set a custom visitor ID for this request. You must set this value to exactly
   * a {@value #ID_LENGTH} character hexadecimal string (containing only characters 01234567890abcdefABCDEF).
   * We recommended to set the UserId rather than the VisitorCustomId.
   *
   * @param visitorCustomId the visitor's custom id to set.  A null value will remove this parameter
   */
  public void setVisitorCustomId(String visitorCustomId) {
    if (visitorCustomId != null) {
      if (visitorCustomId.length() != ID_LENGTH) {
        throw new IllegalArgumentException(visitorCustomId + " is not " + ID_LENGTH + " characters long.");
      }
      // Verify visitorID is a 16 character hexadecimal string
      if (!VISITOR_ID_PATTERN.matcher(visitorCustomId).matches()) {
        throw new IllegalArgumentException(visitorCustomId + " is not a hexadecimal string.");
      }
    }
    setParameter(VISITOR_CUSTOM_ID, visitorCustomId);
  }

  /**
   * Get the timestamp of the visitor's first visit.
   *
   * @return the timestamp of the visitor's first visit
   */
  @Nullable
  public Long getVisitorFirstVisitTimestamp() {
    return castOrNull(VISITOR_FIRST_VISIT_TIMESTAMP);
  }

  /**
   * Set the UNIX timestamp of this visitor's first visit. This could be set
   * to the date where the user first started using your software/app, or when
   * he/she created an account. This parameter is used to populate the
   * <em>Goals &gt; Days to Conversion</em> report.
   *
   * @param timestamp the timestamp of the visitor's first visit to set.  A null value will remove this parameter
   */
  public void setVisitorFirstVisitTimestamp(Long timestamp) {
    setParameter(VISITOR_FIRST_VISIT_TIMESTAMP, timestamp);
  }

  /**
   * Get the visitor's id.
   *
   * @return the visitor's id
   */
  @Nullable
  public String getVisitorId() {
    return castOrNull(VISITOR_ID);
  }

  /**
   * Set the unique visitor ID, must be a {@value #ID_LENGTH} characters hexadecimal string.
   * Every unique visitor must be assigned a different ID and this ID must not
   * change after it is assigned. If this value is not set Matomo will still
   * track visits, but the unique visitors metric might be less accurate.
   *
   * @param visitorId the visitor id to set.  A null value will remove this parameter
   */
  public void setVisitorId(String visitorId) {
    if (visitorId != null) {
      if (visitorId.length() != ID_LENGTH) {
        throw new IllegalArgumentException(visitorId + " is not " + ID_LENGTH + " characters long.");
      }
      // Verify visitorID is a 16 character hexadecimal string
      if (!VISITOR_ID_PATTERN.matcher(visitorId).matches()) {
        throw new IllegalArgumentException(visitorId + " is not a hexadecimal string.");
      }
    }
    setParameter(VISITOR_ID, visitorId);
  }

  /**
   * Get the visitor's ip.
   *
   * @return the visitor's ip
   */
  @Nullable
  public String getVisitorIp() {
    return castOrNull(VISITOR_IP);
  }

  /**
   * Set the override value for the visitor IP (both IPv4 and IPv6 notations
   * supported).  AuthToken must first be set.
   *
   * @param visitorIp the visitor's ip to set.  A null value will remove this parameter
   */
  public void setVisitorIp(String visitorIp) {
    if (visitorIp != null) {
      verifyAuthTokenSet();
    }
    setParameter(VISITOR_IP, visitorIp);
  }

  /**
   * Get the visitor's latitude.
   *
   * @return the visitor's latitude
   */
  @Nullable
  public Double getVisitorLatitude() {
    return castOrNull(VISITOR_LATITUDE);
  }

  /**
   * Set an override value for the visitor's latitude, eg 22.456.  AuthToken
   * must first be set.
   *
   * @param latitude the visitor's latitude to set.  A null value will remove this parameter
   */
  public void setVisitorLatitude(Double latitude) {
    if (latitude != null) {
      verifyAuthTokenSet();
    }
    setParameter(VISITOR_LATITUDE, latitude);
  }

  /**
   * Get the visitor's longitude.
   *
   * @return the visitor's longitude
   */
  @Nullable
  public Double getVisitorLongitude() {
    return castOrNull(VISITOR_LONGITUDE);
  }

  /**
   * Set an override value for the visitor's longitude, eg 22.456.  AuthToken
   * must first be set.
   *
   * @param longitude the visitor's longitude to set.  A null value will remove this parameter
   */
  public void setVisitorLongitude(Double longitude) {
    if (longitude != null) {
      verifyAuthTokenSet();
    }
    setParameter(VISITOR_LONGITUDE, longitude);
  }

  /**
   * Get the timestamp of the visitor's previous visit.
   *
   * @return the timestamp of the visitor's previous visit
   */
  @Nullable
  public Long getVisitorPreviousVisitTimestamp() {
    return castOrNull(VISITOR_PREVIOUS_VISIT_TIMESTAMP);
  }

  /**
   * Set the UNIX timestamp of this visitor's previous visit. This parameter
   * is used to populate the report
   * <em>Visitors &gt; Engagement &gt; Visits</em> by days since last visit.
   *
   * @param timestamp the timestamp of the visitor's previous visit to set.  A null value will remove this parameter
   */
  public void setVisitorPreviousVisitTimestamp(Long timestamp) {
    setParameter(VISITOR_PREVIOUS_VISIT_TIMESTAMP, timestamp);
  }

  /**
   * Get the visitor's region.
   *
   * @return the visitor's region
   */
  @Nullable
  public String getVisitorRegion() {
    return castOrNull(VISITOR_REGION);
  }

  /**
   * Set an override value for the region. Should be set to the two letter
   * region code as defined by
   * <a href="http://www.maxmind.com/?rId=matomo">MaxMind's</a> GeoIP databases.
   * See <a href="http://dev.maxmind.com/static/maxmind-region-codes.csv">here</a>
   * for a list of them for every country (the region codes are located in the
   * second column, to the left of the region name and to the right of the country
   * code).
   *
   * @param region the visitor's region to set.  A null value will remove this parameter
   */
  public void setVisitorRegion(String region) {
    if (region != null) {
      verifyAuthTokenSet();
    }
    setParameter(VISITOR_REGION, region);
  }

  /**
   * Get the count of visits for this visitor.
   *
   * @return the count of visits for this visitor
   */
  @Nullable
  public Integer getVisitorVisitCount() {
    return castOrNull(VISITOR_VISIT_COUNT);
  }

  /**
   * Set the current count of visits for this visitor. To set this value correctly,
   * it would be required to store the value for each visitor in your application
   * (using sessions or persisting in a database). Then you would manually increment
   * the counts by one on each new visit or "session", depending on how you choose
   * to define a visit. This value is used to populate the report
   * <em>Visitors &gt; Engagement &gt; Visits by visit number</em>.
   *
   * @param visitorVisitCount the count of visits for this visitor to set.  A null value will remove this parameter
   */
  public void setVisitorVisitCount(Integer visitorVisitCount) {
    setParameter(VISITOR_VISIT_COUNT, visitorVisitCount);
  }

  public Map<String, Collection<Object>> getParameters() {
    return parameters.asMap();
  }

  /**
   * Get the query string represented by this object.
   *
   * @return the query string represented by this object
   * @deprecated Use {@link URIBuilder} in conjunction with {@link #getParameters()} and {@link QueryParameters#fromMap(Map)} ()} instead
   */
  @Nonnull
  @Deprecated
  public String getQueryString() {
    return parameters.entries().stream().map(parameter -> parameter.getKey() + '=' + parameter.getValue().toString()).collect(Collectors.joining("&"));
  }

  /**
   * Get the url encoded query string represented by this object.
   *
   * @return the url encoded query string represented by this object
   * @deprecated Use {@link URIBuilder} in conjunction with {@link #getParameters()} and {@link QueryParameters#fromMap(Map)} ()} instead
   */
  @Nonnull
  @Deprecated
  public String getUrlEncodedQueryString() {
    String queryString = new URIBuilder().setParameters(QueryParameters.fromMap(getParameters())).toString();
    if (queryString.isEmpty()) {
      return "";
    }
    return queryString.substring(1);
  }

  /**
   * Get a random hexadecimal string of a specified length.
   *
   * @param length length of the string to produce
   * @return a random string consisting only of hexadecimal characters
   */
  @Nonnull
  public static String getRandomHexString(int length) {
    byte[] bytes = new byte[length / 2];
    new SecureRandom().nextBytes(bytes);
    return BaseEncoding.base16().lowerCase().encode(bytes);
  }

  /**
   * Set a stored parameter.
   *
   * @param key   the parameter's key
   * @param value the parameter's value.  Removes the parameter if null
   */
  public void setParameter(@NonNull String key, @Nullable Object value) {
    parameters.removeAll(key);
    if (value != null) {
      addParameter(key, value);
    }
  }

  /**
   * Add more values to the given parameter
   *
   * @param key   the parameter's key. Must not be null
   * @param value the parameter's value. Must not be null
   */
  public void addParameter(@NonNull String key, @NonNull Object value) {
    parameters.put(key, value);
  }


  /**
   * Get a stored parameter that and cast it if present
   *
   * @param key the parameter's key. Must not be null
   * @return the stored parameter's value casted to the requested type or null if no value is present
   */
  @Nullable
  private <T> T castOrNull(@NonNull String key) {
    Collection<Object> values = parameters.get(key);
    if (values.isEmpty()) {
      return null;
    }
    return (T) values.iterator().next();
  }

  /**
   * Set a stored parameter and verify it is a non-empty string.
   *
   * @param key   the parameter's key
   * @param value the parameter's value.  Cannot be the empty.  Removes the parameter if null
   *              string
   */
  private void setNonEmptyStringParameter(@NonNull String key, String value) {
    if (value != null && value.trim().isEmpty()) {
      throw new IllegalArgumentException("Value cannot be empty.");
    }
    setParameter(key, value);
  }

  /**
   * Get a stored parameter that is a boolean.
   *
   * @param key the parameter's key
   * @return the stored parameter's value
   */
  @Nullable
  private Boolean getBooleanParameter(@NonNull String key) {
    MatomoBoolean matomoBoolean = castOrNull(key);
    if (matomoBoolean == null) {
      return null;
    }
    return matomoBoolean.isValue();
  }

  /**
   * Set a stored parameter that is a boolean.
   *
   * @param key   the parameter's key
   * @param value the parameter's value.  Removes the parameter if null
   */
  private void setBooleanParameter(@NonNull String key, @Nullable Boolean value) {
    if (value == null) {
      setParameter(key, null);
    } else {
      setParameter(key, new MatomoBoolean(value));
    }
  }

  /**
   * Get a value that is stored in a json object at the specified parameter.
   *
   * @param parameter the parameter to retrieve the json object from
   * @param index     the index of the value.
   * @return the value at the specified index
   */
  @Nullable
  private CustomVariable getCustomVariable(@NonNull String parameter, int index) {
    CustomVariables customVariables = castOrNull(parameter);
    if (customVariables == null) {
      return null;
    }
    return customVariables.get(index);
  }

  @Nullable
  private String getCustomVariable(@NonNull String parameter, @NonNull String key) {
    CustomVariables customVariables = castOrNull(parameter);
    if (customVariables == null) {
      return null;
    }
    return customVariables.get(key);
  }

  /**
   * Store a value in a json object at the specified parameter.
   *
   * @param parameter      the parameter to store the json object at
   * @param parameter      the key of the value.  Cannot be null
   * @param customVariable the value.  Removes the parameter if null
   */
  private void setCustomVariable(@NonNull String parameter, @Nullable CustomVariable customVariable, Integer index) {

    if (customVariable == null && index == null) {
      throw new IllegalArgumentException("Either custom variable or index must be set");
    }
    CustomVariables customVariables = castOrNull(parameter);
    if (customVariables == null) {
      customVariables = new CustomVariables();
      setParameter(parameter, customVariables);
    }
    if (customVariable == null) {
      customVariables.remove(index);
      if (customVariables.isEmpty()) {
        setParameter(parameter, null);
      }
    } else if (index == null) {
      customVariables.add(customVariable);
    } else {
      customVariables.add(customVariable, index);
    }
  }

  private void removeCustomVariable(@NonNull String parameter, @NonNull String key) {
    CustomVariables customVariables = castOrNull(parameter);
    if (customVariables != null) {
      customVariables.remove(key);
      if (customVariables.isEmpty()) {
        setParameter(parameter, null);
      }
    }
  }

}
