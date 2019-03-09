package ui.Renderer;

import javax.swing.JPanel;

import board.Coord;
import board.Formation;
import board.Shogi;
import board.Team;
import ui.Assert;


public class DynamicBoard extends JPanel{
	protected Shogi game;
	protected boolean centered = true;

	public DynamicBoard(Formation f, Team...teams) {
		Assert.test(f==null || teams.length>4 || teams.length==0, "Invalid formation or invalid number of teams");
		game = new Shogi(f,teams);
	}
	
	// length에 대한 비율값
	private final double board_border = 0.05; 
	private final double border = 0.025;
		
	public Coord toCoord(int px, int py){
		int x = (int)Math.round((px-(boardLength()*board_border+tx()))/tileWidth());
		int y = (int)Math.round((py-(boardLength()*board_border+ty()))/tileHeight());
		return new Coord(x,y);
	}

	public int[] toPixel(Coord pass) {
		return toPixel(pass.x, pass.y);
	}
	
	public int[] toPixel(int[]coord){
		return toPixel(coord[0], coord[1]);
	}
	
	public int[] toPixel(int x, int y){
		int px = tx() + (int)(tileWidth()*x + boardLength()*board_border);
		int py = ty() + (int)(tileHeight()*y + boardLength()*board_border);
		
		int[]pixel = {px, py};
		return pixel;
	}
	
	public float szF(double value){
		return (float) (value * boardLength());
	}
	
	public double sz(double value){
		return value * boardLength();
	}

	public int szI(double value){
		return (int) sz(value);
	}
	
	public int boardLength(){	
		return (int) (Math.min(getWidth(), getHeight())*(1-2*border));
	}
	
	public double tileWidth(){
		return boardLength()*(1-board_border*2)/(game.tile_row);
	}
	
	public double tileHeight(){
		return boardLength()*(1-board_border*2)/(game.tile_column);
	}
	
	public int getBorderedW(){
		return (int) (getWidth()*(1-2*border));
	}
	
	public int getBorderedH(){
		return (int) (getHeight()*(1-2*border));
	}
	
	protected int tx(){
		return szI(border) + (centered ? (getBorderedW() - boardLength())/2 : 0);
	}
	
	protected int ty(){
		return szI(border) + (centered ? (getBorderedH() - boardLength())/2 : 0);
	}
}