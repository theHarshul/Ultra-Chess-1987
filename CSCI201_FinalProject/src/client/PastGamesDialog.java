package client;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultRowSorter;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import networkobjects.PastGame;
import networkobjects.User;
import server.MySQLDriver;

public class PastGamesDialog extends JDialog{
	
	String[] columnNames = { "Game #","W/L", "Opponent", "Chess Notation" };
	DefaultTableModel tableModel;
	JTable statsTable;
	JLabel statsLabel = new JLabel("Past Games");
	GridBagConstraints gbc;
	public PastGame[] pastGames;
	public String username;

	
	public PastGamesDialog(String username){
		
		this.username = username;
		//setLayout(new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,0,20,0);
		
		//add(statsLabel,gbc);
		
		Object[][] data = {};
		tableModel = new DefaultTableModel(data, columnNames){
			
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
			
			@Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return String.class;
                    case 2:
                    	return String.class;
                    case 3:
                    	return String.class;
                    default:
                    	return String.class;
                   
                }
            }
		};
		
		setTitle("Past Games");
		setLocation(400,400);
		setSize(1000,500);
		setResizable(false);
		setModal(true);
	}
	
	public void getPastGames(){
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		pastGames = driver.viewStats(username);
		driver.stop();
		updateLeaderBoard();
	}
	
	public void updateLeaderBoard()
	{
		
		int width = 0;
		
		for(int i = 0; i < pastGames.length; i++)
		{
			tableModel.addRow(new Object[]
			{
				i+1,
				pastGames[i].getWin(),
				pastGames[i].getOpponentName(),
				pastGames[i].getChessNotation()
			});
		}
		
		
		statsTable = new JTable(tableModel);
		statsTable.getColumnModel().getColumn(3).setPreferredWidth(1000);
		
		statsTable.setAutoCreateRowSorter(true);
		statsTable.setShowGrid(false);
	    statsTable.setShowVerticalLines(true);
	    statsTable.getTableHeader().setReorderingAllowed(false);
	    
	    UIDefaults defaults = UIManager.getLookAndFeelDefaults();
	    if (defaults.get("Table.alternateRowColor") == null)
	        defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		statsTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		statsTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		statsTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		
		
		// Get maximum width of column data

		
		/*DefaultRowSorter sorter = ((DefaultRowSorter)statsTable.getRowSorter());
		ArrayList list = new ArrayList();
		list.add(new RowSorter.SortKey(4, SortOrder.DESCENDING));
		sorter.setSortKeys(list);
		sorter.sort();
		*/
		JScrollPane jsp = new JScrollPane(statsTable);
		
		gbc.gridy = 1;
		add(jsp);
	}
	
	
	
	
}
