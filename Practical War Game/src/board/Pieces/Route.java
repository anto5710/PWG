package board.Pieces;

import java.util.ArrayList;
import java.util.List;

import board.Coord;

public class Route {
	List<Coord> coords = new ArrayList<>();
	private int destNum = 1;
	
	public final Coord origin;
	
	public Route(Coord origin){
		this.origin = origin;
	}
	
	public boolean addCoord(Coord pass) {
		return coords.add(pass); 
	}
	
	public boolean isDest(Coord dest){
		return getDests()!=null && getDests().contains(dest);
	}
	
	public List<Coord> getPasses(){
		return coords.subList(0, coords.size()-(destNum));
	}
	
	public List<Coord> getDests(){
		return coords.subList(coords.size()-(destNum), coords.size());
	}
	
	public int index(){
		return coords.size();
	}
}
