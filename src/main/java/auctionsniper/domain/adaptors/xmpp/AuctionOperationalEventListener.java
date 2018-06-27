package auctionsniper.domain.adaptors.xmpp;

import auctionsniper.humble.util.Announcee;

/**
 * Listener for incoming event from message broker
 * Someone within domain will take the role
 */
public interface AuctionOperationalEventListener extends Announcee {
  enum PriceSource {
    FromSniper, FromOtherBidder;
  }
  
  void auctionClosed();
  void currentPrice(int price, int increment, PriceSource priceSource);
  void auctionFailed();
}
