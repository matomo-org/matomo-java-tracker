package org.matomo.java.tracking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MatomoExceptionTest {

  @Test
  void createsMatomoExceptionWithMessage() {
    MatomoException matomoException = new MatomoException("message");

    assertEquals("message", matomoException.getMessage());
  }

  @Test
  void createsMatomoExceptionWithMessageAndCause() {
    Throwable cause = new Throwable();
    MatomoException matomoException = new MatomoException("message", cause);

    assertEquals("message", matomoException.getMessage());
    assertEquals(cause, matomoException.getCause());
  }

}
