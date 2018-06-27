package unit.auctionsniper;

import static org.hamcrest.Matchers.equalTo;

import auctionsniper.domain.*;
import auctionsniper.domain.adaptors.xmpp.Auction;
import auctionsniper.domain.adaptors.xmpp.AuctionHouse;
import auctionsniper.domain.value.AuctionItem;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class SniperLauncherTest {
  private final Mockery context = new Mockery();
  private final States auctionState = context.states("auction state").startsAs("not joined");
  private final Auction auction = context.mock(Auction.class);
  private final AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
  private final SniperCollector sniperCollector = context.mock(SniperCollector.class);
  private final SniperLauncher launcher = new SniperLauncher(auctionHouse, sniperCollector);
  
  @Test public void
  addsNewSniperToCollectorAndThenJoinsAuction() {
    final AuctionItem auctionItem = new AuctionItem("auctionItem 123", 456);

    context.checking(new Expectations() {{
      allowing(auctionHouse).auctionFor(auctionItem); will(returnValue(auction));
      
      oneOf(auction).addAuctionEventListener(with(sniperForItem(auctionItem))); when(auctionState.is("not joined"));
      oneOf(sniperCollector).addSniper(with(sniperForItem(auctionItem))); when(auctionState.is("not joined"));
      
      one(auction).join(); then(auctionState.is("joined"));
    }});
    
    launcher.joinAuction(auctionItem);
  }

  protected Matcher<AuctionSniper>sniperForItem(AuctionItem auctionItem) {
    return new FeatureMatcher<AuctionSniper, String>(equalTo(auctionItem.identifier), "sniper with auctionItem id", "auctionItem") {
      @Override protected String featureValueOf(AuctionSniper actual) {
        return actual.getSnapshot().itemId;
      }
    };
  }
}
