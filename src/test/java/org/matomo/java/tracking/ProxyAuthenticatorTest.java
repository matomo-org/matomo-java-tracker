package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import org.junit.jupiter.api.Test;

class ProxyAuthenticatorTest {

  private PasswordAuthentication passwordAuthentication;

  @Test
  void createsPasswordAuthentication() throws Exception {

    ProxyAuthenticator proxyAuthenticator = new ProxyAuthenticator("user", "password");
    Authenticator.setDefault(proxyAuthenticator);
    givenPasswordAuthentication(RequestorType.PROXY);

    assertThat(passwordAuthentication.getUserName()).isEqualTo("user");
    assertThat(passwordAuthentication.getPassword()).contains('p', 'a', 's', 's', 'w', 'o', 'r', 'd');

  }
  @Test
  void returnsNullIfNoPasswordAuthentication() throws Exception {

    ProxyAuthenticator proxyAuthenticator = new ProxyAuthenticator("user", "password");
    Authenticator.setDefault(proxyAuthenticator);
    givenPasswordAuthentication(RequestorType.SERVER);

    assertThat(passwordAuthentication).isNull();

  }

  private void givenPasswordAuthentication(RequestorType proxy) throws Exception {
    passwordAuthentication = Authenticator.requestPasswordAuthentication(
      "host",
      InetAddress.getLocalHost(),
      8080,
      "http",
      "prompt",
      "https",
      new URL("https://www.daniel-heid.de"),
      proxy
    );
  }


}
