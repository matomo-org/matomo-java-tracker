Piwik Java Tracking API
================

Java implementation of the [Piwik Tracking HTTP API](http://developer.piwik.org/api-reference/tracking-api).

## Using this API
### Create a Request
Each PiwikRequest represents an action the user has taken that you want tracked by your Piwik server.  Create a PiwikRequest through
```java
PiwikRequest request = new PiwikRequest(siteId, actionUrl);
```

The following parameters are also enabled by default:

```java
required = true;
visitorId = random 16 character hex string;
randomValue = random 20 character hex string;
apiVersion = 1;
responseAsImage = false;
```

Overwrite these properties as desired.

Note that if you want to be able to track campaigns using <em>Referrers &gt; Campaigns</em>, you must add the correct URL parameters to your actionUrl.  For example, 
```java
URL actionUrl = new URL("http://example.org/landing.html?pk_campaign=Email-Nov2011&pk_kwd=LearnMore");
```
See [Tracking Campaigns](http://piwik.org/docs/tracking-campaigns/) for more information. 

All HTTP query parameters denoted on the [Piwik Tracking HTTP API](http://developer.piwik.org/api-reference/tracking-api) can be set using the appropriate getters and setters.  See <strong>PiwikRequest.java</strong> for the mappings of the parameters to their corresponding Java getters/setters.

####Some parameters are dependent on the state of other parameters:
<strong>EcommerceEnabled</strong> must be called before the following parameters are set:  <em>EcommerceId</em> and <em>EcommerceRevenue</em>.

<strong>EcommerceId</strong> and <strong>EcommerceRevenue</strong> must be set before the following parameters are set:  <em>EcommerceDiscount</em>, <em>EcommerceItem</em>, <em>EcommerceLastOrderTimestamp</em>, <em>EcommerceShippingCost</em>, <em>EcommerceSubtotal</em>, and <em>EcommerceTax</em>.

<strong>AuthToken</strong> must be set before the following parameters are set:  <em>VisitorCity</em>, <em>VisitorCountry</em>, <em>VisitorIp</em>, <em>VisitorLatitude</em>, <em>VisitorLongitude</em>, and <em>VisitorRegion</em>.

### Sending Requests
Create a PiwikTracker through
```java
PiwikTracker tracker = new PiwikTracker(hostUrl);
```
where hostUrl is the url endpoint of the Piwik server.  Usually in the format <strong>http://your-piwik-domain.tld/piwik.php</strong>.

To send a single request, call
```java
HttpResponse response = tracker.sendRequest(request);
```

If you have multiple requests to wish to track, it may be more efficient to send them in a single HTTP call.  To do this, send a bulk request.  Place your requests in an <strong>Iterable</strong> data structure and call
```java
HttpResponse response = tracker.sendBulkRequest(requests);
```
If some of the parameters that you've specified in the bulk request requre AuthToken to be set, this can also be set in the bulk request through
```java
HttpResponse response = tracker.sendBulkRequest(requests, authToken);
```
## Contact
brett.csorba@ge.com

## License
This software is released under the BSD 3-Clause license.  See [LICENSE](LICENSE).

## Copyright
Copyright (c) 2015 General Electric Company. All rights reserved.
