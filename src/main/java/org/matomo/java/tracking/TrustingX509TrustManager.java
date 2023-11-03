package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class TrustingX509TrustManager implements X509TrustManager {

  @Override
  @Nullable
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }

  @Override
  public void checkClientTrusted(
      @Nullable X509Certificate[] chain, @Nullable String authType
  ) {
    // no operation
  }

  @Override
  public void checkServerTrusted(
      @Nullable X509Certificate[] chain, @Nullable String authType
  ) {
    // no operation
  }
}
