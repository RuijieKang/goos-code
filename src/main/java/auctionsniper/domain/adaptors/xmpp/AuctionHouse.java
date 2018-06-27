package auctionsniper.domain.adaptors.xmpp;

import auctionsniper.domain.value.AuctionItem;

/**
 * Someone from the ports needs to take the role
 */
public interface AuctionHouse {
  Auction auctionFor(AuctionItem auctionItem);
}
