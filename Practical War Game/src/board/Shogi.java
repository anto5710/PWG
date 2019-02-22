package board;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.Pieces.Pieces;
import board.Pieces.Route;
import board.Pieces.iPiece;

public class Shogi {
	public final int tile_row, tile_column; //칸
	public final int width, height;  // 좌표
	private iPiece[][] piecies;
	private Map<Team, Coord> castles = new HashMap<>();
	public final double [] center; //배열의 좌표 아님
	
	
	public Shogi(int tileRow, int tileColumn, Team... teams) {
		this.tile_row = tileRow;
		this.tile_column = tileColumn;
		this.width = tileRow + 1;
		this.height = tileColumn + 1;
		
		double[] centerCoord = {tileRow/2D,tileColumn/2D};
		center = centerCoord;
		piecies = new iPiece[width][height];
		
		setCastles(teams);
		inititate();
	}
	private static final String formation = 
			"車馬象士+士象馬車"+","+
			"++++王++++"+","+
			"+砲+++++砲+"+","+
			"卒+卒+卒+卒+卒"+","+
			"+++++++++"+","+
			"+++++++++"+","+
			"卒+卒+卒+卒+卒"+","+
			"+砲+++++砲+"+","+
			"++++王++++"+","+
			"車馬象士+士象馬車";
	
			
	public boolean isThereStone(int x, int y){
		return piecies[x][y]!=null;
	}
	

	public boolean move(int xi, int yi, int xf, int yf){
		iPiece piece = piecies[xi][yi];
		if(piece==null) return false;
		
		List<Route> routes = piece.findRoutes(this, xi, yi);
		
		boolean destable = routes.stream().filter(r->r.isDest(new Coord(xf, yf))).count() > 0;
		if(!destable) return false;
		
		piecies[xf][yf] = piecies[xi][yi];
		piecies[xi][yi] = null;
		return true;
	}
	
	public boolean onBoard(int x, int y){
		return 0 <= x && x < width && 0 <= y && y < height; 
	}
	
	private void inititate(){
		String[]rows = formation.split(",");
		for(int y=0; y<rows.length; y++){
			for(int x=0; x<rows[y].length(); x++){
				String symbol = ""+rows[y].charAt(x);
				if(!symbol.equals("+")){
					Team team = y>center[1]? (Team) castles.keySet().toArray()[1]:(Team) castles.keySet().toArray()[0];
					iPiece p = Pieces.castFromSymbol(symbol, team);;
					System.out.println(symbol+"  "+ x+" "+y+ "  "+p);
					
					piecies[x][y] = p;
				}
			}
		}
		return;	
	}
	
	public boolean isCastle(int x, int y){
		return castles.values().stream().filter(coord -> coord.x==x && coord.y==y).count() == 1;
	}
	
	private void setCastles(Team[]teams){
		double theta = 2*Math.PI/teams.length;
		double xr = tile_row - center[0] - 1;
		double yr = tile_column - center[1] -1;		
	
		for(int i=0; i<teams.length; i++){
			Team team = teams[i];
			double dx = Math.cos(Math.PI/2 + theta*i)*xr;
			double dy = Math.sin(Math.PI/2 + theta*i)*yr;
		
			int[]point = {(int) Math.round(dx+center[0]), (int)Math.round(dy+center[1])};
			castles.put(team, new Coord(point));
		}
	}
	
	public Map<Team, Coord> castles(){
		return castles;
	}
	
	public iPiece get(int x, int y) {
		return piecies[x][y];
	}


	public boolean inCastle(int x, int y) {
		for(Coord castle:castles.values()){
			if(castle.inDist(new Coord(x,y), Math.sqrt(2)*1)) return true;
		}
		return false;
	}
	
	public boolean onBoard(Coord coord) {
		return onBoard(coord.x, coord.y);
	}


	public boolean isThereStone(Coord coord) {
		return isThereStone(coord.x, coord.y);
	}


	public iPiece get(Coord to) {
		return get(to.x, to.y);
	}


	public boolean inCastle(Coord to) {
		return inCastle(to.x, to.y);
	}


	public boolean isCastle(Coord origin) {
		return isCastle(origin.x, origin.y);
	}
	
}
