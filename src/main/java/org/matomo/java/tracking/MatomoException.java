package org.matomo.java.tracking;

public class MatomoException extends RuntimeException {

  private static final long serialVersionUID = 4592083764365938934L;

  public MatomoException(String message, Throwable cause) {
    super(message, cause);
  }
}
