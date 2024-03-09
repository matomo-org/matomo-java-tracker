package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.net.CookieManager;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Provides a {@link Sender} implementation based on Java 11.
 */
public class Java11SenderProvider implements SenderProvider {

  private static final TrustManager[] TRUST_ALL_MANAGERS = {new TrustingX509TrustManager()};

  @Override
  public Sender provideSender(
      TrackerConfiguration trackerConfiguration, QueryCreator queryCreator
  ) {
    CookieManager cookieManager = new CookieManager();
    ExecutorService executorService = Executors.newFixedThreadPool(
        trackerConfiguration.getThreadPoolSize(),
        new DaemonThreadFactory()
    );
    HttpClient.Builder builder = HttpClient
        .newBuilder()
        .cookieHandler(cookieManager)
        .executor(executorService);
    if (trackerConfiguration.getConnectTimeout() != null
        && trackerConfiguration.getConnectTimeout().toMillis() > 0L) {
      builder.connectTimeout(trackerConfiguration.getConnectTimeout());
    }
    if (!isEmpty(trackerConfiguration.getProxyHost()) && trackerConfiguration.getProxyPort() > 0) {
      builder.proxy(ProxySelector.of(new InetSocketAddress(
          trackerConfiguration.getProxyHost(),
          trackerConfiguration.getProxyPort()
      )));
      if (!isEmpty(trackerConfiguration.getProxyUsername())
          && !isEmpty(trackerConfiguration.getProxyPassword())) {
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

    return new Java11Sender(
        trackerConfiguration,
        queryCreator,
        builder.build(),
        cookieManager.getCookieStore(),
        executorService
    );
  }

  private static boolean isEmpty(
      @Nullable String str
  ) {
    return str == null || str.isEmpty() || str.trim().isEmpty();
  }

}
