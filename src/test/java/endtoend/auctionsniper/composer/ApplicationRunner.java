package endtoend.auctionsniper.composer;

import auctionsniper.domain.value.SniperState;
import auctionsniper.humble.bootstrap.Main;
import auctionsniper.ports.ui.MainWindow;
import static auctionsniper.ports.ui.SnipersTableModel.textFor;
import auctionsniper.humble.bootstrap.Application;
import endtoend.auctionsniper.driver.AuctionLogDriver;
import endtoend.auctionsniper.driver.AuctionSniperDriver;

import static org.hamcrest.Matchers.containsString;
import static endtoend.auctionsniper.composer.FakeAuctionServer.XMPP_HOSTNAME;

import java.io.IOException;

import javax.swing.SwingUtilities;

/**
 * Facade to express the behavior driven test case
 */
public class ApplicationRunner { 
  public static final String SNIPER_ID = "sniper"; 
  public static final String SNIPER_PASSWORD = "sniper";
  public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/Auction";

  private AuctionLogDriver logDriver = new AuctionLogDriver();
  private AuctionSniperDriver driver;
  
  public void startBiddingIn(final FakeAuctionServer... auctions) {
    startSniper(); 
    for (FakeAuctionServer auction : auctions) {
      openBiddingFor(auction, Integer.MAX_VALUE);
    }
  }
  public void startBiddingWithStopPrice(FakeAuctionServer auction, int stopPrice) {
    startSniper();
    openBiddingFor(auction, stopPrice);
  }  

  public void shouldHaveShownSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.LOST));
  } 
  
  public void shouldHaveShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.BIDDING));
  }

  public void shouldHaveShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
    driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, textFor(SniperState.WINNING));
  }

  public void shouldHaveShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.LOSING));
  }

  public void shouldHaveShownSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, textFor(SniperState.WON));
  }

  public void shouldHaveShownSniperHasFailed(FakeAuctionServer auction) {
    driver.showsSniperStatus(auction.getItemId(), 0, 0, textFor(SniperState.FAILED));
  }

  public void reportsInvalidMessage(FakeAuctionServer auction, String brokenMessage) throws IOException {
    logDriver.hasEntry(containsString(brokenMessage));
  }

  public void stop() { 
    if (driver != null) { 
      driver.dispose();  
    } 
  }

  private void startSniper() {
    logDriver.clearLog();
    Thread thread = new Thread("Test Application") { 
      @Override public void run() {  
        try { 
          Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD);
        } catch (Exception e) { 
          e.printStackTrace();  
        } 
      } 
    }; 
    thread.setDaemon(true); 
    thread.start(); 
    makeSureAwtIsLoadedBeforeStartingTheDriverOnOSXToStopDeadlock();
    
    driver = new AuctionSniperDriver(1000);  
    driver.hasTitle(MainWindow.APPLICATION_TITLE); 
    driver.hasColumnTitles();
  } 

  private void openBiddingFor(FakeAuctionServer auction, int stopPrice) {
    final String itemId = auction.getItemId();
    driver.startBiddingWithStopPrice(itemId, stopPrice);
    driver.showsSniperStatus(itemId, 0, 0, textFor(SniperState.JOINING));
  }

  private void makeSureAwtIsLoadedBeforeStartingTheDriverOnOSXToStopDeadlock() {
    try {
      SwingUtilities.invokeAndWait(new Runnable() { public void run() {} });
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
} 
