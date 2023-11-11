package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class DaemonThreadFactory implements ThreadFactory {

  private final AtomicInteger count = new AtomicInteger();

  @Override
  public Thread newThread(@NonNull Runnable r) {
    Thread thread = new Thread(null, r, String.format("MatomoJavaTracker-%d", count.getAndIncrement()));
    thread.setDaemon(true);
    return thread;
  }
}