package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import networkobjects.CreateAccountObject;
import networkobjects.GameQueueObject;
import networkobjects.LeaderBoardObject;
import networkobjects.LoginObject;


public class ClientThread extends Thread
{
	private Server server;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private boolean isQueued = false;	// is queued (waiting) for game
	private boolean isHost = false;
	
	ClientThread(Server inServer, Socket inSocket)
	{
		server = inServer;
		socket = inSocket;
		
		boolean socketReady = initializeVariables();
		if (socketReady)
		{
			sendObject(new LeaderBoardObject(Server.getUserList()));
			start();
		}
	}
	
	private boolean initializeVariables()
	{
		try
		{
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				Object obj = ois.readObject();
				if(obj instanceof LoginObject)
				{
					LoginObject loginObject = (LoginObject)obj;
					loginObject.verify();
					oos.writeObject(loginObject);
				}
				else if(obj instanceof CreateAccountObject)
				{
					CreateAccountObject cao = (CreateAccountObject)obj;
					Server.loadUserInfo();
					server.sendUpdatedLeaderBoardToUsers();
				}
				else if(obj instanceof GameQueueObject)
				{
					GameQueueObject gqo = (GameQueueObject)obj;
					isQueued = gqo.isQueued();
					isHost = gqo.isHost();
					server.checkQueue();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getHostname()
	{
		return socket.getInetAddress().getHostName();
	}
	
	public void hostGame(int inPort)
	{
		sendObject(new GameQueueObject("localhost",inPort,true));
		isQueued = false;
	}
	
	public boolean isHost()
	{
		return isHost;
	}
	
	public boolean isQueued()
	{
		return isQueued;
	}
	
	public void joinGame(String inHost, int inPort)
	{
		sendObject(new GameQueueObject(inHost,inPort,false));
		isQueued = false;
	}
	
	public void sendObject(Object obj)
	{
		try
		{
			oos.writeObject(obj);
			oos.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
