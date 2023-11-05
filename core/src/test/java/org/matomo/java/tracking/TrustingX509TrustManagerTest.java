package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.cert.X509Certificate;
import org.junit.jupiter.api.Test;

class TrustingX509TrustManagerTest {

  private final TrustingX509TrustManager trustingX509TrustManager = new TrustingX509TrustManager();

  @Test
  void acceptedIssuersIsAlwaysNull() {
    X509Certificate[] acceptedIssuers = trustingX509TrustManager.getAcceptedIssuers();
    assertThat(acceptedIssuers).isNull();
  }

  @Test
  void checkClientTrustedDoesNothing() {
    trustingX509TrustManager.checkClientTrusted(null, null);
  }

  @Test
  void checkServerTrustedDoesNothing() {
    trustingX509TrustManager.checkServerTrusted(null, null);
  }

}