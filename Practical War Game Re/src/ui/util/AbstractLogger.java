package ui.util;

import javax.swing.JTextPane;

public abstract class AbstractLogger extends JTextPane{
	public abstract void log(String msg);
	
	public void ln(){
		log("\n");
	}
	
	public void logln(String msg){
		log(msg);
		ln();
	}
}
