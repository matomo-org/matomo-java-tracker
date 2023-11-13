package org.matomo.java.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.Test;
import org.matomo.java.tracking.servlet.MatomoTrackerFilter;

class MatomoTrackerFilterIT {

  @Test
  void sendsAnAsyncRequestOnFilter() throws Exception {


    TestSenderFactory senderFactory = new TestSenderFactory();

    MatomoTracker tracker = new MatomoTracker(
        TrackerConfiguration
            .builder()
            .apiEndpoint(URI.create("http://localhost:8080/matomo.php"))
            .defaultSiteId(1)
            .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
            .logFailedTracking(true)
            .build());
    tracker.setSenderFactory(senderFactory);

    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    context.addFilter(new FilterHolder(new MatomoTrackerFilter(tracker)), "/*", null);
    Server server = new Server(0);
    server.setHandler(context);

    server.start();
    URI uri = server.getURI();
    HttpClient.newHttpClient().send(
        HttpRequest.newBuilder()
                   .header("Accept-Language", "en-US,en;q=0.9,de;q=0.8")
                   .uri(uri)
                   .build(),
        HttpResponse.BodyHandlers.discarding()
    );
    server.stop();

    TestSender testSender = senderFactory.getTestSender();
    assertThat(testSender.getRequests()).hasSize(1).satisfiesExactly(matomoRequest -> {
      assertThat(matomoRequest.getActionUrl()).isEqualTo(uri.toString());
      assertThat(matomoRequest.getVisitorId()).isNotNull();
      assertThat(matomoRequest.getVisitorIp()).isNotNull();
      assertThat(matomoRequest.getHeaders()).containsEntry(
          "accept-language",
          "en-US,en;q=0.9,de;q=0.8"
      );
    });

  }

}