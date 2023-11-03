package org.matomo.java.tracking;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

import static org.assertj.core.api.Assertions.assertThat;

class DaemonThreadFactoryTest {

  private final ThreadFactory daemonThreadFactory = new DaemonThreadFactory();

  private Thread thread;

  @Test
  void threadIsDaemonThread() {

    whenCreatesThread();

    assertThat(thread.isDaemon()).isTrue();

  }

  private void whenCreatesThread() {
    thread = daemonThreadFactory.newThread(null);
  }

  @Test
  void threadHasName() {

    whenCreatesThread();

    assertThat(thread.getName()).isEqualTo("MatomoJavaTracker");

  }

}