package org.matomo.java.tracking;

import static java.util.Collections.singleton;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A {@link Sender} implementation that does not send anything but stores the requests and queries.
 *
 * <p>This class is intended for testing purposes only. It does not send anything to the Matomo server. Instead, it
 * stores the requests and queries in collections that can be accessed via {@link #getRequests()} and {@link
 * #getQueries()}.
 */
@RequiredArgsConstructor
@Getter
class TestSender implements Sender {

  private final Collection<MatomoRequest> requests = new ArrayList<>();

  private final Collection<String> queries = new ArrayList<>();

  private final TrackerConfiguration trackerConfiguration;

  private final QueryCreator queryCreator;

  @NonNull
  @Override
  public CompletableFuture<MatomoRequest> sendSingleAsync(@NonNull MatomoRequest request) {
    createQueryAndAddRequest(request, null);
    return CompletableFuture.completedFuture(request);
  }

  @Override
  public void sendSingle(@NonNull MatomoRequest request) {
    createQueryAndAddRequest(request, null);
  }

  @Override
  public void sendBulk(
      @NonNull Iterable<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    for (MatomoRequest request : requests) {
      createQueryAndAddRequest(request, overrideAuthToken);
    }
  }

  @NonNull
  @Override
  public CompletableFuture<Void> sendBulkAsync(
      @NonNull Collection<? extends MatomoRequest> requests, @Nullable String overrideAuthToken
  ) {
    for (MatomoRequest request : requests) {
      createQueryAndAddRequest(request, overrideAuthToken);
    }
    return CompletableFuture.completedFuture(null);
  }



  private void createQueryAndAddRequest(@lombok.NonNull MatomoRequest request, @Nullable String overrideAuthToken) {
    String authToken = AuthToken.determineAuthToken(overrideAuthToken, singleton(request), trackerConfiguration);
    queries.add(queryCreator.createQuery(request, authToken));
    requests.add(request);
  }

  @Override
  public void close() {
    // do nothing
  }
}
