package client;

import client.GamePiece.Color;
import networkobjects.ChessMoveObject;

/*
 * This class is instantiated when two players connect from GameLobbyPanel. 
 * It uses the settings to create the ChessPanel, ChessGame, and PongPanel. 
 * When an event occurs in ChessPanel (i.e. a square is clicked), it will send that data to ChessGame.
 *  If it is the first click, it will highlight valid squares to move to. 
 *  If it is second click and a valid move has been made, ChessGame will return a Move object. 
 *  Controller will send this object to ChessPanel, as well as across the network to the opponent. 
 *  After the current player has made a move, Controller will wait to receive a MoveObject from the opponent. 
 *  After the game receives a move/right after it sends the move, it checks if GameIsDone (i.e. there was a checkmate or stalemate).
 *   When a checkmate has occurred, the Controller will change the Window’s panel and send the game data to the Central Server.
 * Settings
 * 		GameController is instantiated based on the settings from MainMenu. These Include:
			Initial Piece positions
			Whether the game is using Pong, a Move Timer, or neither
			Whether it is a Checkers or Chess
				If it is a Checkers, myGame will be instantiated as a Checkers instead of a Chess. 
				Checkers will be a subclass of Chess, because they are very similar

	Has a (Variables)
		Socket of opponent
		Socket of server
		int playerColor (indicates the player who is running this instance of the program)

	Has a (Objects)
		ChessGame myGame
		ChessPanel
		PongPanel (may be null)
		Timer (may be null)

 * 
 * 
 */
public class GameController {
	
	public enum GameType
	{
		PONG, SPEED, NONE
	}
	
	private String serverSocketLocation, opponentSocketLocation; //TODO: Update these to correct data type when we implement networking
	private int playerColor; //0 indicates white player, 1 indicates black player
	
	PongPanel pong; //we will send this messages when it's time to reset (i.e. every time it is a new turn)
	ChessPanel chessPanel;
	ChessModel gameModel;
	boolean firstClick;
	boolean myTurn;
	ClientPanel clientPanel;
	GameType gameType;
	
	ChessSquare selectedSquare; //IMPORTANT: set this as null every time the player starts their turn
	
	public ChessPanel getChessPanel(){
		return chessPanel;
	}
	
	public boolean isMyTurn(){
		return myTurn;
	}
	
	public GameController(ClientPanel cp, PongPanel p){ //This should also accept the settings from the previous panels	
		cp.setGameController(this);
		clientPanel = cp;
		pong = p;
		
		chessPanel = new ChessPanel(cp, this); 
		//gameModel = new ChessModel(playerColor); //if statement checks settings. Potentially makes this a CheckersPanel
		//chessPanel.setInitialPositions(gameModel);
		firstClick = true;
		if(playerColor == 0) myTurn = true;
		else myTurn = false;
	
	}
	
	
	public void receiveMoveFromNetwork(int x1, int y1, int x2, int y2){
		gameModel.makeMove(x1, y1, x2, y2);
		chessPanel.makeMove(x1, y1, x2, y2);
		clientPanel.gamePanel.timerPanel.resetTime();
		if(gameModel.determineCheckMate(playerColor)){
			chessPanel.popupMessage("Checkmate", gameModel.chessNotation);
			
			chessPanel.updateLosersStats(); //updates user scores after game is over
			chessPanel.addGameStats(gameModel,false);
			SoundPlayer.playRandomTerrible();
			//go to next panel
		}
		else if(gameModel.determineStalemate(playerColor)){
			chessPanel.popupMessage("Stalemate", gameModel.chessNotation);

			chessPanel.updateLosersStats();
			chessPanel.addGameStats(gameModel,false);

			SoundPlayer.playRandomTerrible();

			//go to the next panel
		}
		else{
			myTurn = true;
		}
	}
	
	/*
	 * Called by Timer and Pong
	 */
	public void skipTurn(){
		if(gameModel != null){
		Move m = gameModel.findOneValidMove();
		gameModel.makeMove(m);
		clientPanel.sendObjectToClient(new ChessMoveObject(m.first.getIndexX(), m.first.getIndexY(), m.second.getIndexX(), m.second.getIndexY()));
		chessPanel.makeMove(m.first.getIndexX(), m.first.getIndexY(), m.second.getIndexX(), m.second.getIndexY());
		myTurn = false;
		System.out.println("Skip turn");
		}
	}
	
	//We should do a nice animation for invalid moves (like a faint red X)
	//returns negative if invalid tile
	//positive if valid tile
	//0 if deselected a previously selected tile
	public int viewSentClick(int x, int y){
		if(firstClick){
			return viewCallsFirstClick(x, y);
		}
		else return viewCallsSecondClick(x, y);
	}
	

	//selects a piece and highlights valid tiles
	//return -2 if: clicked opponent tile
	private int viewCallsFirstClick(int x, int y){

		System.out.println("view calls 1st");
		firstClick = false;
		ChessSquare cs = gameModel.getSquare(x, y);
		
		if(gameModel.validFirstSquare(cs)){
			selectedSquare = gameModel.getSquare(x, y);
			chessPanel.highlightSquares(gameModel.findAllValidMoves(cs));
			return 2;
		}
		return -2;
	}
	
	public void resetViewClick(){
		firstClick = true;
		selectedSquare = null;
		chessPanel.clearHighlights();
	}
	

	//return -1 if invalid move
	//return 0 if player clicked selectedTile (meaning they're deselecting it)
	//return 1 if validMove
	private int viewCallsSecondClick(int x, int y){
		System.out.println("view calls 2nd");
		firstClick = true;
		if(selectedSquare == null) return -1;
		ChessSquare sq = gameModel.getSquare(x, y);
		if(sq.equals(selectedSquare)){
			chessPanel.clearHighlights();
			selectedSquare = null;
			return 0;
		}
		Move m = new Move(selectedSquare, sq);
		
		if(gameModel.checkIfValidMove(m)){
			
			myTurn = false;
			chessPanel.clearHighlights();
			System.out.println("Controller is making the move");
			gameModel.makeMove(m);
			//chessPanel.makeMove(m);
			if(gameModel.determineCheckMate(1-playerColor)){
				chessPanel.popupMessage("Checkmate", gameModel.chessNotation);
				chessPanel.updateWinnersStats();
				chessPanel.addGameStats(gameModel,true);
				SoundPlayer.playRandomGreat();
				//go to next panel
			}
			else if(gameModel.determineStalemate(1-playerColor)){
				chessPanel.popupMessage("Stalemate", gameModel.chessNotation);
				chessPanel.addGameStats(gameModel,true);
				chessPanel.updateLosersStats();

				SoundPlayer.playRandomTerrible();

				//go to the next panel
			}
			
			return 1;
		}
		else{
			return -1;
		}
	}
	
	public void setPlayerColor(Color color)
	{
		if(color.equals(Color.WHITE))
		{
			playerColor = 0;
		}
		else if(color.equals(Color.BLACK))
		{
			playerColor = 1;
		}
	}
	
	public void setupGame(ChessSquare[][] cs, GameType gt){
		gameModel = new ChessModel(playerColor, cs);
		gameType = gt;
		if(gameType == GameType.PONG){
			clientPanel.gamePanel.cl.show(clientPanel.gamePanel.pongSpeed, "pong");
		}
		else if(gameType == GameType.SPEED){
			clientPanel.gamePanel.cl.show(clientPanel.gamePanel.pongSpeed, "timer");
				
		}
		else{
			clientPanel.gamePanel.pongSpeed.setVisible(false);
		}
		
		//clientPanel.gamePanel.setupGame();
	}
}
