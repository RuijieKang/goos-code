package auctionsniper.domain.value;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class AuctionItem {
      public final String identifier;
      public final int stopPrice;

      public AuctionItem(String identifier, int stopPrice) {
        this.identifier = identifier;
        this.stopPrice = stopPrice;
      }

      public boolean allowsBid(int bid) {
        return bid <= stopPrice;
      }

      @Override
      public boolean equals(Object obj) { return EqualsBuilder.reflectionEquals(this, obj); }
      @Override
      public int hashCode() { return HashCodeBuilder.reflectionHashCode(this); }
      @Override
      public String toString() { return "AuctionItem: " + identifier + ", stop price: " + stopPrice; }
}
