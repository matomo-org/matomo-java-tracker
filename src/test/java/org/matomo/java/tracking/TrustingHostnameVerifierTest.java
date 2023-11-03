package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TrustingHostnameVerifierTest {

  @Test
  void verifyAlwaysReturnsTrue() {

    boolean verified = new TrustingHostnameVerifier().verify(null, null);

    assertThat(verified).isTrue();
  }

}