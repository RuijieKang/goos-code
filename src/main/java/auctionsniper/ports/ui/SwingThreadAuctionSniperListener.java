/**
 * 
 */
package auctionsniper.ports.ui;

import javax.swing.SwingUtilities;

import auctionsniper.domain.adaptors.ui.AuctionSniperListener;
import auctionsniper.domain.value.SniperSnapshot;

public class SwingThreadAuctionSniperListener implements AuctionSniperListener {
  private final AuctionSniperListener delegate;
  public SwingThreadAuctionSniperListener(AuctionSniperListener delegate) {
    this.delegate = delegate;
  }
  public void sniperStateChanged(final SniperSnapshot snapshot) { 
    SwingUtilities.invokeLater(new Runnable() { 
      public void run() { 
        delegate.sniperStateChanged(snapshot); 
      } 
    }); 
  }
}