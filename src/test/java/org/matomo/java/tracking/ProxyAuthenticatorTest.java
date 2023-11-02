package org.matomo.java.tracking;

import org.junit.jupiter.api.Test;

import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class ProxyAuthenticatorTest {

  @Test
  void createsPasswordAuthentication() throws Exception {

    ProxyAuthenticator proxyAuthenticator = new ProxyAuthenticator("user", "password");
    Authenticator.setDefault(proxyAuthenticator);

    PasswordAuthentication passwordAuthentication = Authenticator.requestPasswordAuthentication(
      "host",
      InetAddress.getLocalHost(),
      8080,
      "http",
      "prompt",
      "https",
      new URL("https://www.daniel-heid.de"),
      RequestorType.PROXY
    );

    assertThat(passwordAuthentication.getUserName()).isEqualTo("user");
    assertThat(passwordAuthentication.getPassword()).contains('p', 'a', 's', 's', 'w', 'o', 'r', 'd');

  }

}
