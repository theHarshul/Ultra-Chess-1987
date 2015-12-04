import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * Pong Classes

	-PongPanel extends JPanel
	Pong does not utilize the MVC pattern. It is entirely self-contained. So game logic, drawing, and networking are done within this class.
	When a player loses at Pong, this data is sent to GameController
	Contains information on which player is defending (pong paddle) and which player is attacking (cannon)
	When myColor variable (activePlayer) under Chess class changes values, a new game of Pong is instantiated, players switch controls,  and lives variable is reset to 3.
	Both clients contain active listening/receiving threads
	Has a turnEnd() method, that is called from Controller when a turn ends in the ChessGame
	Variables:
	int myColor = Chess.myColor. (activePlayer = paddle controller), (otherPlayer = cannon controller)
	int currLives = 3 (instantiated at 3)
	int cannonLocation (y value)
	int paddleLocation (y value)
	int firingSpeed (speed at which ball travels between cannon and paddle
	int ballXLocation, ballYLocation, ballXSpeed, ballYSpeed
	-move()
	Sends location of respective object (paddle/cannon).
	If client is responsible for cannon, sends signals for “cannon fired” if spacebar is pressed

	-update()
	updates locations of all objects visually on screen

	-isCollision()
	Checks for collision between ball and paddle. This method is called repeatedly
	If x-location of ball == x location of paddle and ball isn’t within the y range of the paddle (i.e. paddle didn’t collide with ball), then subtract 1 from currLIves

	-hasLost()
	Checks if currLives == 0. If so, send signal to Chess stating that the defending player has lost in pong and, in turn, lose their turn in Chess.
	Sends data to GameController once the defending player has lost.

	-ClientConstants
	contains all constants in JLabels, JButtons, and any other constants used in the client package
	public static final Dimension minSize = new Dimension(640,480)	// minimum size for ClientGUI
	public static final String defaultHostnameString = "localhost"
	public static final String defaultPortString = "6789"
	public static final String alphabetString = "ABCDEFGH”;	// used for column index in chess
	public static final int columnCount = 8
	public static final int rowCount = 8

 * 
 *
 */



public class Pong extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;




	private int currPlayer; // // 0 or 1
	private int paddleLoc_y;
	private int cannonLoc_y;
	private int ballLoc_x;
	private int ballLoc_y;
	private int lives;
	private static int paddleLoc_x;
	private static int cannonLoc_x;
	private JPanel jp;
	private boolean isShotFired;


	public void move(){

	}

	public void update(){ // visually live update of gameboard (replace with revalidate/repaint?)
		repaint();
		revalidate();
	}

	public boolean isCollision(){
		return (ballLoc_x == 5 && paddleLoc_y <= ballLoc_y && paddleLoc_y + 70 > ballLoc_y );
	}

	public boolean hasLost(){
		return (lives <= 0);
	}

	public void instantiateVariables(){
		paddleLoc_y = 150;
		cannonLoc_y = 150;
		ballLoc_x = -50;
		ballLoc_y = -50;
		currPlayer = -1;
		lives = 3;
		isShotFired = false;
		jp = new JPanel();

	}
	public void createGUI(){ // initial game board
		setSize(700,300);
		setLocation(300,400);
		setResizable(false);

		add(new drawPaddle());
		setFocusable(true);

	}

	synchronized public void run(){
		isShotFired = true;
		while(ballLoc_x >= -10)
		{
			ballLoc_x -= 1;
			try{wait(1);}
			catch(InterruptedException ie){}
			
			if(isCollision()){
				System.out.println("Success! Lives remaining: " + lives);
			}
			else if (ballLoc_x == 5 && !isCollision()){
				lives--;
				System.out.println("Fail! Lives remaining: " + lives);
			}
			update();
		}
		isShotFired = false;
		
		if(hasLost()){
			System.out.println("game over!");
		}

	}
	public void shotFired(){
		Thread thread = new Thread(this);
		thread.start();
	}

	public void addEvents(){ // read key presses
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




		addKeyListener(new KeyListener() { // paddle move up
			public void keyPressed(KeyEvent e) {
				if(paddleLoc_y>=10)
					if(e.getKeyCode()==KeyEvent.VK_W){
						paddleLoc_y-=10;
						update();
					}
			}

			public void keyReleased(KeyEvent e) { }

			public void keyTyped(KeyEvent e) { }
		});

		addKeyListener(new KeyListener() { // paddle move down
			public void keyPressed(KeyEvent e) {
				if(paddleLoc_y<=190)
					if(e.getKeyCode()==KeyEvent.VK_S){
						paddleLoc_y+=10;
						update();
					}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});


		addKeyListener(new KeyListener() { // cannon move up
			public void keyPressed(KeyEvent e) {
				if(cannonLoc_y>=10)
					if(e.getKeyCode()==KeyEvent.VK_UP){
						cannonLoc_y-=10;
						update();
					}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});

		addKeyListener(new KeyListener() { // cannon move down
			public void keyPressed(KeyEvent e) {
				if(cannonLoc_y<=220)
					if(e.getKeyCode()==KeyEvent.VK_DOWN){
						cannonLoc_y+=10;
						update();
					}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});

		addKeyListener(new KeyListener() { // shoot!
			public void keyPressed(KeyEvent e) {
				if(cannonLoc_y<=220)
					if(isShotFired == false)
						if(e.getKeyCode()==KeyEvent.VK_SPACE){
							ballLoc_x = 650;
							ballLoc_y = cannonLoc_y+10;
							shotFired();
							update();
						}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});


	}

	public Pong(){
		super("Pong");
		instantiateVariables();
		createGUI();
		addEvents(); 

	}

	public static void main(String[] args){
		Pong p = new Pong();
		p.setVisible(true);
	}


	class drawPaddle extends JPanel{
		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			super.setBackground(Color.black);
			g.setColor(Color.WHITE);
			g.fillRect(0, paddleLoc_y, 15, 70);
			g.fillRect(660, cannonLoc_y, 50, 40);
			g.fillRect(600, cannonLoc_y+10, 70, 20);			

			g.fillRect(ballLoc_x, ballLoc_y, 10, 10);



		}
	}


	class drawBall extends JPanel{
		private static final long serialVersionUID = 1L;

	}


	class read{ // read thread
		public read(){

		}

	}

	class write{ // write thread
		public write(){

		}
	}

}
