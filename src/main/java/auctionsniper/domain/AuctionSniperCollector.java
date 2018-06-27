package auctionsniper.domain;

/**
 * Not a strong boundary but helps to promote the testibility
 */
public interface AuctionSniperCollector {
  void addSniper(AuctionSniper sniper);
}
