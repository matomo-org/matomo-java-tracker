package org.matomo.java.tracking;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import lombok.NonNull;

/**
 * This class contains static methods for common tracking items to create {@link MatomoRequest}
 * objects.
 *
 * <p>The intention of this class is to bundle common tracking items in a single place to make
 * tracking easier. The methods contain the typical parameters for the tracking item and return a
 * {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters, like the visitor
 * ID, a user ID or custom dimensions.
 */
public class MatomoRequests {

  /**
   * Creates a {@link MatomoRequest} object for a download or a link action.
   *
   * @param url  The URL of the download or link. Must not be null.
   * @param type The type of the action. Either {@link ActionType#DOWNLOAD} or
   *             {@link ActionType#LINK}.
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder action(
      @NonNull String url, @NonNull ActionType type
  ) {
    return type.applyUrl(MatomoRequest.request(), url);
  }

  /**
   * Creates a {@link MatomoRequest} object for a content impression.
   *
   * <p>A content impression is a view of a content piece. The content piece can be a product, an
   * article, a video, a banner, etc. The content piece can be specified by the parameters
   * {@code piece} and {@code target}. The {@code name} parameter is required and should be a
   * descriptive name of the content piece.
   *
   * @param name   The name of the content piece, like the name of a product or an article. Must not
   *               be null. Example: "SuperPhone".
   * @param piece  The content piece. Can be null. Example: "Smartphone".
   * @param target The target of the content piece, like the URL of a product or an article. Can be
   *               null. Example: "https://example.com/superphone".
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder contentImpression(
      @NonNull String name, @Nullable String piece, @Nullable String target
  ) {
    return MatomoRequest.request().contentName(name).contentPiece(piece).contentTarget(target);
  }

  /**
   * Creates a {@link MatomoRequest} object for a content interaction.
   *
   * <p>Make sure you have tracked a content impression using the same content name and
   * content piece, otherwise it will not count.
   *
   * <p>A content interaction is an interaction with a content piece. The content piece can be a
   * product, an article, a video, a banner, etc. The content piece can be specified by the
   * parameters {@code piece} and {@code target}. The {@code name} parameter is required and should
   * be a descriptive name of the content piece. The {@code interaction} parameter is required and
   * should be the type of the interaction, like "click" or "add-to-cart".
   *
   * @param interaction The type of the interaction. Must not be null. Example: "click".
   * @param name        The name of the content piece, like the name of a product or an article.
   * @param piece       The content piece. Can be null. Example: "Blog Article XYZ".
   * @param target      The target of the content piece, like the URL of a product or an article.
   *                    Can be null. Example: "https://example.com/blog/article-xyz".
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder contentInteraction(
      @NonNull String interaction,
      @NonNull String name,
      @Nullable String piece,
      @Nullable String target
  ) {
    return MatomoRequest
        .request()
        .contentInteraction(interaction)
        .contentName(name)
        .contentPiece(piece)
        .contentTarget(target);
  }

  /**
   * Creates a {@link MatomoRequest} object for a crash.
   *
   * <p>Requires Crash Analytics plugin to be enabled in the target Matomo instance.
   *
   * <p>A crash is an error that causes the application to stop working. The parameters {@code
   * message} and {@code stackTrace} are required. The other parameters are optional. The
   * {@code type} parameter can be used to specify the type of the crash, like
   * {@code NullPointerException}. The {@code category} parameter can be used to specify the
   * category of the crash, like payment failure. The {@code location}, {@code line} and
   * {@code column} can be used to specify the location of the crash. The {@code location} parameter
   * should be the name of the file where the crash occurred. The {@code line} and {@code column}
   * parameters should be the line and column number of the crash.
   *
   * @param message    The message of the crash. Must not be null.
   * @param type       The type of the crash. Can be null. Example:
   *                   {@code java.lang.NullPointerException}
   * @param category   The category of the crash. Can be null. Example: "payment failure".
   * @param stackTrace The stack trace of the crash. Must not be null.
   * @param location   The location of the crash. Can be null. Example: "MainActivity.java".
   * @param line       The line number of the crash. Can be null. Example: 42.
   * @param column     The column number of the crash. Can be null. Example: 23.
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder crash(
      @NonNull String message,
      @Nullable String type,
      @Nullable String category,
      @Nullable String stackTrace,
      @Nullable String location,
      @Nullable Integer line,
      @Nullable Integer column
  ) {
    return MatomoRequest.request()
                        .crashMessage(message)
                        .crashType(type)
                        .crashCategory(category)
                        .crashStackTrace(stackTrace)
                        .crashLocation(location)
                        .crashLine(line)
                        .crashColumn(column);
  }

  /**
   * Creates a {@link MatomoRequest} object for a crash with information from a {@link Throwable}.
   *
   * <p>Requires Crash Analytics plugin to be enabled in the target Matomo instance.
   *
   * <p>The {@code category} parameter can be used to specify the category of the crash, like
   * payment failure.
   *
   * @param throwable The throwable that caused the crash. Must not be null.
   * @param category  The category of the crash. Can be null. Example: "payment failure".
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder crash(
      @NonNull Throwable throwable, @Nullable String category
  ) {
    return MatomoRequest
        .request()
        .crashMessage(throwable.getMessage())
        .crashCategory(category)
        .crashStackTrace(formatStackTrace(throwable))
        .crashType(throwable.getClass().getName())
        .crashLocation(
            getFirstStackTraceElement(throwable)
                .map(StackTraceElement::getFileName)
                .orElse(null))
        .crashLine(
            getFirstStackTraceElement(throwable)
                .map(StackTraceElement::getLineNumber)
                .orElse(null));
  }

  @edu.umd.cs.findbugs.annotations.NonNull
  private static String formatStackTrace(@Nullable Throwable throwable) {
    StringWriter writer = new StringWriter();
    throwable.printStackTrace(new PrintWriter(writer));
    return writer.toString().trim();
  }

  @edu.umd.cs.findbugs.annotations.NonNull
  private static Optional<StackTraceElement> getFirstStackTraceElement(
      @edu.umd.cs.findbugs.annotations.NonNull Throwable throwable
  ) {
    StackTraceElement[] stackTrace = throwable.getStackTrace();
    if (stackTrace == null || stackTrace.length == 0) {
      return Optional.empty();
    }
    return Optional.of(stackTrace[0]);
  }

  /**
   * Creates a {@link MatomoRequest} object for a ecommerce cart update (add item, remove item,
   * update item).
   *
   * <p>The {@code revenue} parameter is required and should be the total revenue of the cart.
   *
   * @param revenue The total revenue of the cart. Must not be null.
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder ecommerceCartUpdate(
      @NonNull Double revenue
  ) {
    return MatomoRequest.request().ecommerceRevenue(revenue);
  }

  /**
   * Creates a {@link MatomoRequest} object for a ecommerce order.
   *
   * <p>All revenues (revenue, subtotal, tax, shippingCost, discount) will be individually summed
   * and reported in Matomo reports.
   *
   * <p>The {@code id} and {@code revenue} parameters are required and should be the order ID and
   * the total revenue of the order. The other parameters are optional. The {@code subtotal},
   * {@code tax}, {@code shippingCost} and {@code discount} parameters should be the subtotal, tax,
   * shipping cost and discount of the order.
   *
   * <p>If the Ecommerce order contains items (products), you must call
   * {@link MatomoRequest.MatomoRequestBuilder#ecommerceItems(EcommerceItems)} to add the items to
   * the request.
   *
   * @param id           An order ID. Can be a stock keeping unit (SKU) or a unique ID. Must not be
   *                     null.
   * @param revenue      The total revenue of the order. Must not be null.
   * @param subtotal     The subtotal of the order. Can be null.
   * @param tax          The tax of the order. Can be null.
   * @param shippingCost The shipping cost of the order. Can be null.
   * @param discount     The discount of the order. Can be null.
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder ecommerceOrder(
      @NonNull String id,
      @NonNull Double revenue,
      @Nullable Double subtotal,
      @Nullable Double tax,
      @Nullable Double shippingCost,
      @Nullable Double discount
  ) {
    return MatomoRequest.request()
                        .ecommerceId(id)
                        .ecommerceRevenue(revenue)
                        .ecommerceSubtotal(subtotal)
                        .ecommerceTax(tax)
                        .ecommerceShippingCost(shippingCost)
                        .ecommerceDiscount(discount);
  }

  /**
   * Creates a {@link MatomoRequest} object for an event.
   *
   * <p>The {@code category} and {@code action} parameters are required and should be the category
   * and action of the event. The {@code name} and {@code value} parameters are optional. The
   * {@code category} parameter should be a category of the event, like "Travel". The {@code action}
   * parameter should be an action of the event, like "Book flight". The {@code name} parameter
   * should be the name of the event, like "Flight to Berlin". The {@code value} parameter should be
   * the value of the event, like the price of the flight.
   *
   * @param category The category of the event. Must not be null. Example: "Music"
   * @param action   The action of the event. Must not be null. Example: "Play"
   * @param name     The name of the event. Can be null. Example: "Edvard Grieg - The Death of Ase"
   * @param value    The value of the event. Can be null. Example: 9.99
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder event(
      @NonNull String category,
      @NonNull String action,
      @Nullable String name,
      @Nullable Double value
  ) {
    return MatomoRequest.request()
                        .eventCategory(category)
                        .eventAction(action)
                        .eventName(name)
                        .eventValue(value);
  }

  /**
   * Creates a {@link MatomoRequest} object for a conversion of a goal of the website.
   *
   * <p>The {@code id} parameter is required and should be the ID of the goal. The {@code revenue},
   * {@code name} and {@code value} parameters are optional. The {@code revenue} parameter should be
   * the revenue of the conversion. The {@code name} parameter should be the name of the conversion.
   * The {@code value} parameter should be the value of the conversion.
   *
   * @param id      The ID of the goal. Must not be null. Example: 1
   * @param revenue The revenue of the conversion. Can be null. Example: 9.99
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder goal(
      int id, @Nullable Double revenue
  ) {
    return MatomoRequest.request().goalId(id).ecommerceRevenue(revenue);
  }

  /**
   * Creates a {@link MatomoRequest} object for a page view.
   *
   * <p>The {@code name} parameter is required and should be the name of the page.
   *
   * @param name The name of the page. Must not be null. Example: "Home"
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder pageView(
      @NonNull String name
  ) {
    return MatomoRequest.request().actionName(name);
  }

  /**
   * Creates a {@link MatomoRequest} object for a search.
   *
   * <p>These are used to populate reports in Actions > Site Search.
   *
   * <p>The {@code query} parameter is required and should be the search query. The {@code
   * category} and {@code resultsCount} parameters are optional. The {@code category} parameter
   * should be the category of the search, like "Music". The {@code resultsCount} parameter should
   * be the number of results of the search.
   *
   * @param query        The search query. Must not be null. Example: "Edvard Grieg"
   * @param category     The category of the search. Can be null. Example: "Music"
   * @param resultsCount The number of results of the search. Can be null. Example: 42
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder siteSearch(
      @NonNull String query, @Nullable String category, @Nullable Long resultsCount
  ) {
    return MatomoRequest.request()
                        .searchQuery(query)
                        .searchCategory(category)
                        .searchResultsCount(resultsCount);
  }

  /**
   * Creates a {@link MatomoRequest} object for a ping.
   *
   * <p>Ping requests do not track new actions. If they are sent within the standard visit
   * length (see global.ini.php), they will extend the existing visit and the current last action
   * for the visit. If after the standard visit length, ping requests will create a new visit using
   * the last action in the last known visit.
   *
   * @return A {@link MatomoRequest.MatomoRequestBuilder} object to add additional parameters.
   */
  @edu.umd.cs.findbugs.annotations.NonNull
  public static MatomoRequest.MatomoRequestBuilder ping() {
    return MatomoRequest.request().ping(true);
  }


}
