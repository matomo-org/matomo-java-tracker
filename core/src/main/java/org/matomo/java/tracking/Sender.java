package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

interface Sender extends AutoCloseable {
  @NonNull
  CompletableFuture<MatomoRequest> sendSingleAsync(
      @NonNull MatomoRequest request
  );

  void sendSingle(
      @NonNull MatomoRequest request
  );

  void sendBulk(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  );

  @NonNull
  CompletableFuture<Void> sendBulkAsync(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  );
}
