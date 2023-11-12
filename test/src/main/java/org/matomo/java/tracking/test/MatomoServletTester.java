package org.matomo.java.tracking.test;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.MatomoTrackerFilter;
import org.matomo.java.tracking.TrackerConfiguration;

@Slf4j
class MatomoServletTester {
  public static void main(String[] args) throws Exception {

    ServletHolder servletHolder = new ServletHolder("default", new DefaultServlet());
    servletHolder.setInitParameter(
        "resourceBase",
        MatomoServletTester.class.getClassLoader().getResource("web").toExternalForm()
    );

    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    context.addServlet(servletHolder, "/");
    context.addFilter(new FilterHolder(new MatomoTrackerFilter(new MatomoTracker(
        TrackerConfiguration
            .builder()
            .apiEndpoint(URI.create("http://localhost:8080/matomo.php"))
            .defaultSiteId(1)
            .defaultAuthToken("ee6e3dd9ed1b61f5328cf5978b5a8c71")
            .logFailedTracking(true)
            .build()))), "/*", null);

    Server server = new Server(8090);
    server.setHandler(context);
    server.start();
    server.join();

  }
}