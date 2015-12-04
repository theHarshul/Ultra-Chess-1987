package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import networkobjects.ChessMoveObject;
import networkobjects.User;
import server.MySQLDriver;
import client.GameController.GameType;

public class ChessPanel extends JPanel
{
	private ClientPanel clientPanel;
	
	private ChessSquare[][] chessSquares;
	private ArrayList<GamePiece> chessPieces;
	
	// For dragging chess pieces
	private GamePiece draggingPiece;
	private int draggingPieceOffsetX;
	private int draggingPieceOffsetY;
	GameController gameController;
	
	boolean animatingDead;
	int deadX, deadY;
	GamePiece deadPiece;
	
	
	ChessPanel(ClientPanel cp, GameController gc)
	{
		animatingDead = false;
		clientPanel = cp;
		gameController = gc;
		createGUI();
		addEvents();
		
		
		
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
	
	private void createGUI()
	{	
		// Set transparent
		setOpaque(false);
		
	}
	
	private void addEvents()
	{
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent me)
			{
				if(!gameController.myTurn) return;
				int mx = me.getX();
				int my = me.getY();
				for(GamePiece chessPiece:chessPieces)
				{
					if(		mx >= chessPiece.getPositionX()
							&& mx <= chessPiece.getPositionX() + ClientConstants.chessSquareOffset
							&& my >= chessPiece.getPositionY()
							&& my <= chessPiece.getPositionY() + ClientConstants.chessSquareOffset )
					{
						if(gameController.viewSentClick(mx/ClientConstants.chessSquareOffset, my/ClientConstants.chessSquareOffset) > 0){
							draggingPiece = chessPiece;
							draggingPieceOffsetX = mx - chessPiece.getPositionX();
							draggingPieceOffsetY = my - chessPiece.getPositionY();
						}
						break;
					}
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent me)
			{
				int mx = me.getX();
				int my = me.getY();
				if(draggingPiece != null)
				{
					if(isMoveValid(mx, my))
					{
						moveChessPiece(mx, my);
					}
					else
					{
						returnChessPiece();
						gameController.resetViewClick();
					}
					draggingPiece = null;
					clearHighlights();
					repaint();
				}
				else{
					gameController.resetViewClick();
				}
			}
		});
		
		addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent me)
			{
				if(draggingPiece != null)
				{
					draggingPiece.setPositionX(me.getX() - draggingPieceOffsetX);
					draggingPiece.setPositionY(me.getY() - draggingPieceOffsetY);
					repaint();
				}
			}
		});
	}
	
	public boolean captureChessPiece(int inX, int inY)
	{
		System.out.println("Capturing");
		if(chessSquares[inX][inY].hasPiece())
		{
			chessPieces.remove(chessSquares[inX][inY].getPiece());
			chessSquares[inX][inY].setPiece(null);
			return true;
		}
		return false;
	}
	
	public ChessSquare getChessSquare(int inX, int inY)
	{
		return chessSquares[inX][inY];
	}
	
	private boolean isMoveValid(int mx, int my)
	{
		System.out.println("I am " + clientPanel.isHost + " a host");
		System.out.println("ME: " + clientPanel.gameLoginPanel.usernameTF.getText());
		System.out.println("Oppononent: " +clientPanel.opponentUsername);
		
		// Check if mouse released outside chessboard
		if(
				mx >= 0
				&& mx <= ClientConstants.columnCount * ClientConstants.chessSquareOffset
				&& my >= 0
				&& my <= ClientConstants.rowCount * ClientConstants.chessSquareOffset
				)
		{
			int moveVal = gameController.viewSentClick(mx/ClientConstants.chessSquareOffset, my/ClientConstants.chessSquareOffset);
			System.out.println("ChessPanel. IsMoveValid: " + moveVal);
			if(moveVal > 0) return true;
			//return true;
		}
		return false;
	}
	
	private void moveChessPiece(int mx, int my)
	{	
		for(int y = 0; y < ClientConstants.rowCount; y++)
		{
			for(int x = 0; x < ClientConstants.columnCount; x++)
			{
				if(		mx >= chessSquares[x][y].getPositionX()
						&& mx <= chessSquares[x][y].getPositionX() + ClientConstants.chessSquareOffset
						&& my >= chessSquares[x][y].getPositionY()
						&& my <= chessSquares[x][y].getPositionY() + ClientConstants.chessSquareOffset )
				{
					clientPanel.sendObjectToClient(new ChessMoveObject(draggingPiece.getColumnX(),draggingPiece.getRowY(),x,y));	// if it was your move, update other client
					
					for(int pieceNum = chessPieces.size()-1; pieceNum >= 0; pieceNum--){ //No overlapping pieces
						GamePiece gp = chessPieces.get(pieceNum);
						if(gp.getColumnX() == x && gp.getRowY() == y){
							chessPieces.remove(pieceNum);
							animateDeadPiece(gp, gp.getPositionX(), gp.getPositionY());
							break;
						}
					}
					draggingPiece.setXY(x,y);

					//Send data to chessModel
					return;
				}
			}
		}
	}
	
	//TODO
	private void animateDeadPiece(GamePiece piece, int oldX, int oldY){
		System.out.println("Animating");
		animatingDead = true;
		deadX = oldX;
		deadY= oldY;
		deadPiece = piece;
		
		/*while(deadX > 0 && deadY > 0){
			animatingDead = true;
			deadX -= 10;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}*/
		animatingDead = false;
	}
	
	
	
	private void returnChessPiece()	// returns chess piece to original position if invalid move
	{
		draggingPiece.setPositionX(draggingPiece.getColumnX() * ClientConstants.chessSquareOffset);
		draggingPiece.setPositionY(draggingPiece.getRowY() * ClientConstants.chessSquareOffset);
	}
	
	public void setupGame(ArrayList<GamePiece> cp, GameType gt)
	{
		// Initialize chess squares
		initializeChessSquares();
		
		// Initialize chess pieces
		chessPieces = cp;
		
		// Add image for each chess piece and attach chess piece to chess square
		for(GamePiece gp:cp)
		{
			gp.initializeImage();
			chessSquares[gp.getColumnX()][gp.getRowY()].setPiece(gp);
		}
		gameController.setupGame(chessSquares, gt);
		

		
		// Draw
		repaint();
	}
	
	// Update chess piece movements over network
	public void updateChessPiece(int fromX, int fromY, int toX, int toY)
	{
		for(GamePiece chessPiece:chessPieces)
		{
			if(chessPiece.getColumnX() == fromX && chessPiece.getRowY() == fromY)
			{
				chessPiece.setXY(toX,toY);
				break;
			}
		}
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// Draw chess board
		g.drawImage(ClientConstants.chessBoardImage, 0, 0, null);
		
		// Draw chess pieces
		if(chessPieces != null)
		{
			for(GamePiece p:chessPieces)
			{
				g.drawImage(p.getImage(), p.getPositionX(), p.getPositionY(), null);
			}
			for(int i = 0 ; i < 8; i++){
				for(int j =0; j < 8; j++){
					g.setColor(new Color(255,255,224, 80)); //transparent light yellow
					//g.setColor(Color.black);
					if(chessSquares[i][j].highlighted) g.fillRect(i*50, j*50, 50, 50);
					//g.fillRect(i*50, j*50, 50, 50);
				}
			}
		}
		if(animatingDead){
			g.drawImage(deadPiece.getImage(), deadX, deadY, null);
		}
	}
	
	//TODO ** ALSO ADD A COOL ANIMATION
	/*Controller calls this when we send him a valid move
	 * or When he receives a move over the network
	 * This animates makes an animated move
	 * Don't worry about checkmate and validity. Controller will call model to take care of that
	*/
	public void makeMove(int x1, int y1, int x2, int y2){
		for(int pieceNum = chessPieces.size()-1; pieceNum >= 0; pieceNum--){ //No overlapping pieces
			GamePiece gp = chessPieces.get(pieceNum);
			if(gp.getColumnX() == x2 && gp.getRowY() == y2){ 
				chessPieces.remove(pieceNum);
				break;
			}
		}
		updateChessPiece(x1, y1, x2, y2);
		repaint();
	}
	
	public void clearHighlights(){
		for (int i = 0; i < chessSquares.length; i++)
			for(int j = 0; j<chessSquares[0].length; j++)
				chessSquares[i][j].highlighted = false;
	}
	
	
	public void highlightSquares(ArrayList<Move> moveList){
		for(int i = 0; i < moveList.size(); i++){
			ChessSquare curSquare = moveList.get(i).second;
			int x = curSquare.getIndexX();
			int y = curSquare.getIndexY();
			chessSquares[x][y].highlighted = true;
		}
	}
	
	/*
	 * Checkmate, stalemate, disconnect
	 */
	//TODO
	public void popupMessage(String message, String chessNot){
		JOptionPane.showMessageDialog(this,
			    message);
		clientPanel.endGame();
	}

	//checks if user is the host 
	public void updateLosersStats() {
		System.out.println(clientPanel.gameLoginPanel.usernameTF.getText() +"lose");
		User u = clientPanel.gameLoginPanel.current; //current user stats
		if(u != null){
			MySQLDriver driver = new MySQLDriver();
			driver.connect();
			driver.updateUserStats(u.getUsername(), u.getRating() - 10, u.getWins(), u.getLosses() + 1, u.getGamesPlayed() + 1);
			driver.stop();
		}
	
	}
	
	

	public void updateWinnersStats() {
		System.out.println(clientPanel.gameLoginPanel.usernameTF.getText() + "wins");
		User u = clientPanel.gameLoginPanel.current;
		if(u != null){
			MySQLDriver driver = new MySQLDriver();
			driver.connect();
			driver.updateUserStats(u.getUsername(), u.getRating() + 10, u.getWins() + 1, u.getLosses(), u.getGamesPlayed() + 1);
			driver.stop();
		}
		
	}

	public void addGameStats(ChessModel cm, boolean iWon) {
		
		String opponent = clientPanel.opponentUsername;
		String username = clientPanel.gameLoginPanel.usernameTF.getText();
		
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		driver.addUserGame(username, opponent, iWon, cm.chessNotation);
		driver.stop();
		
	}
}
