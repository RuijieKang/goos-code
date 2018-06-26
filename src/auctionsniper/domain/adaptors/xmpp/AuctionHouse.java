package auctionsniper.domain.adaptors.xmpp;

import auctionsniper.domain.adaptors.xmpp.Auction;
import auctionsniper.domain.value.AuctionItem;

public interface AuctionHouse {
  Auction auctionFor(AuctionItem auctionItem);
}
