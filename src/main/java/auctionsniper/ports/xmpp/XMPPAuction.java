package auctionsniper.ports.xmpp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.domain.adaptors.xmpp.Auction;
import auctionsniper.domain.adaptors.xmpp.AuctionOperationalEventListener;
import auctionsniper.humble.util.Announcer;

public class XMPPAuction implements Auction { 
  public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
  public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

  private final Announcer<AuctionOperationalEventListener> auctionEventListeners = Announcer.to(AuctionOperationalEventListener.class);
  private final Chat chat;
  private final XMPPFailureReporter failureReporter;
  
  public XMPPAuction(XMPPConnection connection, String auctionJID, XMPPFailureReporter failureReporter) { 
    this.failureReporter = failureReporter;
    AuctionMessageTranslator translator = translatorFor(connection);
    this.chat = connection.getChatManager().createChat( auctionJID, translator);
    addAuctionEventListener(chatDisconnectorFor(translator));
  }
  
  public void bid(int amount) { 
    sendMessage(String.format(BID_COMMAND_FORMAT, amount)); 
  } 
  public void join() { 
    sendMessage(JOIN_COMMAND_FORMAT); 
  } 
 
  public void addAuctionEventListener(AuctionOperationalEventListener listener) {
    auctionEventListeners.addListener(listener);
  }

  private AuctionMessageTranslator translatorFor(XMPPConnection connection) {
    return new AuctionMessageTranslator(connection.getUser(), auctionEventListeners.announce(), failureReporter);
  } 

  private AuctionOperationalEventListener
  chatDisconnectorFor(final AuctionMessageTranslator translator) { 
    return new AuctionOperationalEventListener() {
      public void auctionFailed() { 
        chat.removeMessageListener(translator); 
      }
      public void auctionClosed() { }
      public void currentPrice(int price, int increment, PriceSource priceSource) { }
    }; 
  } 
  private void sendMessage(final String message) { 
    try { 
      chat.sendMessage(message); 
    } catch (XMPPException e) { 
      e.printStackTrace(); 
    } 
  } 
  
} 
