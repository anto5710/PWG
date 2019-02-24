package board;

import java.util.ArrayList;
import java.util.List;

public class Coord {
	public final int x,y ;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord(int[]coord){
		this(coord[0], coord[1]);
		assert coord.length==2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Coord)) return false;
		
		Coord c = (Coord) obj;
		return c.x == x && c.y == y;
	}
	
	public List<Coord> squareRange(double margin){
		List<Coord> coords = new ArrayList<>();
		for(double dx=-margin; dx<=margin; dx+=margin){
			for(double dy=-margin; dy<=margin; dy+=margin){
				Coord cur = new Coord((int)Math.round(dx+x), (int)Math.round(dy+y));
				
				if(!coords.contains(cur)){ 
					coords.add(cur);
				}
			}
		}
		return coords;
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}
	
	public Coord move(Coord delta){
		return move(delta.x, delta.y);
	}
	
	public Coord move(int dx, int dy){
		return new Coord(x+dx, y+dy);
	}

	public Coord multiply(int dx, int dy){
		return new Coord(x*dx, y*dy);
	}
	
	public Coord multiply(int d){
		return multiply(d, d);
	}

	public Coord multiply(int[] toMultiply) {
		return multiply(toMultiply[0], toMultiply[1]);
	}
	
	public static Coord[] listfrom2D(int[][]coordArray){
		Coord[] c = new Coord[coordArray.length];
		for(int i=0;i<coordArray.length;i++){
			c[i] = new Coord(coordArray[i][0],coordArray[i][1]);
		}
		return c;
	}

	public static Coord[] multiplyAll(Coord[]coords, int[]toMultiply){
		Coord[] c = coords.clone();
		for(int i=0; i<c.length; i++){
			c[i] = c[i].multiply(toMultiply);
		}
		return c;
	}
	
	public boolean inDist(Coord c1, double d){
		int dx = c1.x - x;
		int dy = c1.y - y;
		return dx*dx + dy*dy <= d*d;
	}
}
