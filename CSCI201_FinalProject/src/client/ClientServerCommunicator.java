package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServerCommunicator extends Thread
{
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	ClientPanel clientPanel;
	boolean isConnected;
	
	ClientServerCommunicator(ClientPanel cp)
	{
		clientPanel = cp;
		isConnected = false;
	}
	
	public void run()
	{
		try
		{
			ois = new ObjectInputStream(socket.getInputStream());
			while(true)
			{
				Object obj = ois.readObject();
				clientPanel.receiveObject(obj);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean connect(String host, int port)
	{
		if(isConnected) return true;
		try
		{
			socket = new Socket(host, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			start();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		isConnected = true;
		return true;
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
