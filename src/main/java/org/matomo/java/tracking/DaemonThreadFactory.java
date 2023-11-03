/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.concurrent.ThreadFactory;

class DaemonThreadFactory implements ThreadFactory {

  @Override
  public Thread newThread(
      @Nullable
      Runnable runnable
  ) {
    Thread thread = new Thread(runnable);
    thread.setDaemon(true);
    thread.setName("MatomoJavaTracker");
    return thread;
  }

}
