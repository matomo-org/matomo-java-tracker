package org.matomo.java.tracking;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class MatomoRequestBuilder {

  private int siteId;

  private String actionUrl;

  private String actionName;
  private Long actionTime;
  private String apiVersion;
  private String authToken;
  private String campaignKeyword;
  private String campaignName;
  private Charset characterSet;
  private String contentInteraction;
  private String contentName;
  private String contentPiece;
  private String contentTarget;
  private Integer currentHour;
  private Integer currentMinute;
  private Integer currentSecond;

  private Boolean customAction;
  private String deviceResolution;
  private String downloadUrl;
  private Double ecommerceDiscount;
  private String ecommerceId;
  private Long ecommerceLastOrderTimestamp;
  private Double ecommerceRevenue;
  private Double ecommerceShippingCost;
  private Double ecommerceSubtotal;
  private Double ecommerceTax;
  private String eventAction;
  private String eventCategory;
  private String eventName;
  private Number eventValue;
  private Integer goalId;
  private Double goalRevenue;
  private String headerAcceptLanguage;
  private String headerUserAgent;
  private Boolean newVisit;
  private String outlinkUrl;
  private Boolean pluginDirector;
  private Boolean pluginFlash;
  private Boolean pluginGears;
  private Boolean pluginJava;
  private Boolean pluginPDF;
  private Boolean pluginQuicktime;
  private Boolean pluginRealPlayer;
  private Boolean pluginSilverlight;
  private Boolean pluginWindowsMedia;
  private String randomValue;
  private String referrerUrl;
  private MatomoDate requestDatetime;
  private Boolean required;
  private Boolean responseAsImage;
  private String searchCategory;
  private String searchQuery;
  private Long searchResultsCount;
  private Boolean trackBotRequests;
  private String userId;
  private String visitorCity;
  private MatomoLocale visitorCountry;
  private String visitorCustomId;
  private Long visitorFirstVisitTimestamp;
  private String visitorId;
  private String visitorIp;
  private Double visitorLatitude;
  private Double visitorLongitude;
  private Long visitorPreviousVisitTimestamp;
  private String visitorRegion;
  private Integer visitorVisitCount;

  private List<CustomVariable> visitCustomVariables;

  private List<CustomVariable> pageCustomVariables;
  private Map<String, Object> customTrackingParameters;

  public MatomoRequestBuilder siteId(int siteId) {
    this.siteId = siteId;
    return this;
  }

  public MatomoRequestBuilder actionUrl(String actionUrl) {
    this.actionUrl = actionUrl;
    return this;
  }

  public MatomoRequestBuilder actionName(String actionName) {
    this.actionName = actionName;
    return this;
  }

  public MatomoRequestBuilder actionTime(Long actionTime) {
    this.actionTime = actionTime;
    return this;
  }

  public MatomoRequestBuilder apiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
    return this;
  }

  public MatomoRequestBuilder authToken(String authToken) {
    this.authToken = authToken;
    return this;
  }

  public MatomoRequestBuilder campaignKeyword(String campaignKeyword) {
    this.campaignKeyword = campaignKeyword;
    return this;
  }

  public MatomoRequestBuilder campaignName(String campaignName) {
    this.campaignName = campaignName;
    return this;
  }

  public MatomoRequestBuilder characterSet(Charset characterSet) {
    this.characterSet = characterSet;
    return this;
  }

  public MatomoRequestBuilder contentInteraction(String contentInteraction) {
    this.contentInteraction = contentInteraction;
    return this;
  }

  public MatomoRequestBuilder contentName(String contentName) {
    this.contentName = contentName;
    return this;
  }

  public MatomoRequestBuilder contentPiece(String contentPiece) {
    this.contentPiece = contentPiece;
    return this;
  }

  public MatomoRequestBuilder contentTarget(String contentTarget) {
    this.contentTarget = contentTarget;
    return this;
  }

  public MatomoRequestBuilder currentHour(Integer currentHour) {
    this.currentHour = currentHour;
    return this;
  }

  public MatomoRequestBuilder currentMinute(Integer currentMinute) {
    this.currentMinute = currentMinute;
    return this;
  }

  public MatomoRequestBuilder currentSecond(Integer currentSecond) {
    this.currentSecond = currentSecond;
    return this;
  }

  public MatomoRequestBuilder customAction(Boolean customAction) {
    this.customAction = customAction;
    return this;
  }

  public MatomoRequestBuilder deviceResolution(String deviceResolution) {
    this.deviceResolution = deviceResolution;
    return this;
  }

  public MatomoRequestBuilder downloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
    return this;
  }

  public MatomoRequestBuilder ecommerceDiscount(Double ecommerceDiscount) {
    this.ecommerceDiscount = ecommerceDiscount;
    return this;
  }

  public MatomoRequestBuilder ecommerceId(String ecommerceId) {
    this.ecommerceId = ecommerceId;
    return this;
  }

  public MatomoRequestBuilder ecommerceLastOrderTimestamp(Long ecommerceLastOrderTimestamp) {
    this.ecommerceLastOrderTimestamp = ecommerceLastOrderTimestamp;
    return this;
  }

  public MatomoRequestBuilder ecommerceRevenue(Double ecommerceRevenue) {
    this.ecommerceRevenue = ecommerceRevenue;
    return this;
  }

  public MatomoRequestBuilder ecommerceShippingCost(Double ecommerceShippingCost) {
    this.ecommerceShippingCost = ecommerceShippingCost;
    return this;
  }

  public MatomoRequestBuilder ecommerceSubtotal(Double ecommerceSubtotal) {
    this.ecommerceSubtotal = ecommerceSubtotal;
    return this;
  }

  public MatomoRequestBuilder ecommerceTax(Double ecommerceTax) {
    this.ecommerceTax = ecommerceTax;
    return this;
  }

  public MatomoRequestBuilder eventAction(String eventAction) {
    this.eventAction = eventAction;
    return this;
  }

  public MatomoRequestBuilder eventCategory(String eventCategory) {
    this.eventCategory = eventCategory;
    return this;
  }

  public MatomoRequestBuilder eventName(String eventName) {
    this.eventName = eventName;
    return this;
  }

  public MatomoRequestBuilder eventValue(Number eventValue) {
    this.eventValue = eventValue;
    return this;
  }

  public MatomoRequestBuilder goalId(Integer goalId) {
    this.goalId = goalId;
    return this;
  }

  public MatomoRequestBuilder goalRevenue(Double goalRevenue) {
    this.goalRevenue = goalRevenue;
    return this;
  }

  public MatomoRequestBuilder headerAcceptLanguage(String headerAcceptLanguage) {
    this.headerAcceptLanguage = headerAcceptLanguage;
    return this;
  }

  public MatomoRequestBuilder headerUserAgent(String headerUserAgent) {
    this.headerUserAgent = headerUserAgent;
    return this;
  }

  public MatomoRequestBuilder newVisit(Boolean newVisit) {
    this.newVisit = newVisit;
    return this;
  }

  public MatomoRequestBuilder outlinkUrl(String outlinkUrl) {
    this.outlinkUrl = outlinkUrl;
    return this;
  }

  public MatomoRequestBuilder pluginDirector(Boolean pluginDirector) {
    this.pluginDirector = pluginDirector;
    return this;
  }

  public MatomoRequestBuilder pluginFlash(Boolean pluginFlash) {
    this.pluginFlash = pluginFlash;
    return this;
  }

  public MatomoRequestBuilder pluginGears(Boolean pluginGears) {
    this.pluginGears = pluginGears;
    return this;
  }

  public MatomoRequestBuilder pluginJava(Boolean pluginJava) {
    this.pluginJava = pluginJava;
    return this;
  }

  public MatomoRequestBuilder pluginPDF(Boolean pluginPDF) {
    this.pluginPDF = pluginPDF;
    return this;
  }

  public MatomoRequestBuilder pluginQuicktime(Boolean pluginQuicktime) {
    this.pluginQuicktime = pluginQuicktime;
    return this;
  }

  public MatomoRequestBuilder pluginRealPlayer(Boolean pluginRealPlayer) {
    this.pluginRealPlayer = pluginRealPlayer;
    return this;
  }

  public MatomoRequestBuilder pluginSilverlight(Boolean pluginSilverlight) {
    this.pluginSilverlight = pluginSilverlight;
    return this;
  }

  public MatomoRequestBuilder pluginWindowsMedia(Boolean pluginWindowsMedia) {
    this.pluginWindowsMedia = pluginWindowsMedia;
    return this;
  }

  public MatomoRequestBuilder randomValue(String randomValue) {
    this.randomValue = randomValue;
    return this;
  }

  public MatomoRequestBuilder referrerUrl(String referrerUrl) {
    this.referrerUrl = referrerUrl;
    return this;
  }

  public MatomoRequestBuilder requestDatetime(MatomoDate requestDatetime) {
    this.requestDatetime = requestDatetime;
    return this;
  }

  public MatomoRequestBuilder required(Boolean required) {
    this.required = required;
    return this;
  }

  public MatomoRequestBuilder responseAsImage(Boolean responseAsImage) {
    this.responseAsImage = responseAsImage;
    return this;
  }

  public MatomoRequestBuilder searchCategory(String searchCategory) {
    this.searchCategory = searchCategory;
    return this;
  }

  public MatomoRequestBuilder searchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
    return this;
  }

  public MatomoRequestBuilder searchResultsCount(Long searchResultsCount) {
    this.searchResultsCount = searchResultsCount;
    return this;
  }

  public MatomoRequestBuilder trackBotRequests(Boolean trackBotRequests) {
    this.trackBotRequests = trackBotRequests;
    return this;
  }

  public MatomoRequestBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public MatomoRequestBuilder visitorCity(String visitorCity) {
    this.visitorCity = visitorCity;
    return this;
  }

  public MatomoRequestBuilder visitorCountry(MatomoLocale visitorCountry) {
    this.visitorCountry = visitorCountry;
    return this;
  }

  public MatomoRequestBuilder visitorCustomId(String visitorCustomId) {
    this.visitorCustomId = visitorCustomId;
    return this;
  }

  public MatomoRequestBuilder visitorFirstVisitTimestamp(Long visitorFirstVisitTimestamp) {
    this.visitorFirstVisitTimestamp = visitorFirstVisitTimestamp;
    return this;
  }

  public MatomoRequestBuilder visitorId(String visitorId) {
    this.visitorId = visitorId;
    return this;
  }

  public MatomoRequestBuilder visitorIp(String visitorIp) {
    this.visitorIp = visitorIp;
    return this;
  }

  public MatomoRequestBuilder visitorLatitude(Double visitorLatitude) {
    this.visitorLatitude = visitorLatitude;
    return this;
  }

  public MatomoRequestBuilder visitorLongitude(Double visitorLongitude) {
    this.visitorLongitude = visitorLongitude;
    return this;
  }

  public MatomoRequestBuilder visitorPreviousVisitTimestamp(Long visitorPreviousVisitTimestamp) {
    this.visitorPreviousVisitTimestamp = visitorPreviousVisitTimestamp;
    return this;
  }

  public MatomoRequestBuilder visitorRegion(String visitorRegion) {
    this.visitorRegion = visitorRegion;
    return this;
  }

  public MatomoRequestBuilder visitorVisitCount(Integer visitorVisitCount) {
    this.visitorVisitCount = visitorVisitCount;
    return this;
  }

  public MatomoRequestBuilder visitCustomVariables(List<CustomVariable> visitCustomVariables) {
    this.visitCustomVariables = visitCustomVariables;
    return this;
  }

  public MatomoRequestBuilder pageCustomVariables(List<CustomVariable> pageCustomVariables) {
    this.pageCustomVariables = pageCustomVariables;
    return this;
  }

  public MatomoRequestBuilder customTrackingParameters(Map<String, Object> customTrackingParameters) {
    this.customTrackingParameters = customTrackingParameters;
    return this;
  }

  public MatomoRequest build() {
    MatomoRequest matomoRequest = new MatomoRequest(siteId, actionUrl);
    if (actionName != null) {
      matomoRequest.setActionName(actionName);
    }
    if (actionTime != null) {
      matomoRequest.setActionTime(actionTime);
    }
    if (apiVersion != null) {
      matomoRequest.setApiVersion(apiVersion);
    }
    if (authToken != null) {
      matomoRequest.setAuthToken(authToken);
    }
    if (campaignKeyword != null) {
      matomoRequest.setCampaignKeyword(campaignKeyword);
    }
    if (campaignName != null) {
      matomoRequest.setCampaignName(campaignName);
    }
    if (characterSet != null) {
      matomoRequest.setCharacterSet(characterSet);
    }
    if (contentInteraction != null) {
      matomoRequest.setContentInteraction(contentInteraction);
    }
    if (contentName != null) {
      matomoRequest.setContentName(contentName);
    }
    if (contentPiece != null) {
      matomoRequest.setContentPiece(contentPiece);
    }
    if (contentTarget != null) {
      matomoRequest.setContentTarget(contentTarget);
    }
    if (currentHour != null) {
      matomoRequest.setCurrentHour(currentHour);
    }
    if (currentMinute != null) {
      matomoRequest.setCurrentMinute(currentMinute);
    }
    if (currentSecond != null) {
      matomoRequest.setCurrentSecond(currentSecond);
    }
    if (customAction != null) {
      matomoRequest.setCustomAction(customAction);
    }
    if (customTrackingParameters != null) {
      for (Map.Entry<String, Object> customTrackingParameter : customTrackingParameters.entrySet()) {
        matomoRequest.addCustomTrackingParameter(customTrackingParameter.getKey(), customTrackingParameter.getValue());
      }
    }
    if (deviceResolution != null) {
      matomoRequest.setDeviceResolution(deviceResolution);
    }
    if (downloadUrl != null) {
      matomoRequest.setDownloadUrl(downloadUrl);
    }
    if (ecommerceDiscount != null) {
      matomoRequest.setEcommerceDiscount(ecommerceDiscount);
    }
    if (ecommerceId != null) {
      matomoRequest.setEcommerceId(ecommerceId);
    }
    if (ecommerceLastOrderTimestamp != null) {
      matomoRequest.setEcommerceLastOrderTimestamp(ecommerceLastOrderTimestamp);
    }
    if (ecommerceRevenue != null) {
      matomoRequest.setEcommerceRevenue(ecommerceRevenue);
    }
    if (ecommerceShippingCost != null) {
      matomoRequest.setEcommerceShippingCost(ecommerceShippingCost);
    }
    if (ecommerceSubtotal != null) {
      matomoRequest.setEcommerceSubtotal(ecommerceSubtotal);
    }
    if (ecommerceTax != null) {
      matomoRequest.setEcommerceTax(ecommerceTax);
    }
    if (eventAction != null) {
      matomoRequest.setEventAction(eventAction);
    }
    if (eventCategory != null) {
      matomoRequest.setEventCategory(eventCategory);
    }
    if (eventName != null) {
      matomoRequest.setEventName(eventName);
    }
    if (eventValue != null) {
      matomoRequest.setEventValue(eventValue);
    }
    if (goalId != null) {
      matomoRequest.setGoalId(goalId);
    }
    if (goalRevenue != null) {
      matomoRequest.setGoalRevenue(goalRevenue);
    }
    if (headerAcceptLanguage != null) {
      matomoRequest.setHeaderAcceptLanguage(headerAcceptLanguage);
    }
    if (headerUserAgent != null) {
      matomoRequest.setHeaderUserAgent(headerUserAgent);
    }
    if (newVisit != null) {
      matomoRequest.setNewVisit(newVisit);
    }
    if (outlinkUrl != null) {
      matomoRequest.setOutlinkUrl(outlinkUrl);
    }
    if (pageCustomVariables != null) {
      for (int i = 0; i < pageCustomVariables.size(); i++) {
        CustomVariable pageCustomVariable = pageCustomVariables.get(i);
        matomoRequest.setPageCustomVariable(pageCustomVariable, i + 1);
      }
    }
    if (pluginDirector != null) {
      matomoRequest.setPluginDirector(pluginDirector);
    }
    if (pluginFlash != null) {
      matomoRequest.setPluginFlash(pluginFlash);
    }
    if (pluginGears != null) {
      matomoRequest.setPluginGears(pluginGears);
    }
    if (pluginJava != null) {
      matomoRequest.setPluginJava(pluginJava);
    }
    if (pluginPDF != null) {
      matomoRequest.setPluginPDF(pluginPDF);
    }
    if (pluginQuicktime != null) {
      matomoRequest.setPluginQuicktime(pluginQuicktime);
    }
    if (pluginRealPlayer != null) {
      matomoRequest.setPluginRealPlayer(pluginRealPlayer);
    }
    if (pluginSilverlight != null) {
      matomoRequest.setPluginSilverlight(pluginSilverlight);
    }
    if (pluginWindowsMedia != null) {
      matomoRequest.setPluginWindowsMedia(pluginWindowsMedia);
    }
    if (randomValue != null) {
      matomoRequest.setRandomValue(randomValue);
    }
    if (referrerUrl != null) {
      matomoRequest.setReferrerUrl(referrerUrl);
    }
    if (requestDatetime != null) {
      matomoRequest.setRequestDatetime(requestDatetime);
    }
    if (required != null) {
      matomoRequest.setRequired(required);
    }
    if (responseAsImage != null) {
      matomoRequest.setResponseAsImage(responseAsImage);
    }
    if (searchQuery != null) {
      matomoRequest.setSearchQuery(searchQuery);
    }
    if (searchCategory != null) {
      matomoRequest.setSearchCategory(searchCategory);
    }
    if (searchResultsCount != null) {
      matomoRequest.setSearchResultsCount(searchResultsCount);
    }
    if (trackBotRequests != null) {
      matomoRequest.setTrackBotRequests(trackBotRequests);
    }
    if (visitCustomVariables != null) {
      for (int i = 0; i < visitCustomVariables.size(); i++) {
        CustomVariable visitCustomVariable = visitCustomVariables.get(i);
        matomoRequest.setVisitCustomVariable(visitCustomVariable, i + 1);
      }
    }
    if (userId != null) {
      matomoRequest.setUserId(userId);
    }
    if (visitorCity != null) {
      matomoRequest.setVisitorCity(visitorCity);
    }
    if (visitorCountry != null) {
      matomoRequest.setVisitorCountry(visitorCountry);
    }
    if (visitorCustomId != null) {
      matomoRequest.setVisitorCustomId(visitorCustomId);
    }
    if (visitorFirstVisitTimestamp != null) {
      matomoRequest.setVisitorFirstVisitTimestamp(visitorFirstVisitTimestamp);
    }
    if (visitorId != null) {
      matomoRequest.setVisitorId(visitorId);
    }
    if (visitorIp != null) {
      matomoRequest.setVisitorIp(visitorIp);
    }
    if (visitorLatitude != null) {
      matomoRequest.setVisitorLatitude(visitorLatitude);
    }
    if (visitorLongitude != null) {
      matomoRequest.setVisitorLongitude(visitorLongitude);
    }
    if (visitorPreviousVisitTimestamp != null) {
      matomoRequest.setVisitorPreviousVisitTimestamp(visitorPreviousVisitTimestamp);
    }
    if (visitorRegion != null) {
      matomoRequest.setVisitorRegion(visitorRegion);
    }
    if (visitorVisitCount != null) {
      matomoRequest.setVisitorVisitCount(visitorVisitCount);
    }
    return matomoRequest;
  }

}
