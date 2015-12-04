package client;

import java.awt.CardLayout;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import networkobjects.ChessMoveObject;
import networkobjects.CreateAccountObject;
import networkobjects.GameQueueObject;
import networkobjects.GameSetupObject;
import networkobjects.LeaderBoardObject;
import networkobjects.LoginObject;
import networkobjects.PongObject;
import client.GameController.GameType;
import client.GamePiece.Color;
import client.gamelobby.GameLobbyPanel;
import client.gamelogin.ForgotPasswordPanel;
import client.gamelogin.GameLoginPanel;
import client.gamelogin.SignupPanel;

public class ClientPanel extends JPanel
{
	// Card layout
	CardLayout cardLayout;
	
	// Panels
	GameLoginPanel gameLoginPanel;
	GameLobbyPanel gameLobbyPanel;
	GamePanel gamePanel;
	SignupPanel signupPanel;
	ForgotPasswordPanel forgotPasswordPanel;
	boolean isLoggedin;

	
	// Networking
	ClientServerCommunicator clientServerCommunicator;
	ClientClientCommunicator clientClientCommunicator;
	
	// GameController
	GameController gameController;
	
	String opponentUsername;
	boolean isHost;
	
	
	ClientPanel()
	{
		initializeComponents();
		createGUI();
	}
	
	private void initializeComponents()
	{
		// Card layout

		cardLayout = new CardLayout();
		
		// Panels
		gameLoginPanel = new GameLoginPanel();
		gameLobbyPanel = new GameLobbyPanel(this);
		gamePanel = new GamePanel(this);
		
		
		signupPanel = new SignupPanel();
		forgotPasswordPanel = new ForgotPasswordPanel();
		
		// Networking
		clientServerCommunicator = new ClientServerCommunicator(this);
		clientClientCommunicator = new ClientClientCommunicator(this);
		
		
	}
	
	private void createGUI()
	{
		// Set transparent
		setOpaque(false);
		
		// Set layout
		setLayout(cardLayout);
		
		// Panels
		add(gameLoginPanel,ClientConstants.gameLoginPanelName);
		add(gameLobbyPanel,ClientConstants.gameLobbyPanelName);
		add(gamePanel,ClientConstants.gamePanelName);
		add(signupPanel,ClientConstants.signupPanelName);
		add(forgotPasswordPanel,ClientConstants.forgotPasswordPanelName);
		// Show login panel
		
		
		show(ClientConstants.gameLoginPanelName);
		
	}
	
	
	public void createAccount(CreateAccountObject cao)
	{
		if(clientServerCommunicator.connect(gameLoginPanel.getHostname(), gameLoginPanel.getPort()))
		{
			clientServerCommunicator.sendObject(cao);
		}
	}
	
	public void endGame()
	{
		isHost = false;
		opponentUsername = null;
		gameLobbyPanel.show(ClientConstants.leaderBoardPanelName);
		show(ClientConstants.gameLobbyPanelName);
		gameLobbyPanel.getPlayPanel().show(ClientConstants.playButtonPanelName);
	}
	
	public void login(String inHostname, int inPort, String inUsername, String inPassword)
	{
		if(clientServerCommunicator.connect(inHostname, inPort))
		{
			isLoggedin = true;
			System.out.println("Connected.");
			clientServerCommunicator.sendObject(new LoginObject(inUsername, inPassword));
		}
		else
		{
			System.out.println("Connection failed.");
		}
	}
	
	
	public void show(String panelName)
	{
		cardLayout.show(this, panelName);
	}
	
	public void receiveObject(Object obj)
	{
		// Receive login validation from server
		if(obj instanceof LoginObject)
		{
			LoginObject loginObject = (LoginObject)obj;
			if(loginObject.isValid())
			{
				gameLobbyPanel.setLobby(loginObject.getUsername());
				show("GAMELOBBY");
			}
			else
			{
				System.out.println("Login failed.");
			}
		}
		// Receive leaderboard from server
		else if(obj instanceof LeaderBoardObject)
		{
			LeaderBoardObject lbo = (LeaderBoardObject)obj;
			gameLobbyPanel.getLeaderBoardPanel().updateLeaderBoard(lbo.getUsers());
		}
		// Receive directions for starting a game
		else if(obj instanceof GameQueueObject)
		{
			GameQueueObject gqo = (GameQueueObject)obj;
			clientClientCommunicator = new ClientClientCommunicator(this);
			if(gqo.isHost())
			{
				if(clientClientCommunicator.connectAsHost(gqo.getPort()))
				{
					isHost = true;
					
					gameController.setPlayerColor(Color.WHITE);
					gamePanel.timerPanel.resetTime();
					gamePanel.timerPanel.setTurn(false);
					
					this.sendObjectToClient(gameLoginPanel.usernameTF.getText());
					
					show("GAME");
				}
			}
			else
			{
				if(clientClientCommunicator.connectToHost(gqo.getHostname(), gqo.getPort()))
				{
					isHost = false;
					
					gameController.setPlayerColor(Color.BLACK);
					gamePanel.clientPanel.gamePanel.timerPanel.resetTime();
					gamePanel.clientPanel.gamePanel.timerPanel.setTurn(true);
					
					this.sendObjectToClient(gameLoginPanel.usernameTF.getText());
					
					show("GAME");
				}
			}
		}
		else if(obj instanceof GameSetupObject)
		{
			GameSetupObject gso = (GameSetupObject)obj;
			gamePanel.getChessPanel().setupGame(gso.getChessPieces(), gso.getGameType());
		}
		else if(obj instanceof ChessMoveObject)
		{
			ChessMoveObject cmo = (ChessMoveObject)obj;
			//gamePanel.getChessPanel().updateChessPiece(cmo.getFromX(),cmo.getFromY(),cmo.getToX(),cmo.getToY());
			gameController.receiveMoveFromNetwork(cmo.getFromX(),cmo.getFromY(),cmo.getToX(),cmo.getToY());
		}
		else if(obj instanceof PongObject){
			//gamePanel.getPongPanel().setPO_in((PongObject)obj);
			gamePanel.getPongPanel().setVar(((PongObject)obj).getcurrPlayer(), ((PongObject)obj).getpaddleLoc_y(), ((PongObject)obj).getcannonLoc_y(), ((PongObject)obj).getlives(), ((PongObject)obj).getisShotFired(), ((PongObject)obj).getballLoc_x());
		}
		
		else if(obj instanceof String){
			opponentUsername = (String)obj;
		}
		
	}
	
	public void sendObjectToServer(Object obj)
	{
		clientServerCommunicator.sendObject(obj);
	}
	
	public void sendObjectToClient(Object obj)
	{
		clientClientCommunicator.sendObject(obj);
	}
	
	public void setGameController(GameController gc)
	{
		gameController = gc;
	
	}
	
	public void setupGame()
	{
		ArrayList<GamePiece> chessPieces = gameLobbyPanel.getCustomGamePanel().getChessPieces();
		GameType gameType = gameLobbyPanel.getCustomGamePanel().getGameType();
		gamePanel.getChessPanel().setupGame(chessPieces,gameType);
		this.sendObjectToClient(new GameSetupObject(chessPieces, gameLobbyPanel.getCustomGamePanel().getGameType()));
	}
	
	public void paintComponent(Graphics g)
	{
		// super.paintComponent(g);
		g.drawImage(ClientConstants.homeBgImage, 0, 0, super.getWidth(), super.getHeight(), null);
		
		if(gameLobbyPanel.isVisible())
		{
			// Draw background image
			g.drawImage(ClientConstants.lightBgImage, 0, 0, super.getWidth(), super.getHeight(), null);
		}
	}
}

