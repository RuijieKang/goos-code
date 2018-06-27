package auctionsniper.domain.adaptors.ui;

import auctionsniper.domain.value.AuctionItem;
import auctionsniper.humble.util.Announcee;

import java.util.EventListener;

/**
 * Listener for incoming request from UI
 * Someone within domain will take this role
 */
public interface UserRequestController extends Announcee {
  void joinAuction(AuctionItem auctionItem);
}
