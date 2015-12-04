package client;

import java.awt.Image;
import java.io.Serializable;

public class GamePiece implements Serializable
{
	public enum Color
	{
		BLACK, WHITE
	}
	
	public enum Type
	{
		PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING, CHECKERSPAWN, CHECKERSKING
	}
	
	ChessPanel chessPanel;
	transient Image image;
	Color color;
	Type type;
	int columnX, rowY, positionX, positionY;
	
	GamePiece(ChessPanel cp, Color c, Type t, int x, int y)
	{
		chessPanel = cp;
		color = c;
		type = t;
		columnX = x;
		rowY = y;
		positionX = columnX * ClientConstants.chessSquareOffset;
		positionY = rowY * ClientConstants.chessSquareOffset;
		initializeImage();
	}
	
	public GamePiece(Color c, Type t, int x, int y)
	{
		chessPanel = null;
		color = c;
		type = t;
		columnX = x;
		rowY = y;
		positionX = columnX * ClientConstants.chessSquareOffset;
		positionY = rowY * ClientConstants.chessSquareOffset;
		initializeImage();
	}
	
	public GamePiece(GamePiece gp)
	{
		chessPanel = gp.chessPanel;
		color = gp.color;
		type = gp.type;
		columnX = gp.columnX;
		rowY = gp.rowY;
		positionX = columnX * ClientConstants.chessSquareOffset;
		positionY = rowY * ClientConstants.chessSquareOffset;
		initializeImage();
	}
	
	public boolean isPawn(){
		return type.equals(Type.PAWN);
	}
	public boolean isKnight(){
		return type.equals(Type.KNIGHT);
	}
	public boolean isBishop(){
		return type.equals(Type.BISHOP);
	}
	public boolean isRook(){
		return type.equals(Type.ROOK);
	}
	public boolean isKing(){
		return type.equals(Type.KING);
	}
	public boolean isQueen(){
		return type.equals(Type.QUEEN);
	}
	public boolean isCheckersPawn(){
		return type.equals(Type.PAWN);
	}
	public boolean isCheckersKing(){
		return type.equals(Type.KING);
	}
	
	public boolean isBlack(){
		return color.equals(Color.BLACK);
	}
	public boolean isWhite(){
		return color.equals(Color.WHITE);
	}
	
	public void initializeImage()
	{
		if(color.equals(Color.BLACK))
		{
			if(type.equals(Type.PAWN))
			{
				image = ClientConstants.blackPawnImage;
			}
			else if(type.equals(Type.ROOK))
			{
				image = ClientConstants.blackRookImage;
			}
			else if(type.equals(Type.KNIGHT))
			{
				image = ClientConstants.blackKnightImage;
			}
			else if(type.equals(Type.BISHOP))
			{
				image = ClientConstants.blackBishopImage;
			}
			else if(type.equals(Type.QUEEN))
			{
				image = ClientConstants.blackQueenImage;
			}
			else if(type.equals(Type.KING))
			{
				image = ClientConstants.blackKingImage;
			}
		}
		else if(color.equals(Color.WHITE))
		{
			if(type.equals(Type.PAWN))
			{
				image = ClientConstants.whitePawnImage;
			}
			else if(type.equals(Type.ROOK))
			{
				image = ClientConstants.whiteRookImage;
			}
			else if(type.equals(Type.KNIGHT))
			{
				image = ClientConstants.whiteKnightImage;
			}
			else if(type.equals(Type.BISHOP))
			{
				image = ClientConstants.whiteBishopImage;
			}
			else if(type.equals(Type.QUEEN))
			{
				image = ClientConstants.whiteQueenImage;
			}
			else if(type.equals(Type.KING))
			{
				image = ClientConstants.whiteKingImage;
			}
		}
	}
	
	public int getColumnX()
	{
		return columnX;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public int getPositionX()
	{
		return positionX;
	}
	
	public int getPositionY()
	{
		return positionY;
	}
	
	public int getRowY()
	{
		return rowY;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public void setPositionX(int x)
	{
		positionX = x;
	}
	
	public void setPositionY(int y)
	{
		positionY = y;
	}
	
	public void setXY(int inX, int inY)
	{
		if(chessPanel == null)	// if setting up game
		{
			columnX = inX;
			rowY = inY;
			positionX = inX * ClientConstants.chessSquareOffset;
			positionY = inY * ClientConstants.chessSquareOffset;
		}
		else	// if in-game
		{
			// Remove chess piece from old chess square
			chessPanel.getChessSquare(columnX, rowY).removePiece();
			// Remove chess piece from new chess square
			chessPanel.captureChessPiece(inX,inY);
			// Move chess piece to new chess square
			columnX = inX;
			rowY = inY;
			positionX = inX * ClientConstants.chessSquareOffset;
			positionY = inY * ClientConstants.chessSquareOffset;
			chessPanel.getChessSquare(columnX, rowY).setPiece(this);
		}
		
		// Check if pawn reaches opposite end of board
		if(type == Type.PAWN)
		{
			if(color == Color.BLACK && rowY == 7)
			{
				type = Type.QUEEN;
				image = ClientConstants.blackQueenImage;
			}
			else if(color == Color.WHITE && rowY == 0)
			{
				type = Type.QUEEN;
				image = ClientConstants.whiteQueenImage;
			}
		}
	}
	
}
