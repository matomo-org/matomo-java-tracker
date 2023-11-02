/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.parameters;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Multiple things that you can buy online.
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class EcommerceItems {

  @Delegate
  @Singular
  private List<EcommerceItem> items = new ArrayList<>();

  public String toString() {
    return items.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
  }

}
