package auctionsniper.ports.xmpp;

public interface XMPPFailureReporter { 
  void cannotTranslateMessage(String auctionId, String failedMessage, Exception exception);
}