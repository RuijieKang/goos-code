package auctionsniper.domain;

import java.util.ArrayList;

import auctionsniper.domain.adaptors.ui.AuctionSniperPortfolioListener;
import auctionsniper.domain.adaptors.ui.AuctionSniperPortfolioPublisher;
import auctionsniper.humble.util.Announcer;

public class AuctionSniperPortfolio implements AuctionSniperCollector, AuctionSniperPortfolioPublisher {
    private final ArrayList<AuctionSniper> snipers = new ArrayList<AuctionSniper>();
    private final Announcer<AuctionSniperPortfolioListener> announcer = Announcer.to(AuctionSniperPortfolioListener.class);

    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        announcer.announce().sniperAdded(sniper);
    }

    public void addPortfolioListener(AuctionSniperPortfolioListener listener) {
        announcer.addListener(listener);
    }
}
