package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Provides a {@link Sender} implementation based on Java 17.
 */
public class Java17SenderProvider implements SenderProvider {

  private static final TrustManager[] TRUST_ALL_MANAGERS = {new TrustingX509TrustManager()};

  @Override
  public Sender provideSender(
      TrackerConfiguration trackerConfiguration, QueryCreator queryCreator
  ) {
    HttpClient.Builder builder = HttpClient
        .newBuilder()
        .executor(Executors.newFixedThreadPool(trackerConfiguration.getThreadPoolSize(), new DaemonThreadFactory()))
    ;
    if (trackerConfiguration.getConnectTimeout() != null && trackerConfiguration.getConnectTimeout().toMillis() > 0L) {
      builder.connectTimeout(trackerConfiguration.getConnectTimeout());
    }
    if (!isEmpty(trackerConfiguration.getProxyHost()) && trackerConfiguration.getProxyPort() > 0) {
      builder.proxy(ProxySelector.of(new InetSocketAddress(trackerConfiguration.getProxyHost(),
          trackerConfiguration.getProxyPort()
      )));
      if (!isEmpty(trackerConfiguration.getProxyUsername()) && !isEmpty(trackerConfiguration.getProxyPassword())) {
        builder.authenticator(new ProxyAuthenticator(
            trackerConfiguration.getProxyUsername(),
            trackerConfiguration.getProxyPassword()
        ));
      }
    }
    if (trackerConfiguration.isDisableSslCertValidation()) {
      try {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, TRUST_ALL_MANAGERS, new SecureRandom());
        builder.sslContext(sslContext);
      } catch (Exception e) {
        throw new MatomoException("Could not disable SSL certification validation", e);
      }
    }
    if (trackerConfiguration.isDisableSslHostVerification()) {
      throw new MatomoException("Please disable SSL hostname verification manually using the system parameter -Djdk.internal.httpclient.disableHostnameVerification=true");
    }

    return new Java17Sender(trackerConfiguration, queryCreator, builder.build());
  }

  private static boolean isEmpty(
      @Nullable String str
  ) {
    return str == null || str.isEmpty() || str.trim().isEmpty();
  }

}
