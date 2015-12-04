package networkobjects;

import java.io.Serializable;
import java.util.ArrayList;

import client.ChessSquare;
import client.GameController.GameType;
import client.GamePiece;

// Client sends this object to tell server that the client is queueing(looking) for a game.

public class GameQueueObject implements Serializable
{
	private boolean isQueued, isHost;
	private int port;
	private String hostname;
	
	// Warning: Should only be used for dequeue!
	public GameQueueObject(boolean isQueued)
	{
		this.isQueued = isQueued;
	}
	
	// Create game
	public GameQueueObject(String inHostname, int inPort, boolean isHost)
	{
		hostname = inHostname;
		port = inPort;
		this.isHost = isHost;
	}
	
	// Queue
	public GameQueueObject(boolean isQueued, boolean isHost)
	{
		this.isQueued = isQueued;
		this.isHost = isHost;
	}
	
	public String getHostname() { return hostname; }
	
	public int getPort() { return port; }
	
	public boolean isHost(){ return isHost; }
	
	public boolean isQueued(){ return isQueued; }
}
