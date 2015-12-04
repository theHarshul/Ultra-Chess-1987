package networkobjects;

import java.io.Serializable;

public class ChessMoveObject implements Serializable
{
	private int fromX, fromY, toX, toY;
	
	public ChessMoveObject(int fx, int fy, int tx, int ty)
	{
		fromX = fx;
		fromY = fy;
		toX = tx;
		toY = ty;
	}
	
	public int getFromX() { return fromX; }
	public int getFromY() { return fromY; }
	public int getToX() { return toX; }
	public int getToY() { return toY; }
	
}
