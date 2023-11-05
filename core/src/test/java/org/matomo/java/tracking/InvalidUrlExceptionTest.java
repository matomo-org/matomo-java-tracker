package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InvalidUrlExceptionTest {

  @Test
  void createsInvalidUrlException() {
    InvalidUrlException invalidUrlException = new InvalidUrlException(new Throwable());

    assertThat(invalidUrlException).isNotNull();
    assertThat(invalidUrlException.getCause()).isNotNull();

  }

}
