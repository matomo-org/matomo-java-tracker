package org.matomo.java.tracking;

import java.util.function.BiConsumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The type of action performed (download or outlink).
 */
@RequiredArgsConstructor
public enum ActionType {
  DOWNLOAD(MatomoRequest.MatomoRequestBuilder::downloadUrl),
  LINK(MatomoRequest.MatomoRequestBuilder::outlinkUrl);

  @NonNull
  private final BiConsumer<MatomoRequest.MatomoRequestBuilder, String> consumer;

  /**
   * Applies the action URL to the given builder.
   *
   * @param builder   The builder to apply the action URL to.
   * @param actionUrl The action URL to apply.
   *
   * @return The builder with the action URL applied.
   */
  public MatomoRequest.MatomoRequestBuilder applyUrl(
      @NonNull MatomoRequest.MatomoRequestBuilder builder,
      @NonNull String actionUrl
  ) {
    consumer.accept(builder, actionUrl);
    return builder;
  }
}
