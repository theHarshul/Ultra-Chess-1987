package networkobjects;

import java.io.Serializable;

public class User implements Serializable
{
	private String username;
	private int wins, losses,gamesPlayed, rating;
	
	public User(String inUsername, int inWins, int inLosses,int inGamesPlayed, int inRating)
	{
		username = inUsername;
		wins = inWins;
		losses = inLosses;
		gamesPlayed = inGamesPlayed;
		rating = inRating;
		
	}
	
	public String getUsername(){return username;}
	
	public int getWins(){return wins;}
	
	public int getLosses(){return losses;}
	
	public int getRating(){return rating;}
	
	public int getGamesPlayed(){return gamesPlayed;}
}
