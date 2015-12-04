package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientClientCommunicator extends Thread
{
	Socket socket;
	ServerSocket ss;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	ClientPanel clientPanel;
	boolean isConnected;
	
	public ClientClientCommunicator(ClientPanel cp)
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
	
	public boolean connectAsHost(int inPort)
	{
		try
		{
			System.out.println("Connecting as host: " + inPort);
			ss = new ServerSocket(inPort);
			System.out.println("Server socket created. Waiting to connect");
			socket = ss.accept();
			System.out.println("Socket connected");
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			if(!isAlive())start();
			clientPanel.setupGame();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean connectToHost(String host, int port)
	{
		if(isConnected) return true;
		try
		{
			Thread.sleep(1000);
			System.out.println("Connecting to host - " + host + " Port: " + port);
			socket = new Socket(host, port);
			oos = new ObjectOutputStream(socket.getOutputStream());

			if(!isAlive())start();
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
