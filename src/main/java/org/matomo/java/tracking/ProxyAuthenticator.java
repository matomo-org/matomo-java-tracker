/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@RequiredArgsConstructor
class ProxyAuthenticator extends Authenticator {

  @NonNull
  private final String user;

  @NonNull
  private final String password;

  @Nullable
  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    if (getRequestorType() == RequestorType.PROXY) {
      return new PasswordAuthentication(user, password.toCharArray());
    }
    return null;
  }

}
