package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

class TrustingHostnameVerifier implements HostnameVerifier {

  @Override
  public boolean verify(
      @Nullable
      String hostname,
      @Nullable
      SSLSession session
  ) {
    return true;
  }
}
