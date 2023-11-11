/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.spring;

import java.net.URI;
import org.matomo.java.tracking.TrackerConfiguration;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

class StandardTrackerConfigurationBuilderCustomizer implements TrackerConfigurationBuilderCustomizer, Ordered {

  private final MatomoTrackerProperties properties;

  StandardTrackerConfigurationBuilderCustomizer(MatomoTrackerProperties properties) {
    this.properties = properties;
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public void customize(@NonNull TrackerConfiguration.TrackerConfigurationBuilder builder) {
    PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
    map.from(properties::getApiEndpoint).as(URI::create).to(builder::apiEndpoint);
    map.from(properties::getDefaultSiteId).to(builder::defaultSiteId);
    map.from(properties::getDefaultAuthToken).to(builder::defaultAuthToken);
    map.from(properties::getEnabled).to(builder::enabled);
    map.from(properties::getConnectTimeout).to(builder::connectTimeout);
    map.from(properties::getSocketTimeout).to(builder::socketTimeout);
    map.from(properties::getProxyHost).to(builder::proxyHost);
    map.from(properties::getProxyPort).to(builder::proxyPort);
    map.from(properties::getProxyUsername).to(builder::proxyUsername);
    map.from(properties::getProxyPassword).to(builder::proxyPassword);
    map.from(properties::getUserAgent).to(builder::userAgent);
    map.from(properties::getLogFailedTracking).to(builder::logFailedTracking);
    map.from(properties::getDisableSslCertValidation).to(builder::disableSslCertValidation);
    map.from(properties::getDisableSslHostVerification).to(builder::disableSslHostVerification);
    map.from(properties::getThreadPoolSize).to(builder::threadPoolSize);
  }


}