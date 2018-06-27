package auctionsniper.domain;


import auctionsniper.domain.adaptors.ui.UserRequestListener;
import auctionsniper.domain.adaptors.xmpp.Auction;
import auctionsniper.domain.adaptors.xmpp.AuctionHouse;
import auctionsniper.domain.value.AuctionItem;

public class SniperLauncher implements UserRequestListener {
  private final AuctionHouse auctionHouse;
  private final SniperCollector collector;

  public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector) {
    this.auctionHouse = auctionHouse;
    this.collector = collector;
  }

  public void joinAuction(AuctionItem auctionItem) {
    Auction auction = auctionHouse.auctionFor(auctionItem);
    AuctionSniper sniper = new AuctionSniper(auctionItem, auction);
    auction.addAuctionEventListener(sniper); 
    collector.addSniper(sniper); 
    auction.join(); 
  }
}