package org.matomo.java.tracking;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class MatomoRequestsTest {

  @Test
  void actionRequestBuilderContainsDownloadUrl() {
    MatomoRequest.MatomoRequestBuilder builder =
        MatomoRequests.action("https://example.com", ActionType.DOWNLOAD);
    MatomoRequest request = builder.build();
    assertThat(request.getDownloadUrl())
        .isEqualTo("https://example.com");
  }

  @Test
  void actionRequestBuilderContainsOutlinkUrl() {
    MatomoRequest.MatomoRequestBuilder builder =
        MatomoRequests.action("https://example.com", ActionType.LINK);
    MatomoRequest request = builder.build();
    assertThat(request.getOutlinkUrl())
        .isEqualTo("https://example.com");
  }

  @Test
  void contentImpressionRequestBuilderContainsContentInformation() {
    MatomoRequest.MatomoRequestBuilder builder =
        MatomoRequests.contentImpression("Product", "Smartphone", "https://example.com/product");
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(
            MatomoRequest::getContentName,
            MatomoRequest::getContentPiece,
            MatomoRequest::getContentTarget
        )
        .containsExactly("Product", "Smartphone", "https://example.com/product");
  }

  @Test
  void contentInteractionRequestBuilderContainsInteractionAndContentInformation() {
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.contentInteraction(
        "click",
        "Product",
        "Smartphone",
        "https://example.com/product"
    );
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(
            MatomoRequest::getContentInteraction,
            MatomoRequest::getContentName,
            MatomoRequest::getContentPiece,
            MatomoRequest::getContentTarget
        )
        .containsExactly("click", "Product", "Smartphone", "https://example.com/product");
  }

  @Test
  void crashRequestBuilderContainsCrashInformation() {
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.crash(
        "Error",
        "NullPointerException",
        "payment failure",
        "stackTrace",
        "MainActivity.java",
        42,
        23
    );
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(
            MatomoRequest::getCrashMessage,
            MatomoRequest::getCrashType,
            MatomoRequest::getCrashCategory,
            MatomoRequest::getCrashStackTrace,
            MatomoRequest::getCrashLocation,
            MatomoRequest::getCrashLine,
            MatomoRequest::getCrashColumn
        )
        .containsExactly(
            "Error",
            "NullPointerException",
            "payment failure",
            "stackTrace",
            "MainActivity.java",
            42,
            23
      );
  }

  @Test
  void crashWithThrowableRequestBuilderContainsCrashInformationFromThrowable() {
    Throwable throwable = new NullPointerException("Test NullPointerException");
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.crash(throwable, "payment failure");
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(MatomoRequest::getCrashMessage) // Additional assertions for other properties
        .isEqualTo("Test NullPointerException");
  }

  @Test
  void ecommerceCartUpdateRequestBuilderContainsEcommerceRevenue() {
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.ecommerceCartUpdate(100.0);
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(MatomoRequest::getEcommerceRevenue)
        .isEqualTo(100.0);
  }

  @Test
  void ecommerceOrderRequestBuilderContainsEcommerceOrderInformation() {
    MatomoRequest.MatomoRequestBuilder builder =
        MatomoRequests.ecommerceOrder("123", 200.0, 180.0, 10.0, 5.0, 5.0);
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(
            MatomoRequest::getEcommerceId,
            MatomoRequest::getEcommerceRevenue,
            MatomoRequest::getEcommerceSubtotal,
            MatomoRequest::getEcommerceTax,
            MatomoRequest::getEcommerceShippingCost,
            MatomoRequest::getEcommerceDiscount
        )
        .containsExactly("123", 200.0, 180.0, 10.0, 5.0, 5.0);
  }

  @Test
  void eventRequestBuilderContainsEventInformation() {
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.event(
        "Music",
        "Play",
        "Edvard Grieg - The Death of Ase",
        9.99
    );
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(
            MatomoRequest::getEventCategory,
            MatomoRequest::getEventAction,
            MatomoRequest::getEventName,
            MatomoRequest::getEventValue
        )
        .containsExactly("Music", "Play", "Edvard Grieg - The Death of Ase", 9.99);
  }

  @Test
  void goalRequestBuilderContainsGoalInformation() {
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.goal(
        1,
        9.99
    );
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(
            MatomoRequest::getGoalId,
            MatomoRequest::getEcommerceRevenue
        )
        .containsExactly(1, 9.99);
  }

  @Test
  void pageViewRequestBuilderContainsPageViewInformation() {
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.pageView("About");
    MatomoRequest request = builder.build();
    assertThat(request.getActionName())
        .isEqualTo("About");
  }

  @Test
  void searchRequestBuilderContainsSearchInformation() {
    MatomoRequest.MatomoRequestBuilder builder =
        MatomoRequests.siteSearch("Matomo", "Download", 42L);
    MatomoRequest request = builder.build();
    assertThat(request)
        .isNotNull()
        .extracting(
            MatomoRequest::getSearchQuery,
            MatomoRequest::getSearchCategory,
            MatomoRequest::getSearchResultsCount
        )
        .containsExactly("Matomo", "Download", 42L);
  }

  @Test
  void pingRequestBuilderContainsPingInformation() {
    MatomoRequest.MatomoRequestBuilder builder = MatomoRequests.ping();
    MatomoRequest request = builder.build();
    assertThat(request.getPing()).isTrue();
  }

  @Test
  void nullParametersThrowNullPointerExceptionForInvalidInput() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.action(null, ActionType.DOWNLOAD))
        .withMessage("url is marked non-null but is null");
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.contentImpression(null, null, null))
        .withMessage("name is marked non-null but is null");
    // Add similar checks for other methods
  }

  @Test
  void actionNullUrlThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.action(null, ActionType.DOWNLOAD))
        .withMessage("url is marked non-null but is null");
  }

  @Test
  void actionNullTypeThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.action("https://example.com", null))
        .withMessage("type is marked non-null but is null");
  }

  @Test
  void contentImpressionNullNameThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.contentImpression(
            null,
            "Smartphone",
            "https://example.com/product"
        ))
        .withMessage("name is marked non-null but is null");
  }

  // Add similar null checks for other methods...

  @Test
  void crashNullMessageThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.crash(
            null,
            "NullPointerException",
            "payment failure",
            "stackTrace",
            "MainActivity.java",
            42,
            23
        ))
        .withMessage("message is marked non-null but is null");
  }

  @Test
  void crashWithThrowableNullThrowableThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.crash(null, "payment failure"))
        .withMessage("throwable is marked non-null but is null");
  }

  @Test
  void ecommerceCartUpdateNullRevenueThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.ecommerceCartUpdate(null))
        .withMessage("revenue is marked non-null but is null");
  }

  @Test
  void ecommerceOrderNullIdThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.ecommerceOrder(null, 200.0, 180.0, 10.0, 5.0, 5.0))
        .withMessage("id is marked non-null but is null");
  }

  @Test
  void eventNullCategoryThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.event(
            null,
            "Play",
            "Edvard Grieg - The Death of Ase",
            9.99
        ))
        .withMessage("category is marked non-null but is null");
  }

  @Test
  void pageViewNullNameThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.pageView(null))
        .withMessage("name is marked non-null but is null");
  }

  @Test
  void siteSearchNullQueryThrowsNullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> MatomoRequests.siteSearch(null, "Music", 42L))
        .withMessage("query is marked non-null but is null");
  }

  @Test
  void crashDoesNotIncludeStackTraceIfStackTraceOfThrowableIsEmpty() {
    MatomoRequest.MatomoRequestBuilder builder =
        MatomoRequests.crash(new TestThrowable(), "payment failure");
    MatomoRequest request = builder.build();
    assertThat(request.getCrashMessage()).isEqualTo("message");
    assertThat(request.getCrashType()).isEqualTo("org.matomo.java.tracking.TestThrowable");
    assertThat(request.getCrashCategory()).isEqualTo("payment failure");
    assertThat(request.getCrashStackTrace()).isEqualTo(
        "org.matomo.java.tracking.TestThrowable: message");
    assertThat(request.getCrashLocation()).isNull();
    assertThat(request.getCrashLine()).isNull();
    assertThat(request.getCrashColumn()).isNull();
  }

}