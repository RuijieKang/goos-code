package test.integration.auctionsniper.ui;

import static org.hamcrest.Matchers.equalTo;

import auctionsniper.domain.value.AuctionItem;
import org.junit.Test;

import test.endtoend.auctionsniper.driver.AuctionSniperDriver;
import auctionsniper.domain.adaptors.ui.UserRequestListener;
import auctionsniper.domain.SniperPortfolio;
import auctionsniper.ports.ui.MainWindow;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

public class MainWindowTest {
  private final MainWindow mainWindow = new MainWindow(new SniperPortfolio()); 
  private final AuctionSniperDriver driver = new AuctionSniperDriver(100); 
  
  @Test public void 
  makesUserRequestWhenJoinButtonClicked() { 
    final ValueMatcherProbe<AuctionItem> itemProbe =
      new ValueMatcherProbe<AuctionItem>(equalTo(new AuctionItem("an item-id", 789)),
                                  "item request");
    mainWindow.addUserRequestListener( 
        new UserRequestListener() { 
          public void joinAuction(AuctionItem auctionItem) {
            itemProbe.setReceivedValue(auctionItem);
          } 
        }); 
    
    driver.startBiddingWithStopPrice("an item-id", 789);
    driver.check(itemProbe); 
  }
}
