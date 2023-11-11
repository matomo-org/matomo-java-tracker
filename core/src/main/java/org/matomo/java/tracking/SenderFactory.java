package org.matomo.java.tracking;

/**
 * A factory for {@link Sender} instances.
 */
public interface SenderFactory {

  Sender createSender(TrackerConfiguration trackerConfiguration, QueryCreator queryCreator);

}
