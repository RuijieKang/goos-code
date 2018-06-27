package auctionsniper.humble.bootstrap;

import auctionsniper.domain.AuctionSniperLauncher;
import auctionsniper.domain.AuctionSniperPortfolio;
import auctionsniper.ports.xmpp.XMPPAuctionHouse;

public class ApplicationFactory {
    private String auctionHouseHostname;
    private String auctionHouseUserName;
    private String auctionHousePassword;

    public ApplicationFactory(String hostname, String username, String password) {
        auctionHouseHostname = hostname;
        auctionHouseUserName = username;
        auctionHousePassword = password;
    }

    public Application createApplication() throws Exception{
        AuctionSniperPortfolio portfolio = new AuctionSniperPortfolio();
        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(auctionHouseHostname, auctionHouseUserName, auctionHousePassword);
        AuctionSniperLauncher userRequestController = new AuctionSniperLauncher(auctionHouse, portfolio);

        return new Application(new ApplicationContext(portfolio, auctionHouse, userRequestController));
    }
}
