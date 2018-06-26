package auctionsniper.domain.adaptors.xmpp;

import auctionsniper.domain.adaptors.xmpp.AuctionEventListener;

public interface Auction {

  void join();
  void bid(int amount);
  void addAuctionEventListener(AuctionEventListener listener);
}
