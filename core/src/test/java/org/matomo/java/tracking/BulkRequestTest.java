package org.matomo.java.tracking;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BulkRequestTest {

  @Test
  void formatsQueriesAsJson() {
    BulkRequest bulkRequest = BulkRequest.builder()
        .queries(singleton("idsite=1&rec=1&action_name=TestBulkRequest"))
        .authToken("token")
        .build();

    byte[] bytes = bulkRequest.toBytes();

    assertThat(new String(bytes)).isEqualTo("{\"requests\":[\"?idsite=1&rec=1&action_name=TestBulkRequest\"],\"token_auth\":\"token\"}");
  }

  @Test
  void failsIfQueriesAreEmpty() {

    BulkRequest bulkRequest = BulkRequest.builder().queries(emptyList()).build();

    assertThatThrownBy(bulkRequest::toBytes)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Queries must not be empty");

  }

}