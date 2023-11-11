package org.matomo.java.tracking;

import java.util.concurrent.Executors;

/**
 * Provides a {@link Sender} implementation based on Java 8.
 */
public class Java8SenderProvider implements SenderProvider {

  @Override
  public Sender provideSender(
      TrackerConfiguration trackerConfiguration, QueryCreator queryCreator
  ) {
    return new Java8Sender(
        trackerConfiguration,
        queryCreator,
        Executors.newFixedThreadPool(trackerConfiguration.getThreadPoolSize(), new DaemonThreadFactory())
    );
  }
}
