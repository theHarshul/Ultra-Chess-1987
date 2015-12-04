package client;

import java.io.Serializable;

public class ChessSquare implements Serializable
{
	private int indexX, indexY;
	private int positionX, positionY;
	GamePiece myPiece;
	public boolean highlighted;
	//TODO: Override this class's .equals() method
	
	//TODO: this should also take GamePiece
	public ChessSquare(int x, int y)
	{
		indexX = x;
		indexY = y;
		
		positionX = x * ClientConstants.chessSquareOffset;
		positionY = y * ClientConstants.chessSquareOffset;
		highlighted = false;
		myPiece = null;
	}
	
	public int getIndexX() { return indexX; }
	public int getIndexY() { return indexY; }
	public int getPositionX() { return positionX; }
	public int getPositionY() { return positionY; }
	public GamePiece getPiece(){
		return myPiece;
	}
	public void setPiece(GamePiece gp){
		myPiece = gp;
	}
	
	public boolean hasPiece(){
		return (myPiece != null);
	}
	public void removePiece(){
		if(myPiece == null) System.out.println("Error in ChessSquare: Attempting to remove piece from an empty tile");
		myPiece = null;		
	}
	
	public boolean equals(ChessSquare sq){
		return(sq.indexX == indexX && sq.indexY == indexY);
	}
}
