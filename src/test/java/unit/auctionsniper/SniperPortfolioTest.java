package unit.auctionsniper;

import auctionsniper.domain.adaptors.ui.AuctionSniperPortfolioListener;
import auctionsniper.domain.value.AuctionItem;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import auctionsniper.domain.AuctionSniper;
import auctionsniper.domain.AuctionSniperPortfolio;

@RunWith(JMock.class)
public class SniperPortfolioTest {
  private final Mockery context = new Mockery();
  private final AuctionSniperPortfolioListener listener = context.mock(AuctionSniperPortfolioListener.class);
  private final AuctionSniperPortfolio portfolio = new AuctionSniperPortfolio();
  
  @Test public void
  notifiesListenersOfNewSnipers() {
    final AuctionSniper sniper = new AuctionSniper(new AuctionItem("item id", 123), null);
    context.checking(new Expectations() {{
      oneOf(listener).sniperAdded(sniper);
    }});
    portfolio.addPortfolioListener(listener);
    
    portfolio.addSniper(sniper);
  }
}
