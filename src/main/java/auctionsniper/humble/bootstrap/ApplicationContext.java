package auctionsniper.humble.bootstrap;

import auctionsniper.domain.AuctionSniperLauncher;
import auctionsniper.domain.AuctionSniperPortfolio;
import auctionsniper.ports.xmpp.XMPPAuctionHouse;

public class ApplicationContext {
    /**
     * These are single instances. But we dont use singlton pattern to avoid globals
     * Single instance : one to many relationship. Interleaving or not is not the high level concern.
     */
    private final AuctionSniperPortfolio portfolio;//One portfolio to house many snipers
    private final XMPPAuctionHouse auctionHouse;//One house to house many auctions
    private final AuctionSniperLauncher userRequestController;//One controller to handle many request

    public ApplicationContext(AuctionSniperPortfolio portfolio, XMPPAuctionHouse auctionHouse, AuctionSniperLauncher userRequestController) {
        this.portfolio = portfolio;
        this.auctionHouse = auctionHouse;
        this.userRequestController = userRequestController;
    }

    public AuctionSniperPortfolio getPortfolio() {
        return portfolio;
    }
    public XMPPAuctionHouse getAuctionHouse() {
        return auctionHouse;
    }
    public AuctionSniperLauncher getUserRequestController() {
        return userRequestController;
    }
}
