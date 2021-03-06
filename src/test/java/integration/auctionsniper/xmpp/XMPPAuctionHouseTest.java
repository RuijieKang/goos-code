package integration.auctionsniper.xmpp;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;

import auctionsniper.domain.value.AuctionItem;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import endtoend.auctionsniper.composer.ApplicationRunner;
import endtoend.auctionsniper.composer.FakeAuctionServer;
import auctionsniper.domain.adaptors.xmpp.Auction;
import auctionsniper.domain.adaptors.xmpp.AuctionOperationalEventListener;
import auctionsniper.ports.xmpp.XMPPAuctionException;
import auctionsniper.ports.xmpp.XMPPAuctionHouse;

public class XMPPAuctionHouseTest {
  private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");  
  private XMPPAuctionHouse auctionHouse; 

  @Before public void openConnection() throws XMPPAuctionException {
    auctionHouse = XMPPAuctionHouse.connect(FakeAuctionServer.XMPP_HOSTNAME, ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD);
    
  }
  @After public void closeConnection() {
    if (auctionHouse != null) {
      auctionHouse.disconnect();
    }
  }
  @Before public void startAuction() throws XMPPException {
    auctionServer.startSellingItem();
  }
  @After public void stopAuction() {
    auctionServer.stop();
  }


  @Test public void 
  receivesEventsFromAuctionServerAfterJoining() throws Exception { 
    CountDownLatch auctionWasClosed = new CountDownLatch(1); 
    
    Auction auction = auctionHouse.auctionFor(new AuctionItem(auctionServer.getItemId(), 567));
    auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
    auction.join(); 
    auctionServer.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
    auctionServer.announceClosed(); 
    
    assertTrue("should have been closed", auctionWasClosed.await(4, SECONDS)); 
  } 

  private AuctionOperationalEventListener
  auctionClosedListener(final CountDownLatch auctionWasClosed) { 
    return new AuctionOperationalEventListener() {
      public void auctionClosed() { 
        auctionWasClosed.countDown(); 
      } 
      public void currentPrice(int price, int increment, PriceSource priceSource) { }
      public void auctionFailed() { }
    }; 
  }
}
