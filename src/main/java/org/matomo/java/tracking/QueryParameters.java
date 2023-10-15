package org.matomo.java.tracking;

import lombok.NonNull;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nonnull;
import java.util.*;

public final class QueryParameters {

  private QueryParameters() {
    // utility
  }

  @Nonnull
  public static List<NameValuePair> fromMap(@NonNull Map<String, Collection<Object>> map) {
    List<NameValuePair> queryParameters = new ArrayList<>();
    for (Map.Entry<String, Collection<Object>> entries : map.entrySet()) {
      for (Object value : entries.getValue()) {
        queryParameters.add(new BasicNameValuePair(entries.getKey(), value.toString()));
      }
    }
    queryParameters.sort(Comparator.comparing(NameValuePair::getName));
    return queryParameters;
  }

}
