package auctionsniper.domain.adaptors.xmpp;

/**
 * Someone from the ports layer should take this role
 */
public interface Auction {
  void join();
  void bid(int amount);

  void addAuctionEventListener(AuctionOperationalEventListener listener);
}
