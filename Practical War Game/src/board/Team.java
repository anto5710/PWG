package board;

import java.awt.Color;

public class Team {
	private final String name;
	private final Color color;
	public Team(String name, Color color){
		this.name = name;
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	public String getName(){
		return name;
	}
}
