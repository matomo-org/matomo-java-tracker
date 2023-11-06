/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.net.URI;
import java.time.Duration;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Defines configuration settings for the Matomo tracking.
 */
@Builder
@Value
public class TrackerConfiguration {

  private static final Pattern AUTH_TOKEN_PATTERN = Pattern.compile("[a-z0-9]+");
  /**
   * The Matomo Tracking HTTP API endpoint, for example https://your-matomo-domain.example/matomo.php
   */
  @NonNull URI apiEndpoint;

  /**
   * The default ID of the website that will be used if not specified explicitly.
   */
  Integer defaultSiteId;

  /**
   * The authorization token (parameter token_auth) to use if not specified explicitly.
   */
  String defaultAuthToken;

  /**
   * Allows to stop the tracker to send requests to the Matomo endpoint.
   */
  @Builder.Default
  boolean enabled = true;

  /**
   * The timeout until a connection is established.
   *
   * <p>A timeout value of zero is interpreted as an infinite timeout.
   * A `null` value is interpreted as undefined (system default if applicable).</p>
   *
   * <p>Default: 10 seconds</p>
   */
  @Builder.Default
  Duration connectTimeout = Duration.ofSeconds(5L);

  /**
   * The socket timeout ({@code SO_TIMEOUT}), which is the timeout for waiting for data or, put differently, a maximum
   * period inactivity between two consecutive data packets.
   *
   * <p>A timeout value of zero is interpreted as an infinite timeout.
   * A `null value is interpreted as undefined (system default if applicable).</p>
   *
   * <p>Default: 30 seconds</p>
   */
  @Builder.Default
  Duration socketTimeout = Duration.ofSeconds(5L);

  /**
   * The hostname or IP address of an optional HTTP proxy. {@code proxyPort} must be configured as well
   */
  @Nullable
  String proxyHost;

  /**
   * The port of an HTTP proxy. {@code proxyHost} must be configured as well.
   */
  int proxyPort;

  /**
   * If the HTTP proxy requires a username for basic authentication, it can be configured here. Proxy host, port and
   * password must also be set.
   */
  @Nullable
  String proxyUserName;

  /**
   * The corresponding password for the basic auth proxy user. The proxy host, port and username must be set as well.
   */
  @Nullable
  String proxyPassword;

  /**
   * A custom user agent to be set. Defaults to "MatomoJavaClient"
   */
  @Builder.Default
  @NonNull String userAgent = "MatomoJavaClient";

  /**
   * Logs if the Matomo Tracking API endpoint responds with an erroneous HTTP code. Defaults to
   * false.
   */
  boolean logFailedTracking;

  /**
   * Disables SSL certificate validation. This is useful for testing with self-signed certificates.
   * Do not use in production environments. Defaults to false.
   *
   * <p>Attention: This slows down performance

   * @see #disableSslHostVerification
   */
  boolean disableSslCertValidation;

  /**
   * Disables SSL host verification. This is useful for testing with self-signed certificates. Do
   * not use in production environments. Defaults to false.
   *
   * <p>Attention: This slows down performance
   *
   * @see #disableSslCertValidation
   */
  boolean disableSslHostVerification;

  /**
   * The thread pool size for the async sender. Defaults to 2.
   *
   * <p>Attention: If you use this library in a web application, make sure that this thread pool
   * does not exceed the thread pool of the web application. Otherwise, you might run into
   * problems.
   */
  int threadPoolSize = 2;

  /**
   * Validates the auth token. The auth token must be exactly 32 characters long.
   */
  public void validate() {
    if (defaultAuthToken != null) {
      if (defaultAuthToken.trim().length() != 32) {
        throw new IllegalArgumentException("Auth token must be exactly 32 characters long");
      }
      if (!AUTH_TOKEN_PATTERN.matcher(defaultAuthToken).matches()) {
        throw new IllegalArgumentException(
            "Auth token must contain only lowercase letters and numbers");
      }
    }
  }
}