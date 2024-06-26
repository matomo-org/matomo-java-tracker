# Official Matomo Java Tracker

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.piwik.java.tracking/matomo-java-tracker/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/org.piwik.java.tracking/matomo-java-tracker)
[![Build Status](https://github.com/matomo-org/matomo-java-tracker/actions/workflows/build.yml/badge.svg)](https://github.com/matomo-org/matomo-java-tracker/actions/workflows/build.yml)
[![Average time to resolve an issue](https://isitmaintained.com/badge/resolution/matomo-org/matomo-java-tracker.svg)](https://isitmaintained.com/project/matomo-org/matomo-java-tracker "Average time to resolve an issue")
[![Percentage of issues still open](https://isitmaintained.com/badge/open/matomo-org/matomo-java-tracker.svg)](https://isitmaintained.com/project/matomo-org/matomo-java-tracker "Percentage of issues still open")

The Matomo Java Tracker functions as the official Java implementation for
the [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api). This versatile tracker empowers
you to monitor visits, goals, and ecommerce transactions and items. Specifically designed for integration into
server-side applications, it seamlessly integrates with Java-based web applications or web services.

Key features:

* Comprehensive tracking capabilities: Monitor page views, goals, ecommerce transactions, and items.
* Customization options: Support for custom dimensions and variables.
* Extensive tracking parameters: Capture data on campaigns, events, downloads, outlinks, site searches, devices, and
  visitors.
* Java compatibility: Supports Java 8 and higher, with a dedicated artifact (matomo-java-tracker-java11) for Java 11.
* SSL certificate flexibility: Option to skip SSL certificate validation (caution: not recommended for production).
* Minimal runtime dependencies: Relies solely on SLF4J.
* Asynchronous request support: Permits non-blocking requests.
* Compatibility with Matomo versions 4 and 5.
* Versatile request handling: Send both single and multiple requests.
* Robust documentation: Thoroughly documented with Javadoc for easy reference.
* Data accuracy assurance: Ensures correct values are transmitted to the Matomo Tracking API.
* Logging capabilities: Include debug and error logging for effective troubleshooting.
* Seamless integration: Easily integrates into frameworks such as Spring by creating the MatomoTracker Spring bean for
  use in other beans.

Please prefer the Java 11 version as the Java 8 will become obsolete in the future.

You can find our [Developer Guide here](https://developer.matomo.org/api-reference/tracking-java)

Further information on Matomo and Matomo HTTP tracking:

* [Matomo PHP Tracker](https://github.com/matomo-org/matomo-php-tracker)
* [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api)
* [Introducing the Matomo Java Tracker](https://matomo.org/blog/2015/11/introducing-piwik-java-tracker/)
* [Tracking API User Guide](https://matomo.org/guide/apis/tracking-api/)
* [Matomo Developer](https://developer.matomo.org/)
* [The Matomo project](https://matomo.org/)

Projects that use Matomo Java Tracker:

* [Box-c - supports the UNC Libraries' Digital Collections Repository](https://github.com/UNC-Libraries/box-c)
* [DSpace - provide durable access to digital resources](https://github.com/thanvanlong/dspace)
* [Identifiers.org satellite Web SPA](https://github.com/identifiers-org/cloud-satellite-web-spa)
* [Cloud native Resolver Web Service for identifiers.org](https://github.com/identifiers-org/cloud-ws-resolver)
* [Resource Catalogue](https://github.com/madgeek-arc/resource-catalogue)
* [INCEpTION - A semantic annotation platform offering intelligent assistance and knowledge management](https://github.com/inception-project/inception)
* [QualiChain Analytics Intelligent Profiling](https://github.com/JoaoCabrita95/IP)
* [Digitale Ehrenamtskarte](https://github.com/digitalfabrik/entitlementcard)
* [skidfuscator-java-obfuscator](https://github.com/skidfuscatordev/skidfuscator-java-obfuscator)
* [DnA](https://github.com/mercedes-benz/DnA)
* And many closed source projects that we are not aware of :smile:

## Table of Contents

* [What Is New?](#what-is-new)
* [Javadoc](#javadoc)
* [Need help?](#need-help)
* [Using this API](#using-this-api)
* [Migration from Version 2 to 3](#migration-from-version-2-to-3)
* [Building and Testing](#building-and-testing)
* [Versioning](#versioning)
* [Contribute](#contribute)
* [License](#license)

## What Is New?

### Version 3.4.x

We fixed a synchronization issue in the Java 8 sender (https://github.com/matomo-org/matomo-java-tracker/issues/168). 
To consume the exact amount of space needed for the queries to send to Matomo, we need the collection size of the incoming
requests. So we changed `Iterable` to `Collection` in some `MatomoTracker`. This could affect users, that use parameters
of type `Iterable` in the tracker. Please use `Collection` instead.

### Version 3.3.x

Do you still use Matomo Java Tracker 2.x? We created version 3, that is compatible with Matomo 4 and 5 and contains
fewer
dependencies. Release notes can be found here: https://github.com/matomo-org/matomo-java-tracker/releases

Here are the most important changes:

* Matomo Java Tracker 3.4.0 is compatible with Matomo 4 and 5
* less dependencies
* new dimension parameter
* special types allow to provide valid parameters now
* a new implementation for Java 11 uses the HttpClient available since Java 11

See also the [Developer Guide here](https://developer.matomo.org/api-reference/tracking-java)

## Javadoc

The Javadoc for all versions can be found
[at javadoc.io](https://javadoc.io/doc/org.piwik.java.tracking/matomo-java-tracker-core/latest/index.html). Thanks to
[javadoc.io](https://javadoc.io) for hosting it.

## Need help?

* Check the [Developer Guide](https://developer.matomo.org/api-reference/tracking-java)
* Open an issue in the [Issue Tracker](https://github.com/matomo-org/matomo-java-tracker/issues)
* Use [our GitHub discussions](https://github.com/matomo-org/matomo-java-tracker/discussions)
* Ask your question on [Stackoverflow with the tag `matomo`](https://stackoverflow.com/questions/tagged/matomo)
* Create a thread in the [Matomo Forum](https://forum.matomo.org/)
* Contact [Matomo Support](https://matomo.org/support/)

## Using this API

See the following sections for information on how to use this API. For more information, see the Javadoc. We also
recommend to read the [Tracking API User Guide](https://matomo.org/guide/apis/tracking-api/).
The [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api) is well
documented and contains many examples.

This project contains the following Maven artifacts:

1. **matomo-java-tracker-core**: This artifact is the core module of the Matomo Java Tracker project. It provides the
   main functionality of the Matomo Java Tracker, which is a Java implementation for the Matomo Tracking HTTP API. This
   module is designed to be used as a base for other modules in the project that provide additional functionality or
   integrations.
2. **matomo-java-tracker**: This is a specific implementation of the core module designed for Java 8. It provides the
   main functionality of the Matomo Java Tracker and is built upon the core. This artifact is
   specifically designed for applications running on Java 8.
3. **matomo-java-tracker-java11**: This artifact is a Java 11 implementation of the Matomo Java Tracker. It uses the
   HttpClient available since Java 11. It is recommended to use this version if you are using Java 11 or higher.
4. **matomo-java-tracker-spring-boot-starter**: This artifact is a Spring Boot Starter for the Matomo Java Tracker. It
   provides auto-configuration for the Matomo Java Tracker in a Spring Boot application. By including this artifact in
   your project, you can take advantage of Spring Boot's auto-configuration features to automatically set up and
   configure the Matomo Java Tracker.
5. **matomo-java-tracker-servlet-jakarta**: This artifact is specifically designed for applications using the Jakarta
   Servlet API (part of Jakarta EE).
6. **matomo-java-tracker-servlet-javax**: This artifact is specifically designed for applications using the older Java
   Servlet API (part of Java EE).
7. **matomo-java-tracker-test**: This artifact contains tools for manual testing against a local Matomo instance created
   with Docker. It contains a tester class that sends randomized requests to a local Matomo instance and a servlet that
   can be used to test the servlet integration.

Each of these artifacts serves a different purpose and can be used depending on the specific needs of your project and
the Java version you are using.

### Add library to your build

Add a dependency on Matomo Java Tracker using Maven. For Java 8:

```xml

<dependency>
    <groupId>org.piwik.java.tracking</groupId>
    <artifactId>matomo-java-tracker</artifactId>
    <version>3.4.0</version>
</dependency>
```

For Java 11:

```xml

<dependency>
    <groupId>org.piwik.java.tracking</groupId>
    <artifactId>matomo-java-tracker-java11</artifactId>
    <version>3.4.0</version>
</dependency>
```

or Gradle (Java 8):

```groovy
dependencies {
    implementation("org.piwik.java.tracking:matomo-java-tracker:3.4.0")
}
```

or Gradle (Java 11):

```groovy
dependencies {
    implementation("org.piwik.java.tracking:matomo-java-tracker-java11:3.4.0")
}
```

or Gradle with Kotlin DSL (Java 8)

```kotlin
implementation("org.piwik.java.tracking:matomo-java-tracker:3.4.0")
```

or Gradle with Kotlin DSL (Java 11)

```kotlin
implementation("org.piwik.java.tracking:matomo-java-tracker-java11:3.4.0")
```

### Spring Boot Module

If you use Spring Boot 3, you can use the Spring Boot Starter artifact. It will create a MatomoTracker bean for you
and allows you to configure the tracker via application properties. Add the following dependency to your build:

```xml

<dependency>
    <groupId>org.piwik.java.tracking</groupId>
    <artifactId>matomo-java-tracker-spring-boot-starter</artifactId>
    <version>3.4.0</version>
</dependency>
```

or Gradle:

```groovy
dependencies {
    implementation("org.piwik.java.tracking:matomo-java-tracker-spring-boot-starter:3.4.0")
}
```

or Gradle with Kotlin DSL

```kotlin
implementation("org.piwik.java.tracking:matomo-java-tracker-spring-boot-starter:3.4.0")
```

The following properties are supported:

| Property Name                                | Description                                                                                                                                            |
|----------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| matomo.tracker.api-endpoint (required)       | The URL to the Matomo Tracking API endpoint. Must be set.                                                                                              |
| matomo.tracker.default-site-id               | If you provide a default site id, it will be taken if the action does not contain a site id.                                                           |
| matomo.tracker.default-token-auth            | If you provide a default token auth, it will be taken if the action does not contain a token auth.                                                     |
| matomo.tracker.enabled                       | The tracker is enabled per default. You can disable it per configuration with this flag.                                                               |
| matomo.tracker.log-failed-tracking           | Will send errors to the log if the Matomo Tracking API responds with an erroneous HTTP code                                                            |
| matomo.tracker.connect-timeout               | allows you to change the default connection timeout of 10 seconds. 0 is interpreted as infinite, null uses the system default                          |
| matomo.tracker.socket-timeout                | allows you to change the default socket timeout of 10 seconds. 0 is interpreted as infinite, null uses the system default                              |
| matomo.tracker.user-agent                    | used by the request made to the endpoint is `MatomoJavaClient` per default. You can change it by using this builder method.                            |
| matomo.tracker.proxy-host                    | The hostname or IP address of an optional HTTP proxy. `proxyPort` must be configured as well                                                           |
| matomo.tracker.proxy-port                    | The port of an HTTP proxy. `proxyHost` must be configured as well.                                                                                     |
| matomo.tracker.proxy-username                | If the HTTP proxy requires a username for basic authentication, it can be configured with this method. Proxy host, port and password must also be set. |
| matomo.tracker.proxy-password                | The corresponding password for the basic auth proxy user. The proxy host, port and username must be set as well.                                       |
| matomo.tracker.disable-ssl-cert-validation   | If set to true, the SSL certificate of the Matomo server will not be validated. This should only be used for testing purposes. Default: false          |
| matomo.tracker.disable-ssl-host-verification | If set to true, the SSL host of the Matomo server will not be validated. This should only be used for testing purposes. Default: false                 |
| matomo.tracker.thread-pool-size              | The number of threads that will be used to asynchronously send requests. Default: 2                                                                    |
| matomo.tracker.filter.enabled                | Enables a servlet filter that tracks every request within the application                                                                              |

To ensure the `MatomoTracker` bean is created by the auto configuration, you have to add the following property to
your `application.properties` file:

```properties
matomo.tracker.api-endpoint=https://your-matomo-domain.tld/matomo.php
```

Or if you use YAML:

```yaml
matomo:
    tracker:
        api-endpoint: https://your-matomo-domain.tld/matomo.php
```

You can automatically add the `MatomoTrackerFilter` to your Spring Boot application if you add the following property:

```properties
matomo.tracker.filter.enabled=true
```

Or if you use YAML:

```yaml
matomo:
    tracker:
        filter:
            enabled: true
```

The filter uses `ServletMatomoRequest` to create a `MatomoRequest` from a `HttpServletRequest` on every filter call.

### Sending a Tracking Request

To let the Matomo Java Tracker send a request to the Matomo instance, you need the following minimal code:

```java
import java.net.URI;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * Example for sending a request.
 */
public class SendExample {

    /**
     * Example for sending a request.
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        TrackerConfiguration configuration = TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://www.yourdomain.com/matomo.php"))
                .defaultSiteId(1)
                .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
                .logFailedTracking(true)
                .build();

        try (MatomoTracker tracker = new MatomoTracker(configuration)) {
            tracker.sendRequestAsync(MatomoRequests
                    .event("Training", "Workout completed", "Bench press", 60.0)
                    .visitorId(VisitorId.fromString("customer@mail.com"))
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not close tracker", e);
        }
    }

}

```

This will send a request to the Matomo instance at https://www.yourdomain.com/matomo.php and track a page view for the
visitor customer@mail.com with the action name "Checkout" and action URL "https://www.yourdomain.com/checkout" for
the site with id 1. The request will be sent asynchronously, that means the method will return immediately and your
application will not wait for the response of the Matomo server. In the configuration we set the default site id to 1
and configure the default auth token. With `logFailedTracking` we enable logging of failed tracking requests.

If you want to perform an operation after a successful asynchronous call to Matomo, you can use the completable future
result like this:

```java
import java.net.URI;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * Example for sending a request and performing an action when the request was sent successfully.
 */
public class ConsumerExample {

    /**
     * Example for sending a request and performing an action when the request was sent successfully.
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        TrackerConfiguration configuration = TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://www.yourdomain.com/matomo.php"))
                .defaultSiteId(1)
                .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
                .logFailedTracking(true)
                .build();

        try (MatomoTracker tracker = new MatomoTracker(configuration)) {
            MatomoRequest request = MatomoRequests
                    .event("Training", "Workout completed", "Bench press", 60.0)
                    .visitorId(VisitorId.fromString("customer@mail.com"))
                    .build();

            tracker.sendRequestAsync(request)
                    .thenAccept(req -> System.out.printf("Sent request %s%n", req))
                    .exceptionally(throwable -> {
                        System.err.printf("Failed to send request: %s%n", throwable.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            throw new RuntimeException("Could not close tracker", e);
        }

    }

}


```

If you have multiple requests to wish to track, it may be more efficient to send them in a single HTTP call. To do this,
send a bulk request. Place your requests in an _Iterable_ data structure and call

```java
import java.net.URI;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * Example for sending multiple requests in one bulk request.
 */
public class BulkExample {

    /**
     * Example for sending multiple requests in one bulk request.
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        TrackerConfiguration configuration = TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://www.yourdomain.com/matomo.php"))
                .defaultSiteId(1)
                .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
                .logFailedTracking(true)
                .build();

        try (MatomoTracker tracker = new MatomoTracker(configuration)) {
            VisitorId visitorId = VisitorId.fromString("customer@mail.com");
            tracker.sendBulkRequestAsync(
                    MatomoRequests.siteSearch("Running shoes", "Running", 120L)
                            .visitorId(visitorId).build(),
                    MatomoRequests.pageView("VelocityStride ProX Running Shoes")
                            .visitorId(visitorId).build(),
                    MatomoRequests.ecommerceOrder("QXZ-789LMP", 100.0, 124.0, 19.0, 10.0, 5.0)
                            .visitorId(visitorId)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not close tracker", e);
        }

    }

}
```

This will send two requests in a single HTTP call. The requests will be sent asynchronously.

Per default every request has the following default parameters:

| Parameter Name  | Default Value                  |
|-----------------|--------------------------------|
| required        | true                           |
| visitorId       | random 16 character hex string |
| randomValue     | random 20 character hex string |
| apiVersion      | 1                              |
| responseAsImage | false                          |

Overwrite these properties as desired. We strongly recommend your to determine the visitor id for every user using
a unique identifier, e.g. an email address. If you do not provide a visitor id, a random visitor id will be generated.

Ecommerce requests contain ecommerce items, that can be fluently build:

```java
import java.net.URI;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.matomo.java.tracking.parameters.EcommerceItem;
import org.matomo.java.tracking.parameters.EcommerceItems;
import org.matomo.java.tracking.parameters.VisitorId;

/**
 * Example for sending an ecommerce request.
 */
public class EcommerceExample {

    /**
     * Example for sending an ecommerce request.
     *
     * @param args ignored
     */
    public static void main(String[] args) {

        TrackerConfiguration configuration = TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://www.yourdomain.com/matomo.php"))
                .defaultSiteId(1)
                .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
                .logFailedTracking(true)
                .build();

        try (MatomoTracker tracker = new MatomoTracker(configuration)) {
            tracker.sendBulkRequestAsync(MatomoRequests
                    .ecommerceCartUpdate(50.0)
                    .ecommerceItems(EcommerceItems
                            .builder()
                            .item(EcommerceItem
                                    .builder()
                                    .sku("XYZ12345")
                                    .name("Matomo - The big book about web analytics")
                                    .category("Education & Teaching")
                                    .price(23.1)
                                    .quantity(2)
                                    .build())
                            .item(EcommerceItem
                                    .builder()
                                    .sku("B0C2WV3MRJ")
                                    .name("Matomo for data visualization")
                                    .category("Education & Teaching")
                                    .price(15.0)
                                    .quantity(1)
                                    .build())
                            .build())
                    .visitorId(VisitorId.fromString("customer@mail.com"))
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not close tracker", e);
        }

    }

}
```

Note that if you want to be able to track campaigns using *Referrers &gt; Campaigns*, you must add the correct
URL parameters to your actionUrl. See [Tracking Campaigns](https://matomo.org/docs/tracking-campaigns/) for more
information. All HTTP query parameters
denoted on the [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api) can be set using the
appropriate getters and setters. See
[MatomoRequest](core/src/main/java/org/matomo/java/tracking/MatomoRequest.java) for the mappings of the parameters to
their corresponding attributes.

Requests are validated prior to sending. If a request is invalid, a `MatomoException` will be thrown.

In a Servlet environment, it might be easier to use the `ServletMatomoRequest` class to create a `MatomoRequest` from a
`HttpServletRequest`:

```java
import jakarta.servlet.http.HttpServletRequest;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequests;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.parameters.VisitorId;
import org.matomo.java.tracking.servlet.JakartaHttpServletWrapper;
import org.matomo.java.tracking.servlet.ServletMatomoRequest;

public class ServletMatomoRequestExample {

    private final MatomoTracker tracker;

    public ServletMatomoRequestExample(MatomoTracker tracker) {
        this.tracker = tracker;
    }

    public void someControllerMethod(HttpServletRequest request) {
        MatomoRequest matomoRequest = ServletMatomoRequest
                .addServletRequestHeaders(
                        MatomoRequests.contentImpression(
                                "Latest Product Announced",
                                "Main Blog Text",
                                "https://www.yourdomain.com/blog/2018/10/01/new-product-launches"
                        ),
                        JakartaHttpServletWrapper.fromHttpServletRequest(request)
                ).visitorId(VisitorId.fromString("customer@mail.com"))
                // ...
                .build();
        tracker.sendRequestAsync(matomoRequest);
        // ...
    }

}
```

The `ServletMatomoRequest` automatically sets the action URL, applies browser request headers, corresponding Matomo
cookies and the visitor IP address. It sets the visitor ID, Matomo session ID, custom variables and heatmap
if Matomo cookies are present. Since there was a renaming from Java EE (javax) to Jakarta EE (jakarta), we provide a
wrapper class `JakartaHttpServletWrapper` for Jakarta and `JavaxHttpServletWrapper` for javax.

### Tracking Configuration

The `MatomoTracker` can be configured using the `TrackerConfiguration` object. The following configuration options are
available:

* `.apiEndpoint(...)` An `URI` object that points to the Matomo Tracking API endpoint of your Matomo installation. Must
  be set.
* `.defaultSiteId(...)` If you provide a default site id, it will be taken if the action does not contain a site id.
* `.defaultTokenAuth(...)` If you provide a default token auth, it will be taken if the action does not contain a token
  auth.
* `.enabled(...)` The tracker is enabled per default. You can disable it per configuration with this flag.
* `.logFailedTracking(...)` Will send errors to the log if the Matomo Tracking API responds with an erroneous HTTP code
* `.connectTimeout(...)` allows you to change the default connection timeout of 10 seconds. 0 is
  interpreted as infinite, null uses the system default
* `.socketTimeout(...)` allows you to change the default socket timeout of 10 seconds. 0 is
  interpreted as infinite, null uses the system default
* `.userAgent(...)` used by the request made to the endpoint is `MatomoJavaClient` per default. You can change it by
  using this builder method.
* `.proxyHost(...)` The hostname or IP address of an optional HTTP proxy. `proxyPort` must be
  configured as well
* `.proxyPort(...)` The port of an HTTP proxy. `proxyHost` must be configured as well.
* `.proxyUsername(...)` If the HTTP proxy requires a username for basic authentication, it can be
  configured with this method. Proxy host, port and password must also be set.
* `.proxyPassword(...)` The corresponding password for the basic auth proxy user. The proxy host,
  port and username must be set as well.
* `.disableSslCertValidation(...)` If set to true, the SSL certificate of the Matomo server will not be validated. This
  should only be used for testing purposes. Default: false
* `.disableSslHostVerification(...)` If set to true, the SSL host of the Matomo server will not be validated. This
  should only be used for testing purposes. Default: false
* `.threadPoolSize(...)` The number of threads that will be used to asynchronously send requests. Default: 2

## Migration from Version 2 to 3

We improved this library by adding the dimension parameter and removing outdated parameters in Matomo version 5,
removing some dependencies (that even contained vulnerabilities) and increasing maintainability. Sadly this includes the
following breaking changes:

### Removals

* The parameter `actionTime` (`gt_ms`) is no longer supported by Matomo 5 and was removed.
* Many methods marked as deprecated in version 2 were removed. Please see the
  former [Javadoc](https://javadoc.io/doc/org.piwik.java.tracking/matomo-java-tracker/2.1/index.html) of version 2 to
  get the
  deprecated methods.
* We removed the vulnerable dependency to the Apache HTTP client. Callbacks are no longer of
  type `FutureCallback<HttpResponse>`, but
  `Consumer<Void>` instead.
* The `send...` methods of `MatomoTracker` no longer return a value (usually Matomo always returns an HTTP 204 response
  without a body). If the request fails, an exception will be thrown.
* Since there are several ways on how to set the auth token, `verifyAuthTokenSet` was removed. Just check yourself,
  whether your auth token is null. However, the tracker checks, whether an auth token is either set by parameter, by
  request or per configuration.
* Due to a major refactoring on how the queries are created, we no longer use a large map instead of concrete attributes
  to collect the Matomo parameters. Therefore `getParameters()` of class `MatomoRequest` no longer exists. Please use
  getters and setters instead.
* The methods `verifyEcommerceEnabled()` and `verifyEcommerceState()` were removed from `MatomoRequest`. The request
  will be validated prior to sending and not during construction.
* `getRandomHexString` was removed. Use `RandomValue.random()` or `VisitorId.random()` instead.

### Type Changes and Renaming

* `requestDatetime`, `visitorPreviousVisitTimestamp`, `visitorFirstVisitTimestamp`, `ecommerceLastOrderTimestamp` are
  of type `Instant`. You can use `Instant.ofEpochSecond()` to create
  them from epoch seconds.
* `requestDatetime` was renamed to `requestTimestamp` due to setter collision and downwards compatibility
* `goalRevenue` is the same parameter as `ecommerceRevenue` and was removed to prevent duplication.
  Use `ecommerceRevenue` instead.
* `setEventValue` requires a double parameter
* `setEcommerceLastOrderTimestamp` requires an `Instant` parameter
* `headerAcceptLanguage` is of type `AcceptLanguage`. You can build it easily
  using `AcceptLanguage.fromHeader("de")`
* `visitorCountry` is of type `Country`. You can build it easily using `AcceptLanguage.fromCode("fr")`
* `deviceResolution` is of type `DeviceResolution`. You can build it easily
  using `DeviceResolution.builder.width(...).height(...).build()`. To easy the migration, we added a constructor
  method `DeviceResolution.fromString()` that accepts inputs of kind _width_x_height_, e.g. `100x200`
* `pageViewId` is of type `UniqueId`. You can build it easily using `UniqueId.random()`
* `randomValue` is of type `RandomValue`. You can build it easily using `RandomValue.random()`. However, if you
  really
  want to insert a custom string here, use `RandomValue.fromString()` construction method.
* URL was removed due to performance and complicated exception handling and problems with parsing of complex
  URLs. `actionUrl`, `referrerUrl`, `outlinkUrl`, `contentTarget` and `downloadUrl` are strings.
* `getCustomTrackingParameter()` of `MatomoRequest` returns an unmodifiable list.
* Instead of `IllegalStateException` the tracker throws `MatomoException`
* In former versions the goal id had always to be zero or null. You can define higher numbers than zero.
* For more type changes see the sections below.

### Visitor ID

* `visitorId` and `visitorCustomId` are of type `VisitorId`. You can build them easily
  using `VisitorId.fromHash(...)`.
* You can use `VisitorId.fromHex()` to create a `VisitorId` from a string that contains only hexadecimal characters.
* Or simply use `VisitorId.fromUUID()` to create a `VisitorId` from a `UUID` object.
* VisitorId.fromHex() supports less than 16 hexadecimal characters. If the string is shorter than 16 characters,
  the remaining characters will be filled with zeros.

### Custom Variables

* According to Matomo, custom variables should no longer be used. Please use dimensions instead. Dimension support has
  been introduced.
* `CustomVariable` is in package `org.matomo.java.tracking.parameters`.
* `customTrackingParameters` in `MatomoRequestBuilder` requires a `Map<String, Collection<String>>` instead
  of `Map<String, String>`
* `pageCustomVariables` and `visitCustomVariables` are of type `CustomVariables` instead of collections. Create them
  with `new CustomVariables().add(customVariable)`
* `setPageCustomVariable` and `getPageCustomVariable` no longer accept a string as an index. Please use integers
  instead.
* Custom variables will be sent URL encoded

## Building and testing

This project can be tested and built by calling

```shell
mvn install
```

The built jars and javadoc can be found in `target`. By using
the Maven goal `install, a snapshot
version can be used in your local Maven repository for testing purposes, e.g.

```xml
<dependency>
    <groupId>org.piwik.java.tracking</groupId>
    <artifactId>matomo-java-tracker</artifactId>
    <version>3.4.1-SNAPSHOT</version>
</dependency>
```

## Testing on a local Matomo instance

To start a local Matomo instance for testing, you can use the docker-compose file in the root directory of this project.
Start the docker containers with

```shell
docker-compose up -d
```

You need to adapt your config.ini.php file and change
the following line:

```ini
[General]
trusted_hosts[] = "localhost:8080"
```

to

```ini
[General]
trusted_hosts[] = "localhost:8080"
```

After that you can access Matomo at http://localhost:8080. You have to set up Matomo first. The database credentials are
`matomo` and `matomo`. The database name is `matomo`. The (internal) database host address is `database`. The database
port is `3306`. Set the URL to http://localhost and enable ecommerce.

The following snippets helps you to do this quickly:

```shell
docker-compose exec matomo sed -i 's/localhost/localhost:8080/g' /var/www/html/config/config.ini.php
```

After the installation you can run `MatomoTrackerTester` in the module `test` to test the tracker. It will send
multiple randomized
requests to the local Matomo instance.

To enable debug logging, you append the following line to the `config.ini.php` file:

```ini
[Tracker]
debug = 1
```

Use the following snippet to do this:

```shell
docker-compose exec matomo sh -c 'echo -e "\n\n[Tracker]\ndebug = 1\n" >> /var/www/html/config/config.ini.php'
```

To test the servlet integration, run `MatomoServletTester` in your favorite IDE. It starts an embedded Jetty server
that serves a simple servlet. The servlet sends a request to the local Matomo instance if you call the URL
http://localhost:8090/track.html. Maybe you need to disable support for the Do Not Track preference in Matomo to get the
request tracked: Go to _Administration &gt; Privacy &gt; Do Not Track_ and disable the checkbox _Respect Do Not Track.
We also recommend to install the Custom Variables plugin from Marketplace to the test custom variables feature and
setup some dimensions.

## Versioning

We use [SemVer](https://semver.org/) for versioning. For the versions available, see
the [tags on this repository](https://github.com/matomo-org/matomo-java-tracker/tags).

## Contribute

Have a fantastic feature idea? Spot a bug? We would absolutely love for you to contribute to this project!  Please feel
free to:

* Fork this project
* Create a feature branch from the _master_ branch
* Write awesome code that does awesome things
* Write awesome test to test your awesome code
* Verify that everything is working as it should by running _mvn test_. If everything passes, you may
  want to make sure that your tests are covering everything you think they are!
  Run `mvn verify` to find out!
* Commit this code to your repository
* Submit a pull request from your branch to our dev branch and let us know why you made the changes you did
* We'll take a look at your request and work to get it integrated with the repo!

Please read [the contribution document](CONTRIBUTING.md) for details on our code of conduct, and the
process for submitting pull requests to us.

We use Checkstyle and JaCoCo to ensure code quality. Please run `mvn verify` before submitting a pull request. Please
provide tests for your changes. We use JUnit 5 for testing. Coverage should be at least 80%.

## Other Java Matomo Tracker Implementations

* [The original Piwik Java Tracker Implementation](https://github.com/summitsystemsinc/piwik-java-tracking)
* [Matomo SDK for Android](https://github.com/matomo-org/matomo-sdk-android)
* [Piwik SDK Android]( https://github.com/lkogler/piwik-sdk-android)
* [piwik-tracking](https://github.com/ralscha/piwik-tracking)
* [Matomo Tracking API Java Client](https://github.com/dheid/matomo-tracker) -> Most of the code was integrated in the
  official Matomo Java Tracker

## License

This software is released under the BSD 3-Clause license. See [LICENSE](LICENSE).

## Copyright

Copyright (c) 2015 General Electric Company. All rights reserved.


