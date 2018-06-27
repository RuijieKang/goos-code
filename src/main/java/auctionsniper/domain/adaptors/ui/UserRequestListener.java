package auctionsniper.domain.adaptors.ui;

import auctionsniper.domain.value.AuctionItem;

import java.util.EventListener;

/**
 * Listener for incoming event from UI
 */
public interface UserRequestListener extends EventListener {
  void joinAuction(AuctionItem auctionItem);
}
