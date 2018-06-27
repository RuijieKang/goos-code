package auctionsniper.domain.adaptors.xmpp;

import java.util.EventListener;

/**
 * Listener for incoming event from message broker
 */
public interface AuctionEventListener extends EventListener {
  enum PriceSource {
    FromSniper, FromOtherBidder;
  };
  
  void auctionClosed();
  void currentPrice(int price, int increment, PriceSource priceSource);
  void auctionFailed();
}
