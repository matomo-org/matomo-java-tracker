package org.matomo.java.tracking;

import lombok.Getter;

class TestSenderFactory implements SenderFactory {

  @Getter
  private TestSender testSender;

  @Override
  public Sender createSender(TrackerConfiguration trackerConfiguration, QueryCreator queryCreator) {
    TestSender testSender = new TestSender(trackerConfiguration, queryCreator);
    this.testSender = testSender;
    return testSender;
  }
}
