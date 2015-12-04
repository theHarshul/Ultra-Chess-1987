package client.gamelobby;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.ClientConstants;
import client.ClientPanel;
import client.SoundPlayer;
import networkobjects.GameQueueObject;

// Panel to select whether you want to join game or create game.

public class GameLobbyGameSelectorPanel extends JPanel
{
	ClientPanel clientPanel;
	GameLobbyPanel gameLobbyPanel;
	private JButton joinGameButton, createGameButton, homeButton;
	
	public GameLobbyGameSelectorPanel(ClientPanel cp, GameLobbyPanel glp)
	{	
		clientPanel = cp;
		gameLobbyPanel = glp;
		
		initializeComponents();
		createGUI();
		addEvents();
	}
	
	private void initializeComponents()
	{
		joinGameButton = new JButton(ClientConstants.joinGameButtonString);
		createGameButton = new JButton(ClientConstants.createGameButtonString);
		homeButton = new JButton(ClientConstants.homeButtonString);
	}
	
	private void createGUI()
	{
		// Set transparent
		setOpaque(false);
		
		add(createGameButton);
		add(joinGameButton);
		add(homeButton);
	}
	
	private void addEvents()
	{
		createGameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				gameLobbyPanel.show(ClientConstants.customGamePanelName);
			}
		});
		
		joinGameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				
				clientPanel.sendObjectToServer(new GameQueueObject(true, false));
				
				gameLobbyPanel.getPlayPanel().show(ClientConstants.searchForMatchPanelName);
				
				gameLobbyPanel.show(ClientConstants.leaderBoardPanelName);
			}
		});
		
		homeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				gameLobbyPanel.show(ClientConstants.leaderBoardPanelName);
			}
		});
	}
}
