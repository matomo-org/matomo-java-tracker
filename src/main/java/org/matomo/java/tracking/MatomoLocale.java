/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking;

import static java.util.Objects.requireNonNull;

import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.matomo.java.tracking.parameters.Country;

/**
 * Object representing a locale required by some Matomo query parameters.
 *
 * @author brettcsorba
 * @deprecated Use {@link Country} instead
 */
@Setter
@Getter
@Deprecated
public class MatomoLocale extends Country {

  /**
   * Constructs a new MatomoLocale.
   *
   * @param locale The locale to get the country code from
   * @deprecated Please use {@link Country}
   */
  @Deprecated
  public MatomoLocale(@NotNull Locale locale) {
    super(requireNonNull(locale, "Locale must not be null"));
  }

}
