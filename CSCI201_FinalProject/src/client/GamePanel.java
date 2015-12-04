package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

public class GamePanel extends JPanel
{
	ClientPanel clientPanel;
	ChessPanel chessPanel;
	TimerPanel timerPanel;
	PongPanel pongPanel;
	GameController controller;
	public CardLayout cl;
	JPanel pongSpeed;
	
	GamePanel(ClientPanel cp)
	{
		clientPanel = cp;
		initializeComponents();
		createGUI();
		//createGUI();
		//setupGame();
		addEvents();
		
	}
	
	private void initializeComponents()
	{
		// Panels
		controller = new GameController(clientPanel, pongPanel); /*TODO: Pass it the game settings as parameters */
		pongPanel = new PongPanel(clientPanel, controller);
		chessPanel = controller.getChessPanel();
		timerPanel = new TimerPanel(clientPanel,controller);
		
		chessPanel.setPreferredSize(ClientConstants.chessPanelSize);
		pongPanel.setPreferredSize(ClientConstants.pongPanelSize);
	}
	

	private void createGUI()
	{
		pongSpeed = new JPanel();
		cl = new CardLayout();
		pongSpeed.setLayout(cl);
		pongSpeed.add(pongPanel, "pong");
		pongSpeed.add(timerPanel, "timer");
		
		setOpaque(false);
		setLayout(new BorderLayout());
		
		add(chessPanel, BorderLayout.CENTER);
		add(pongSpeed, BorderLayout.SOUTH);
		
		/*
		JPanel chessSpeed = new JPanel();
		chessSpeed.setLayout(new BorderLayout());
		// Set transparent
		setOpaque(false);
		
		//GridLayout
		setLayout(new BorderLayout());
		
		// Add components
		chessSpeed.add(chessPanel, BorderLayout.CENTER);
		chessSpeed.add(timerPanel, BorderLayout.EAST);
		add(chessSpeed,BorderLayout.CENTER);
		add(pongPanel,BorderLayout.SOUTH);
		*/
	}
	
	
	private void addEvents()
	{
		
	}
	
	public ChessPanel getChessPanel()
	{
		return chessPanel;
	}
	
	public PongPanel getPongPanel()
	{
		return pongPanel;
	}
}
