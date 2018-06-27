package auctionsniper.domain.adaptors.ui;

import auctionsniper.domain.AuctionSniper;

import java.util.EventListener;

public interface PortfolioListener extends EventListener {
    void sniperAdded(AuctionSniper sniper);
}
