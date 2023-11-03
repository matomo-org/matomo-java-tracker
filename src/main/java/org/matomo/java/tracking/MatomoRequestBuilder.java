package org.matomo.java.tracking;

import static java.util.Collections.singleton;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.matomo.java.tracking.parameters.AcceptLanguage;

/**
 * The former MatomoRequestBuilder class has been moved to MatomoRequest.MatomoRequestBuilder.
 * This class is only here for backwards compatibility.
 *
 * @deprecated Use {@link MatomoRequest.MatomoRequestBuilder} instead.
 */
@Deprecated
public class MatomoRequestBuilder extends MatomoRequest.MatomoRequestBuilder {


  /**
   * Sets the tracking parameter for the accept languages of a user. Only here for backwards
   * compatibility.
   *
   * @param headerAcceptLanguage The accept language header of a user. Must be in the format
   *                             specified in RFC 2616.
   * @return This builder
   * @deprecated Use {@link MatomoRequest.MatomoRequestBuilder#headerAcceptLanguage(AcceptLanguage)}
   * in combination with {@link AcceptLanguage#fromHeader(String)} instead.
   */
  @Deprecated
  public MatomoRequestBuilder headerAcceptLanguage(@Nullable String headerAcceptLanguage) {
    headerAcceptLanguage(AcceptLanguage.fromHeader(headerAcceptLanguage));
    return this;
  }

  /**
   * Sets the custom tracking parameters to the given parameters.
   *
   * <p>This converts the given map to a map of collections. Only included for backwards
   * compatibility.
   *
   * @param parameters The custom tracking parameters to set
   * @return This builder
   * @deprecated Use {@link MatomoRequest.MatomoRequestBuilder#additionalParameters(Map)}}
   */
  @Deprecated
  public MatomoRequestBuilder customTrackingParameters(@Nullable Map<String, Object> parameters) {
    if (parameters == null || parameters.isEmpty()) {
      additionalParameters(null);
    } else {
      additionalParameters(parameters.entrySet().stream().collect(Collectors.toMap(
          e -> e.getKey(),
          e -> toCollection(e)
      )));
    }
    return this;
  }

  private static Collection<Object> toCollection(@NonNull Map.Entry<String, Object> e) {
    if (e.getValue() instanceof Collection) {
      return (Collection<Object>) e.getValue();
    }
    return new ArrayList<>(singleton(e.getValue()));
  }

}
