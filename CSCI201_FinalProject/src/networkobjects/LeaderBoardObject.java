package networkobjects;

import java.io.Serializable;
import java.util.ArrayList;

public class LeaderBoardObject implements Serializable
{
	ArrayList<User> userList;
	
	public LeaderBoardObject(ArrayList<User> inUsers)
	{
		userList = inUsers;
	}
	
	public ArrayList<User> getUsers(){return userList;}
}
