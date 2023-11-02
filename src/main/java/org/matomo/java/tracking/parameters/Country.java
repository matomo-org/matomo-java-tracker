/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;

/**
 * A two-letter country code representing a country.
 *
 * <p>See <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166-1 alpha-2</a> for a list of valid codes.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Country {

  @NonNull
  private String code;

  /**
   * Only for internal use to grant downwards compatibility to {@link org.matomo.java.tracking.MatomoLocale}.
   *
   * @param locale A locale that must contain a country code
   */
  @Deprecated
  protected Country(@NotNull Locale locale) {
    setLocale(locale);
  }

  /**
   * Creates a country from a given code.
   *
   * @param code Must consist of two lower letters or simply null. Case is ignored
   * @return The country or null if code was null
   */
  @Nullable
  public static Country fromCode(@Nullable String code) {
    if (code == null || code.isEmpty() || code.trim().isEmpty()) {
      return null;
    }
    if (code.length() == 2) {
      return new Country(code.toLowerCase(Locale.ROOT));
    }
    throw new IllegalArgumentException("Invalid country code");
  }

  /**
   * Extracts the country from the given accept language header.
   *
   * @param ranges A language range list. See {@link LanguageRange#parse(String)}
   * @return The country or null if ranges was null
   */
  @Nullable
  public static Country fromLanguageRanges(@Nullable String ranges) {
    if (ranges == null || ranges.isEmpty() || ranges.trim().isEmpty()) {
      return null;
    }
    List<LanguageRange> languageRanges = LanguageRange.parse(ranges);
    for (LanguageRange languageRange : languageRanges) {
      String range = languageRange.getRange();
      String[] split = range.split("-");
      if (split.length == 2 && split[1].length() == 2) {
        return new Country(split[1].toLowerCase(Locale.ROOT));
      }
    }
    throw new IllegalArgumentException("Invalid country code");
  }

  /**
   * Returns the locale for this country.
   *
   * @return The locale for this country
   * @see Locale#forLanguageTag(String)
   * @deprecated Since you instantiate this class, you can determine the language on your own
   *     using {@link Locale#forLanguageTag(String)}
   */
  @Deprecated
  public Locale getLocale() {
    return Locale.forLanguageTag(code);
  }

  /**
   * Sets the locale for this country.
   *
   * @param locale A locale that must contain a country code
   * @see Locale#getCountry()
   * @deprecated Since you instantiate this class, you can determine the language on your own
   *     using {@link Locale#getCountry()}
   */
  public final void setLocale(Locale locale) {
    if (locale == null || locale.getCountry() == null || locale.getCountry().isEmpty()
      || locale.getCountry().trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid locale");
    }
    code = locale.getCountry().toLowerCase(Locale.ENGLISH);
  }

  @Override
  public String toString() {
    return code;
  }

}
