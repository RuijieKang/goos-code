package auctionsniper.humble.bootstrap;

import auctionsniper.ports.ui.MainWindow;
import auctionsniper.ports.xmpp.XMPPAuctionHouse;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

/**
 * How the object graph is constructed should be independent on how it is executed by the use cases
 */
public class Application {
    private ApplicationContext context;
    private MainWindow ui;


    public Application(ApplicationContext context) {
        this.context = context;
    }

    public void start() throws Exception{
        startUI();
        configureUIWithAuctionHouse();
    }

    private void startUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow(context.getPortfolio());
            }
        });
    }

    private void configureUIWithAuctionHouse(){
        disconnectWhenUICloses(context.getAuctionHouse());
        addUserRequestListenerFor(context.getAuctionHouse());
    }

    private void disconnectWhenUICloses(final XMPPAuctionHouse auctionHouse) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                auctionHouse.disconnect();
            }
        });
    }

    private void addUserRequestListenerFor(final XMPPAuctionHouse auctionHouse) {
        ui.addUserRequestListener(context.getUserRequestController());
    }
}
