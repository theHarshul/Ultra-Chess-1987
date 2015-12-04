package networkobjects;

import java.io.Serializable;

public class PongObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 718882025217686488L;
	private int currPlayer; // 0 or 1
	private int paddleLoc_y;
	private int cannonLoc_y;
	private int ballLoc_x;
	private int lives;
	private boolean isShotFired;

	public PongObject(int currPlayer_in, int paddleLoc_y_in, int cannonLoc_y_in, int lives_in, boolean isShotFired_in, int ballLoc_x_in){
		currPlayer = currPlayer_in;
		paddleLoc_y = paddleLoc_y_in;
		cannonLoc_y = cannonLoc_y_in;
		lives = lives_in;
		ballLoc_x = ballLoc_x_in;
		isShotFired = isShotFired_in;
	}

	
	public int getcurrPlayer(){ return currPlayer; }
	public int getpaddleLoc_y(){ return paddleLoc_y; }
	public int getcannonLoc_y(){ return cannonLoc_y; }
	public int getlives(){ return lives; }
	public boolean getisShotFired(){ return isShotFired; }
	public int getballLoc_x(){ return ballLoc_x;}

	
	
}
