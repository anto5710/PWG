package board;

import java.awt.Color;
import board.Pieces.iPiece;

public class Team {
	private final String name;
	private final Color color;	
	public int num;
	public Team(String name, Color color){
		this.name = name;
		this.color = color;
	}
	
	public Team(String string, Color blue, int i) {
		this(string, blue);
		num = i;
	}

	public boolean in(iPiece piece){
		return piece!=null && this.equals(piece.getTeam());
	}
	
	public Color getColor(){
		return color;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
