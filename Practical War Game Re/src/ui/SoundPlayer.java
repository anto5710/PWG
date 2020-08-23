package ui;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;

import ui.util.Util;

public class SoundPlayer {

	private Clip clip;
	
	public void stop(){
		if(clip!=null) clip.close();
	}
	
	public void play(File sound, int max_duration){
		Assert.throwIF(sound==null || !sound.exists(), "Imaginary .wav cannot be played");
		try {
		    AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
		    AudioFormat format = stream.getFormat();
		    Info info = new DataLine.Info(Clip.class, format);
		    
		    Clip clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		    this.clip = clip;
		    
			Util.runAfter(()->stop(), max_duration);		
		}
		catch (Exception e) {
		   e.printStackTrace(System.err);
		}
	}
		
	//without auto stop
	public void play(File sound) {
		Assert.throwIF(sound==null || !sound.exists(), "Imaginary .wav cannot be played");
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
		    AudioFormat format = stream.getFormat();

		    Info info = new DataLine.Info(Clip.class, format);
		    Clip clip = (Clip) AudioSystem.getLine(info);
		    
		    clip.open(stream);
		    clip.start();
		    this.clip = clip;
		    
//			Util.runAfter(()->stop(), max_duration);		
		}
		catch (Exception e) {
		   e.printStackTrace(System.err);
		}
	}
}
