package client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
	static Clip airhorn, itsgone, knocks, name, twentyfive, notagain, thebottom, notafraid, hand, yessir, dothis, luger, piper, godno, winter, playButton, button;
	static {
		//initClip(airhorn, "src/sounds/airhorn.mp3");
		try {
			AudioInputStream s1 = AudioSystem.getAudioInputStream(new File("src/sounds/gone.wav"));
			itsgone = AudioSystem.getClip();
			itsgone.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/airhorn.wav"));
			airhorn = AudioSystem.getClip();
			airhorn.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/breakingbad_knocks.wav"));
			knocks = AudioSystem.getClip();
			knocks.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/breakingbad_name.wav"));
			name = AudioSystem.getClip();
			name.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/drake_25mil.wav"));
			twentyfive = AudioSystem.getClip();
			twentyfive.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/drake_notagain.wav"));
			notagain = AudioSystem.getClip();
			notagain.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/drake_thebottom.wav"));
			thebottom = AudioSystem.getClip();
			thebottom.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/eminem_notafraid.wav"));
			notafraid = AudioSystem.getClip();
			notafraid.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/handdown.wav"));
			hand = AudioSystem.getClip();
			hand.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/jayz_yessir.wav"));
			yessir = AudioSystem.getClip();
			yessir.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/kanye_dothis.wav"));
			dothis = AudioSystem.getClip();
			dothis.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/luger.wav"));
			luger = AudioSystem.getClip();
			luger.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/piedpiper.wav"));
			piper = AudioSystem.getClip();
			piper.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/the-office-no.wav"));
			godno = AudioSystem.getClip();
			godno.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/winter.wav"));
			winter = AudioSystem.getClip();
			winter.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/playbutton.wav"));
			playButton = AudioSystem.getClip();
			playButton.open(s1);
			
			s1 = AudioSystem.getAudioInputStream(new File("src/sounds/button.wav"));
			button = AudioSystem.getClip();
			button.open(s1);
			
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		initClip(winter, "src/sounds/winter.wav");

	}
	
	static void blank(){}
	
	static void initClip(Clip clip, String loc){
		AudioInputStream stream;
		try {
			stream = AudioSystem.getAudioInputStream(new File(loc));
			clip = AudioSystem.getClip();
			clip.open(stream);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void playRandomGreat(){//check or checkmate
		double val = Math.random();
		if(val < .3)
			playLuger();
		else if (val < .6)
			playAirhorn();
		else
			playNotafraid();
		//luger
		//airhorn
		//not afraid
		
	}
	
	public static void playRandomGood(){ //capture
		double val = Math.random();
		if(val < .1) playName();
		else if (val <.4) playTwentyfive();
		else if (val <.6) playDothis();
		//name
		//twentyfive
		//dothis
	}
	
	public static void playRandomBad(){
		double val = Math.random();
		if(val < .2) playNotagain();
		else if (val < .4) playHand();
		else if( val < .6) playItsgone();
		//not again
		//hand
		//itsgone
	}
	
	public static void playRandomTerrible(){
		double val = Math.random();
		if(val < .3) playNo();
		else if (val < .6) playKnocks();
		else if (val < .8) playPiper();
		else playTwentyfive();
		//no
		//knocks
		//piper
		//twentyfive
	}
	
	
	static void playAirhorn(){	
		SoundThread sound = new SoundThread(airhorn);
		sound.run();
	}
	
	static void playItsgone(){
		SoundThread sound = new SoundThread(itsgone);
		sound.run();
	}
	
	static void playKnocks(){
		SoundThread sound = new SoundThread(knocks);
		sound.run();
	}
	
	static void playHand(){
		SoundThread sound = new SoundThread(hand);
		sound.run();
	}
	
	static void playNo(){
		SoundThread sound = new SoundThread(godno);
		sound.run();
	}
	
	static void playName(){
		SoundThread sound = new SoundThread(name);
		sound.run();
	}
	
	static void playNotafraid(){
		SoundThread sound = new SoundThread(notafraid);
		sound.run();
	}
	
	static void playNotagain(){
		SoundThread sound = new SoundThread(notagain);
		sound.run();
	}
	
	static void playPiper(){
		SoundThread sound = new SoundThread(piper);
		sound.run();
	}
	
	static void playDothis(){
		SoundThread sound = new SoundThread(dothis);
		sound.run();
	}
	
	static void playBottom(){
		SoundThread sound = new SoundThread(thebottom);
		sound.run();
	}
	
	static void playTwentyfive(){
		SoundThread sound = new SoundThread(twentyfive);
		sound.run();
	}
	
	static void playWinter(){
		SoundThread sound = new SoundThread(winter);
		sound.run();
	}
	
	public static void playYes(){
		SoundThread sound = new SoundThread(yessir);
		sound.run();
	}
	
	public static void playLuger(){
		SoundThread sound = new SoundThread(luger);
		sound.run();
	}
	
	public static void playPlayButton()
	{
		SoundThread sound = new SoundThread(playButton);
		sound.run();
	}
	
	public static void playButton()
	{
		SoundThread sound = new SoundThread(button);
		sound.run();
	}
}

class SoundThread extends Thread{
	Clip myClip;
	public SoundThread(Clip clip){
		myClip = clip;
	}
	
	public void run(){
		myClip.stop();
		myClip.setFramePosition(0);
		myClip.start();
	}
}
