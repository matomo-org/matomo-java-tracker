package org.matomo.java.tracking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;

/**
 * Helps to close an executor service.
 */
public class ExecutorServiceCloser {

  /**
   * Closes the given executor service.
   *
   * <p>This will check whether the executor service is already terminated, and if not, it
   * initiates a shutdown and waits a minute. If the minute expires, the executor service
   * is shutdown immediately.
   *
   * @param executorService The executor service to close
   */
  public static void close(@NonNull ExecutorService executorService) {
    boolean terminated = executorService.isTerminated();
    if (!terminated) {
      executorService.shutdown();
      boolean interrupted = false;
      while (!terminated) {
        try {
          terminated = executorService.awaitTermination(1L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
          if (!interrupted) {
            executorService.shutdownNow();
            interrupted = true;
          }
        }
      }
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }

}
