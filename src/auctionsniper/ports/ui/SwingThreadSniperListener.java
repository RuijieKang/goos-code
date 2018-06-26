/**
 * 
 */
package auctionsniper.ports.ui;

import javax.swing.SwingUtilities;

import auctionsniper.domain.adaptors.ui.SniperListener;
import auctionsniper.domain.value.SniperSnapshot;

public class SwingThreadSniperListener implements SniperListener {
  private final SniperListener delegate;
  public SwingThreadSniperListener(SniperListener delegate) {
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