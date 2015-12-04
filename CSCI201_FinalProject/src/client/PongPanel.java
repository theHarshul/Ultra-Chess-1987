package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import networkobjects.PongObject;

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
	If client is responsible for cannon, sends signals for �cannon fired� if spacebar is pressed

	-update()
	updates locations of all objects visually on screen

	-isCollision()
	Checks for collision between ball and paddle. This method is called repeatedly
	If x-location of ball == x location of paddle and ball isn�t within the y range of the paddle (i.e. paddle didn�t collide with ball), then subtract 1 from currLIves

	-hasLost()
	Checks if currLives == 0. If so, send signal to Chess stating that the defending player has lost in pong and, in turn, lose their turn in Chess.
	Sends data to GameController once the defending player has lost.

	-ClientConstants
	contains all constants in JLabels, JButtons, and any other constants used in the client package
	public static final Dimension minSize = new Dimension(640,480)	// minimum size for ClientGUI
	public static final String defaultHostnameString = "localhost"
	public static final String defaultPortString = "6789"
	public static final String alphabetString = "ABCDEFGH�;	// used for column index in chess
	public static final int columnCount = 8
	public static final int rowCount = 8

 * 
 *
 */



public class PongPanel extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;

	public volatile int currPlayer; // // 0 or 1
	public volatile int paddleLoc_y;
	public volatile int cannonLoc_y;
	private volatile int ballLoc_x;
	private volatile int ballLoc_y;
	public volatile int lives;
	private boolean playerSet;
	public volatile boolean isShotFired;
	public volatile boolean shotTraveling;
	public volatile boolean canCallShotFired;
	protected static PongPanel p;
	PongObject PO_in;
	ClientPanel cp;
	GameController myController;
	Thread trw; 
	Thread t;

	/*
	 * TODO: -Call a method in the controller when the game is lost
	 * TODO: -Have a reset method in here that the controller can call (one arg should be cannonOrPaddle)
	 * 		Never call the reset method yourself. The game controller will take care of that when there is a new turn
	 */

	public void update(){ // visually live update of gameboard 
		repaint();
	}

	public boolean isCollision(){
		return (ballLoc_x <= 5 && paddleLoc_y <= ballLoc_y && paddleLoc_y + 70 > ballLoc_y );
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
		playerSet = false;
		shotTraveling = false;
		canCallShotFired = true;

	}
	public void createGUI(){ // initial game board
		setFocusable(true);
	}

	public void setVar(int currPlayer_in, int paddleLoc_y_in, int cannonLoc_y_in, int lives_in, boolean isShotFired_in, int ballLoc_x_in){
		

		/*
		if(!playerSet){
			
			
			if(currPlayer_in == 1){
				currPlayer = 0;
			}
			else if(currPlayer_in == 0){
				currPlayer = 1;
			}
			playerSet = true;
		}
		*/
		
		if(currPlayer == 1) {
			paddleLoc_y = paddleLoc_y_in;
		}

		if(currPlayer == 0){ // if cannon controller, update location of cannon
			cannonLoc_y = cannonLoc_y_in;
			isShotFired = isShotFired_in;
			//ballLoc_x = ballLoc_x_in;
			lives = lives_in;
		}


		update();

	}
	/*
	public void updatePOIn(){
		if(playerSet == false){
			if(PO_in.getcurrPlayer() == 0)
				currPlayer = 1;
			else if(PO_in.getcurrPlayer() == 1)
				currPlayer = 0;
			playerSet = true;
		}

		// PO_in is outdated (default values).
		if(currPlayer == 1) {
			paddleLoc_y = PO_in.getpaddleLoc_y();
		}

		if(currPlayer == 0){ // if cannon controller, update location of cannon
			cannonLoc_y = PO_in.getcannonLoc_y();
		}

		lives = PO_in.getlives();
		/*if(currPlayer == 0){
			ballLoc_x = PO_in.getballLoc_x();

		}
		update();
	}
	 */
	public void setPO_in(PongObject poin){
		PO_in = poin;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		super.setBackground(Color.black);
		g.setColor(Color.WHITE);
		g.fillRect(0, paddleLoc_y, 15, 70);
		g.fillRect(760, cannonLoc_y, 50, 40);
		g.fillRect(700, cannonLoc_y+10, 70, 20);			
		g.fillRect(ballLoc_x, ballLoc_y, 10, 10);
		g.drawLine(0, 270, (int)ClientConstants.pongPanelSize.getWidth(), 270);
		g.setColor(Color.RED);
		g.drawString("Lives remaining: " + lives, 0, 250);
		g.drawString("Player: "+ currPlayer, 0 ,260);
		if(lives <=0){
			g.drawString("Game Over!", 0, 270);
		}
		//g.drawString("Paddle y: " + paddleLoc_y, 0, 330);
		//g.drawString("Cannon y: "+ cannonLoc_y, 0, 340);

	}



	public void run(){
		while(true){
			//System.out.println("Cannon:" + cannonLoc_y);
			//System.out.println("Paddle:" + paddleLoc_y);
			//System.out.println("Cannon: " + PO_out.getcannonLoc_y());
			//System.out.println("Paddle" + PO_out.getpaddleLoc_y());
			// PO_out is getting the correct values!

			//if(!playerSet && cp != null && cp.clientClientCommunicator != null && cp.clientClientCommunicator.isConnected){	
			//	cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, ballLoc_x, lives, isShotFired, shotTraveling));
			//}

			//	if(PO_in != null){
			//updatePOIn();
			//	}
			/*
			if(myController != null)
			if(myController.isMyTurn()){
				currPlayer = 0;
			}
			else{
				currPlayer = 1;
			}
			 */
			
			if(myController.isMyTurn()){
				if(currPlayer == 1)
				{
					instantiateVariables();
				}
				currPlayer = 0;
			}
			
			else if(!myController.isMyTurn()){
				if(currPlayer == 0){
					instantiateVariables();
				}
				currPlayer = 1;
			}
			
			if(hasLost() && currPlayer == 0){ 
				instantiateVariables();
				myController.skipTurn();
			}
			
			if(isShotFired && canCallShotFired){
				shotFired();
			}
			
			update();
		}
	}
	
	public void shotFired(){
		canCallShotFired = false;
		ballLoc_x = 750;
		ballLoc_y = cannonLoc_y+10;

		t = new Thread(new Runnable(){
			@Override
			synchronized public void run(){
				shotTraveling = true;
				while(ballLoc_x >= -10)
				{	
					ballLoc_x -= 15;
					
					try{wait(15);}
					catch(InterruptedException ie){}

					if(isCollision()){
						System.out.println("Success! Lives remaining: " + lives);
					}
					else if (ballLoc_x <0  && !isCollision()){
						lives--;
						System.out.println("Fail! Lives remaining: " + lives);
					}
					update();
				}
				if(hasLost()){ 
					System.out.println("Game Over!");
				}
				
				shotTraveling = false;
				isShotFired = false;
				canCallShotFired = true;
				
				update();
				Thread.currentThread().interrupt();
			}

		});

		t.start();	
		
	}

	public void addEvents(){ // read key presses

		Action down = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
				if(currPlayer == 1)
					if(cannonLoc_y<=220){
						cannonLoc_y+=10;
						update();
						cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, lives, isShotFired, ballLoc_x));

					}
				if(currPlayer == 0)
					if(paddleLoc_y<=190){
						paddleLoc_y+=10;
						update();
						cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, lives, isShotFired, ballLoc_x));

					}
			}
		};

		Action up = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
				if(currPlayer == 1)
					if(cannonLoc_y>=10){
						cannonLoc_y-=10;
						update();
						cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, lives, isShotFired, ballLoc_x));

					}
				if(currPlayer == 0)
					if(paddleLoc_y>=10){
						paddleLoc_y-=10;
						update();
						cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, lives, isShotFired, ballLoc_x));


					}
			}
		
		};
/*
		Action w = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
				if(currPlayer == 0)
					if(paddleLoc_y>=10){
						paddleLoc_y-=10;
						update();
						cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, lives, isShotFired, ballLoc_x));


					}
			}
		};

		Action s = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
				if(currPlayer == 0)
					if(paddleLoc_y<=190){
						paddleLoc_y+=10;
						update();
						cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, lives, isShotFired, ballLoc_x));

					}
			}
		};
*/
		Action shoot = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
				if(currPlayer == 1)
					if(cannonLoc_y<=230){
						if(!shotTraveling){
							isShotFired = true;
							update();
							cp.sendObjectToClient(new PongObject(currPlayer, paddleLoc_y, cannonLoc_y, lives, isShotFired, ballLoc_x));

						}
					}
			}
		};


		this.getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
		this.getActionMap().put("DOWN", down);
		this.getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "UP");
		this.getActionMap().put("UP", up);
		/*this.getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "W");
		this.getActionMap().put("W", w);
		this.getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "S");
		this.getActionMap().put("S", s);
		*/
		this.getInputMap(this.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "SPACE");
		this.getActionMap().put("SPACE", shoot);




		/*
		addKeyListener(new KeyListener() { // paddle move up
			public void keyPressed(KeyEvent e) {
				if(paddleLoc_y>=10){
					if(e.getKeyCode()==KeyEvent.VK_W){
						paddleLoc_y-=10;
						update();
					}
				}
			}

			public void keyReleased(KeyEvent e) { }

			public void keyTyped(KeyEvent e) { }
		});

		addKeyListener(new KeyListener() { // paddle move down
			public void keyPressed(KeyEvent e) {
				if(paddleLoc_y<=190){
					if(e.getKeyCode()==KeyEvent.VK_S){
						paddleLoc_y+=10;
						update();
					}
				}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});


		addKeyListener(new KeyListener() { // cannon move up
			public void keyPressed(KeyEvent e) {
				if(cannonLoc_y>=10){
					if(e.getKeyCode()==KeyEvent.VK_UP){
						cannonLoc_y-=10;
						update();
					}
				}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});

		addKeyListener(new KeyListener() { // cannon move down
			public void keyPressed(KeyEvent e) {
				if(cannonLoc_y<=220){
					if(e.getKeyCode()==KeyEvent.VK_DOWN){
						cannonLoc_y+=10;
						update();
					}
				}
			}


			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});

		addKeyListener(new KeyListener() { // shoot!
			public void keyPressed(KeyEvent e) {
				if(cannonLoc_y<=220){
					if(isShotFired == false)
						if(e.getKeyCode()==KeyEvent.VK_SPACE){
							ballLoc_x = 650;
							ballLoc_y = cannonLoc_y+10;
							shotFired();
							update();
						}
				}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		 */

	}

	public PongPanel(ClientPanel cp_in, GameController gc_in){		
		cp = cp_in;
		myController = gc_in;
		instantiateVariables();
		createGUI();
		addEvents(); 
		Thread t = new Thread(this);
		t.start();
	}

	public static void main(String[] args){
		JFrame holder = new JFrame();
		holder.setSize(ClientConstants.pongPanelSize);
		holder.setLocation(300,400);
		holder.setResizable(false);
		holder.setContentPane(p);
		holder.setVisible(true);
		holder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}




}

// if lives == 0, send has lost message

// if chess.newTurn -> receive newGame message




