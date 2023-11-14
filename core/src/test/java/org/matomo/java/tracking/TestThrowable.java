package org.matomo.java.tracking;

class TestThrowable extends Throwable {

  TestThrowable() {
    super("message", null, false, false);
  }

}
