/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import static java.util.Collections.singleton;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;
import org.matomo.java.tracking.parameters.AcceptLanguage;
import org.matomo.java.tracking.parameters.Country;
import org.matomo.java.tracking.parameters.CustomVariable;
import org.matomo.java.tracking.parameters.CustomVariables;
import org.matomo.java.tracking.parameters.DeviceResolution;
import org.matomo.java.tracking.parameters.EcommerceItem;
import org.matomo.java.tracking.parameters.EcommerceItems;
import org.matomo.java.tracking.parameters.RandomValue;
import org.matomo.java.tracking.parameters.UniqueId;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * A class that implements the <a href="https://developer.matomo.org/api-reference/tracking-api">
 * Matomo Tracking HTTP API</a>.  These requests can be sent using {@link MatomoTracker}.
 *
 * @author brettcsorba
 */
@Builder(builderMethodName = "request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MatomoRequest {

  /**
   * The ID of the website we're tracking a visit/action for. Only needed, if no default site id is
   * configured
   */
  @TrackingParameter(name = "rec")
  @Default
  private Boolean required = true;

  /**
   * The ID of the website we're tracking a visit/action for. Only needed, if no default site id is
   * configured
   */
  @TrackingParameter(name = "idsite")
  private Integer siteId;

  /**
   * The title of the action being tracked. For page tracks this is used as page title. If enabled
   * in your installation you may use the category tree structure in this field. For example, "game
   * / register new user" would then create a group "game" and add the item "register new user" in
   * it.
   */
  @TrackingParameter(name = "action_name")
  private String actionName;

  /**
   * The full URL for the current action.
   */
  @TrackingParameter(name = "url")
  private String actionUrl;

  /**
   * Defines the API version to use (default: 1).
   */
  @TrackingParameter(name = "apiv")
  @Default
  private String apiVersion = "1";

  /**
   * The unique visitor ID. See {@link VisitorId}. Default is {@link VisitorId#random()}
   *
   * <p>Since version 3.0.0 this parameter is of type {@link VisitorId} and not a String anymore.
   * Use {@link VisitorId#fromHex(String)} to create a VisitorId from a hex string,
   * {@link VisitorId#fromUUID(UUID)} to create it from a UUID or {@link VisitorId#fromHash(long)}
   * to create it from a long value.
   */
  @TrackingParameter(name = "_id")
  @Default
  private VisitorId visitorId = VisitorId.random();

  /**
   * Tracks if the visitor is a returning visitor.
   *
   * <p>This is done by storing a visitor ID in a 1st party cookie.
   */
  @TrackingParameter(name = "_idn")
  private Boolean newVisitor;

  /**
   * The full HTTP Referrer URL. This value is used to determine how someone got to your website
   * (ie, through a website, search engine or campaign)
   */
  @TrackingParameter(name = "urlref")
  private String referrerUrl;

  /**
   * Custom variables are custom name-value pairs that you can assign to your visitors (or page
   * views).
   */
  @TrackingParameter(name = "_cvar")
  private CustomVariables visitCustomVariables;

  /**
   * The current count of visits for this visitor. To set this value correctly, it would be required
   * to store the value for each visitor in your application (using sessions or persisting in a
   * database). Then you would manually increment the counts by one on each new visit or "session",
   * depending on how you choose to define a visit.
   */
  @TrackingParameter(name = "_idvc")
  private Integer visitorVisitCount;

  /**
   * The UNIX timestamp of this visitor's previous visit. This parameter is used to populate the
   * report Visitors > Engagement > Visits by days since last visit.
   */
  @TrackingParameter(name = "_viewts")
  private Instant visitorPreviousVisitTimestamp;

  /**
   * The UNIX timestamp of this visitor's first visit. This could be set to the date where the user
   * first started using your software/app, or when he/she created an account.
   */
  @TrackingParameter(name = "_idts")
  private Instant visitorFirstVisitTimestamp;

  /**
   * The campaign name. This parameter will only be used for the first pageview of a visit.
   */
  @TrackingParameter(name = "_rcn")
  private String campaignName;

  /**
   * The campaign keyword (see
   * <a href="https://matomo.org/docs/tracking-campaigns/">Tracking Campaigns</a>). Used to
   * populate
   * the <em>Referrers &gt; Campaigns</em> report (clicking on a campaign loads all keywords for
   * this campaign). This parameter will only be used for the first pageview of a visit.
   */
  @TrackingParameter(name = "_rck")
  private String campaignKeyword;

  /**
   * The resolution of the device the visitor is using.
   */
  @TrackingParameter(name = "res")
  private DeviceResolution deviceResolution;

  /**
   * The current hour (local time).
   */
  @TrackingParameter(name = "h")
  private Integer currentHour;

  /**
   * The current minute (local time).
   */
  @TrackingParameter(name = "m")
  private Integer currentMinute;

  /**
   * The current second (local time).
   */
  @TrackingParameter(name = "s")
  private Integer currentSecond;

  /**
   * Does the visitor use the Adobe Flash Plugin.
   */
  @TrackingParameter(name = "fla")
  private Boolean pluginFlash;

  /**
   * Does the visitor use the Java plugin.
   */
  @TrackingParameter(name = "java")
  private Boolean pluginJava;

  /**
   * Does the visitor use Director plugin.
   */
  @TrackingParameter(name = "dir")
  private Boolean pluginDirector;

  /**
   * Does the visitor use Quicktime plugin.
   */
  @TrackingParameter(name = "qt")
  private Boolean pluginQuicktime;

  /**
   * Does the visitor use Realplayer plugin.
   */
  @TrackingParameter(name = "realp")
  private Boolean pluginRealPlayer;

  /**
   * Does the visitor use a PDF plugin.
   */
  @TrackingParameter(name = "pdf")
  private Boolean pluginPDF;

  /**
   * Does the visitor use a Windows Media plugin.
   */
  @TrackingParameter(name = "wma")
  private Boolean pluginWindowsMedia;

  /**
   * Does the visitor use a Gears plugin.
   */
  @TrackingParameter(name = "gears")
  private Boolean pluginGears;

  /**
   * Does the visitor use a Silverlight plugin.
   */
  @TrackingParameter(name = "ag")
  private Boolean pluginSilverlight;

  /**
   * Does the visitor's client is known to support cookies.
   */
  @TrackingParameter(name = "cookie")
  private Boolean supportsCookies;

  /**
   * An override value for the User-Agent HTTP header field.
   */
  @TrackingParameter(name = "ua")
  private String headerUserAgent;

  /**
   * An override value for the Accept-Language HTTP header field. This value is used to detect the
   * visitor's country if GeoIP is not enabled.
   */
  @TrackingParameter(name = "lang")
  private AcceptLanguage headerAcceptLanguage;

  /**
   * Defines the User ID for this request. User ID is any non-empty unique string identifying the
   * user (such as an email address or a username). When specified, the User ID will be "enforced".
   * This means that if there is no recent visit with this User ID, a new one will be created. If a
   * visit is found in the last 30 minutes with your specified User ID, then the new action will be
   * recorded to this existing visit.
   */
  @TrackingParameter(name = "uid")
  private String userId;

  /**
   * defines the visitor ID for this request.
   */
  @TrackingParameter(name = "cid")
  private VisitorId visitorCustomId;

  /**
   * will force a new visit to be created for this action.
   */
  @TrackingParameter(name = "new_visit")
  private Boolean newVisit;

  /**
   * Custom variables are custom name-value pairs that you can assign to your visitors (or page
   * views).
   */
  @TrackingParameter(name = "cvar")
  private CustomVariables pageCustomVariables;

  /**
   * An external URL the user has opened. Used for tracking outlink clicks. We recommend to also set
   * the url parameter to this same value.
   */
  @TrackingParameter(name = "link")
  private String outlinkUrl;

  /**
   * URL of a file the user has downloaded. Used for tracking downloads. We recommend to also set
   * the url parameter to this same value.
   */
  @TrackingParameter(name = "download")
  private String downloadUrl;

  /**
   * The Site Search keyword. When specified, the request will not be tracked as a normal pageview
   * but will instead be tracked as a Site Search request
   */
  @TrackingParameter(name = "search")
  private String searchQuery;

  /**
   * When search is specified, you can optionally specify a search category with this parameter.
   */
  @TrackingParameter(name = "search_cat")
  private String searchCategory;

  /**
   * When search is specified, we also recommend setting the search_count to the number of search
   * results displayed on the results page. When keywords are tracked with &search_count=0 they will
   * appear in the "No Result Search Keyword" report.
   */
  @TrackingParameter(name = "search_count")
  private Long searchResultsCount;

  /**
   * Accepts a six character unique ID that identifies which actions were performed on a specific
   * page view. When a page was viewed, all following tracking requests (such as events) during that
   * page view should use the same pageview ID. Once another page was viewed a new unique ID should
   * be generated. Use [0-9a-Z] as possible characters for the unique ID.
   */
  @TrackingParameter(name = "pv_id")
  private UniqueId pageViewId;

  /**
   * If specified, the tracking request will trigger a conversion for the goal of the website being
   * tracked with this ID.
   */
  @TrackingParameter(name = "idgoal")
  private Integer goalId;

  /**
   * The grand total for the ecommerce order (required when tracking an ecommerce order).
   */
  @TrackingParameter(name = "revenue")
  private Double ecommerceRevenue;

  /**
   * The charset of the page being tracked. Specify the charset if the data you send to Matomo is
   * encoded in a different character set than the default utf-8
   */
  @TrackingParameter(name = "cs")
  private Charset characterSet;

  /**
   * can be optionally sent along any tracking request that isn't a page view. For example, it can
   * be sent together with an event tracking request. The advantage being that should you ever
   * disable the event plugin, then the event tracking requests will be ignored vs if the parameter
   * is not set, a page view would be tracked even though it isn't a page view.
   */
  @TrackingParameter(name = "ca")
  private Boolean customAction;

  /**
   * How long it took to connect to server.
   */
  @TrackingParameter(name = "pf_net")
  private Long networkTime;

  /**
   * How long it took the server to generate page.
   */
  @TrackingParameter(name = "pf_srv")
  private Long serverTime;

  /**
   * How long it takes the browser to download the response from the server.
   */
  @TrackingParameter(name = "pf_tfr")
  private Long transferTime;

  /**
   * How long the browser spends loading the webpage after the response was fully received until the
   * user can start interacting with it.
   */
  @TrackingParameter(name = "pf_dm1")
  private Long domProcessingTime;

  /**
   * How long it takes for the browser to load media and execute any Javascript code listening for
   * the DOMContentLoaded event.
   */
  @TrackingParameter(name = "pf_dm2")
  private Long domCompletionTime;

  /**
   * How long it takes the browser to execute Javascript code waiting for the window.load event.
   */
  @TrackingParameter(name = "pf_onl")
  private Long onloadTime;

  /**
   * eg. Videos, Music, Games...
   */
  @TrackingParameter(name = "e_c")
  private String eventCategory;

  /**
   * An event action like Play, Pause, Duration, Add Playlist, Downloaded, Clicked...
   */
  @TrackingParameter(name = "e_a")
  private String eventAction;

  /**
   * The event name for example a Movie name, or Song name, or File name...
   */
  @TrackingParameter(name = "e_n")
  private String eventName;

  /**
   * Some numeric value that represents the event value.
   */
  @TrackingParameter(name = "e_n")
  private Double eventValue;

  /**
   * The name of the content. For instance 'Ad Foo Bar'
   */
  @TrackingParameter(name = "c_n")
  private String contentName;

  /**
   * The actual content piece. For instance the path to an image, video, audio, any text
   */
  @TrackingParameter(name = "c_p")
  private String contentPiece;

  /**
   * The target of the content. For instance the URL of a landing page
   */
  @TrackingParameter(name = "c_t")
  private String contentTarget;

  /**
   * The name of the interaction with the content. For instance a 'click'
   */
  @TrackingParameter(name = "c_i")
  private String contentInteraction;

  /**
   * The unique string identifier for the ecommerce order (required when tracking an ecommerce
   * order). you must set &idgoal=0 in the request to track an ecommerce interaction: cart update or
   * an ecommerce order.
   */
  @TrackingParameter(name = "ec_id")
  private String ecommerceId;

  /**
   * Items in the Ecommerce order.
   */
  @TrackingParameter(name = "ec_items")
  private EcommerceItems ecommerceItems;

  /**
   * The subtotal of the order; excludes shipping.
   */
  @TrackingParameter(name = "ec_st")
  private Double ecommerceSubtotal;

  /**
   * Tax amount of the order.
   */
  @TrackingParameter(name = "ec_tx")
  private Double ecommerceTax;

  /**
   * Shipping cost of the order.
   */
  @TrackingParameter(name = "ec_sh")
  private Double ecommerceShippingCost;

  /**
   * Discount offered.
   */
  @TrackingParameter(name = "ec_dt")
  private Double ecommerceDiscount;

  /**
   * The UNIX timestamp of this customer's last ecommerce order. This value is used to process the
   * "Days since last order" report.
   */
  @TrackingParameter(name = "_ects")
  private Instant ecommerceLastOrderTimestamp;

  /**
   * 32 character authorization key used to authenticate the API request. We recommend to create a
   * user specifically for accessing the Tracking API, and give the user only write permission on
   * the website(s).
   */
  @TrackingParameter(
      name = "token_auth",
      regex = "[a-z0-9]{32}"
  )
  private String authToken;


  /**
   * Override value for the visitor IP (both IPv4 and IPv6 notations supported).
   */
  @TrackingParameter(name = "cip")
  private String visitorIp;

  /**
   * Override for the datetime of the request (normally the current time is used). This can be used
   * to record visits and page views in the past.
   */
  @TrackingParameter(name = "cdt")
  private Instant requestTimestamp;

  /**
   * An override value for the country. Must be a two-letter ISO 3166 Alpha-2 country code.
   */
  @TrackingParameter(
      name = "country",
      maxLength = 2
  )
  private Country visitorCountry;

  /**
   * An override value for the region. Should be set to a ISO 3166-2 region code, which are used by
   * MaxMind's and DB-IP's GeoIP2 databases. See here for a list of them for every country.
   */
  @TrackingParameter(
      name = "region",
      maxLength = 2
  )
  private String visitorRegion;

  /**
   * An override value for the city. The name of the city the visitor is located in, eg, Tokyo.
   */
  @TrackingParameter(name = "city")
  private String visitorCity;

  /**
   * An override value for the visitor's latitude, eg 22.456.
   */
  @TrackingParameter(name = "lat")
  private Double visitorLatitude;

  /**
   * An override value for the visitor's longitude, eg 22.456.
   */
  @TrackingParameter(name = "long")
  private Double visitorLongitude;

  /**
   * When set to false, the queued tracking handler won't be used and instead the tracking request
   * will be executed directly. This can be useful when you need to debug a tracking problem or want
   * to test that the tracking works in general.
   */
  @TrackingParameter(name = "queuedtracking")
  private Boolean queuedTracking;

  /**
   * If set to 0 (send_image=0) Matomo will respond with an HTTP 204 response code instead of a GIF
   * image. This improves performance and can fix errors if images are not allowed to be obtained
   * directly (like Chrome Apps). Available since Matomo 2.10.0
   *
   * <p>Default is {@code false}
   */
  @TrackingParameter(name = "send_image")
  @Default
  private Boolean responseAsImage = false;

  /**
   * If set to true, the request will be a Heartbeat request which will not track any new activity
   * (such as a new visit, new action or new goal). The heartbeat request will only update the
   * visit's total time to provide accurate "Visit duration" metric when this parameter is set. It
   * won't record any other data. This means by sending an additional tracking request when the user
   * leaves your site or app with &ping=1, you fix the issue where the time spent of the last page
   * visited is reported as 0 seconds.
   */
  @TrackingParameter(name = "ping")
  private Boolean ping;

  /**
   * By default, Matomo does not track bots. If you use the Tracking HTTP API directly, you may be
   * interested in tracking bot requests.
   */
  @TrackingParameter(name = "bots")
  private Boolean trackBotRequests;


  /**
   * Meant to hold a random value that is generated before each request. Using it helps avoid the
   * tracking request being cached by the browser or a proxy.
   */
  @TrackingParameter(name = "rand")
  @Default
  private RandomValue randomValue = RandomValue.random();

  /**
   * Meant to hold a random value that is generated before each request. Using it helps avoid the
   * tracking request being cached by the browser or a proxy.
   */
  @TrackingParameter(name = "debug")
  private Boolean debug;

  /**
   * Contains an error message describing the error that occurred during the last tracking request.
   *
   * <p>Custom action must be enabled for this.
   *
   * <p>Required for crash analytics
   */
  @TrackingParameter(name = "cra")
  private String crashMessage;

  /**
   * The type of exception that occurred during the last tracking request.
   *
   * <p>Custom action must be enabled for this.
   *
   * <p>Typically a fully qualified class name of the exception, e.g.
   * {@code java.lang.NullPointerException}.
   *
   * <p>Optional for crash analytics
   */
  @TrackingParameter(name = "cra_tp")
  private String crashType;

  /**
   * Category of a crash to group crashes by.
   *
   * <p>Custom action must be enabled for this.
   *
   * <p>Optional for crash analytics
   */
  @TrackingParameter(name = "cra_ct")
  private String crashCategory;

  /**
   * A stack trace of the exception that occurred during the last tracking request.
   *
   * <p>Custom action must be enabled for this.
   *
   * <p>Optional for crash analytics
   */
  @TrackingParameter(name = "cra_st")
  private String crashStackTrace;

  /**
   * The originating source of the crash.
   *
   * <p>Could be a source file URI or something similar
   *
   * <p>Custom action must be enabled for this.
   *
   * <p>Optional for crash analytics
   */
  @TrackingParameter(name = "cra_ru")
  private String crashLocation;

  /**
   * The line number of the crash source, where the crash occurred.
   *
   * <p>Custom action must be enabled for this.
   *
   * <p>Optional for crash analytics
   */
  @TrackingParameter(name = "cra_rl")
  private Integer crashLine;

  /**
   * The column within the line where the crash occurred.
   *
   * <p>Optional for crash analytics
   */
  @TrackingParameter(name = "cra_rc")
  private Integer crashColumn;

  /**
   * The Matomo session ID sent as a cookie {@code MATOMO_SESSID}.
   *
   * <p>If not null a cookie with the name {@code MATOMO_SESSID} will be sent with the value of
   * this
   * parameter.
   */
  private String sessionId;

  /**
   * Custom Dimension values for specific Custom Dimension IDs.
   *
   * <p><a href="https://plugins.matomo.org/CustomDimensions">Custom Dimensions plugin</a> must be
   * installed. See the
   * <a href="https://matomo.org/docs/custom-dimensions/">Custom Dimensions guide</a>. Requires
   * Matomo at least 2.15.1
   */
  private Map<Long, Object> dimensions;

  /**
   * Allows you to specify additional HTTP request parameters that will be sent to Matomo.
   *
   * <p>For example, you can use this to set the <em>Accept-Language</em> header, or to set the
   * <em>Content-Type</em>.
   */
  private Map<String, Collection<Object>> additionalParameters;

  /**
   * You can set additional HTTP headers for the request sent to Matomo.
   *
   * <p>For example, you can use this to set the <em>Accept-Language</em> header, or to set the
   * <em>Content-Type</em>.
   */
  private Map<String, String> headers;

  /**
   * Appends additional cookies to the request.
   *
   * <p>This allows you to add Matomo specific cookies, like {@code _pk_id} or {@code _pk_sess}
   * coming from Matomo responses to the request.
   */
  private Map<String, String> cookies;

  /**
   * Create a new request from the id of the site being tracked and the full url for the current
   * action.  This constructor also sets:
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
   *
   * @deprecated Please use {@link MatomoRequest#request()}
   */
  @Deprecated
  public MatomoRequest(int siteId, String actionUrl) {
    this.siteId = siteId;
    this.actionUrl = actionUrl;
    required = true;
    visitorId = VisitorId.random();
    randomValue = RandomValue.random();
    apiVersion = "1";
    responseAsImage = false;
  }

  /**
   * Gets the list of objects currently stored at the specified custom tracking parameter.  An empty
   * list will be returned if there are no objects set at that key.
   *
   * @param key the key of the parameter whose list of objects to get.  Cannot be null
   *
   * @return the list of objects currently stored at the specified key
   */
  public List<Object> getCustomTrackingParameter(@NonNull String key) {
    if (additionalParameters == null || additionalParameters.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<Object> parameterValues = additionalParameters.get(key);
    if (parameterValues == null || parameterValues.isEmpty()) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(new ArrayList<>(parameterValues));
  }

  /**
   * Set a custom tracking parameter whose toString() value will be sent to the Matomo server. These
   * parameters are stored separately from named Matomo parameters, meaning it is not possible to
   * overwrite or clear named Matomo parameters with this method. A custom parameter that has the
   * same name as a named Matomo parameter will be sent in addition to that named parameter.
   *
   * @param key   the parameter's key.  Cannot be null
   * @param value the parameter's value.  Removes the parameter if null
   *
   * @deprecated Use {@link MatomoRequest.MatomoRequestBuilder#additionalParameters(Map)} instead.
   */
  @Deprecated
  public void setCustomTrackingParameter(
      @NonNull String key, @Nullable Object value
  ) {

    if (value == null) {
      if (additionalParameters != null) {
        additionalParameters.remove(key);
      }
    } else {
      if (additionalParameters == null) {
        additionalParameters = new LinkedHashMap<>();
      }
      Collection<Object> values = additionalParameters.computeIfAbsent(key, k -> new ArrayList<>());
      values.clear();
      values.add(value);
    }
  }

  /**
   * Add a custom tracking parameter to the specified key.  This allows users to have multiple
   * parameters with the same name and different values, commonly used during situations where list
   * parameters are needed
   *
   * @param key   the parameter's key.  Cannot be null
   * @param value the parameter's value.  Cannot be null
   *
   * @deprecated Use {@link MatomoRequest.MatomoRequestBuilder#additionalParameters(Map)} instead.
   */
  @Deprecated
  public void addCustomTrackingParameter(@NonNull String key, @NonNull Object value) {
    if (additionalParameters == null) {
      additionalParameters = new LinkedHashMap<>();
    }
    additionalParameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
  }

  /**
   * Removes all custom tracking parameters.
   *
   * @deprecated Please use {@link MatomoRequest.MatomoRequestBuilder#additionalParameters(Map)}
   * instead so that you can manage the map yourself.
   */
  @Deprecated
  public void clearCustomTrackingParameter() {
    additionalParameters.clear();
  }

  /**
   * Sets <em>idgoal&#61;0</em> in the request to track an ecommerce interaction: cart update or an
   * ecommerce order.
   *
   * @deprecated Please use {@link MatomoRequest#setGoalId(Integer)} instead
   */
  @Deprecated
  public void enableEcommerce() {
    setGoalId(0);
  }

  /**
   * Get the {@link EcommerceItem} at the specified index.
   *
   * @param index the index of the {@link EcommerceItem} to return
   *
   * @return the {@link EcommerceItem} at the specified index
   * @deprecated Use @link {@link MatomoRequest.MatomoRequestBuilder#ecommerceItems(EcommerceItems)}
   * instead
   */
  @Nullable
  @Deprecated
  public EcommerceItem getEcommerceItem(int index) {
    if (ecommerceItems == null || ecommerceItems.isEmpty()) {
      return null;
    }
    return ecommerceItems.get(index);
  }

  /**
   * Add an {@link EcommerceItem} to this order.  Ecommerce must be enabled, and EcommerceId and
   * EcommerceRevenue must first be set.
   *
   * @param item the {@link EcommerceItem} to add.  Cannot be null
   *
   * @deprecated Use @link {@link MatomoRequest.MatomoRequestBuilder#ecommerceItems(EcommerceItems)}
   * instead
   */
  @Deprecated
  public void addEcommerceItem(@NonNull EcommerceItem item) {
    if (ecommerceItems == null) {
      ecommerceItems = new EcommerceItems();
    }
    ecommerceItems.add(item);
  }

  /**
   * Clears all {@link EcommerceItem} from this order.
   *
   * @deprecated Use @link {@link MatomoRequest.MatomoRequestBuilder#ecommerceItems(EcommerceItems)}
   * instead
   */
  @Deprecated
  public void clearEcommerceItems() {
    ecommerceItems.clear();
  }

  /**
   * Get the page custom variable at the specified key.
   *
   * @param key the key of the variable to get
   *
   * @return the variable at the specified key, null if key is not present
   * @deprecated Use the {@link #getPageCustomVariables()} method instead.
   */
  @Nullable
  @Deprecated
  public String getPageCustomVariable(String key) {
    if (pageCustomVariables == null) {
      return null;
    }
    return pageCustomVariables.get(key);
  }

  /**
   * Get the page custom variable at the specified index.
   *
   * @param index the index of the variable to get.  Must be greater than 0
   *
   * @return the variable at the specified key, null if nothing at this index
   * @deprecated Use {@link MatomoRequest#getPageCustomVariables()} instead
   */
  @Deprecated
  @Nullable
  public CustomVariable getPageCustomVariable(int index) {
    return getCustomVariable(pageCustomVariables, index);
  }

  @Nullable
  @Deprecated
  private static CustomVariable getCustomVariable(CustomVariables customVariables, int index) {
    if (customVariables == null) {
      return null;
    }
    return customVariables.get(index);
  }

  /**
   * Set a page custom variable with the specified key and value at the first available index. All
   * page custom variables with this key will be overwritten or deleted
   *
   * @param key   the key of the variable to set
   * @param value the value of the variable to set at the specified key.  A null value will remove
   *              this custom variable
   *
   * @deprecated Use {@link MatomoRequest#getPageCustomVariables()} instead
   */
  @Deprecated
  public void setPageCustomVariable(
      @NonNull String key, @Nullable String value
  ) {
    if (value == null) {
      if (pageCustomVariables == null) {
        return;
      }
      pageCustomVariables.remove(key);
    } else {
      CustomVariable variable = new CustomVariable(key, value);
      if (pageCustomVariables == null) {
        pageCustomVariables = new CustomVariables();
      }
      pageCustomVariables.add(variable);
    }
  }

  /**
   * Set a page custom variable at the specified index.
   *
   * @param customVariable the CustomVariable to set.  A null value will remove the CustomVariable
   *                       at the specified index
   * @param index          the index of he CustomVariable to set
   *
   * @deprecated Use {@link #getPageCustomVariables()} instead
   */
  @Deprecated
  public void setPageCustomVariable(
      @Nullable CustomVariable customVariable, int index
  ) {
    if (pageCustomVariables == null) {
      if (customVariable == null) {
        return;
      }
      pageCustomVariables = new CustomVariables();
    }
    setCustomVariable(pageCustomVariables, customVariable, index);
  }

  @Deprecated
  private static void setCustomVariable(
      CustomVariables customVariables, @Nullable CustomVariable customVariable, int index
  ) {
    if (customVariable == null) {
      customVariables.remove(index);
    } else {
      customVariables.add(customVariable, index);
    }
  }

  /**
   * Get the datetime of the request.
   *
   * @return the datetime of the request
   * @deprecated Use {@link #getRequestTimestamp()} instead
   */
  @Deprecated
  @Nullable
  public MatomoDate getRequestDatetime() {
    return requestTimestamp == null ? null : new MatomoDate(requestTimestamp.toEpochMilli());
  }

  /**
   * Set the datetime of the request (normally the current time is used). This can be used to record
   * visits and page views in the past. The datetime must be sent in UTC timezone. <em>Note: if you
   * record data in the past, you will need to <a href="https://matomo.org/faq/how-to/faq_59">force
   * Matomo to re-process reports for the past dates</a>.</em> If you set the <em>Request
   * Datetime</em> to a datetime older than four hours then <em>Auth Token</em> must be set. If you
   * set
   * <em>Request Datetime</em> with a datetime in the last four hours then you
   * don't need to pass <em>Auth Token</em>.
   *
   * @param matomoDate the datetime of the request to set.  A null value will remove this parameter
   *
   * @deprecated Use {@link #setRequestTimestamp(Instant)} instead
   */
  @Deprecated
  public void setRequestDatetime(MatomoDate matomoDate) {
    if (matomoDate == null) {
      requestTimestamp = null;
    } else {
      setRequestTimestamp(matomoDate.getZonedDateTime().toInstant());
    }
  }


  /**
   * Get the visit custom variable at the specified key.
   *
   * @param key the key of the variable to get
   *
   * @return the variable at the specified key, null if key is not present
   * @deprecated Use the {@link #getVisitCustomVariables()} method instead.
   */
  @Nullable
  @Deprecated
  public String getUserCustomVariable(String key) {
    if (visitCustomVariables == null) {
      return null;
    }
    return visitCustomVariables.get(key);
  }

  /**
   * Get the visit custom variable at the specified index.
   *
   * @param index the index of the variable to get
   *
   * @return the variable at the specified index, null if nothing at this index
   * @deprecated Use {@link #getVisitCustomVariables()} instead
   */
  @Nullable
  @Deprecated
  public CustomVariable getVisitCustomVariable(int index) {
    return getCustomVariable(visitCustomVariables, index);
  }

  /**
   * Set a visit custom variable with the specified key and value at the first available index. All
   * visit custom variables with this key will be overwritten or deleted
   *
   * @param key   the key of the variable to set
   * @param value the value of the variable to set at the specified key.  A null value will remove
   *              this parameter
   *
   * @deprecated Use {@link #setVisitCustomVariables(CustomVariables)} instead
   */
  @Deprecated
  public void setUserCustomVariable(
      @NonNull String key, @Nullable String value
  ) {
    if (value == null) {
      if (visitCustomVariables == null) {
        return;
      }
      visitCustomVariables.remove(key);
    } else {
      CustomVariable variable = new CustomVariable(key, value);
      if (visitCustomVariables == null) {
        visitCustomVariables = new CustomVariables();
      }
      visitCustomVariables.add(variable);
    }
  }

  /**
   * Set a user custom variable at the specified key.
   *
   * @param customVariable the CustomVariable to set.  A null value will remove the custom variable
   *                       at the specified index
   * @param index          the index to set the customVariable at.
   *
   * @deprecated Use {@link #setVisitCustomVariables(CustomVariables)} instead
   */
  @Deprecated
  public void setVisitCustomVariable(
      @Nullable CustomVariable customVariable, int index
  ) {
    if (visitCustomVariables == null) {
      if (customVariable == null) {
        return;
      }
      visitCustomVariables = new CustomVariables();
    }
    setCustomVariable(visitCustomVariables, customVariable, index);
  }

  /**
   * Sets a custom parameter to append to the Matomo tracking parameters.
   *
   * <p>Attention: If a parameter with the same name already exists, it will be appended twice!
   *
   * @param parameterName The name of the query parameter to append. Must not be null or empty.
   * @param value         The value of the query parameter to append. To remove the parameter, pass
   *                      null.
   *
   * @deprecated Use @link {@link MatomoRequest.MatomoRequestBuilder#additionalParameters(Map)}
   * instead
   */
  @Deprecated
  public void setParameter(@NonNull String parameterName, Object value) {
    if (parameterName.trim().isEmpty()) {
      throw new IllegalArgumentException("Parameter name must not be empty");
    }
    if (additionalParameters == null) {
      if (value == null) {
        return;
      }
      additionalParameters = new LinkedHashMap<>();
    }
    if (value == null) {
      additionalParameters.remove(parameterName);
    } else {
      additionalParameters.put(parameterName, singleton(value));
    }
  }

  /**
   * Creates a new {@link MatomoRequestBuilder} instance. Only here for backwards compatibility.
   *
   * @deprecated Use {@link MatomoRequest#request()} instead.
   */
  @Deprecated
  public static org.matomo.java.tracking.MatomoRequestBuilder builder() {
    return new org.matomo.java.tracking.MatomoRequestBuilder();
  }

  /**
   * Parses the given device resolution string and sets the {@link #deviceResolution} field.
   *
   * @param deviceResolution the device resolution string to parse. Format: "WIDTHxHEIGHT"
   *
   * @deprecated Use {@link #setDeviceResolution(DeviceResolution)} instead.
   */
  @Tolerate
  @Deprecated
  public void setDeviceResolution(@Nullable String deviceResolution) {
    if (deviceResolution == null || deviceResolution.trim().isEmpty()) {
      this.deviceResolution = null;
    } else {
      this.deviceResolution = DeviceResolution.fromString(deviceResolution);
    }
  }

}
