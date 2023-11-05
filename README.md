# Official Matomo Java Tracker

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.piwik.java.tracking/matomo-java-tracker/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/org.piwik.java.tracking/matomo-java-tracker)
[![Build Status](https://github.com/matomo-org/matomo-java-tracker/actions/workflows/build.yml/badge.svg)](https://github.com/matomo-org/matomo-java-tracker/actions/workflows/build.yml)
[![Average time to resolve an issue](https://isitmaintained.com/badge/resolution/matomo-org/matomo-java-tracker.svg)](https://isitmaintained.com/project/matomo-org/matomo-java-tracker "Average time to resolve an issue")
[![Percentage of issues still open](https://isitmaintained.com/badge/open/matomo-org/matomo-java-tracker.svg)](https://isitmaintained.com/project/matomo-org/matomo-java-tracker "Percentage of issues still open")

Matomo Java Tracker is the official Java implementation of
the [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api). The tracker allows you to track
visits, goals and ecommerce transactions and items. It is designed to be used in server-side applications, such as
Java-based web applications or web services.

Features include:

* Track page views, goals, ecommerce transactions and items
* Supports custom dimensions and custom variables
* Includes tracking parameters for campaigns, events, downloads, outlinks, site search, devices, visitors
* Supports Java 8 and higher
* Allows you to skip SSL certificate validation (not recommended for production)
* Contains nearly no runtime dependencies (only SLF4J)
* Allows asynchronous requests
* Supports Matomo 4 and 5
* Single and multiple requests can be sent
* Well documented with Javadoc
* Ensures correct values are sent to Matomo Tracking API
* Includes debug and error logging
* Easy to integrate in frameworks, e.g. Spring: Just create the MatomoTracker Spring bean and use it in other beans

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

Do you still use Matomo Java Tracker 2.x? We created version 3, that is compatible with Matomo 4 and 5 and contains fewer
dependencies. Release notes can be found here: https://github.com/matomo-org/matomo-java-tracker/releases

Here are the most important changes:

* Matomo Java Tracker 3.x is compatible with Matomo 4 and 5
* less dependencies
* new dimension parameter
* special types allow to provide valid parameters now

## Javadoc

The Javadoc for this project is hosted as a GitHub page for this repo. The latest Javadoc can be
found [here](https://matomo-org.github.io/matomo-java-tracker/javadoc/index.html). Javadoc folder older versions can be
found here: https://javadoc.io/doc/org.piwik.java.tracking/matomo-java-tracker
You can also build the Javadoc yourself. See the section [Building](#building-and-testing) below.

## Need help?

* Open an issue in the [Issue Tracker](https://github.com/matomo-org/matomo-java-tracker/issues)
* Use [our GitHub discussions](https://github.com/matomo-org/matomo-java-tracker/discussions)
* Ask your question on [Stackoverflow with the tag `matomo`](https://stackoverflow.com/questions/tagged/matomo)
* Create a thread in the [Matomo Forum](https://forum.matomo.org/)
* Contact [Matomo Support](https://matomo.org/support/)

## Using this API

See the following sections for information on how to use this API. For more information, see the Javadoc. We also
recommend
to read the [Tracking API User Guide](https://matomo.org/guide/apis/tracking-api/).
The [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api) is well
documented and contains many examples.

### Add library to your build

Add a dependency on Matomo Java Tracker using Maven:

```xml

<dependency>
    <groupId>org.piwik.java.tracking</groupId>
    <artifactId>matomo-java-tracker</artifactId>
    <version>3.0.0-rc1</version>
</dependency>
```

or Gradle:

```groovy
dependencies {
    implementation("org.piwik.java.tracking:matomo-java-tracker:3.0.0-rc1")
}
```

or Gradle with Kotlin DSL:

```kotlin
implementation("org.piwik.java.tracking:matomo-java-tracker:3.0.0-rc1")
```

### Create a Request

Each MatomoRequest represents an action the user has taken that you want tracked by your Matomo server. Create a
MatomoRequest through

```java

import org.matomo.java.tracking.MatomoRequest;

public class YourImplementation {

    public void yourMethod() {
        MatomoRequest request = MatomoRequest
                .builder()
                .siteId(42)
                .actionUrl("https://www.mydomain.com/signup")
                .actionName("Signup")
                .build();
    }

}

```

Per default every request has the following default parameters:

| Parameter Name  | Default Value                  |
|-----------------|--------------------------------|
| required        | true                           |
| visitorId       | random 16 character hex string |
| randomValue     | random 20 character hex string |
| apiVersion      | 1                              |
| responseAsImage | false                          |

Overwrite these properties as desired.

Note that if you want to be able to track campaigns using *Referrers &gt; Campaigns*, you must add the correct
URL parameters to your actionUrl. For example,

```java

package example;

import org.matomo.java.tracking.MatomoRequest;

public class YourImplementation {

    public void yourMethod() {

        MatomoRequest request = MatomoRequest
                .builder()
                .siteId(42)
                .actionUrl("https://example.org/landing.html?pk_campaign=Email-Nov2011&pk_kwd=LearnMore") // include the query parameters to the url
                .actionName("LearnMore")
                .build();
    }

}
```

See [Tracking Campaigns](https://matomo.org/docs/tracking-campaigns/) for more information. All HTTP query parameters
denoted on
the [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api) can be set using the appropriate
getters and setters. See [MatomoRequest](src/main/java/org/matomo/java/tracking/MatomoRequest.java) for the mappings of
the
parameters to their corresponding attributes.

Requests are validated prior to sending. If a request is invalid, a `MatomoException` will be thrown.

### Sending Requests

Create a MatomoTracker using the constructor

```java
package example;

import java.net.URI;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;

public class YourImplementation {

    public void yourMethod() {

        MatomoTracker tracker = new MatomoTracker(TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://your-matomo-domain.tld/matomo.php"))
                .build());

    }

}
```

The Matomo Tracker currently supports the following builder methods:

* `.apiEndpoint(...)` An `URI` object that points to the Matomo Tracking API endpoint of your Matomo installation. Must
  be set.
* `.defaultSiteId(...)` If you provide a default site id, it will be taken if the action does not contain a site id.
* `.defaultTokenAuth(...)` If you provide a default token auth, it will be taken if the action does not contain a token
  auth.
* `.delay(...)` The duration on how long the tracker collects actions until they will be sent out as a bulk request.
  Default: 1 seconds
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
* `.proxyUserName(...)` If the HTTP proxy requires a username for basic authentication, it can be
  configured with this method. Proxy host, port and password must also be set.
* `.proxyPassword(...)` The corresponding password for the basic auth proxy user. The proxy host,
  port and username must be set as well.
* `.disableSslCertValidation(...)` If set to true, the SSL certificate of the Matomo server will not be validated. This
  should only be used for testing purposes. Default: false
* `.disableSslHostVerification(...)` If set to true, the SSL host of the Matomo server will not be validated. This
  should
  only be used for testing purposes. Default: false

To send a single request synchronously via GET, call

```java
package example;

import org.apache.http.HttpResponse;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoTracker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class YourImplementation {

    public void yourMethod() {

        MatomoRequest request = MatomoRequest
                .builder()
                .siteId(42)
                .actionUrl("https://www.mydomain.com/some/page")
                .actionName("Signup")
                .build();

        MatomoTracker tracker = new MatomoTracker(TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://your-matomo-domain.tld/matomo.php"))
                .build());

        tracker.sendRequestAsync(request);

    }

}
```

If you have multiple requests to wish to track, it may be more efficient to send them in a single HTTP call. To do this,
send a bulk request. Place your requests in an _Iterable_ data structure and call

```java
package example;

import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoTracker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class YourImplementation {

    public void yourMethod() {

        Collection<MatomoRequest> requests = new ArrayList<>();
        MatomoRequestBuilder builder = MatomoRequest.request().siteId(42);
        requests.add(builder.actionUrl("https://www.mydomain.com/some/page").actionName("Some Page").build());
        requests.add(builder.actionUrl("https://www.mydomain.com/another/page").actionName("Another Page").build());

        MatomoTracker tracker = new MatomoTracker(TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://your-matomo-domain.tld/matomo.php"))
                .build());

        tracker.sendBulkRequestAsync(requests);

    }

}

```

If some of the parameters that you've specified in the bulk request require AuthToken to be set, this can also be set in
the bulk request through

```java
package example;

import org.apache.http.HttpResponse;
import org.matomo.java.tracking.MatomoLocale;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoTracker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class YourImplementation {

    public void yourMethod() {

        Collection<MatomoRequest> requests = new ArrayList<>();
        MatomoRequestBuilder builder = MatomoRequest.request().siteId(42);
        requests.add(builder.actionUrl("https://www.mydomain.com/some/page").actionName("Some Page").build());
        requests.add(builder
                .actionUrl("https://www.mydomain.com/another/page")
                .actionName("Another Page")
                .visitorCountry(new MatomoLocale(Locale.GERMANY))
                .build());

        MatomoTracker tracker = new MatomoTracker(TrackerConfiguration
                .builder()
                .apiEndpoint(URI.create("https://your-matomo-domain.tld/matomo.php"))
                .build());

        // second parameter is authentication token need for country override
        tracker.sendBulkRequestAsync(requests, "33dc3f2536d3025974cccb4b4d2d98f4");


    }

}
```

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
  with `CustomVariables.builder().variable(customVariable)`
* `setPageCustomVariable` and `getPageCustomVariable` no longer accept a string as an index. Please use integers
  instead.
* Custom variables will be sent URL encoded

## Building and testing

This project can be tested and built by calling

```shell
mvn install
```

The built jars and javadoc can be found in `target`. By using the Maven goal `install, a snapshot
version can be used in your local Maven repository for testing purposes, e.g.

```xml

<dependency>
    <groupId>org.piwik.java.tracking</groupId>
    <artifactId>matomo-java-tracker</artifactId>
    <version>3.0.0-rc2-SNAPSHOT</version>
</dependency>
```

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

* [Matomo SDK for Android](https://github.com/matomo-org/matomo-sdk-android)
* [piwik-tracking](https://github.com/ralscha/piwik-tracking)
* [Matomo Tracking API Java Client](https://github.com/dheid/matomo-tracker) -> Most of the code was integrated in the
  official Matomo Java Tracker

## License

This software is released under the BSD 3-Clause license. See [LICENSE](LICENSE).

## Copyright

Copyright (c) 2015 General Electric Company. All rights reserved.


