package auctionsniper.domain.adaptors.ui;

import auctionsniper.domain.value.SniperSnapshot;
import auctionsniper.humble.util.Announcee;

import java.util.EventListener;

/**
 * Listeners to be implemented by adaptors for out going event to UI
 * Someone from the ports layer need to take this role
 * This is a more technical interface because it's not directly from user scenario. But a technical boundary crossing
 */
public interface AuctionSniperListener extends Announcee {
  void sniperStateChanged(SniperSnapshot snapshot);
}