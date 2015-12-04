package client;

import java.awt.Desktop;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import server.MySQLDriver;

public class ClientGUI extends JFrame
{
	JMenuBar menuBar;
	JMenu helpMenu,statsMenu;
	// Main panel
	public static ClientPanel clientPanel;
	
	ClientGUI()
	{
		super(ClientConstants.gameNameString);
		initializeComponents();
		createGUI();
	}
	
	private void initializeComponents()
	{
		clientPanel = new ClientPanel();
		
		
		menuBar = new JMenuBar();
		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		helpMenu.addMenuListener(new MenuListener(){

			@Override
			public void menuSelected(MenuEvent e) {
				if(Desktop.isDesktopSupported()){
					Desktop desktop = Desktop.getDesktop();
					try{
						desktop.browse(new URL("http://faraza.github.io/").toURI());
					}
					catch (Exception ex){
						System.out.println("Error opening faraza webpage");
						ex.printStackTrace();
					}
				}
				else{
					System.out.println("Opening web pages wont' work on this computer");
				}
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {				
			}
			
		});
		
		statsMenu = new JMenu("Past Games");
		//statsMenu.setEnabled(false);
		menuBar.add(statsMenu);
		statsMenu.addMenuListener(new MenuListener(){

			@Override
			public void menuCanceled(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuSelected(MenuEvent arg0) {
				if(clientPanel.isLoggedin){
					MySQLDriver driver = new MySQLDriver();
					PastGamesDialog pgd = new PastGamesDialog(clientPanel.gameLoginPanel.usernameTF.getText());
					pgd.getPastGames();
					pgd.setVisible(true);
				}
				else System.out.println("Not logged in");
				
			}
			
		});
	}
	
	private void createGUI()
	{
		setJMenuBar(menuBar);
		add(clientPanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(ClientConstants.minSize);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String [] cheese)
	{
		new ClientGUI();
	}
}
