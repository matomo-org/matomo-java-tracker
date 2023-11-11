package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DaemonThreadFactoryTest {

  private final DaemonThreadFactory daemonThreadFactory = new DaemonThreadFactory();

  @Test
  void createsNewThreadAsDaemonThread() {
    Thread thread = daemonThreadFactory.newThread(() -> {
      // do nothing
    });
    assertThat(thread.isDaemon()).isTrue();
  }

  @Test
  void createsNewThreadWithMatomoJavaTrackerName() {
    Thread thread = daemonThreadFactory.newThread(() -> {
      // do nothing
    });
    assertThat(thread.getName()).startsWith("MatomoJavaTracker-");
  }

}