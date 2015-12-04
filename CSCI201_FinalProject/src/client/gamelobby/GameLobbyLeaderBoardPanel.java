package client.gamelobby;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultRowSorter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import client.ClientConstants;
import networkobjects.User;

public class GameLobbyLeaderBoardPanel extends JPanel
{
	String[] columnNames = { "Rank", "Players", "Wins", "Losses", "Points" };
	DefaultTableModel tableModel;
	JTable leaderBoardTable;
	
	GridBagConstraints gbc;
	
	GameLobbyLeaderBoardPanel()
	{
		// Settings
		setOpaque(false);
		setLayout(new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,0,20,0);
		
		// Title
		JLabel titleLabel = new JLabel("High Scores");
		titleLabel.setForeground(ClientConstants.backgroundColor);
		add(titleLabel, gbc);
		
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
                    	return Integer.class;
                    case 3:
                    	return Integer.class;
                    default:
                    	return Integer.class;
                   
                }
            }
		};
	}
	
	public void updateLeaderBoard(ArrayList<User> inUsers)
	{
		for(int i = 0; i < inUsers.size(); i++)
		{
			tableModel.addRow(new Object[]
			{
				i+1,
				inUsers.get(i).getUsername(),
				inUsers.get(i).getWins(),
				inUsers.get(i).getLosses(),
				inUsers.get(i).getRating()
			});
		}
		
		
		leaderBoardTable = new JTable(tableModel);
		
		leaderBoardTable.setAutoCreateRowSorter(true);
		leaderBoardTable.setShowGrid(false);
	    leaderBoardTable.setShowVerticalLines(true);
	    leaderBoardTable.getTableHeader().setReorderingAllowed(false);
	    
	    UIDefaults defaults = UIManager.getLookAndFeelDefaults();
	    if (defaults.get("Table.alternateRowColor") == null)
	        defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		leaderBoardTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		leaderBoardTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		leaderBoardTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
		leaderBoardTable.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
		
		
		DefaultRowSorter sorter = ((DefaultRowSorter)leaderBoardTable.getRowSorter());
		ArrayList list = new ArrayList();
		list.add(new RowSorter.SortKey(4, SortOrder.DESCENDING));
		sorter.setSortKeys(list);
		sorter.sort();
		JScrollPane jsp = new JScrollPane(leaderBoardTable);
		
		gbc.gridy = 1;
		add(jsp, gbc);
	}
	
	
}
