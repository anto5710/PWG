package board;


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
	
	public static int[] move(int [] c1, int [] c2){
		return move(c1[0], c1[1], c2[0], c2[1]);
	}
	
	public Coord move(Coord coord){
		int dx = coord.x;
		int dy =coord.y;
		return new Coord(x+dx, y+dy);
	}
	
	public static int[] move(int [] c1, int dx, int dy){
		return move(c1[0], c1[1], dx, dy);	
	}
	
	public static int[] move(int x, int y, int dx, int dy){
		int []c3 = {x + dx, y + dy};
		return c3;
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
