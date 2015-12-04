package networkobjects;

import java.io.Serializable;

import server.MySQLDriver;

public class LoginObject implements Serializable
{
	private String username, password;
	private boolean isValid;
	
	public LoginObject(String inUsername, String inPassword)
	{
		username = inUsername;
		password = inPassword;
	}
	
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public boolean isValid() { return isValid; }
	public void setValid(boolean b) { isValid = b; }
	
	//verifies user info to database and updates isValid
	public  void verify(){
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		isValid = driver.verifyUser(username, password);
		//System.out.println(driver.verifyUser("Sheldon", "password1"));
		//System.out.println(driver.verifyUser("sd", "password1"));
		driver.stop();
	}
}
