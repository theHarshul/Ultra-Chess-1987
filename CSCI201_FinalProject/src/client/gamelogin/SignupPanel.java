package client.gamelogin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ClientConstants;
import client.ClientPanel;
import networkobjects.CreateAccountObject;
import server.MySQLDriver;

public class SignupPanel extends JPanel
{
	JLabel signupTitleLabel, usernameLabel, passwordLabel, confirmPasswordLabel, secretQuestionLabel, secretAnswerLabel;
	JLabel usernameRequiredErrorLabel, usernameLengthErrorLabel, usernameAlphanumericErrorLabel;
	JLabel passwordRequiredErrorLabel, passwordLengthErrorLabel, passwordLengthErrorLabel2, passwordAlphanumericErrorLabel, usernameInUseErrorLabel;
	JLabel confirmPasswordRequiredErrorLabel, confirmPasswordMatchErrorLabel;
	JLabel secretQuestionRequiredErrorLabel, secretAnswerRequiredErrorLabel;
	ArrayList<JLabel> errorLabels;
	JTextField usernameTF, secretQuestionTF, secretAnswerTF;
	JPasswordField passwordPF, confirmPasswordPF;
	JButton createAccountButton, backButton;
	
	public SignupPanel()
	{
		initializeComponents();
		createGUI();
		addEvents();
	}
	
	private void initializeComponents()
	{
		// Labels
		signupTitleLabel = new JLabel(ClientConstants.signupTitleLabelString);
		signupTitleLabel.setFont(signupTitleLabel.getFont().deriveFont(ClientConstants.loginTitleFontSize));
		usernameLabel = new JLabel(ClientConstants.usernameLabelString);
		passwordLabel = new JLabel(ClientConstants.passwordLabelString);
		confirmPasswordLabel = new JLabel(ClientConstants.confirmPasswordLabelString);
		secretQuestionLabel = new JLabel(ClientConstants.secretQuestionLabelString);
		secretAnswerLabel = new JLabel(ClientConstants.secretAnswerLabelString);
		signupTitleLabel.setForeground(ClientConstants.textColor);
		usernameLabel.setForeground(ClientConstants.textColor);
		passwordLabel.setForeground(ClientConstants.textColor);
		confirmPasswordLabel.setForeground(ClientConstants.textColor);
		secretQuestionLabel.setForeground(ClientConstants.textColor);
		secretAnswerLabel.setForeground(ClientConstants.textColor);
		
		// TextFields
		usernameTF = new JTextField(ClientConstants.longTextFieldSize);
		passwordPF = new JPasswordField(ClientConstants.mediumTextFieldSize);
		confirmPasswordPF = new JPasswordField(ClientConstants.mediumTextFieldSize);
		secretQuestionTF = new JTextField(ClientConstants.longTextFieldSize);
		secretAnswerTF = new JTextField(ClientConstants.longTextFieldSize);
		
		// Button
		createAccountButton = new JButton(ClientConstants.createAccountButtonString);
		backButton = new JButton(ClientConstants.backButtonString);
		
		// Error labels
		usernameRequiredErrorLabel = new JLabel(ClientConstants.requiredFieldErrorString);
		usernameLengthErrorLabel = new JLabel(ClientConstants.usernameLengthErrorString);
		usernameAlphanumericErrorLabel = new JLabel(ClientConstants.alphanumericErrorString);
		passwordRequiredErrorLabel = new JLabel(ClientConstants.requiredFieldErrorString);
		passwordLengthErrorLabel = new JLabel(ClientConstants.passwordLengthErrorString);
		passwordLengthErrorLabel2 = new JLabel(ClientConstants.passwordLengthErrorString2);
		passwordAlphanumericErrorLabel = new JLabel(ClientConstants.alphanumericErrorString);
		confirmPasswordRequiredErrorLabel = new JLabel(ClientConstants.requiredFieldErrorString);
		confirmPasswordMatchErrorLabel = new JLabel(ClientConstants.passwordMatchErrorString);
		secretQuestionRequiredErrorLabel = new JLabel(ClientConstants.requiredFieldErrorString);
		secretAnswerRequiredErrorLabel = new JLabel(ClientConstants.requiredFieldErrorString);
		usernameInUseErrorLabel = new JLabel(ClientConstants.errorUsernameInUseString);
		
		
		usernameRequiredErrorLabel.setForeground(ClientConstants.textErrorColor);
		usernameLengthErrorLabel.setForeground(ClientConstants.textErrorColor);
		usernameAlphanumericErrorLabel.setForeground(ClientConstants.textErrorColor);
		passwordRequiredErrorLabel.setForeground(ClientConstants.textErrorColor);
		passwordLengthErrorLabel.setForeground(ClientConstants.textErrorColor);
		passwordLengthErrorLabel2.setForeground(ClientConstants.textErrorColor);
		passwordAlphanumericErrorLabel.setForeground(ClientConstants.textErrorColor);
		confirmPasswordRequiredErrorLabel.setForeground(ClientConstants.textErrorColor);
		confirmPasswordMatchErrorLabel.setForeground(ClientConstants.textErrorColor);
		secretQuestionRequiredErrorLabel.setForeground(ClientConstants.textErrorColor);
		secretAnswerRequiredErrorLabel.setForeground(ClientConstants.textErrorColor);
		usernameInUseErrorLabel.setForeground(ClientConstants.textErrorColor);
		
		errorLabels = new ArrayList<JLabel>();
		errorLabels.add(usernameRequiredErrorLabel);
		errorLabels.add(usernameLengthErrorLabel);
		errorLabels.add(usernameAlphanumericErrorLabel);
		errorLabels.add(passwordRequiredErrorLabel);
		errorLabels.add(passwordLengthErrorLabel);
		errorLabels.add(passwordLengthErrorLabel2);
		errorLabels.add(passwordAlphanumericErrorLabel);
		errorLabels.add(confirmPasswordRequiredErrorLabel);
		errorLabels.add(confirmPasswordMatchErrorLabel);
		errorLabels.add(secretQuestionRequiredErrorLabel);
		errorLabels.add(secretAnswerRequiredErrorLabel);
		errorLabels.add(usernameInUseErrorLabel);
		
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
		gbc.insets = new Insets(0,9,10,9);			// Bottom padding
		gbc.anchor = GridBagConstraints.LINE_START;	// Left-aligned
		add(signupTitleLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2,9,2,9);			// Bottom padding
		add(usernameLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(usernameTF,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		add(passwordLabel,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		add(confirmPasswordLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 7;
		add(passwordPF,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		add(confirmPasswordPF,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 12;
		gbc.gridwidth = 2;
		add(secretQuestionLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 13;
		add(secretQuestionTF,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 15;
		add(secretAnswerLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 16;
		add(secretAnswerTF,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 18;
		gbc.insets = new Insets(20,9,0,9);
		add(backButton,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 18;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(createAccountButton,gbc);
	}
	
	private void addEvents()
	{
		backButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				ClientPanel cp = (ClientPanel)SignupPanel.this.getParent();
				cp.show(ClientConstants.gameLoginPanelName);
			}
		});
		
		createAccountButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				if(!hasErrors())
				{
					createAccount();
				}
			}
		});
	}
	
	private boolean hasErrors()
	{
		boolean hasErrors = false;
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2,9,2,9);
		gbc.anchor = GridBagConstraints.LINE_START;
		
		// Clear errors
		for(JLabel jl:errorLabels)
		{
			remove(jl);
		}
		
		// Test for errors
		if(usernameTF.getText().isEmpty())
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 3;
			add(usernameRequiredErrorLabel,gbc);
		}
		if(usernameTF.getText().length() < ClientConstants.usernameMinLength
				|| usernameTF.getText().length() > ClientConstants.usernameMaxLength)
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 4;
			add(usernameLengthErrorLabel,gbc);
		}
		if(!isAlphanumeric(usernameTF.getText()))
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 5;
			add(usernameAlphanumericErrorLabel,gbc);
		}
		gbc.gridwidth = 1;	// width = 1 for passwords
		if(passwordPF.getText().isEmpty())
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 8;
			add(passwordRequiredErrorLabel,gbc);
		}
		if(passwordPF.getText().length() < ClientConstants.passwordMinLength
				|| passwordPF.getText().length() > ClientConstants.passwordMaxLength)
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 9;
			add(passwordLengthErrorLabel,gbc);
			gbc.gridx = 0;
			gbc.gridy = 10;
			add(passwordLengthErrorLabel2,gbc);
		}
		if(!isAlphanumeric(passwordPF.getText()))
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 11;
			add(passwordAlphanumericErrorLabel,gbc);
		}
		if(confirmPasswordPF.getText().isEmpty() && !hasErrors)
		{
			hasErrors = true;
			gbc.gridx = 1;
			gbc.gridy = 8;
			add(confirmPasswordRequiredErrorLabel,gbc);
		}
		if(!confirmPasswordPF.getText().equals(passwordPF.getText()) && !hasErrors)
		{
			hasErrors = true;
			gbc.gridx = 1;
			gbc.gridy = 9;
			add(confirmPasswordMatchErrorLabel,gbc);
		}
		if(secretQuestionTF.getText().isEmpty())
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 14;
			add(secretQuestionRequiredErrorLabel,gbc);
		}
		if(secretAnswerTF.getText().isEmpty())
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 17;
			add(secretAnswerRequiredErrorLabel,gbc);
		}
		
		if(hasErrors)
		{
			revalidate();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isAlphanumeric(String str)
	{
        for (int i=0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
            {
                return false;
            }
        }
        return true;
    }
	
	private void userAlreadyExists(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2,9,2,9);
		gbc.anchor = GridBagConstraints.LINE_START;
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(usernameInUseErrorLabel,gbc);
		//usernameInUseErrorLabel.setVisible(true);
		revalidate();
		
		
		
		
	}
	
	private synchronized void createAccount()
	{
		String username = usernameTF.getText();
		String password = String.valueOf(passwordPF.getPassword());
		String secretQuestion = secretQuestionTF.getText();
		String secretAnswer = secretAnswerTF.getText();
		
		CreateAccountObject cao = new CreateAccountObject(username,password,secretQuestion,secretAnswer);
		if(!cao.isValid()){
			userAlreadyExists();
		}
		
		else
		{
			ClientPanel cp = (ClientPanel)SignupPanel.this.getParent();
			cp.createAccount(cao);
			cp.show(ClientConstants.gameLoginPanelName);
		}
		
		
	}
}
