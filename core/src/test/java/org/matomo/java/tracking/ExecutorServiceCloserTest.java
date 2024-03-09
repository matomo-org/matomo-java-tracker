package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class ExecutorServiceCloserTest {

  @Test
  void shutsDownExecutorService() {

    ExecutorService executorService = Executors.newFixedThreadPool(2, new DaemonThreadFactory());

    ExecutorServiceCloser.close(executorService);

    assertThat(executorService.isTerminated()).isTrue();
    assertThat(executorService.isShutdown()).isTrue();

  }

  @Test
  void shutsDownExecutorServiceImmediately() throws Exception {

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      try {
        Thread.sleep(10000L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    Thread thread = new Thread(() -> {
      ExecutorServiceCloser.close(executorService);
    });
    thread.start();
    Thread.sleep(1000L);
    thread.interrupt();

    assertThat(executorService.isShutdown()).isTrue();

  }

}