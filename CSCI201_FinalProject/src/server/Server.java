package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import networkobjects.LeaderBoardObject;
import networkobjects.User;

public class Server {
	private static Vector<ClientThread> clientThreadVector = new Vector<ClientThread>();
	private static ArrayList<User> userList = new ArrayList<User>();
	
	private int currentAvailablePort;

	Server()
	{
		try {
			ServerSocket ss = new ServerSocket(ServerConstants.defaultPortInt);
			System.out.println("Bound to port " + ServerConstants.defaultPortInt);
			currentAvailablePort = ServerConstants.defaultPortInt + 1;
			while (true) {
				Socket s = ss.accept();
				System.out.println("Connected: " + s.getInetAddress());
				ClientThread ct = new ClientThread(this,s);
				clientThreadVector.add(ct);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in Server constructor: " + ioe.getMessage());
		}
	}
	
	
	public static void loadUserInfo() {
		
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		driver.viewAllUsers();
		driver.stop();

	}

	public void checkQueue()
	{
		ClientThread hostClient = null;
		ClientThread otherClient = null;
		
		for(ClientThread ct:clientThreadVector)
		{
			if(ct.isQueued() && ct.isHost())
			{
				hostClient = ct;
				break;
			}
		}
		
		for(ClientThread ct:clientThreadVector)
		{
			if(ct.isQueued() && !ct.isHost())
			{
				otherClient = ct;
				break;
			}
		}
		
		if(hostClient != null && otherClient != null)
		{	
			hostClient.hostGame(currentAvailablePort);
			otherClient.joinGame(hostClient.getHostname(),currentAvailablePort);
			currentAvailablePort++;
		}
	}

	public static void addUser(String inUsername, int inWins, int inLosses, int inGamesPlayed,int inRating)
	{
		userList.add(new User(inUsername, inWins, inLosses,inGamesPlayed, inRating));
	}
	
	public static ArrayList<User> getUserList()
	{
		return userList;
	}
	
	public static void removeAllUsers()
	{
		userList = new ArrayList<User>();
	}
	
	public void sendUpdatedLeaderBoardToUsers()
	{
		for(ClientThread ct:clientThreadVector)
		{
			ct.sendObject(new LeaderBoardObject(userList));
		}
	}
	
	public static void main(String[] cheese) {
		loadUserInfo();
		new Server();
		
	}
}
