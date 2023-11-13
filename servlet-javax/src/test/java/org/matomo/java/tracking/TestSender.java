package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A {@link Sender} implementation that does not send anything but stores the requests.
 *
 * <p>This class is intended for testing purposes only. It does not send anything to the Matomo server. Instead, it
 * stores the requests and queries in collections that can be accessed via {@link #getRequests()}.
 */
@RequiredArgsConstructor
@Getter
class TestSender implements Sender {

  private final Collection<MatomoRequest> requests = new ArrayList<>();

  private final TrackerConfiguration trackerConfiguration;

  private final QueryCreator queryCreator;

  @NonNull
  @Override
  public CompletableFuture<MatomoRequest> sendSingleAsync(@NonNull MatomoRequest request) {
    requests.add(request);
    return CompletableFuture.completedFuture(request);
  }

  @Override
  public void sendSingle(@NonNull MatomoRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void sendBulk(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    throw new UnsupportedOperationException();
  }

  @NonNull
  @Override
  public CompletableFuture<Void> sendBulkAsync(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    throw new UnsupportedOperationException();
  }

}
