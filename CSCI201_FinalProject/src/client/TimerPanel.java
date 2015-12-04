package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class TimerPanel extends JPanel implements Runnable{
	
	public volatile int timeRemaining;
	private JLabel timeRemainingLabel;
	public boolean myTurn;
	GameController gc;
	ClientPanel cp;
	
	public TimerPanel(ClientPanel cp_in, GameController gc_in){
		gc = gc_in;
		cp = cp_in;

		myTurn = gc.isMyTurn();
		
		timeRemaining = ClientConstants.timePerMove; // change this to input time remaining 
		new Thread(this).start();
	}
	
	public void setTurn(boolean myTurn_in){
		myTurn = myTurn_in;
	}
	
	public void setTime(int time_in){
		timeRemaining = time_in;
	}
	
	public void resetTime(){
		myTurn = gc.isMyTurn();
		timeRemaining = ClientConstants.timePerMove;
	}
	public void timeOut(){// what happens when time runs out
		resetTime();
		if(gc != null && gc.isMyTurn()){
			gc.skipTurn();
		}
	}
	
	public void checkConditions(){
		if(myTurn != gc.isMyTurn()){
			resetTime();
		}
		
		if(timeRemaining == 0){
			timeOut();
		}
	}
	synchronized public void run(){
		while(timeRemaining>0)
		try{
				Thread.sleep(1000);
				timeRemaining--;
				repaint();
				checkConditions();
			}catch(InterruptedException ie){}
		
		checkConditions();
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		super.setBackground(ClientConstants.backgroundColor);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
		g.drawString("" + timeRemaining, 150, 150);
	}

	public static void main(String[] args){
		JFrame holder = new JFrame();
		holder.setSize(400,400);
		holder.setLocation(300,400);
		holder.setResizable(false);
		holder.setVisible(true);
		holder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		holder.add(new TimerPanel(null, null));	
	}
}
