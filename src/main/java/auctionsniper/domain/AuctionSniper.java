package auctionsniper.domain;

import auctionsniper.domain.adaptors.ui.AuctionSniperPublisher;
import auctionsniper.domain.adaptors.xmpp.Auction;
import auctionsniper.domain.adaptors.xmpp.AuctionOperationalEventListener;
import auctionsniper.domain.adaptors.ui.AuctionSniperListener;
import auctionsniper.domain.value.AuctionItem;
import auctionsniper.domain.value.SniperSnapshot;
import auctionsniper.humble.util.Announcer;

public class AuctionSniper implements AuctionOperationalEventListener,AuctionSniperPublisher {

  private final Auction auction;
  private SniperSnapshot snapshot;
  private final AuctionItem auctionItem;

  private final Announcer<AuctionSniperListener> listeners = Announcer.to(AuctionSniperListener.class);
    
  public AuctionSniper(AuctionItem auctionItem, Auction auction) {
    this.auctionItem = auctionItem;
    this.auction = auction;
    this.snapshot = SniperSnapshot.joining(auctionItem.identifier);
  }

  public void addSniperListener(AuctionSniperListener listener) {
    listeners.addListener(listener);
  }
  
  public void auctionClosed() {
    snapshot = snapshot.closed();
    notifyChange();
  }

  public void auctionFailed() {
    snapshot = snapshot.failed(); 
    notifyChange();
  }
  
  public void currentPrice(int price, int increment, PriceSource priceSource) {
    switch(priceSource) {
    case FromSniper:
      snapshot = snapshot.winning(price); 
      break;
    case FromOtherBidder:
      int bid = price + increment;
      if (auctionItem.allowsBid(bid)) {
        auction.bid(bid);
        snapshot = snapshot.bidding(price, bid);
      } else {
        snapshot = snapshot.losing(price);
      }
      break;
    }
    notifyChange();
  }

  public SniperSnapshot getSnapshot() {
    return snapshot;
  }
  
  private void notifyChange() {
    listeners.announce().sniperStateChanged(snapshot);
  }

}
