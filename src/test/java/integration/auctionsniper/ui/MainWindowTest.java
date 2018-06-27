package integration.auctionsniper.ui;

import static org.hamcrest.Matchers.equalTo;

import auctionsniper.domain.value.AuctionItem;
import org.junit.Test;

import endtoend.auctionsniper.driver.AuctionSniperDriver;
import auctionsniper.domain.adaptors.ui.UserRequestController;
import auctionsniper.domain.AuctionSniperPortfolio;
import auctionsniper.ports.ui.MainWindow;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

public class MainWindowTest {
  private final MainWindow mainWindow = new MainWindow(new AuctionSniperPortfolio());
  private final AuctionSniperDriver driver = new AuctionSniperDriver(100); 
  
  @Test public void 
  makesUserRequestWhenJoinButtonClicked() { 
    final ValueMatcherProbe<AuctionItem> itemProbe =
      new ValueMatcherProbe<AuctionItem>(equalTo(new AuctionItem("an item-id", 789)),
                                  "item request");
    mainWindow.addUserRequestListener( 
        new UserRequestController() {
          public void joinAuction(AuctionItem auctionItem) {
            itemProbe.setReceivedValue(auctionItem);
          } 
        }); 
    
    driver.startBiddingWithStopPrice("an item-id", 789);
    driver.check(itemProbe); 
  }
}
