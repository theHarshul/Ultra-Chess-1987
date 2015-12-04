package client.gamelogin;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ClientConstants;
import client.ClientPanel;
import client.SoundPlayer;
import networkobjects.User;
import server.MySQLDriver;

public class GameLoginPanel extends JPanel
{	
	JLabel loginTitleLabel, usernameLabel, passwordLabel, signupLabel, forgotPasswordLabel, hostnameLabel, portLabel;
	JLabel usernameRequiredErrorLabel, passwordRequiredErrorLabel, connectionErrorLabel;
	ArrayList<JLabel> errorLabels;
	JTextField hostnameTF, portTF;
	public JTextField usernameTF;
	JPasswordField passwordPF;
	JButton loginButton;
	public User current;
	
	int wins,losses,gamesPlayed,rating; //userStats upon login
	
	public GameLoginPanel()
	{
		initializeComponents();
		createGUI();
		addEvents();
		
		// TEMP
		addGuestLoginButton();
	}
	
	private void initializeComponents()
	{	
		// Labels
		loginTitleLabel = new JLabel(ClientConstants.loginTitleLabelString);
		loginTitleLabel.setFont(loginTitleLabel.getFont().deriveFont(ClientConstants.loginTitleFontSize));
		usernameLabel = new JLabel(ClientConstants.usernameLabelString);
		passwordLabel = new JLabel(ClientConstants.passwordLabelString);
		signupLabel = new JLabel(ClientConstants.signupLabelString);
		forgotPasswordLabel = new JLabel(ClientConstants.forgotPasswordLabelString);
		hostnameLabel = new JLabel(ClientConstants.hostnameLabelString);
		portLabel = new JLabel(ClientConstants.portLabelString);
		loginTitleLabel.setForeground(ClientConstants.textColor);
		usernameLabel.setForeground(ClientConstants.textColor);
		passwordLabel.setForeground(ClientConstants.textColor);
		signupLabel.setForeground(ClientConstants.textLinkColor);
		forgotPasswordLabel.setForeground(ClientConstants.textLinkColor);
		hostnameLabel.setForeground(ClientConstants.textColor);
		portLabel.setForeground(ClientConstants.textColor);
		signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// TextFields
		usernameTF = new JTextField(ClientConstants.longTextFieldSize);
		passwordPF = new JPasswordField(ClientConstants.longTextFieldSize);
		hostnameTF = new JTextField(ClientConstants.defaultHostnameString,ClientConstants.shortTextFieldSize);
		portTF =  new JTextField(ClientConstants.defaultPortString,ClientConstants.shortTextFieldSize);
		
		// Button
		loginButton = new JButton(ClientConstants.loginButtonString);
		loginButton.setFont(loginButton.getFont().deriveFont(ClientConstants.loginTitleFontSize));
		
		// Error labels
		usernameRequiredErrorLabel = new JLabel(ClientConstants.requiredFieldErrorString);
		passwordRequiredErrorLabel = new JLabel(ClientConstants.requiredFieldErrorString);
		connectionErrorLabel = new JLabel(ClientConstants.connectionErrorString);
		usernameRequiredErrorLabel.setForeground(ClientConstants.textErrorColor);
		passwordRequiredErrorLabel.setForeground(ClientConstants.textErrorColor);
		connectionErrorLabel.setForeground(ClientConstants.textErrorColor);
		errorLabels = new ArrayList<JLabel>();
		errorLabels.add(usernameRequiredErrorLabel);
		errorLabels.add(passwordRequiredErrorLabel);
		errorLabels.add(connectionErrorLabel);
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
		add(loginTitleLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(2,0,2,0);			// Reset padding	
		add(usernameLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		add(usernameTF,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		add(passwordLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		add(passwordPF,gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20,0,20,0);			// Top and bottom padding
		gbc.anchor = GridBagConstraints.LINE_END; 	// Right-aligned
		add(loginButton,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.insets = new Insets(2,0,2,0);			// Top and bottom padding
		gbc.anchor = GridBagConstraints.LINE_START;	// Left-aligned
		add(signupLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 9;
		add(forgotPasswordLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.insets = new Insets(40,0,2,0);			// Top padding
		add(hostnameLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 11;
		gbc.insets = new Insets(2,0,2,0);			// Reset padding
		add(hostnameTF,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 12;
		add(portLabel,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 13;
		add(portTF,gbc);
	}
	
	private void addEvents()
	{
		// Logs in if you click button
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				SoundPlayer.playYes();
				if(!hasErrors())login();
			}
		});
		
		// Logs in if you click 'ENTER' on text fields
		usernameTF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { if(!hasErrors()) login(); }});
		passwordPF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { if(!hasErrors()) login(); }});
		hostnameTF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { if(!hasErrors()) login(); }});
		portTF.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent ae) { if(!hasErrors()) login(); }});
		
		// SignupPanel
		signupLabel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent me)
			{
				ClientPanel cp = (ClientPanel)GameLoginPanel.this.getParent();
				cp.show(ClientConstants.signupPanelName);
			}
		});
		
		// ForgotPasswordPanel
		forgotPasswordLabel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent me)
			{
				ClientPanel cp = (ClientPanel)GameLoginPanel.this.getParent();
				cp.show(ClientConstants.forgotPasswordPanelName);
			}
		});
	}
	
	public String getHostname()
	{
		return hostnameTF.getText();
	}
	
	public int getPort()
	{
		return Integer.parseInt(portTF.getText());
	}
	
	private boolean hasErrors()
	{
		boolean hasErrors = false;
		
		GridBagConstraints gbc = new GridBagConstraints();
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
		if(passwordPF.getPassword().length == 0)
		{
			hasErrors = true;
			gbc.gridx = 0;
			gbc.gridy = 6;
			add(passwordRequiredErrorLabel,gbc);
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
	
	private void login()
	{
		ClientPanel cp = (ClientPanel)GameLoginPanel.this.getParent();
		
		cp.login(hostnameTF.getText(), Integer.parseInt(portTF.getText()), usernameTF.getText(), String.valueOf(passwordPF.getPassword()));
		
		getUserStats();
		//if(cp.loginToServer(hostnameTF.getText(), Integer.parseInt(portTF.getText()), usernameTF.getText(), passwordTF.getText()))
			//cp.show(ClientConstants.gameLobbyPanelName);
	}
	
	private void getUserStats() {
		MySQLDriver driver = new MySQLDriver();
		driver.connect();
		String username = usernameTF.getText();
		current = driver.getCurrStats(username);
		driver.stop();
		
	}

	public void showConnectionError()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Clear errors
		for(JLabel jl:errorLabels)
		{
			remove(jl);
		}
		
		// Add error label
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(connectionErrorLabel,gbc);
		revalidate();
	}
	
	//TEMP
	private void addGuestLoginButton()
	{
		// initialize variables
		JButton offlineLoginButton = new JButton("Guest Login");
		GridBagConstraints gbc = new GridBagConstraints();
		
		// add to gui
		gbc.gridx = 2;
		gbc.gridy = 14;
		gbc.anchor = GridBagConstraints.LINE_END;
		add(offlineLoginButton,gbc);
		
		// add events
		offlineLoginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				ClientPanel cp = (ClientPanel)GameLoginPanel.this.getParent();
				
				cp.login(hostnameTF.getText(), Integer.parseInt(portTF.getText()), "guest", "guest");
				
				getUserStats();
			}
		});
	}
}
