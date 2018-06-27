package auctionsniper.domain;


import auctionsniper.domain.adaptors.ui.UserRequestController;
import auctionsniper.domain.adaptors.xmpp.Auction;
import auctionsniper.domain.adaptors.xmpp.AuctionHouse;
import auctionsniper.domain.value.AuctionItem;

public class AuctionSniperLauncher implements UserRequestController {
  private final AuctionHouse auctionHouse;
  private final AuctionSniperCollector collector;

  public AuctionSniperLauncher(AuctionHouse auctionHouse, AuctionSniperCollector collector) {
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