package board;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import ui.Assert;
import ui.util.Util;

public class Coord {
	public final int x,y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord(int[]coord){
		Assert.throwIF(coord==null || coord.length!=2, "Invalid Format for a Coord Instance. Only (x,y) is allowed");
		this.x = coord[0];
		this.y = coord[1];
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Coord)) return false;
		
		Coord c = (Coord) obj;
		return c.x == x && c.y == y;
	}
	
	public static boolean anyCoord(List<Coord>c, Predicate<Coord> condition){
		return c!=null && condition!=null && c.stream().filter(condition).count()>0;
	}
	
	public double slope(Coord c){
		if(c.x==x) return (c.y-y>0) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
		
		return (c.y - y)/(c.x - x); 
	}
	
	public boolean between(Coord c1, Coord c2){
		return Util.between(c1.x, x, c2.x) && Util.between(c1.y, y, c2.y);
	}
	
	public Set<Coord> squareRange(double margin){
		Set<Coord> coords = new HashSet<>();
		for(double dx=-margin; dx<=margin; dx+=margin){
			for(double dy=-margin; dy<=margin; dy+=margin){
				Coord cur = new Coord((int)Math.round(dx+x), (int)Math.round(dy+y));				 
				coords.add(cur);
			}
		}
		return coords;
	}
	
	public static boolean onALine(Coord...coords){
		return onALine(0, coords);
	}
	
	private static boolean onALine(int i, Coord...coords){
		if(i>=coords.length-2) return true;
		
		Coord c1 = coords[i];
		Coord c2 = coords[i+1];
		Coord c3 = coords[i+2];
		
		return (c1.equals(c2) || c3.equals(c2) || c1.slope(c2)==c2.slope(c3)) && onALine(i+1, coords);
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}
	
	public Coord add(Coord delta){
		return add(delta.x, delta.y);
	}
	
	public Coord add(int dx, int dy){
		return new Coord(x+dx, y+dy);
	}
	
	public Coord subtract(Coord delta){
		return add(delta.multiply(-1));
	}
	
	public Coord subtract(int dx, int dy){
		return add(-dx, -dy);
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

	public static Coord parse(String st) {
		int x = 0, y=0;
		String string = st.substring(1, st.length()-1);
		
		
		try{
			String [] s23t = string.split(",");
		x = Integer.parseInt(s23t[0].trim());
		y = Integer.parseInt(s23t[1].trim());
		}catch(Exception e){
			System.out.println("Failed to parse coords movement data from server");
			e.printStackTrace();
		}
		return new Coord(x,y);
	}
}
