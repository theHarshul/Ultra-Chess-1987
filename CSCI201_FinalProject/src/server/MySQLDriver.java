package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.Driver;

import networkobjects.PastGame;
import networkobjects.User;

public class MySQLDriver
{
	private Connection con;

	// All user Info
	
	private final static String selectAll = "SELECT u.userID, u.username, u.pass, secret.secretQuestion,"
			+ " secret.secretAnswer, basic.rating,basic.wins,basic.losses,basic.gamesPlayed FROM User u, SecretInfo secret, BasicStats basic WHERE u.userID=secret.userID AND u.userID = basic.userID ORDER BY rating DESC";

	private final static String getLastUserID = "SELECT userID FROM sql395256.User ORDER BY userID DESC LIMIT 1";
	
	private final static String addUser = "INSERT INTO User (username,pass) VALUES(?,?)";
	private final static String addUserSecretInfo = "INSERT INTO SecretInfo (userID,secretQuestion,secretAnswer) VALUES(?,?,?)";
	private final static String addUserBasicStats = "INSERT INTO BasicStats (userID,rating,wins,losses,gamesPlayed) VALUES(?,?,?,?,?)";
	
	private final static String verifyUser = "SELECT userID FROM sql395256.User u WHERE u.username = ? AND u.pass = ?";
	private final static String userExists = "SELECT userID FROM sql395256.User u WHERE u.username = ?";
	
	private final static String userIDFromUsername = "SELECT userID FROM sql395256.User u WHERE u.username = ?";
	private final static String getUserSecretQuestion = "SELECT secretQuestion FROM sql395256.SecretInfo s WHERE s.userID = ?";
	private final static String verifyUserSecret = "SELECT userID FROM sql395256.SecretInfo s WHERE s.userID = ? AND s.secretAnswer = ?";
	private final static String userPassword = "SELECT pass FROM sql395256.User u WHERE u.username = ?";
	private final static String updateUserRecord = "UPDATE sql395256.BasicStats SET rating = ?, wins = ?, losses = ?, gamesPlayed = ? WHERE userID = ?;";
	private final static String getCurrentRecord = "SELECT rating, wins, losses,gamesPlayed FROM sql395256.BasicStats WHERE userID = ?;";
	private final static String addChessNotationGame = "INSERT INTO GameStats(userId,opponentName,iWon,chessNotation) VALUES(?,?,?,?);";
	private final static String viewMyPast = "SELECT opponentName, iWon, chessNotation FROM sql395256.GameStats WHERE userID = ?;";
	
	public MySQLDriver()
	{
		try {
			new Driver();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://sql3.freemysqlhosting.net/sql395256", "sql395256",
					"aK8%aV1!");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void viewAllUsers() {
		Server.removeAllUsers();
		try {
			Statement st = con.createStatement();

			PreparedStatement ps = con.prepareStatement(selectAll);

			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int userID = rs.getInt("userID");
				String username = rs.getString("username");
				String password = rs.getString("pass");
				String secretQuestion = rs.getString("secretQuestion");
				String secretAnswer = rs.getString("secretAnswer");
				int rating = rs.getInt("rating");
				System.out.println(rating);
				int wins = rs.getInt("wins");
				int losses = rs.getInt("losses");
				int gamesPlayed = rs.getInt("gamesPlayed");
				
				Server.addUser(username, wins, losses,gamesPlayed, rating);
				
				System.out.println("userID: " + userID + "\n" + "username: " +username + "\npassword:  " + password + "\n" + "Secret Question: "
						+ secretQuestion + "\nSecretAnswer: " + secretAnswer +"\nRating: "+ rating + "\nwins: " + wins + "\nlosses: "  + losses + "\nGames Played: " + gamesPlayed +"\n");
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("ViewAll SQL Exception: " + e.getMessage());
		}

	}

	public void addUser(String username, String password, String secretQuestion, String secretAnswer) {
		try {
			PreparedStatement insert = con.prepareStatement(addUser);

			insert.setString(1, username);
			insert.setString(2, password);
			insert.executeUpdate();

			// get ID of most recently entered user
			PreparedStatement lastUserID = con.prepareStatement(getLastUserID);

			ResultSet rs = lastUserID.executeQuery();

			int userID = -1;

			if (rs.next()) {
				userID = rs.getInt("userID");
			
			}
			
			lastUserID.close();

			// for matching userSecretInfo table, needs to update first
			// paramater of
			// VALUES to know where to add user
			PreparedStatement secretInfo = con.prepareStatement(addUserSecretInfo);

			secretInfo.setInt(1, userID);
			secretInfo.setString(2, secretQuestion);
			secretInfo.setString(3, secretAnswer);
			secretInfo.executeUpdate();
			
			PreparedStatement basicStats = con.prepareStatement(addUserBasicStats);
			
			basicStats.setInt(1, userID);
			basicStats.setInt(2, 1200); //base rating
			basicStats.setInt(3, 0);
			basicStats.setInt(4, 0);
			basicStats.setInt(5, 0);
			basicStats.executeUpdate();
					
			basicStats.close();		
			secretInfo.close();
			rs.close();
			insert.close();

		} catch (SQLException e) {
			System.out.println("addUser SQL Exception: " + e.getMessage());

		}
	}
	
	public boolean alreadyExists(String username){
		try{
			PreparedStatement ps = con.prepareStatement(userExists);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			
			if(rs.next()){
				return true;
			}
			
			rs.close();
			
		}catch(SQLException e){
			System.out.println("SQLException in alreadyExists(): " + e.getMessage());
		}
		
		
		return false;
	}

	public boolean verifyUser(String username, String password) {
		
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(verifyUser);

			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			

			// if such a user exists
			if (rs.next()) {
				return true;
			}
			
			else return false;
			

		} catch (SQLException e) {
			System.out.println("SQLException in verifyUser() " + e.getMessage());
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public boolean verifyUserSecret(String username,String secretAnswer){
		
		int userID = -1;
		
		try{
			PreparedStatement ps = con.prepareStatement(userIDFromUsername);
			ps.setString(1, username);
			
			ResultSet resUserID = ps.executeQuery();
			if(resUserID.next()){
				userID = resUserID.getInt("userID");
				PreparedStatement prepare = con.prepareStatement(verifyUserSecret);
				prepare.setInt(1, userID);
				prepare.setString(2, secretAnswer);
				
				ResultSet rs = prepare.executeQuery();
				if(rs.next())
					return true;
			}
		}catch(SQLException e){
			System.out.println("SQLException in verifyUserSecret(): " + e.getMessage());
		}
		
		return false;
		
	}

	public String lostPassword(String username, String secretQuestion, String secretAnswer) {
		
		String password = "User does not exist";
		int userID = -1;
		
		try{
			PreparedStatement ps = con.prepareStatement(userIDFromUsername);
			ps.setString(1, username);
			
			ResultSet resUserID = ps.executeQuery();
			if(resUserID.next()){
				userID = resUserID.getInt("userID");
				PreparedStatement prepare = con.prepareStatement(verifyUserSecret);
				prepare.setInt(1, userID);
				prepare.setString(2, secretAnswer);
				
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					PreparedStatement obtainPassword = con.prepareStatement(userPassword);
					obtainPassword.setString(1,username);
					
					ResultSet passwordResult = obtainPassword.executeQuery();
					if(passwordResult.next()){
						password = passwordResult.getString("pass");
					}
					
				}
			}
			
			
			
		}catch(SQLException e){
			System.out.println("SQLException in lostPassword(): " + e.getMessage());
		}
		
		return password;
		
	}

	public String getSecretQuestion(String username) {
		String secretQuestion = null;
		int userID = -1;
		
		try{
			PreparedStatement ps = con.prepareStatement(userIDFromUsername);
			ps.setString(1, username);
			
			ResultSet resUserID = ps.executeQuery();
			if(resUserID.next()){
				userID = resUserID.getInt("userID");
				PreparedStatement prepare = con.prepareStatement(getUserSecretQuestion);
				prepare.setInt(1, userID);
				
				ResultSet rs = prepare.executeQuery();
				if(rs.next()){
					secretQuestion = rs.getString("secretQuestion");
				}
				
			}
		}catch(SQLException e){
			System.out.println("SQLException in getSecretQuestion(): " + e.getMessage());
		}
		return secretQuestion;
	}
	
	//pass new values after game is complete
	public void updateUserStats(String username,int rating , int wins, int losses, int gamesPlayed){
		
		int userID = -1;
		
		try{
			PreparedStatement ps = con.prepareStatement(userIDFromUsername);
			ps.setString(1, username);
			
			ResultSet resUserID = ps.executeQuery();
			if(resUserID.next()){
				userID = resUserID.getInt("userID");
				PreparedStatement update = con.prepareStatement(updateUserRecord);
				update.setInt(1, rating);
				update.setInt(2, wins);
				update.setInt(3, losses);
				update.setInt(4, gamesPlayed);
				update.setInt(5, userID);
				
				update.executeUpdate();
				
				ps.close();
				update.close();
				resUserID.close();
				
			}
		}catch(SQLException e){
			System.out.println("SQLException in updateUserStats(): " + e.getMessage());
		}
		
		
		
	}
	
	public User getCurrStats(String username) {
		
		
		
		int userID = -1;
		User u;
		int wins, losses, rating,gamesPlayed;
		
		try{
			PreparedStatement ps = con.prepareStatement(userIDFromUsername);
			ps.setString(1, username);
			ResultSet resUserID = ps.executeQuery();
			if(resUserID.next()){
				userID = resUserID.getInt("userID");
				
				PreparedStatement allStats = con.prepareStatement(getCurrentRecord);
				
				allStats.setInt(1, userID);
				ResultSet rs = allStats.executeQuery();
				
				if(rs.next()){
					rating = rs.getInt("rating");
					wins = rs.getInt("wins");
					losses = rs.getInt("losses");
					gamesPlayed = rs.getInt("gamesPlayed");
					u = new User(username,wins,losses,gamesPlayed,rating);
					
					rs.close();
					ps.close();
					allStats.close();
					resUserID.close();
					return u;
				}
			}
		}catch(SQLException e){
			System.out.println("SQLException in getCurrStats()");
		}
		return null;
	}
	
	public void addUserGame(String username,String opponent,boolean iWon, String chessNotation){
		
		if(username.equals("guest") || username.equals("Guest")) return;
		
		int userID = -1;
		
		try{
			PreparedStatement ps = con.prepareStatement(userIDFromUsername);
			ps.setString(1, username);
			
			ResultSet resUserID = ps.executeQuery();
			
			if(resUserID.next()){
				userID = resUserID.getInt("userID");
				
				PreparedStatement prepare = con.prepareStatement((addChessNotationGame));
				prepare.setInt(1, userID);
				prepare.setString(2, opponent);
				prepare.setBoolean(3, iWon);
				prepare.setString(4, chessNotation);
				
				prepare.executeUpdate();
				
				prepare.close();
				ps.close();
				resUserID.close();
				
			}
			
		}catch(SQLException e){
			System.out.println("SQLException in addUserGame(): " + e.getMessage());
		}
		
	}
	
	public PastGame[] viewStats(String username){
		
		int userID = -1;
		ArrayList<PastGame> pastGames = new ArrayList<>();
		
		try{
			PreparedStatement ps = con.prepareStatement(userIDFromUsername);
			ps.setString(1, username);
			
			ResultSet resUserID = ps.executeQuery();
			
			if(resUserID.next()){
				userID = resUserID.getInt("userID");
				
				PreparedStatement past = con.prepareStatement(viewMyPast);
				past.setInt(1, userID);
				
				ResultSet rs = past.executeQuery();
				
				while(rs.next()){
					String win;
					int iWon = rs.getInt("iWon");
					if(iWon == 1) win = "W";
					else win = "L";					
					String opponentName = rs.getString("opponentName");
					String chessNotation = rs.getString("chessNotation");	
					
					pastGames.add(new PastGame(win,opponentName,chessNotation));
					
				}
				
				ps.close();
				resUserID.close();
				past.close();
				rs.close();
				
				PastGame [] pastGamesArray = new PastGame[pastGames.size()];
				pastGamesArray = pastGames.toArray(pastGamesArray);
				
				for(int i = 0; i < pastGamesArray.length; ++i){
					System.out.println(pastGamesArray[i].getChessNotation());
				}
				
				return pastGamesArray;
				
				
			}
		}catch(SQLException e){
			System.out.println("SQLException in + ChessNotations():" + e);
		}
		
		return null;
		
		
		
		
	}
	
	
	public static void main(String [] args){
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		//driver.updateUserStats("Sheldon", 2, 3, 4, 5);
		driver.viewStats("a");
		driver.stop();
		
	}

	
	

}
