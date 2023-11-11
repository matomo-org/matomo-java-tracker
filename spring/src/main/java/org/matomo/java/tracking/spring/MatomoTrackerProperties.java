/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.spring;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.matomo.java.tracking.TrackerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Matomo Tracker.
 *
 * <p>These properties can be configured in the application.properties file. For example:
 * <pre>
 *   matomo.tracker.api-endpoint=https://your-matomo-domain.example/matomo.php
 *   matomo.tracker.default-site-id=1
 *   matomo.tracker.default-auth-token=1234567890abcdef1234567890abcdef
 *   matomo.tracker.enabled=true
 *   matomo.tracker.connect-timeout=10s
 *   matomo.tracker.socket-timeout=30s
 *   matomo.tracker.proxy-host=proxy.example.com
 *   matomo.tracker.proxy-port=8080
 *   matomo.tracker.proxy-username=proxyuser
 *   matomo.tracker.proxy-password=proxypassword
 *   matomo.tracker.user-agent=MatomoJavaClient
 *   matomo.tracker.log-failed-tracking=true
 *   matomo.tracker.disable-ssl-cert-validation=true
 *   matomo.tracker.disable-ssl-host-validation=true
 *   matomo.tracker.thread-pool-size=2
 * </pre>
 *
 * @see MatomoTrackerAutoConfiguration
 * @see TrackerConfiguration
 */
@ConfigurationProperties(prefix = "matomo.tracker")
@Getter
@Setter
public class MatomoTrackerProperties {

  /**
   * The Matomo Tracking HTTP API endpoint, for example https://your-matomo-domain.example/matomo.php
   */
  private String apiEndpoint;

  /**
   * The default ID of the website that will be used if not specified explicitly.
   */
  private Integer defaultSiteId;

  /**
   * The authorization token (parameter token_auth) to use if not specified explicitly.
   */
  private String defaultAuthToken;

  /**
   * Allows to stop the tracker to send requests to the Matomo endpoint.
   */
  private Boolean enabled = true;

  /**
   * The timeout until a connection is established.
   *
   * <p>A timeout value of zero is interpreted as an infinite timeout.
   * A `null` value is interpreted as undefined (system default if applicable).</p>
   *
   * <p>Default: 10 seconds</p>
   */
  private Duration connectTimeout = Duration.ofSeconds(5L);

  /**
   * The socket timeout ({@code SO_TIMEOUT}), which is the timeout for waiting for data or, put differently, a maximum
   * period inactivity between two consecutive data packets.
   *
   * <p>A timeout value of zero is interpreted as an infinite timeout.
   * A `null value is interpreted as undefined (system default if applicable).</p>
   *
   * <p>Default: 30 seconds</p>
   */
  private Duration socketTimeout = Duration.ofSeconds(5L);

  /**
   * The hostname or IP address of an optional HTTP proxy. {@code proxyPort} must be configured as well
   */
  private String proxyHost;

  /**
   * The port of an HTTP proxy. {@code proxyHost} must be configured as well.
   */
  private Integer proxyPort;

  /**
   * If the HTTP proxy requires a username for basic authentication, it can be configured here. Proxy host, port and
   * password must also be set.
   */
  private String proxyUsername;

  /**
   * The corresponding password for the basic auth proxy user. The proxy host, port and username must be set as well.
   */
  private String proxyPassword;

  /**
   * A custom user agent to be set. Defaults to "MatomoJavaClient"
   */
  private String userAgent = "MatomoJavaClient";

  /**
   * Logs if the Matomo Tracking API endpoint responds with an erroneous HTTP code. Defaults to
   * false.
   */
  private Boolean logFailedTracking;

  /**
   * Disables SSL certificate validation. This is useful for testing with self-signed certificates.
   * Do not use in production environments. Defaults to false.
   *
   * <p>Attention: This slows down performance

   * @see #disableSslHostVerification
   */
  private Boolean disableSslCertValidation;

  /**
   * Disables SSL host verification. This is useful for testing with self-signed certificates. Do
   * not use in production environments. Defaults to false.
   *
   * <p>Attention: This slows down performance
   *
   * @see #disableSslCertValidation
   */
  private Boolean disableSslHostVerification;

  /**
   * The thread pool size for the async sender. Defaults to 2.
   *
   * <p>Attention: If you use this library in a web application, make sure that this thread pool
   * does not exceed the thread pool of the web application. Otherwise, you might run into
   * problems.
   */
  private Integer threadPoolSize = 2;

}
