package client.gamelobby;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientConstants;
import client.ClientPanel;

public class GameLobbyPanel extends JPanel
{
	ClientPanel clientPanel;
	CardLayout cardLayout;
	JPanel northPanel, centerPanel;
	GameLobbyPlayPanel playPanel;
	GameLobbyLeaderBoardPanel leaderBoardPanel;
	GameLobbyGameSelectorPanel gameSelectPanel;
	GameLobbyCustomGamePanel customGamePanel;
	JLabel usernameLabel;
	
	public GameLobbyPanel(ClientPanel cp)
	{
		clientPanel = cp;
		
		initializeComponents();
		createGUI();
		addEvents();
	}
	
	private void initializeComponents()
	{
		// CardLayout
		cardLayout = new CardLayout();
		
		// Panels
		northPanel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(cardLayout);
		northPanel.setOpaque(false);
		centerPanel.setOpaque(false);
		
		// Username
		usernameLabel = new JLabel("Guest");
		usernameLabel.setForeground(ClientConstants.textColor);
		
		// Custom panels
		playPanel = new GameLobbyPlayPanel(clientPanel, this);
		leaderBoardPanel = new GameLobbyLeaderBoardPanel();
		gameSelectPanel = new GameLobbyGameSelectorPanel(clientPanel,this);
		customGamePanel = new GameLobbyCustomGamePanel(clientPanel, this);
	}
	
	private void createGUI()
	{
		// Set transparent
		setOpaque(false);
		
		// Layout
		setLayout(new BorderLayout());
		
		// Panels
		add(northPanel,BorderLayout.NORTH);
		add(centerPanel,BorderLayout.CENTER);
		
		// NorthPanel
		JPanel playPanelContainer = new JPanel();
		JPanel usernamePanel = new JPanel();
		usernamePanel.setOpaque(false);
		playPanelContainer.setOpaque(false);
		usernamePanel.add(usernameLabel);
		playPanelContainer.add(playPanel);
		//playPanelContainer.add(highscoresLabel);
		northPanel.add(usernamePanel,BorderLayout.EAST);
		northPanel.add(playPanelContainer, BorderLayout.CENTER);
		
		// CenterPanel
	
		centerPanel.add(leaderBoardPanel,ClientConstants.leaderBoardPanelName);
		centerPanel.add(gameSelectPanel,ClientConstants.gameSelectorPanelName);
		centerPanel.add(customGamePanel,ClientConstants.customGamePanelName);
	}
	
	private void addEvents()
	{
		
	}
	
	public GameLobbyCustomGamePanel getCustomGamePanel()
	{
		return customGamePanel;
	}
	
	public GameLobbyLeaderBoardPanel getLeaderBoardPanel()
	{
		return leaderBoardPanel;
	}
	
	public GameLobbyPlayPanel getPlayPanel()
	{
		return playPanel;
	}
	
	public void setLobby(String inUsername)
	{
		usernameLabel.setText(inUsername + "   ");
	}
	
	public void show(String panelName)
	{
		cardLayout.show(centerPanel, panelName);
		
		if(panelName.equals(ClientConstants.leaderBoardPanelName))
		{
			playPanel.enablePlayButton();
		}
	}
}
