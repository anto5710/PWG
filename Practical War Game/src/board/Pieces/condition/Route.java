package board.Pieces.condition;

import java.util.ArrayList;
import java.util.List;

import board.Coord;

public class Route {
	private List<Coord> coords = new ArrayList<>();
	public int destN;
	public final Coord origin;
	
	public Route(Coord origin){
		this.origin = origin;
		this.destN = 1;
	}
	
	public Route(Coord origin, int destN){
		this(origin);
		if(destN>=0) this.destN = destN;
	}
	
	public void plusDest(){
		destN++;
	}
	
	public boolean addCoord(Coord pass) {
		return coords.add(pass); 
	}
	
	public boolean isDest(int x, int y){
		Coord c = new Coord(x,y);
		return getDests()!=null && getDests().contains(c);
	}
	
	public boolean onRoute(Coord point) {	
		return origin.equals(point) || coords.contains(point);
	}
	
	public List<Coord> getPasses(){
		return coords.subList(0, coords.size()-(destN));
	}
	
	public List<Coord> getDests(){
		return coords.subList(coords.size()-(destN), coords.size());
	}
	
	public int index(){
		return coords.size();
	}

	public List<Coord> getAll() {
		List<Coord> c = new ArrayList<>(coords); c.add(0, origin);
		return c;
	}
}
