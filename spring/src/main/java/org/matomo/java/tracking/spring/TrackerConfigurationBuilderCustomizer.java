/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.spring;

import org.jspecify.annotations.NonNull;
import org.matomo.java.tracking.TrackerConfiguration;

/**
 * Allows to customize the {@link TrackerConfiguration.TrackerConfigurationBuilder} with additional
 * properties.
 *
 * <p>Implementations of this interface are detected automatically by the {@link
 * MatomoTrackerAutoConfiguration}.
 *
 * @see MatomoTrackerAutoConfiguration
 * @see TrackerConfiguration
 * @see TrackerConfiguration.TrackerConfigurationBuilder
 */
@FunctionalInterface
public interface TrackerConfigurationBuilderCustomizer {

  /**
   * Customize the {@link TrackerConfiguration.TrackerConfigurationBuilder}.
   *
   * @param builder the {@link TrackerConfiguration.TrackerConfigurationBuilder} instance (never
   *     {@code null})
   * @see TrackerConfiguration#builder()
   * @see MatomoTrackerProperties
   */
  void customize(TrackerConfiguration.@NonNull TrackerConfigurationBuilder builder);
}
