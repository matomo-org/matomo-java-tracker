package org.matomo.java.tracking;

interface SenderProvider {

  Sender provideSender(TrackerConfiguration trackerConfiguration, QueryCreator queryCreator);

}
