package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
class BulkRequest {

  @NonNull
  Collection<String> queries;

  @Nullable
  String authToken;

  byte[] toBytes(

  ) {
    if (queries.isEmpty()) {
      throw new IllegalArgumentException("Queries must not be empty");
    }
    StringBuilder payload = new StringBuilder("{\"requests\":[");
    Iterator<String> iterator = queries.iterator();
    while (iterator.hasNext()) {
      String query = iterator.next();
      payload.append("\"?").append(query).append('"');
      if (iterator.hasNext()) {
        payload.append(',');
      }
    }
    payload.append(']');
    if (authToken != null) {
      payload.append(",\"token_auth\":\"").append(authToken).append('"');
    }
    return payload.append('}').toString().getBytes(StandardCharsets.UTF_8);
  }

}
