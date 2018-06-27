package auctionsniper.ports.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import auctionsniper.domain.AuctionSniper;
import auctionsniper.domain.adaptors.ui.AuctionSniperPortfolioListener;
import auctionsniper.domain.adaptors.ui.AuctionSniperListener;
import auctionsniper.domain.value.SniperSnapshot;
import auctionsniper.domain.value.SniperState;
import auctionsniper.humble.util.Defect;

public class SnipersTableModel extends AbstractTableModel implements AuctionSniperListener, AuctionSniperPortfolioListener {
  private final static String[] STATUS_TEXT = { 
    "Joining", "Bidding", "Winning", "Losing", "Lost", "Won", "Failed" 
  };
  private ArrayList<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

  public int getColumnCount() { 
    return Column.values().length; 
  } 
  
  public int getRowCount() { 
    return snapshots.size();
  }

  @Override public String getColumnName(int column) { 
    return Column.at(column).name; 
  } 

  public Object getValueAt(int rowIndex, int columnIndex) {
    return Column.at(columnIndex).valueIn(snapshots.get(rowIndex)); 
  } 

  public static String textFor(SniperState state) { 
    return STATUS_TEXT[state.ordinal()]; 
  }
  
  public void sniperStateChanged(SniperSnapshot newSnapshot) {
    for (int i = 0; i < snapshots.size(); i++) {
      if (newSnapshot.isForSameItemAs(snapshots.get(i))) {
        snapshots.set(i, newSnapshot); 
        fireTableRowsUpdated(i, i);
        return;
      }
    }
    throw new Defect("No existing Sniper state for " + newSnapshot.itemId);
  }

  public void sniperAdded(AuctionSniper sniper) {
    addSniperSnapshot(sniper.getSnapshot());
    sniper.addSniperListener(new SwingThreadAuctionSniperListener(this));
  } 

  private void addSniperSnapshot(SniperSnapshot newSniper) {
    snapshots.add(newSniper);
    int row = snapshots.size() - 1;
    fireTableRowsInserted(row, row);
  }
}
