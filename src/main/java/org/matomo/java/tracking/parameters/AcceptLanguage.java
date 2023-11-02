/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import java.util.List;
import java.util.Locale.LanguageRange;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Describes the content for the Accept-Language header field that can be overridden by a custom parameter. The format
 * is specified in the corresponding <a href="http://tools.ietf.org/html/rfc4647">RFC 4647 Matching of Language Tags</a>
 *
 * <p>Example: "en-US,en;q=0.8,de;q=0.6"
 */
@Builder
@Value
public class AcceptLanguage {

  @Singular
  List<LanguageRange> languageRanges;

  /**
   * Creates the Accept-Language definition for a given header.
   *
   * <p>Please see {@link LanguageRange#parse(String)} for more information. Example: "en-US,en;q=0.8,de;q=0.6"
   *
   * @param header A header that can be null
   * @return The parsed header (probably reformatted). null if the header is null.
   * @see LanguageRange#parse(String)
   */
  @Nullable
  public static AcceptLanguage fromHeader(@Nullable String header) {
    if (header == null || header.trim().isEmpty()) {
      return null;
    }
    return new AcceptLanguage(LanguageRange.parse(header));
  }

  /**
   * Returns the Accept Language header value.
   *
   * @return The header value, e.g. "en-US,en;q=0.8,de;q=0.6"
   */
  @NotNull
  public String toString() {
    return languageRanges.stream()
      .filter(Objects::nonNull)
      .map(AcceptLanguage::format)
      .collect(Collectors.joining(","));
  }

  private static String format(@NotNull LanguageRange languageRange) {
    return languageRange.getWeight() == LanguageRange.MAX_WEIGHT ? languageRange.getRange() :
      String.format("%s;q=%s", languageRange.getRange(), languageRange.getWeight());
  }

}
