package networkobjects;

import java.io.Serializable;

import server.MySQLDriver;

public class CreateAccountObject implements Serializable
{
	private String username, password, secretQuestion, secretAnswer;
	private boolean isValid;
		
	public CreateAccountObject(String inUsername, String inPassword, String inSecretQuestion, String inSecretAnswer)
	{
		username = inUsername;
		password = inPassword;
		secretQuestion = inSecretQuestion;
		secretAnswer = inSecretAnswer;
		createUser();
	}
	
	
	
	private boolean createUser() {
		
		isValid = false;
		
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		if(driver.alreadyExists(username)){
			isValid = false;
			driver.stop();
		}
		else{
			isValid = true;
			driver.addUser(username, password, secretQuestion, secretAnswer);			
			driver.stop();
		}
		
		return isValid;
	}



	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public String getSecretQuestion() { return secretQuestion; }
	public String getSecretAnswer() { return secretAnswer; }
	public boolean isValid() { return isValid; }
	public void setValid(boolean b) { isValid = b; }
	
	
}


