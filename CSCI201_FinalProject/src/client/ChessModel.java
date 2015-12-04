package client;

import java.util.ArrayList;
import java.util.Random;

public class ChessModel {
	int playerColor; //0 is white 1 is black
	public ChessSquare[][] chessSquares;
	public boolean fakeMove;
	String chessNotation;
	public ChessModel(int pColor){ //should also take initial positions
		fakeMove = false;
		chessNotation = "";
		playerColor = pColor;
		chessSquares = new ChessSquare[8][8];
		for(int i = 0; i <8; i++){
			for(int j= 0; j<8; j++){
				chessSquares[i][j] = new ChessSquare(i, j);
			}
		}
		setDefaultInitialPositions();
	}
	
	////TODO: Call this instead of the above constructor from whoever does that
	public ChessModel(int pColor, ChessSquare[][] cs){ //should also take initial positions		
		chessNotation = "";
		fakeMove = false;
		playerColor = pColor;
		chessSquares = new ChessSquare[8][8];

		for(int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				ChessSquare csSquare = cs[i][j];
				if(csSquare != null){
					chessSquares[i][j] = new ChessSquare(csSquare.getIndexX(), csSquare.getIndexY());
					GamePiece hisPiece = csSquare.getPiece();
					if(hisPiece != null)
						chessSquares[i][j].setPiece(new GamePiece(hisPiece.color, hisPiece.type, hisPiece.columnX, hisPiece.rowY));
				}else{
					System.out.println("ERROR in ChessModel. cur CS is null");
				}
			}
		}
	}
	
	public ChessModel (ChessModel cm){
		this(cm.playerColor, cm.chessSquares);
	}
	
	public void setSquares(ChessSquare [][] newSquares){
		
	}
	
	private void setDefaultInitialPositions(){
		chessSquares[0][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 0, 0));
		chessSquares[1][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 1, 0));
		chessSquares[2][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 2, 0));
		chessSquares[3][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KING, 3, 0));
		chessSquares[4][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.QUEEN, 4, 0));
		chessSquares[5][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.BISHOP, 5, 0));
		chessSquares[6][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.KNIGHT, 6, 0));
		chessSquares[7][0].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.ROOK, 7, 0));
		chessSquares[0][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 0, 7));
		chessSquares[1][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 1, 7));
		chessSquares[2][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 2, 7));
		chessSquares[3][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KING, 3, 7));
		chessSquares[4][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.QUEEN, 4, 7));
		chessSquares[5][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.BISHOP, 5, 7));
		chessSquares[6][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.KNIGHT, 6, 7));
		chessSquares[7][7].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.ROOK, 7, 7));
		
		// Pawns
		for(int column = 0; column < ClientConstants.columnCount; column++)
		{
			chessSquares[column][1].setPiece(new GamePiece(GamePiece.Color.BLACK, GamePiece.Type.PAWN, column, 1));
			chessSquares[column][6].setPiece(new GamePiece(GamePiece.Color.WHITE, GamePiece.Type.PAWN, column, 6));
		}
	}
	
	//TODO **needs testing
	/*
	calculating when there is a check/checkmate
	To determine White King check:
	Have ArrayList called ThreatenedSquares
	for each square, if piece on the square is Black, call the getThreatenedSquares method and add its contents to get ThreatenedSquares
	If the King’s square lies in getThreatenedSquares, it is in check
	Do the opposite for Black King check
	To determine White King checkmate:
	Determine that the White King is in Check
	For each white piece:
	For each square in that piece’s getValidMoves return value (an array):
	Create a new ChessBoard object that is identical. In that new ChessBoard, move that piece to that square. If the King is no longer in check, the King is not Checkmated.
	If you make it past every valid square without returning false, the king is checkmated.
	Do the opposite for Black King checkmate*/
	public boolean determineCheckMate(int activePlayer){
		if(!determineCheck(activePlayer)) {
			System.out.println("ERROR in ChessModel. determineCheckmate was called when we were not in check");
			return false;
		}
		for(int i = 0; i < 8; i++){
			for(int j = 0; j< 8; j++){
				ChessSquare cs = chessSquares[i][j];
				if(cs.hasPiece()){
					GamePiece gp = cs.getPiece();
					boolean isWhite = true;
					if(activePlayer == 1) isWhite = false;
					if(gp.isWhite() == isWhite){
						if(findAllValidMoves(cs).size() > 0) return false;
					}
				}
			}
		}
		if(Math.random() > .5 && !fakeMove){
			SoundPlayer.playRandomGreat();
		}
		else if (!fakeMove)SoundPlayer.playRandomTerrible();
		return true;
	}
	
	//TODO *** needs testing
	/*
	calculating when there is a stalemate
	To determine White Stalemate:
	Make sure it is white’s turn. If it isn’t, it is not a stalemate.
	Determine that the White King is not in Check. If it is, it is not a stalemate.
	Other than that, follow the same procedure for determining a checkmate.
	If that returns false (i.e. it would not be a checkmate, ignoring the pre-condition that the king must be in check), it is not a stalemate.
	For each tile on the board, determine if it has a white piece. If it does, call that piece’s getValidMoves() function. If any of these pieces have a non-empty return value, it is not a stalemate.
	If you make it past all of these conditions without returning false, it is a stalemate.
	Do the opposite for Black Stalemate*/

	public boolean determineStalemate(int activePlayer){
		if(determineCheck(activePlayer)) {
			System.out.println("ERROR in ChessModel. determineStalemate was called while we were in check");
			return false;
		}
		for(int i = 0; i < 8; i++){
			for(int j = 0; j< 8; j++){
				ChessSquare cs = chessSquares[i][j];
				if(cs.hasPiece()){
					GamePiece gp = cs.getPiece();
					boolean isWhite = true;
					if(activePlayer == 1) isWhite = false;
					if(gp.isWhite() == isWhite){
						if(findAllValidMoves(cs).size() > 0) return false;
					}
				}
			}
		}
		return true;
	}
	
	//pass 0 if you want to see if white is in check
	public boolean determineCheck(int checkedPlayer){
		if(checkedPlayer == 0){
			for(int i= 0; i < 8; i++)
				for(int j= 0; j < 8; j++){
					ChessSquare cs = chessSquares[i][j];
					if(cs.hasPiece()){
						GamePiece gp = cs.getPiece();
						if(gp.isBlack()){
							if(gp.isPawn()){
								if(determineCheckPawn(cs)) return true;
							}
							if(gp.isRook()){
								if(determineCheckRook(cs)) return true;
							}
							if(gp.isKnight()){
								if(determineCheckKnight(cs)) return true;
							}
							if(gp.isBishop()){
								if(determineCheckBishop(cs)) return true;
							}
							if(gp.isQueen()){
								if(determineCheckQueen(cs)) return true;
							}
							if(gp.isKing()){
								if(determineCheckKing(cs)) return true;
							}
						}
					}
				}
		}
		else{
			for(int i= 0; i < 8; i++)
				for(int j= 0; j < 8; j++){
					ChessSquare cs = chessSquares[i][j];
					if(cs.hasPiece()){
						GamePiece gp = cs.getPiece();
						if(gp.isWhite()){
							if(gp.isPawn()){
								if(determineCheckPawn(cs)) return true;
							}
							else if(gp.isRook()){
								if(determineCheckRook(cs)) return true;
							}
							else if(gp.isKnight()){
								if(determineCheckKnight(cs)) return true;
							}
							else if(gp.isBishop()){
								if(determineCheckBishop(cs)) return true;
							}
							else if(gp.isQueen()){
								if(determineCheckQueen(cs)) return true;
							}
							else if(gp.isKing()){
								if(determineCheckKing(cs)) return true;
							}
						}
					}
				}
		}
		return false;
	}
	
	private boolean determineCheckPawn(ChessSquare cs){
		int x= cs.getIndexX();
		int y = cs.getIndexY();
		
		if(cs.getPiece().isWhite()){
			//up left
			int tempX = x-1;
			int tempY = y-1;
			 if (determineCheckHelper(tempX, tempY, true)) return true;
			//up right
			 tempX = x+1;
			 tempY = y-1;
			 if (determineCheckHelper(tempX, tempY, true)) return true;

		}
		else{
			//down left
			int tempX = x-1;
			int tempY = y+1;
			 if (determineCheckHelper(tempX, tempY, false)) return true;

			//down right
			
			 tempX = x+1;
			 tempY = y+1;
			 if (determineCheckHelper(tempX, tempY, false)) return true;
		}
		return false;
	}
	
	private boolean determineCheckHelper(int tempX, int tempY, boolean isWhite){
		if(tempX >= 0 && tempX <= 7 && tempY >= 0 && tempY <= 7){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isKing() && ns.getPiece().isWhite()!= isWhite)
					return true;
			}
		}
		return false;
	}
	
	private boolean determineCheckKnight(ChessSquare cs){
		int x = cs.getIndexX();
		int y = cs.getIndexY();
		boolean isWhite = cs.getPiece().isWhite();
		
		int tempX = x + 1;
		int tempY = y-2;
		//up right
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		
		//up left
		tempX = x + 1;
		tempY = y-2;
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		
		//right up
		tempX = x+2;
		tempY = y-1;
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		
		//right down
		tempY = y+1;
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		
		//down right
		tempX = x+1;
		tempY = y-2;
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		
		//down left
		tempX = x-1;
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		
		//left down
		tempX = x-2;
		tempY = y+1;
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		
		//left up
		tempY = y-1;
		 if (determineCheckHelper(tempX, tempY, isWhite)) return true;

		return false;
	}
	
	
	private boolean determineCheckBishop(ChessSquare cs){
		int x = cs.getIndexX();
		int y = cs.getIndexY();
		boolean isWhite = cs.getPiece().isWhite();
		
		//up right
				int tempX = x + 1;
				int tempY = y-1;
				while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
					ChessSquare ns = chessSquares[tempX][tempY];
					if(ns.hasPiece()){
						if (determineCheckHelper(tempX, tempY, isWhite)) return true;				
						break;
					}
					else{
						if (determineCheckHelper(tempX, tempY, isWhite)) return true;
					}
					
					tempX++;
					tempY--;
				}
				
				//up left
				tempX = x-1;
				tempY = y-1;
				while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
					ChessSquare ns = chessSquares[tempX][tempY];
					if(ns.hasPiece()){
						if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
							if (determineCheckHelper(tempX, tempY, isWhite)) return true;
						}				
						break;
					}
					else{
						if (determineCheckHelper(tempX, tempY, isWhite)) return true;
					}
					
					tempX--;
					tempY--;
				}
				//down left
				tempX = x-1;
				tempY = y+1;
				while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
					ChessSquare ns = chessSquares[tempX][tempY];
					if(ns.hasPiece()){
						if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
							if (determineCheckHelper(tempX, tempY, isWhite)) return true;
						}				
						break;
					}
					else{
						if (determineCheckHelper(tempX, tempY, isWhite)) return true;
					}
					
					tempX--;
					tempY++;
				}
				//down right
				tempX = x+1;
				tempY = y+1;
				while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
					ChessSquare ns = chessSquares[tempX][tempY];
					if(ns.hasPiece()){
						if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
							if (determineCheckHelper(tempX, tempY, isWhite)) return true;
						}				
						break;
					}
					else{
						if (determineCheckHelper(tempX, tempY, isWhite)) return true;
					}
					
					tempX++;
					tempY++;
				}
				return false;
	}
	
	private boolean determineCheckRook(ChessSquare cs){
		int x = cs.getIndexX();
		int y = cs.getIndexY();
		boolean isWhite = cs.getPiece().isWhite();
		int tempX = x;
		int tempY = y-1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					if (determineCheckHelper(tempX, tempY, isWhite)) return true;

				}				
				break;
			}
			else{
				if (determineCheckHelper(tempX, tempY, isWhite)) return true;

			}
			
			tempY--;
		}
		//down
		tempY = y+1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					if (determineCheckHelper(tempX, tempY, isWhite)) return true;
				}				
				break;
			}
			else{
				if (determineCheckHelper(tempX, tempY, isWhite)) return true;
			}
			
			tempY++;
		}
		
		//left
		tempY = y;
		tempX = x -1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					if (determineCheckHelper(tempX, tempY, isWhite)) return true;
				}				
				break;
			}
			else{
				if (determineCheckHelper(tempX, tempY, isWhite)) return true;
			}
			
			tempX--;
		}
		
		//right
		tempY = y;
		tempX = x+1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					if (determineCheckHelper(tempX, tempY, isWhite)) return true;

				}				
				break;
			}
			else{
				if (determineCheckHelper(tempX, tempY, isWhite)) return true;
			}
			
			tempX++;
		}
		return false;
	}
	
	private boolean determineCheckQueen(ChessSquare cs){
		if(determineCheckBishop(cs)) return true;
		if(determineCheckRook(cs)) return true;
		return false;
	}
	
	//Note: this is just so you can't put two kings next to each other
	private boolean determineCheckKing(ChessSquare cs){ 
		int x= cs.getIndexX();
		int y = cs.getIndexY();
		boolean isWhite = cs.getPiece().isWhite();
		
		int tempX = x;
		int tempY = y-1;
		//up
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		//down
		tempX = x;
		tempY = y+1;
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		
		//up right
		tempX = x+1;
		tempY = y-1;
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		
		//down right
		tempY = y+1;
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		
		//up left
		tempX = x-1;
		tempY = y-1;
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		
		//down left
		tempY = y+1;
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		
		//left
		tempX = x-1;
		tempY = y;
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		
		tempX = x+1;
		tempY = y;
		if (determineCheckHelper(tempX, tempY, isWhite)) return true;
		return false;
	}
	
	//TODO*done
	//for forcing the player to move (when time expires)
	public Move findOneValidMove(){
		ArrayList<Move> moveList = new ArrayList<Move>();
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j< 8; j++){
				ChessSquare cs = chessSquares[i][j];
				if(cs.hasPiece()){
					GamePiece gp = cs.getPiece();
					boolean isWhite = true;
					if(playerColor == 1) isWhite = false;
					if(gp.isWhite() == isWhite){
						ArrayList<Move> newMoveList = findAllValidMoves(cs);
						if(newMoveList.size() > 0){
							moveList.addAll(newMoveList);
						}
					}
				}
			}
		}
		
		return moveList.get(new Random().nextInt(moveList.size()));
	}
	
	//TODO *Done	
	/*
	Determining valid moves
	Create a set of potential diagonal moves based on the piece (e.g. Bishop gets all diagonals, rook gets all forward and backward, etc.)
	Remove all squares occupied by a piece of the same color
	Unless it is a knight, remove all squares that have a roadblock along that path (e.g. for a queen if a diagonal square has a piece on it, all subsequent tiles along that diagonal are removed)
	For each square, determine that if after moving the piece there, your King would be in check. If it would be, remove that tile.
	Return what is left*/
//IMPORTANT NOTE: This is assuming white starts on bottom black starts on top
	public ArrayList<Move> findAllValidMoves(ChessSquare cs){
		System.out.println("ChessModel. Find all valid moves: " + cs.getPiece().type);
		
		
		if(cs.getPiece().isPawn()){
			return findAllValidMovesPawn(cs);
		}
		if(cs.getPiece().isBishop()){
			return findAllValidMovesBishop(cs);
		}
		if(cs.getPiece().isKing()){
			return findAllValidMovesKing(cs);
		}
		if(cs.getPiece().isKnight()){
			return findAllValidMovesKnight(cs);
		}
		if(cs.getPiece().isQueen()){
			return findAllValidMovesQueen(cs);
		}
		if(cs.getPiece().isRook()){
			return findAllValidMovesRook(cs);
		}
		System.out.println("Error in ChessModel.findAllValidMoves: ChessSquare passed does not have a valid piece type");
		return null;
	}
	
	public ArrayList<Move> findAllValidMovesPawn(ChessSquare cs){
		ArrayList<Move> moveList = new ArrayList<Move>();
		int x = cs.getIndexX();
		int y = cs.getIndexY();
		
		if(cs.getPiece().isWhite()){						
			//Top
			if(y == 0) System.out.println("Error in findAllValidMoves: White pawn is located at top row (should upgrade to new piece)");
			ChessSquare top = chessSquares[x][y-1];
			if(!top.hasPiece()){
				Move nextMove =new  Move(cs, top);
				if(moveDoesNotCauseCheck(0, nextMove)){
					moveList.add(nextMove);
				}
			}
			
			//double top
			if(y == 6){
				ChessSquare toptop = chessSquares[x][y-2];
				if(!toptop.hasPiece() && !top.hasPiece()){
					Move nextMove =new  Move(cs, toptop);
					if(moveDoesNotCauseCheck(0, nextMove)){
						moveList.add(nextMove);
					}
				}
			}
			
			//Left
			if(x>0){
				ChessSquare left = chessSquares[x-1][y-1];
				if(left.hasPiece() && left.getPiece().isBlack()){
					Move nextMove = new Move(cs, left);
					if(moveDoesNotCauseCheck(0, nextMove))
						moveList.add(nextMove);
				}
			}				
			//Right
			if(x < 7){
				ChessSquare right = chessSquares[x+1][y-1];
				if(right.hasPiece() && right.getPiece().isBlack()){
					Move nextMove = new Move(cs, right);
					if(moveDoesNotCauseCheck(0, nextMove))
						moveList.add(nextMove);
				}
			}
			
			return moveList;
		}
		else{
			if(y == 7) System.out.println("Error in findAllValidMoves: Black pawn is located at bottom row (should upgrade to new piece)");
			//Bottom
			ChessSquare bottom = chessSquares[x][y+1]; 
			if(!bottom.hasPiece()){
				Move nextMove =new  Move(cs, bottom);
				if(moveDoesNotCauseCheck(1, nextMove)){
					moveList.add(nextMove);
				}
			}
			//BottomBottom
			if(y == 1){
				ChessSquare bottombottom = chessSquares[x][y+2]; 
				if(!bottombottom.hasPiece() && !bottom.hasPiece()){
					Move nextMove =new  Move(cs, bottombottom);
					if(moveDoesNotCauseCheck(1, nextMove)){
						moveList.add(nextMove);
					}
				}
			}
			
			//Left
			if(x>0){
				ChessSquare left = chessSquares[x-1][y+1];
				if(left.hasPiece() && left.getPiece().isWhite()){
					Move nextMove = new Move(cs, left);
					if(moveDoesNotCauseCheck(0, nextMove))
						moveList.add(nextMove);
				}
			}				
			//Right
			if(x < 7){
				ChessSquare right = chessSquares[x+1][y+1];
				if(right.hasPiece() && right.getPiece().isWhite()){
					Move nextMove = new Move(cs, right);
					if(moveDoesNotCauseCheck(0, nextMove))
						moveList.add(nextMove);
				}
			}
			
			return moveList;
		}
		
	}
	
	private Move getSimplePieceMoveHelper(ChessSquare cs, int tempX, int tempY, int pieceColor){
		if(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						return nextMove;
					else return null;
				}
				else return null;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					return nextMove;
				else return null;
			}
		}
		return null;
	}
	
	public ArrayList<Move> findAllValidMovesKnight(ChessSquare cs){
		ArrayList<Move> moveList = new ArrayList<Move>();
		int x = cs.getIndexX();
		int y = cs.getIndexY();
		int pieceColor = 0;
		if(cs.getPiece().isBlack()) pieceColor = 1;
		
		int tempX = x + 1;
		int tempY = y-2;
		//up right
		Move ur = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(ur != null) moveList.add(ur);
		
		//up left
		tempX = x - 1;
		tempY = y-2;
		Move ul = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(ul != null) moveList.add(ul);
		
		//right up
		tempX = x+2;
		tempY = y-1;
		Move ru = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(ru != null) moveList.add(ru);
		
		//right down
		tempY = y+1;
		Move rd = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(rd != null) moveList.add(rd);
		
		//down right
		tempX = x+1;
		tempY = y+2;
		Move dr = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(dr != null) moveList.add(dr);
		
		//down left
		tempX = x-1;
		Move dl = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(dl != null) moveList.add(dl);
		
		//left down
		tempX = x-2;
		tempY = y+1;
		Move ld = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(ld != null) moveList.add(ld);
		
		//left up
		tempY = y-1;
		Move lu = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(lu != null) moveList.add(lu);
		return moveList;
	}
	

	
	public ArrayList<Move> findAllValidMovesBishop(ChessSquare cs){
		ArrayList<Move> moveList = new ArrayList<Move>();
		int x = cs.getIndexX();
		int y = cs.getIndexY();
		int pieceColor = 0;
		if(cs.getPiece().isBlack()) pieceColor = 1;
	
		//up right
		int tempX = x + 1;
		int tempY = y-1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempX++;
			tempY--;
		}
		
		//up left
		tempX = x-1;
		tempY = y-1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempX--;
			tempY--;
		}
		//down left
		tempX = x-1;
		tempY = y+1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempX--;
			tempY++;
		}
		//down right
		tempX = x+1;
		tempY = y+1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempX++;
			tempY++;
		}
		return moveList;
	}
	
	public ArrayList<Move> findAllValidMovesRook(ChessSquare cs){
		ArrayList<Move> moveList = new ArrayList<Move>();
		int x = cs.getIndexX();
		int y = cs.getIndexY();
		int pieceColor = 0;
		if(cs.getPiece().isBlack()) pieceColor = 1;
	
		//up
		int tempX = x;
		int tempY = y-1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempY--;
		}
		//down
		tempY = y+1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempY++;
		}
		
		//left
		tempY = y;
		tempX = x -1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempX--;
		}
		
		//right
		tempY = y;
		tempX = x+1;
		while(tempX <= 7 && tempX >= 0 && tempY <= 7 && tempY >= 0){
			ChessSquare ns = chessSquares[tempX][tempY];
			if(ns.hasPiece()){
				if(ns.getPiece().isWhite() != cs.getPiece().isWhite()){
					Move nextMove = new Move(cs, ns);
					if(moveDoesNotCauseCheck(pieceColor, nextMove))
						moveList.add(nextMove);
				}				
				break;
			}
			else{
				Move nextMove = new Move(cs, ns);
				if(moveDoesNotCauseCheck(pieceColor, nextMove))
					moveList.add(nextMove);
			}
			
			tempX++;
		}
		
		return moveList;
	}
	
	public ArrayList<Move> findAllValidMovesQueen(ChessSquare cs){
		ArrayList<Move> moveList = findAllValidMovesBishop(cs);
		ArrayList<Move> rookList = findAllValidMovesRook(cs);
		for(int x = 0; x < rookList.size(); x++){
			Move rookMove = rookList.get(x);
			boolean notDup = true;
			for(int y = 0; y < moveList.size(); y++){
				if(moveList.get(y).equals(rookMove)){
					notDup = false;
					break;
				}
			}
			if(notDup) moveList.add(rookMove);
		}
		return moveList;
	}
	
	
	public ArrayList<Move> findAllValidMovesKing(ChessSquare cs){
		ArrayList<Move> validMoves = new ArrayList<Move>();
		int x= cs.getIndexX();
		int y = cs.getIndexY();
		int pieceColor = 0;
		if(cs.getPiece().isBlack())
			pieceColor = 1;
		
		int tempX = x;
		int tempY = y-1;
		//up
		Move up = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(up != null)
			validMoves.add(up);
		//down
		tempX = x;
		tempY = y+1;
		Move down = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(down != null)
			validMoves.add(down);
		
		//up right
		tempX = x+1;
		tempY = y-1;
		Move ur = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(ur != null)
			validMoves.add(ur);
		
		//down right
		tempY = y+1;
		Move dr = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(dr != null)
			validMoves.add(dr);
		
		//up left
		tempX = x-1;
		tempY = y-1;
		Move ul = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(ul != null)
			validMoves.add(ul);
		
		//down left
		tempY = y+1;
		Move dl = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(dl != null)
			validMoves.add(dl);
		
		//left
		tempX = x-1;
		tempY = y;
		Move l = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(l != null)
			validMoves.add(l);
		
		tempX = x+1;
		tempY = y;
		Move r = getSimplePieceMoveHelper(cs, tempX, tempY, pieceColor);
		if(r != null)
			validMoves.add(r);
		
		return validMoves;
		
	}
	
	
	//TODO: This method is broken. It makes shallow copies of model's members, and modifies them. Need to fix it
	public boolean moveDoesNotCauseCheck(int playerColor, Move nextMove){
		ChessModel nextModel = new ChessModel(this);
		nextModel.fakeMove = true;
		nextModel.makeMove(nextMove);
		return !nextModel.determineCheck(playerColor);
		
	}
	
	
	//TODO*Done
	public boolean checkIfValidMove(Move m){
		ArrayList<Move> goodMoves = findAllValidMoves(m.first);
		for(int i = 0; i < goodMoves.size(); i++){
			if(goodMoves.get(i).equals(m)) return true;
		}
		return false;
	}
	
	
	//TODO*Done
	public void makeMove(Move m){
		GamePiece myPiece = chessSquares[m.first.getIndexX()][m.first.getIndexY()].getPiece();
		chessSquares[m.first.getIndexX()][m.first.getIndexY()].removePiece();
		if(Math.random() > .5){
			System.out.println("Playing good");
			if(chessSquares[m.second.getIndexX()][m.second.getIndexY()].hasPiece() && !fakeMove)
				SoundPlayer.playRandomGood();
		}
		else{
			System.out.println("playing bad");
			if(chessSquares[m.second.getIndexX()][m.second.getIndexY()].hasPiece()){
				if(chessSquares[m.second.getIndexX()][m.second.getIndexY()].getPiece().isQueen() && !fakeMove)
					SoundPlayer.playNo();
				else if (!fakeMove)
					SoundPlayer.playRandomBad();
			}
		}
		
		chessSquares[m.second.getIndexX()][m.second.getIndexY()].setPiece(myPiece);
		if(myPiece.isPawn() && (m.second.getIndexY() == 7 || m.second.getIndexY() == 0)){
			//System.out.println("ChessModel.Queen me");
			if(!fakeMove)
			SoundPlayer.playBottom();
			chessSquares[m.second.getIndexX()][m.second.getIndexY()].setPiece(new GamePiece(myPiece.color, GamePiece.Type.QUEEN, m.second.getIndexX(), m.second.getIndexY()));
		}
	}
	
	public void makeMove(int x1, int y1, int x2, int y2){
		Move m = new Move(chessSquares[x1][y1], chessSquares[x2][y2]);
		String moveString = (x1+1) + getColumn(y1) + "-" + (x2+1) + getColumn(y2) + "\n";
		chessNotation += moveString;
		makeMove(m);		
	}
	
	private String getColumn(int col){
		switch(col) {
			case 0: return "a";
			case 1: return "b";
			case 2: return "c";
			case 3: return "d";
			case 4: return "e";
			case 5: return "f";
			case 6: return "g";
			case 7: return "h";
		}
		System.out.println("Error. ChessModel.getColumn() receive in valid input");
		return "Error. ChessModel.getColumn() receive in valid input";
	}
	
	//TODO*Done
	public ChessSquare getSquare(int x, int y){
		return chessSquares[x][y];
	}

	//TODO*Done
	//Called by controller
	//Return false if player is clicking opponent square
	// or if there are no valid moves that can be made from that square
	public boolean validFirstSquare(ChessSquare square){
		System.out.println("ChessModel, Valid first square");
		ChessSquare fs = chessSquares[square.getIndexX()][square.getIndexY()];
		boolean whitePlayer = true;
		if(playerColor == 1) whitePlayer = false;
		if(fs.getPiece().isWhite() != whitePlayer){
			System.out.println("ChessModel. Invalid first square. Wrong color");
			return false;
		}
		if(findAllValidMoves(fs).size() <= 0){
			System.out.println("ChessModel. Invalid first square. No possible moves");
			return false;
		}
		
		return true;
	}
	
}
