package org.matomo.java.tracking;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.StreamSupport;

class ServiceLoaderSenderFactory implements SenderFactory {

  @Override
  public Sender createSender(TrackerConfiguration trackerConfiguration, QueryCreator queryCreator) {
    ServiceLoader<SenderProvider> serviceLoader = ServiceLoader.load(SenderProvider.class);
    Map<String, SenderProvider> senderProviders = StreamSupport
        .stream(serviceLoader.spliterator(), false)
        .collect(toMap(senderProvider -> senderProvider.getClass().getName(), Function.identity()));
    SenderProvider senderProvider = senderProviders.get("org.matomo.java.tracking.Java17SenderProvider");
    if (senderProvider == null) {
      senderProvider = senderProviders.get("org.matomo.java.tracking.Java8SenderProvider");
    }
    if (senderProvider == null) {
      throw new MatomoException("No SenderProvider found");
    }
    return senderProvider.provideSender(trackerConfiguration, new QueryCreator(trackerConfiguration));
  }

}
