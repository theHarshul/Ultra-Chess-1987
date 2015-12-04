package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

public class ClientConstants
{
	// Images
	public static final Image homeBgImage = new ImageIcon("resources/images/layout/bg_home.jpg").getImage();
	public static final Image lightBgImage = new ImageIcon("resources/images/layout/bg_light.png").getImage();
	public static final Image playButtonImage = new ImageIcon("resources/images/layout/btn_play.png").getImage();
	public static final Image exitButtonImage = new ImageIcon("resources/images/layout/btn_exit.png").getImage();
	
	// Panel names
	public static final String gameLoginPanelName = "GAMELOGIN";
	public static final String gameLobbyPanelName = "GAMELOBBY";
	public static final String gamePanelName = "GAME";
	public static final String signupPanelName = "SIGNUP";
	public static final String forgotPasswordPanelName = "FORGOTPASSWORD";
	
	// GUI constants
	public static final Dimension minSize = new Dimension(800,800);
	public static final Dimension chessPanelSize = new Dimension(800, 500);
	public static final Dimension pongPanelSize = new Dimension(800, 300);
	public static final String gameNameString = "Ultra Chess 1987 Bronze Edition";
	public static final Color backgroundColor = Color.DARK_GRAY;
	public static final Color textColor = Color.WHITE;
	public static final Color textErrorColor = Color.RED;
	public static final Color textLinkColor = new Color(75, 75, 255);	//light blue
	public static final Color playButtonColor = new Color(255,255,255,200);
	
	// GameLoginPanel constants
	public static final String loginTitleLabelString = "Account Login";
	public static final String signupTitleLabelString = "Sign Up";
	public static final String forgotPasswordTitleLabelString = "Forgot Password";
	public static final String hostnameLabelString = "Hostname";
	public static final String portLabelString = "Port";
	public static final String defaultHostnameString = "localhost";
	public static final String defaultPortString = "6789";
	public static final String usernameLabelString = "Username";
	public static final String passwordLabelString = "Password";
	public static final String confirmPasswordLabelString = "Confirm Password";
	public static final String loginButtonString = "Log In";
	public static final String signupLabelString = "Don't have an account? Sign up now!";
	public static final String forgotPasswordLabelString = "Forgot your password?";
	public static final String secretQuestionLabelString = "Secret Question";
	public static final String secretAnswerLabelString = "Secret Answer";
	public static final String createAccountButtonString = "Create Account";
	public static final String retrievePasswordButtonString = "Retrieve Password";
	public static final String retrieveSecretQuestion = "Find Account";
	public static final String backButtonString = "Back";
	public static final String errorWrongAnswerString = "Incorrect Attempt";
	public static final String errorAccountDNEString = "User Does not Exist";
	public static final String errorUsernameInUseString = "This username is taken";
	public static final int usernameMinLength = 4;
	public static final int usernameMaxLength = 12;
	public static final int passwordMinLength = 6;
	public static final int passwordMaxLength = 12;
	public static final int longTextFieldSize = 30;
	public static final int mediumTextFieldSize = 14;
	public static final int shortTextFieldSize = 10;
	public static final float loginTitleFontSize = 13f;

	// GameLobbyPanel constants
	public static final Dimension playPanelSize = new Dimension(100,45);
	// GameLobbyPlayPanel constants
	public static final Border playPanelBorder = BorderFactory.createLineBorder(Color.GRAY);
	public static final String playButtonPanelName = "PLAYBUTTON";
	public static final String searchForMatchPanelName = "SEARCHFORMATCH";
	public static final String playButtonString = "Play";
	public static final Dimension exitButtonSize = new Dimension(15,15);
	public static final String searchForMatchString = "Searching for Match";
	public static final float searchForMatchFontSize = 9f;
	// GameLobbyLeaderBoardPanel constants
	public static final String leaderBoardPanelName = "LEADERBOARD";
	// GameLobbyGameSelectorPanel constants
	public static final String gameSelectorPanelName = "GAMESELECT";
	public static final String createGameButtonString = "Create Game";
	public static final String joinGameButtonString = "Join Game";
	public static final String homeButtonString = "Home";
	// GameLobbyCustomGamePanel constants
	public static final String customGamePanelName = "CUSTOMGAME";
	public static final String standardGameButtonString = "Standard";
	public static final String quitButtonString = "Quit";
	public static final String checkBoxPongString = "Pong";
	public static final String checkBoxSpeedString = "Speed";
	public static final String checkBoxNoneString = "None";
	public static final int chessBoardOffsetX = 200;
	
	// Chess constants
	public static final String alphabetString = "ABCDEFGH";
	public static final int chessSquareOffset = 50;
	public static final int columnCount = 8;
	public static final int rowCount = 8;
	public static final Image chessBoardImage = new ImageIcon("resources/images/chess/board.png").getImage();
	public static final Image blackPawnImage = new ImageIcon("resources/images/chess/bp.png").getImage();
	public static final Image blackRookImage = new ImageIcon("resources/images/chess/br.png").getImage();
	public static final Image blackKnightImage = new ImageIcon("resources/images/chess/bn.png").getImage();
	public static final Image blackBishopImage = new ImageIcon("resources/images/chess/bb.png").getImage();
	public static final Image blackQueenImage = new ImageIcon("resources/images/chess/bq.png").getImage();
	public static final Image blackKingImage = new ImageIcon("resources/images/chess/bk.png").getImage();
	public static final Image whitePawnImage = new ImageIcon("resources/images/chess/wp.png").getImage();
	public static final Image whiteRookImage = new ImageIcon("resources/images/chess/wr.png").getImage();
	public static final Image whiteKnightImage = new ImageIcon("resources/images/chess/wn.png").getImage();
	public static final Image whiteBishopImage = new ImageIcon("resources/images/chess/wb.png").getImage();
	public static final Image whiteQueenImage = new ImageIcon("resources/images/chess/wq.png").getImage();
	public static final Image whiteKingImage = new ImageIcon("resources/images/chess/wk.png").getImage();
	
	// Error constants
	public static final String requiredFieldErrorString = "- This field is required";
	public static final String alphanumericErrorString = "- Must contain only letters and numbers";
	public static final String usernameLengthErrorString = "- Must be between " + usernameMinLength + " and " + usernameMaxLength + " characters long";
	public static final String passwordLengthErrorString = "- Must be between " + passwordMinLength;
	public static final String passwordLengthErrorString2 = "and " + passwordMaxLength + " characters long";
	public static final String passwordMatchErrorString = "- Passwords must match";
	public static final String connectionErrorString = "- Unable to connect to host";
	
	//Timer setting
	public static final int timePerMove = 20;

}
