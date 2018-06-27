package endtoend.auctionsniper;

import endtoend.auctionsniper.composer.ApplicationRunner;
import endtoend.auctionsniper.composer.FakeAuctionServer;

import org.junit.After;
import org.junit.Test;


public class AuctionSniperEndToEndTest { 
  private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
  private final FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");  

  private final ApplicationRunner application = new ApplicationRunner();
  
  @Test public void 
  sniperJoinsAuctionUntilAuctionCloses() throws Exception {
    //Given
    auction.startSellingItem();

    //When
    application.startBiddingIn(auction);
    //Then
    auction.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.announceClosed();
    //Then
    application.shouldHaveShownSniperHasLostAuction(auction, 0, 0);
  } 

  
  @Test public void 
  sniperMakesAHigherBidButLoses() throws Exception { 
    //Given
    auction.startSellingItem();

    //When
    application.startBiddingIn(auction); 
    //Then
    auction.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.reportPrice(1000, 98, "other bidder");
    //Then
    application.shouldHaveShownSniperIsBidding(auction, 1000, 1098);
    //And
    auction.shouldHaveReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.announceClosed();
    //Then
    application.shouldHaveShownSniperHasLostAuction(auction, 1000, 1098);
  } 
  
  @Test public void 
  sniperWinsAnAuctionByBiddingHigher() throws Exception {
    //Given
    auction.startSellingItem(); 

    //When
    application.startBiddingIn(auction); 
    //Then
    auction.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.reportPrice(1000, 98, "other bidder");
    //Then
    application.shouldHaveShownSniperIsBidding(auction, 1000, 1098);
    //And
    auction.shouldHaveReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID); 
    //Then
    application.shouldHaveShownSniperIsWinning(auction, 1098);

    //When
    auction.announceClosed(); 
    //Then
    application.shouldHaveShownSniperHasWonAuction(auction, 1098);
  } 

  @Test public void 
  sniperBidsForMultipleItems() throws Exception {
    //Given
    auction.startSellingItem();
    //And
    auction2.startSellingItem(); 

    //When
    application.startBiddingIn(auction, auction2); 
    //Then
    auction.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
    //And
    auction2.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
    
    //When
    auction.reportPrice(1000, 98, "other bidder");
    //Then
    auction.shouldHaveReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction2.reportPrice(500, 21, "other bidder"); 
    //Then
    auction2.shouldHaveReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);    
    //And
    auction2.reportPrice(521, 22, ApplicationRunner.SNIPER_XMPP_ID);

    //Then
    application.shouldHaveShownSniperIsWinning(auction, 1098);
    //And
    application.shouldHaveShownSniperIsWinning(auction2, 521);

    //When
    auction.announceClosed(); 
    //And
    auction2.announceClosed();

    //Then
    application.shouldHaveShownSniperHasWonAuction(auction, 1098);
    //And
    application.shouldHaveShownSniperHasWonAuction(auction2, 521);
  } 

  @Test public void 
  sniperLosesAnAuctionWhenThePriceIsTooHigh() throws Exception { 
    //Given
    auction.startSellingItem();

    //When
    application.startBiddingWithStopPrice(auction, 1100); 
    //Then
    auction.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.reportPrice(1000, 98, "other bidder");
    //Then
    application.shouldHaveShownSniperIsBidding(auction, 1000, 1098);
    //Then
    auction.shouldHaveReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
    
    //When
    auction.reportPrice(1197, 10, "third party");
    //Then
    application.shouldHaveShownSniperIsLosing(auction, 1197, 1098);
    
    //When
    auction.reportPrice(1207, 10, "fourth party");
    //Then
    application.shouldHaveShownSniperIsLosing(auction, 1207, 1098);

    //When
    auction.announceClosed();
    //Then
    application.shouldHaveShownSniperHasLostAuction(auction, 1207, 1098);
  } 

  @Test public void 
  sniperReportsInvalidAuctionMessageAndStopsRespondingToEvents() 
      throws Exception 
  { 
    String brokenMessage = "a broken message"; 
    //Given
    auction.startSellingItem();
    //And
    auction2.startSellingItem();

    //When
    application.startBiddingIn(auction, auction2);
    //Then
    auction.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.reportPrice(500, 20, "other bidder");
    //Then
    auction.shouldHaveReceivedBid(520, ApplicationRunner.SNIPER_XMPP_ID);

    //When
    auction.sendInvalidMessageContaining(brokenMessage); 
    //Then
    application.shouldHaveShownSniperHasFailed(auction);

    //When
    auction.reportPrice(520, 21, "other bidder"); 
    waitForAnotherAuctionEvent(); 

    //When
    application.reportsInvalidMessage(auction, brokenMessage); 
    //Then
    application.shouldHaveShownSniperHasFailed(auction);
  }

  @After public void stopAuction() { 
    auction.stop(); 
    auction2.stop();
  } 
  @After public void stopApplication() { 
    application.stop(); 
  }

  private void waitForAnotherAuctionEvent() throws Exception {
    auction2.shouldHaveReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
    auction2.reportPrice(600, 6, "other bidder");
    application.shouldHaveShownSniperIsBidding(auction2, 600, 606);
  }
} 
