package auctionsniper.domain.adaptors.ui;

import auctionsniper.domain.value.SniperSnapshot;

import java.util.EventListener;

/**
 * Listeners to be implemented by adaptors for out going event to UI
 */
public interface SniperListener extends EventListener { 
  void sniperStateChanged(SniperSnapshot snapshot);
}