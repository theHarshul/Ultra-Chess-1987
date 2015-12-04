package networkobjects;

import java.io.Serializable;
import java.util.ArrayList;

import client.GameController.GameType;
import client.GamePiece;

public class GameSetupObject implements Serializable
{
	ArrayList<GamePiece> chessPieces;
	GameType gameType;
	
	public GameSetupObject(ArrayList<GamePiece> cp, GameType gp)
	{
		chessPieces = cp;
		gameType = gp;
	}
	
	public ArrayList<GamePiece> getChessPieces()
	{
		return chessPieces;
	}
	
	public GameType getGameType()
	{
		return gameType;
	}
}
