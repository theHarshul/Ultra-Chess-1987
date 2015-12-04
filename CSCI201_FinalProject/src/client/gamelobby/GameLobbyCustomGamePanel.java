package client.gamelobby;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import networkobjects.GameQueueObject;
import client.ChessSquare;
import client.ClientConstants;
import client.ClientPanel;
import client.GameController.GameType;
import client.GamePiece;
import client.GamePiece.Type;
import client.SoundPlayer;

public class GameLobbyCustomGamePanel extends JPanel
{
	ClientPanel clientPanel;
	GameLobbyPanel gameLobbyPanel;
	
	// CheckBoxes
	JRadioButton radioButtonPong, radioButtonSpeed, radioButtonNone;
	
	// ComboBox
	JComboBox chessVariantsCB;
	
	// Buttons
	JButton emptyBoardButton, createGameButton, quitButton;
	
	// Error labels
	JLabel requiredKingsErrorLabel;
	
	// Chess board
	private ChessSquare[][] chessSquares;
	private ArrayList<GamePiece> chessPieces;
	private ArrayList<GamePiece> chessPieceBuckets;	// buckets of chess pieces to place on board
	
	// For dragging chess pieces
	private GamePiece draggingPiece;
	private GamePiece draggingBucketPiece;
	private int draggingPieceOffsetX;
	private int draggingPieceOffsetY;
	
	GameLobbyCustomGamePanel(ClientPanel cp, GameLobbyPanel glp)
	{
		clientPanel = cp;
		gameLobbyPanel = glp;
		
		initializeComponents();
		initializeChessSquares();
		initializeChessPieces();
		createGUI();
		addEvents();
	}
	
	private void initializeComponents()
	{
		// Radio buttons
		
		radioButtonPong = new JRadioButton(ClientConstants.checkBoxPongString);
		radioButtonSpeed = new JRadioButton(ClientConstants.checkBoxSpeedString);
		radioButtonNone = new JRadioButton(ClientConstants.checkBoxNoneString);
		
		radioButtonPong.setBounds(50,100,(int)radioButtonPong.getPreferredSize().getWidth(), (int)radioButtonPong.getPreferredSize().getHeight());
		radioButtonSpeed.setBounds(50,150,(int)radioButtonSpeed.getPreferredSize().getWidth(), (int)radioButtonSpeed.getPreferredSize().getHeight());
		radioButtonNone.setBounds(50,200,(int)radioButtonNone.getPreferredSize().getWidth(), (int)radioButtonNone.getPreferredSize().getHeight());
		
		radioButtonPong.setOpaque(false);
		radioButtonSpeed.setOpaque(false);
		radioButtonNone.setOpaque(false);
		radioButtonPong.setForeground(ClientConstants.backgroundColor);
		radioButtonSpeed.setForeground(ClientConstants.backgroundColor);
		radioButtonNone.setForeground(ClientConstants.backgroundColor);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioButtonPong);
		bg.add(radioButtonSpeed);
		bg.add(radioButtonNone);
		
		radioButtonNone.setSelected(true);
		
		// ComboBox
		String[] chessVariantStrings = { "Standard", "Upside-down", "Black Rebellion", "Endgame", "Legal's Game", "Peasants' Revolt", "Weak!" };
		chessVariantsCB = new JComboBox(chessVariantStrings);
		chessVariantsCB.setBounds(50,250,(int)chessVariantsCB.getPreferredSize().getWidth(), (int)chessVariantsCB.getPreferredSize().getHeight());
		
		// Buttons
		emptyBoardButton = new JButton("Clear");
		createGameButton = new JButton(ClientConstants.createGameButtonString);
		quitButton = new JButton(ClientConstants.quitButtonString);
		emptyBoardButton.setBounds(50,300,(int)emptyBoardButton.getPreferredSize().getWidth(), (int)emptyBoardButton.getPreferredSize().getHeight());
		createGameButton.setBounds(50,400,(int)createGameButton.getPreferredSize().getWidth(), (int)createGameButton.getPreferredSize().getHeight());
		quitButton.setBounds(50,450,(int)createGameButton.getPreferredSize().getWidth(), (int)createGameButton.getPreferredSize().getHeight());
	
		// Error labels
		requiredKingsErrorLabel = new JLabel("Must have one king on each side to start game");
		requiredKingsErrorLabel.setForeground(ClientConstants.textErrorColor);
		requiredKingsErrorLabel.setBounds(275,525,(int)requiredKingsErrorLabel.getPreferredSize().getWidth(), (int)requiredKingsErrorLabel.getPreferredSize().getHeight());
	}
	
	private void initializeChessSquares()
	{
		chessSquares = new ChessSquare[8][8];
		for(int y = 0; y < ClientConstants.rowCount; y++)
		{
			for(int x = 0; x < ClientConstants.columnCount; x++)
			{
				chessSquares[x][y] = new ChessSquare(x,y);
			}
		}
	}
	
	private void initializeChessPieces()
	{
		// Chess piece buckets
		chessPieceBuckets = new ArrayList<GamePiece>();
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 0, 0));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.QUEEN, 1, 0));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 2, 0));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 3, 0));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 4, 0));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, 5, 0));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 0, 9));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.QUEEN, 1, 9));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 2, 9));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 3, 9));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 4, 9));
		chessPieceBuckets.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 5, 9));
		
		setStandardBoard();
	}
	
	private void createGUI()
	{
		// Panel settings
		setLayout(null);
		setOpaque(false);
		
		// Buttons
		add(radioButtonPong);
		add(radioButtonSpeed);
		add(radioButtonNone);
		add(emptyBoardButton);
		add(chessVariantsCB);
		add(createGameButton);
		add(quitButton);
		
		// Errors
		add(requiredKingsErrorLabel);
		requiredKingsErrorLabel.setVisible(false);
	}
	
	private void addEvents()
	{
		chessVariantsCB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				
				if(chessVariantsCB.getSelectedItem() == "Standard")
				{
					setStandardBoard();
				}
				else if(chessVariantsCB.getSelectedItem() == "Upside-down")
				{
					setUpsideDownBoard();
				}
				else if(chessVariantsCB.getSelectedItem() == "Black Rebellion")
				{
					setBlackRebellionBoard();
				}
				else if(chessVariantsCB.getSelectedItem() == "Endgame")
				{
					setEndgameBoard();
				}
				else if(chessVariantsCB.getSelectedItem() == "Legal's Game")
				{
					setLegalsGame();
				}
				else if(chessVariantsCB.getSelectedItem() == "Peasants' Revolt")
				{
					setPeasantsRevoltBoard();
				}
				else if(chessVariantsCB.getSelectedItem() == "Weak!")
				{
					setWeakBoard();
				}
			}
		});
		
		emptyBoardButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				chessPieces = new ArrayList<GamePiece>();
				repaint();
			}
		});
		
		createGameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				
				if(isBoardValid())
				{
					clientPanel.sendObjectToServer(new GameQueueObject(true,true));
					gameLobbyPanel.getPlayPanel().show(ClientConstants.searchForMatchPanelName);
				}
			}
		});
		
		quitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playButton();
				gameLobbyPanel.show(ClientConstants.gameSelectorPanelName);
			}
		});
		
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent me)
			{
				int mx = me.getX();
				int my = me.getY();
				
				// bucket pieces
				for(GamePiece bucketPiece:chessPieceBuckets)
				{
					if(		mx >= bucketPiece.getPositionX() + ClientConstants.chessBoardOffsetX + ClientConstants.chessSquareOffset
							&& mx <= bucketPiece.getPositionX() + ClientConstants.chessBoardOffsetX + ClientConstants.chessSquareOffset + ClientConstants.chessSquareOffset
							&& my >= bucketPiece.getPositionY()
							&& my <= bucketPiece.getPositionY() + ClientConstants.chessSquareOffset )
					{
						draggingBucketPiece = bucketPiece;
						draggingPieceOffsetX = mx - bucketPiece.getPositionX();
						draggingPieceOffsetY = my - bucketPiece.getPositionY();
						break;
					}
				}
				
				// game pieces
				for(GamePiece chessPiece:chessPieces)
				{
					if(		mx >= chessPiece.getPositionX() + ClientConstants.chessBoardOffsetX
							&& mx <= chessPiece.getPositionX() + ClientConstants.chessBoardOffsetX + ClientConstants.chessSquareOffset
							&& my >= chessPiece.getPositionY() + ClientConstants.chessSquareOffset
							&& my <= chessPiece.getPositionY() + ClientConstants.chessSquareOffset + ClientConstants.chessSquareOffset )
					{
						draggingPiece = chessPiece;
						draggingPieceOffsetX = mx - chessPiece.getPositionX();
						draggingPieceOffsetY = my - chessPiece.getPositionY();
						break;
					}
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent me)
			{
				int mx = me.getX();
				int my = me.getY();
				
				// bucket piece
				if(draggingBucketPiece != null)
				{
					if(isMoveValid(mx,my))
					{
						createChessPiece(mx,my);
					}
					returnBucketPiece();
					draggingBucketPiece = null;
					repaint();
				}
				
				// game piece
				if(draggingPiece != null)
				{
					if(isMoveValid(mx, my))
					{
						moveChessPiece(mx, my);
					}
					else
					{
						removeChessPiece();
					}
					draggingPiece = null;
					repaint();
				}
			}
		});
		
		addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent me)
			{
				// bucket piece
				if(draggingBucketPiece != null)
				{
					draggingBucketPiece.setPositionX(me.getX() - draggingPieceOffsetX);
					draggingBucketPiece.setPositionY(me.getY() - draggingPieceOffsetY);
					repaint();
				}
				
				// game piece
				if(draggingPiece != null)
				{
					draggingPiece.setPositionX(me.getX() - draggingPieceOffsetX);
					draggingPiece.setPositionY(me.getY() - draggingPieceOffsetY);
					repaint();
				}
			}
		});
	}
	private void createChessPiece(int mx, int my)
	{
		GamePiece gp = new GamePiece(draggingBucketPiece);
		chessPieces.add(gp);
		
		for(int y = 0; y < ClientConstants.rowCount; y++)
		{
			for(int x = 0; x < ClientConstants.columnCount; x++)
			{
				if(		mx >= chessSquares[x][y].getPositionX() + ClientConstants.chessBoardOffsetX
						&& mx <= chessSquares[x][y].getPositionX() + ClientConstants.chessBoardOffsetX + ClientConstants.chessSquareOffset
						&& my >= chessSquares[x][y].getPositionY() + ClientConstants.chessSquareOffset
						&& my <= chessSquares[x][y].getPositionY() + ClientConstants.chessSquareOffset  + ClientConstants.chessSquareOffset)
				{
					gp.setXY(x,y);
					return;
				}
			}
		}
	}
	
	public ArrayList<GamePiece> getChessPieces()
	{
		return chessPieces;
	}
	
	public GameType getGameType()
	{
		if(radioButtonPong.isSelected())
		{
			return GameType.PONG;
		}
		if(radioButtonSpeed.isSelected())
		{
			return GameType.SPEED;
		}
		return GameType.NONE;
	}
	
	private boolean isBoardValid()
	{
		int blackKingCounter = 0;
		int whiteKingCounter = 0;
		
		for(GamePiece gp:chessPieces)
		{
			if(gp.getType() == Type.KING)
			{
				if(gp.isBlack())
				{
					blackKingCounter++;
				}
				else if(gp.isWhite())
				{
					whiteKingCounter++;
				}
			}
		}
		
		if(blackKingCounter == 1 && whiteKingCounter == 1)
		{
			requiredKingsErrorLabel.setVisible(false);
			SoundPlayer.playLuger();
			return true;
		}
		SoundPlayer.playRandomTerrible();
		requiredKingsErrorLabel.setVisible(true);
		return false;
	}
	
	private boolean isMoveValid(int mx, int my)	// TO-DO
	{
		// Check if mouse released outside chessboard
		if(
				mx >= ClientConstants.chessBoardOffsetX
				&& mx <= ClientConstants.columnCount * ClientConstants.chessSquareOffset + ClientConstants.chessBoardOffsetX
				&& my >=  ClientConstants.chessSquareOffset
				&& my <= ClientConstants.rowCount * ClientConstants.chessSquareOffset + ClientConstants.chessSquareOffset
				)
		{
			return true;
		}
		return false;
	}
	
	private void moveChessPiece(int mx, int my)
	{	
		for(int y = 0; y < ClientConstants.rowCount; y++)
		{
			for(int x = 0; x < ClientConstants.columnCount; x++)
			{
				if(		mx >= chessSquares[x][y].getPositionX() + ClientConstants.chessBoardOffsetX
						&& mx <= chessSquares[x][y].getPositionX() + ClientConstants.chessBoardOffsetX + ClientConstants.chessSquareOffset
						&& my >= chessSquares[x][y].getPositionY() + ClientConstants.chessSquareOffset
						&& my <= chessSquares[x][y].getPositionY() + ClientConstants.chessSquareOffset  + ClientConstants.chessSquareOffset)
				{
					draggingPiece.setXY(x,y);
					return;
				}
			}
		}
	}
	
	private void removeChessPiece()
	{
		chessPieces.remove(draggingPiece);
	}
	
	private void returnBucketPiece()
	{
		draggingBucketPiece.setPositionX(draggingBucketPiece.getColumnX() * ClientConstants.chessSquareOffset);
		draggingBucketPiece.setPositionY(draggingBucketPiece.getRowY() * ClientConstants.chessSquareOffset);
	}
	
	private void setBlackRebellionBoard()
	{
		// Rooks, knights, bishops, kings, queens
		chessPieces = new ArrayList<GamePiece>();
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 3, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 0, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 1, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 2, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 3, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.QUEEN, 4, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 5, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 6, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 7, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			if(column != 3)
			{
				chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 0));
			}
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 1));
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 2));
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 3));
			chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 6));
		}
		repaint();
	}
	
	private void setEndgameBoard()
	{
		// Rooks, knights, bishops, kings, queens
		chessPieces = new ArrayList<GamePiece>();
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 4, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 4, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 1));
			chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 6));
		}
		
		repaint();
	}
	
	private void setLegalsGame()
	{
		// Rooks, knights, bishops, kings, queens
		chessPieces = new ArrayList<GamePiece>();
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 0, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 1, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 2, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 4, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.QUEEN, 3, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 5, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 6, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 7, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 0, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 1, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 2, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 4, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 5, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 6, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 7, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 1));
			chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 6));
		}
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 1, 5));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 2, 5));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 5, 5));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 6, 5));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 2, 4));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 3, 4));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 4, 4));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, 5, 4));
		
		repaint();
	}
	
	private void setPeasantsRevoltBoard()
	{
		chessPieces = new ArrayList<GamePiece>();
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 1, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 2, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 4, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 5, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 6, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, 4, 1));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 4, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 6));
		}
		
		repaint();
	}
	
	private void setStandardBoard()
	{
		// Rooks, knights, bishops, kings, queens
		chessPieces = new ArrayList<GamePiece>();
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 0, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 1, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 2, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 3, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.QUEEN, 4, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 5, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 6, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 7, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 0, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 1, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 2, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 3, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.QUEEN, 4, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 5, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 6, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 7, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 1));
			chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 6));
		}
		
		repaint();
	}
	
	private void setUpsideDownBoard()
	{
		// Rooks, knights, bishops, kings, queens
		chessPieces = new ArrayList<GamePiece>();
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 0, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 1, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 2, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.QUEEN, 3, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 4, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 5, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 6, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 7, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 0, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 1, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 2, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.QUEEN, 3, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 4, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 5, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 6, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 7, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 1));
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 6));
		}
		
		repaint();
	}
	
	private void setWeakBoard()
	{
		// Rooks, knights, bishops, kings, queens
		chessPieces = new ArrayList<GamePiece>();
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 0, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 1, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 2, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 3, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 4, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 5, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 6, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 7, 0));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 0, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 1, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 2, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.QUEEN, 3, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 4, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 5, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 6, 7));
		chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 7, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 1));
			chessPieces.add(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 6));
			if(column != 0 && column != 7)
			{
				chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 3));
			}
		}
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, 2, 2));
		chessPieces.add(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, 5, 2));
		
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// Draw chess board
		g.drawImage(ClientConstants.chessBoardImage, ClientConstants.chessBoardOffsetX, ClientConstants.chessSquareOffset, null);
		
		// Draw chess pieces
		for(GamePiece p:chessPieces)
		{
			g.drawImage(p.getImage(), p.getPositionX() + ClientConstants.chessBoardOffsetX, p.getPositionY() + ClientConstants.chessSquareOffset, null);
		}
		
		// Draw chess piece buckets
		for(GamePiece p:chessPieceBuckets)
		{
			g.drawImage(p.getImage(), p.getPositionX() + ClientConstants.chessBoardOffsetX + ClientConstants.chessSquareOffset, p.getPositionY(), null);
		}
	}
}
