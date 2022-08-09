# Matomo Java Tracker

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.piwik.java.tracking/matomo-java-tracker/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/org.piwik.java.tracking/matomo-java-tracker)
[![Build Status](https://travis-ci.org/matomo-org/matomo-java-tracker.svg?branch=master)](https://travis-ci.org/matomo-org/matomo-java-tracker)
[![Average time to resolve an issue](https://isitmaintained.com/badge/resolution/matomo-org/matomo-java-tracker.svg)](https://isitmaintained.com/project/matomo-org/matomo-java-tracker "Average time to resolve an issue")
[![Percentage of issues still open](https://isitmaintained.com/badge/open/matomo-org/matomo-java-tracker.svg)](https://isitmaintained.com/project/matomo-org/matomo-java-tracker "Percentage of issues still open")

Official Java implementation of the [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api).

## Javadoc

The Javadoc for this project is hosted as a Github page for this repo. The latest Javadoc can be
found [here](https://matomo-org.github.io/matomo-java-tracker/javadoc/HEAD/index.html). Javadoc for the latest and all
releases can be found [here](https://matomo-org.github.io/matomo-java-tracker/javadoc/index.html).

## Using this API

### Add library to your build

Add a dependency on Matomo Java Tracker using Maven:

```xml

<dependency>
  <groupId>org.piwik.java.tracking</groupId>
  <artifactId>matomo-java-tracker</artifactId>
  <version>2.0</version>
</dependency>
```

or Gradle:

```groovy
dependencies {
  implementation("org.piwik.java.tracking:matomo-java-tracker:2.0")
}
```

### Create a Request

Each MatomoRequest represents an action the user has taken that you want tracked by your Matomo server. Create a
MatomoRequest through

```java

import org.matomo.java.tracking.MatomoRequest;

public class YourImplementation {

  public void yourMethod() {
    MatomoRequest request = MatomoRequest.builder()
      .siteId(42)
      .actionUrl("https://www.mydomain.com/signup")
      .actionName("Signup")
      .build();
  }

}

```

Per default every request has the following default parameters:

| Parameter Name   | Default Value                  |
|------------------|--------------------------------|
| required         | true                           |
| visitorId        | random 16 character hex string |
| randomValue      | random 20 character hex string |
| apiVersion       | 1                              |
| responseAsImage  | false                          |

Overwrite these properties as desired.

Note that if you want to be able to track campaigns using *Referrers &gt; Campaigns*, you must add the correct
URL parameters to your actionUrl. For example,

```java

package example;

import org.matomo.java.tracking.MatomoRequest;

public class YourImplementation {

  public void yourMethod() {

    MatomoRequest request = MatomoRequest.builder()
      .siteId(42)
      .actionUrl("http://example.org/landing.html?pk_campaign=Email-Nov2011&pk_kwd=LearnMore") // include the query parameters to the url
      .actionName("LearnMore")
      .build();
  }

}
```

See [Tracking Campaigns](https://matomo.org/docs/tracking-campaigns/) for more information.

All HTTP query parameters denoted on
the [Matomo Tracking HTTP API](https://developer.matomo.org/api-reference/tracking-api) can be set using the appropriate
getters and setters. See _MatomoRequest.java_ for the mappings of the parameters to their corresponding
Java getters/setters.

Some parameters are dependent on the state of other parameters:
_EcommerceEnabled_ must be called before the following parameters are set: *EcommerceId* and *
EcommerceRevenue*.

_EcommerceId_ and _EcommerceRevenue_ must be set before the following parameters are
set:  *EcommerceDiscount*, *EcommerceItem*, *EcommerceLastOrderTimestamp*, *
EcommerceShippingCost*, *EcommerceSubtotal*, and *EcommerceTax*.

_AuthToken_ must be set before the following parameters are set: *VisitorCity*, *
VisitorCountry*, *VisitorIp*, *VisitorLatitude*, *VisitorLongitude*, and *VisitorRegion*
.

### Sending Requests

Create a MatomoTracker using the constructor

```java
package example;

import org.matomo.java.tracking.MatomoTracker;

public class YourImplementation {

  public void yourMethod() {

    MatomoTracker tracker = new MatomoTracker("https://your-matomo-domain.tld/matomo.php");

  }

}
```

using the Matomo Endpoint URL as the first parameter.

To send a single request, call

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

    MatomoRequest request = MatomoRequest.builder().siteId(42).actionUrl("https://www.mydomain.com/some/page").actionName("Signup").build();

    MatomoTracker tracker = new MatomoTracker("https://your-matomo-domain.tld/matomo.php");
    try {
      Future<HttpResponse> response = tracker.sendRequestAsync(request);
      // usually not needed:
      HttpResponse httpResponse = response.get();
      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if (statusCode > 399) {
        // problem
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Could not send request to Matomo", e);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException("Error while getting response", e);
    }

  }

}
```

If you have multiple requests to wish to track, it may be more efficient to send them in a single HTTP call. To do this,
send a bulk request. Place your requests in an _Iterable_ data structure and call

```java
package example;

import org.apache.http.HttpResponse;
import org.matomo.java.tracking.MatomoRequest;
import org.matomo.java.tracking.MatomoRequestBuilder;
import org.matomo.java.tracking.MatomoTracker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class YourImplementation {

  public void yourMethod() {

    Collection<MatomoRequest> requests = new ArrayList<>();
    MatomoRequestBuilder builder = MatomoRequest.builder().siteId(42);
    requests.add(builder.actionUrl("https://www.mydomain.com/some/page").actionName("Some Page").build());
    requests.add(builder.actionUrl("https://www.mydomain.com/another/page").actionName("Another Page").build());

    MatomoTracker tracker = new MatomoTracker("https://your-matomo-domain.tld/matomo.php");
    try {
      Future<HttpResponse> response = tracker.sendBulkRequestAsync(requests);
      // usually not needed:
      HttpResponse httpResponse = response.get();
      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if (statusCode > 399) {
        // problem
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Could not send request to Matomo", e);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException("Error while getting response", e);
    }

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
import org.matomo.java.tracking.MatomoRequestBuilder;
import org.matomo.java.tracking.MatomoTracker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class YourImplementation {

  public void yourMethod() {

    Collection<MatomoRequest> requests = new ArrayList<>();
    MatomoRequestBuilder builder = MatomoRequest.builder().siteId(42);
    requests.add(builder.actionUrl("https://www.mydomain.com/some/page").actionName("Some Page").build());
    requests.add(builder.actionUrl("https://www.mydomain.com/another/page").actionName("Another Page").visitorCountry(new MatomoLocale(Locale.GERMANY)).build());

    MatomoTracker tracker = new MatomoTracker("https://your-matomo-domain.tld/matomo.php");
    try {
      Future<HttpResponse> response = tracker.sendBulkRequestAsync(requests, "33dc3f2536d3025974cccb4b4d2d98f4"); // second parameter is authentication token need for country override
      // usually not needed:
      HttpResponse httpResponse = response.get();
      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if (statusCode > 399) {
        // problem
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Could not send request to Matomo", e);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException("Error while getting response", e);
    }

  }

}


```

## Building

You need a GPG signing key on your machine. Please follow these
instructions: https://docs.github.com/en/authentication/managing-commit-signature-verification/generating-a-new-gpg-key

This project can be tested and built by calling

```shell
mvn install
```

The built jars and javadoc can be found in `target`. By using the install Maven goal, the snapshot
version can be used using your local Maven repository for testing purposes, e.g.

```xml

<dependency>
  <groupId>org.piwik.java.tracking</groupId>
  <artifactId>matomo-java-tracker</artifactId>
  <version>2.1-SNAPSHOT</version>
</dependency>
```

This project also supports [Pitest](http://pitest.org/) mutation testing. This report can be generated by calling

```shell
mvn org.pitest:pitest-maven:mutationCoverage
```

and will produce an HTML report at `target/pit-reports/YYYYMMDDHHMI`

Clean this project using

```shell
mvn clean
```

## Contribute

Have a fantastic feature idea? Spot a bug? We would absolutely love for you to contribute to this project!  Please feel
free to:

* Fork this project
* Create a feature branch from the _master_ branch
* Write awesome code that does awesome things
* Write awesome test to test your awesome code
* Verify that everything is working as it should by running _mvn test_. If everything passes, you may
  want to make sure that your tests are covering everything you think they are!
  Run `mvn org.pitest:pitest-maven:mutationCoverage` to find out!
* Commit this code to your repository
* Submit a pull request from your branch to our dev branch and let us know why you made the changes you did
* We'll take a look at your request and work to get it integrated with the repo!

## License

This software is released under the BSD 3-Clause license. See [LICENSE](LICENSE).

## Copyright

Copyright (c) 2015 General Electric Company. All rights reserved.
