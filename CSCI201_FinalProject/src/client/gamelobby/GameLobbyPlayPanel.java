package client.gamelobby;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientConstants;
import client.ClientPanel;
import client.SoundPlayer;
import client.customUI.PaintedButton;
import networkobjects.GameQueueObject;

// Includes play button

public class GameLobbyPlayPanel extends JPanel
{
	// ClientPanel
	ClientPanel clientPanel;
	GameLobbyPanel gameLobbyPanel;
	
	// Layout + Panels
	CardLayout cardLayout;
	JPanel playButtonPanel, searchForMatchPanel;
	
	// Other components
	JButton exitButton;
	PaintedButton playButton;
	JLabel searchForMatchLabel;
	
	GameLobbyPlayPanel(ClientPanel cp, GameLobbyPanel glp)
	{
		clientPanel = cp;
		gameLobbyPanel = glp;
		
		initializeComponents();
		createGUI();
		addEvents();
	}
	
	private void initializeComponents()
	{
		// Layout + panels
		cardLayout = new CardLayout();
		playButtonPanel = new JPanel(new BorderLayout());
		searchForMatchPanel = new JPanel();
		searchForMatchPanel.setBackground(ClientConstants.backgroundColor);
		searchForMatchPanel.setBorder(ClientConstants.playPanelBorder);
		// Other components
		playButton = new PaintedButton(ClientConstants.playPanelSize,ClientConstants.playButtonImage);
		playButton.setText(ClientConstants.playButtonString);
		playButton.setForeground(ClientConstants.playButtonColor);
		exitButton = new PaintedButton(ClientConstants.exitButtonSize,ClientConstants.exitButtonImage);
		searchForMatchLabel = new JLabel(ClientConstants.searchForMatchString);
		searchForMatchLabel.setFont(searchForMatchLabel.getFont().deriveFont(ClientConstants.searchForMatchFontSize));
		searchForMatchLabel.setForeground(ClientConstants.textColor);
	}
	
	private void createGUI()
	{
		// Layout + panels
		setLayout(cardLayout);
		add(playButtonPanel,ClientConstants.playButtonPanelName);
		add(searchForMatchPanel,ClientConstants.searchForMatchPanelName);
		
		// Play button panel
		playButtonPanel.add(playButton);
		
		// Search for match panel
		searchForMatchPanel.add(searchForMatchLabel);
		searchForMatchPanel.add(exitButton);
		
		cardLayout.show(this, "playButton");
		setPreferredSize(ClientConstants.playPanelSize);
	}
	
	private void addEvents()
	{
		playButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playPlayButton();
				playButton.setEnabled(false);
				gameLobbyPanel.show(ClientConstants.gameSelectorPanelName);
			}
		});
		
		exitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				clientPanel.sendObjectToServer(new GameQueueObject(false));
				show(ClientConstants.playButtonPanelName);
				enablePlayButton();
			}
		});
	}
	
	public void enablePlayButton()
	{
		playButton.setEnabled(true);
	}
	
	public void show(String panelName)
	{
		cardLayout.show(this, panelName);
	}
}
