package client.gamelogin;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientConstants;
import client.ClientPanel;
import server.MySQLDriver;

public class ForgotPasswordPanel extends JPanel
{
	JLabel forgotPasswordTitleLabel, usernameLabel, secretQuestionLabel, secretAnswerLabel, passwordLabel, errorLabel;
	JTextField usernameTF, secretQuestionTF, secretAnswerTF,passwordTF;
	JButton backButton, retrievePasswordButton,locateButton;
	String secretQuestion,secretAnswer,username,password;
	
	public ForgotPasswordPanel()
	{
		initializeComponents();
		createGUI();
		addEvents();
	}
	
	private void initializeComponents()
	{
		// Labels
		forgotPasswordTitleLabel = new JLabel(ClientConstants.forgotPasswordTitleLabelString);
		forgotPasswordTitleLabel.setFont(forgotPasswordTitleLabel.getFont().deriveFont(ClientConstants.loginTitleFontSize));
		usernameLabel = new JLabel(ClientConstants.usernameLabelString);
		secretQuestionLabel = new JLabel(ClientConstants.secretQuestionLabelString);
		secretAnswerLabel = new JLabel(ClientConstants.secretAnswerLabelString);
		forgotPasswordTitleLabel.setForeground(ClientConstants.textColor);
		usernameLabel.setForeground(ClientConstants.textColor);
		secretQuestionLabel.setForeground(ClientConstants.textColor);
		secretAnswerLabel.setForeground(ClientConstants.textColor);
		passwordLabel = new JLabel(ClientConstants.passwordLabelString);
		passwordLabel.setForeground(Color.red);
		errorLabel = new JLabel();
		errorLabel.setForeground(Color.red);

	
		
		// TextFields
		usernameTF = new JTextField(ClientConstants.longTextFieldSize);
		secretQuestionTF = new JTextField(ClientConstants.longTextFieldSize);
		secretAnswerTF = new JTextField(ClientConstants.longTextFieldSize);
		passwordTF = new JTextField(ClientConstants.longTextFieldSize);
		
		// Buttons
		backButton = new JButton(ClientConstants.backButtonString);
		retrievePasswordButton = new JButton(ClientConstants.retrievePasswordButtonString);
		locateButton = new JButton(ClientConstants.retrieveSecretQuestion);
		
	}
	
	private void createGUI()
	{
		// Set transparent
		setOpaque(false);
		
		// GridBagLayout
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,0,10,0);			// Bottom padding
		gbc.anchor = GridBagConstraints.LINE_START;	// Left-aligned
		gbc.gridwidth = 2;
		add(forgotPasswordTitleLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(2,0,2,0);			// Reset padding	
		add(usernameLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(usernameTF,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(secretQuestionLabel,gbc);
		secretQuestionLabel.setVisible(false);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(secretQuestionTF,gbc);
		secretQuestionTF.setVisible(false);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(secretAnswerLabel,gbc);
		secretAnswerLabel.setVisible(false);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		add(secretAnswerTF,gbc);
		secretAnswerTF.setVisible(false);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20,0,0,0);
		add(backButton,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.insets = new Insets(20,0,0,0);
		add(locateButton,gbc);
		
		
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(retrievePasswordButton,gbc);
		retrievePasswordButton.setVisible(false);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(2,0,2,10);
		add(passwordLabel,gbc);
		passwordLabel.setVisible(false);
		
		
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(passwordTF,gbc);
		passwordTF.setEditable(false);
		passwordTF.setVisible(false);
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(errorLabel,gbc);
	}
	
	private void addEvents()
	{
		backButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				ClientPanel cp = (ClientPanel)ForgotPasswordPanel.this.getParent();
				cp.show(ClientConstants.gameLoginPanelName);
			}
		});
		
		retrievePasswordButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(correctAnswer()){
					
					errorLabel.setVisible(false);
					
					getPassword();
					passwordLabel.setVisible(true);
					passwordTF.setText(password);
					passwordTF.setVisible(true);
				}
				
				else{
					errorLabel.setText(ClientConstants.errorWrongAnswerString);
					errorLabel.setVisible(true);
				}
			}
			
		});
		
		locateButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getSecretQuestion();
				if(secretQuestion != null){
				
				errorLabel.setVisible(false);
				
				secretQuestionLabel.setVisible(true);
				secretQuestionTF.setText(secretQuestion);
				secretQuestionTF.setVisible(true);
				secretAnswerLabel.setVisible(true);
				secretAnswerTF.setVisible(true);
				locateButton.setVisible(false);
				retrievePasswordButton.setVisible(true);
				}
				else{
					errorLabel.setText(ClientConstants.errorAccountDNEString);
					errorLabel.setVisible(true);
				}
				
			}
			
		});
	}

	private boolean correctAnswer() {
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		secretAnswer = secretAnswerTF.getText();
		boolean isCorrect = driver.verifyUserSecret(username, secretAnswer);
		driver.stop();
		return isCorrect;		
	}

	private void getSecretQuestion() {
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		username = usernameTF.getText();
		secretQuestion = driver.getSecretQuestion(username);
		//System.out.println(secretQuestion);
		
	}

	private void getPassword() {
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		//System.out.println(driver.lostPassword("Sheldon", "blank", "USC"));
		password = driver.lostPassword(usernameTF.getText(),secretQuestionTF.getText(),secretAnswerTF.getText());
		driver.stop();
		
	}
	
	
	
}
