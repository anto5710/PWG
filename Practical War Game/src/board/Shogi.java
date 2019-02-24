package board;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import board.Pieces.Pieces;
import board.Pieces.iPiece;
import board.Pieces.ChessPieces.King;
import board.Pieces.condition.Route;

public class Shogi {
	public final int tile_row, tile_column; //칸
	public final int width, height;  // 좌표
	private iPiece[][] pieces;
	private Map<Team, Coord> castles = new HashMap<>();
	public final double [] center; //배열의 좌표 아님
	private Team []teams;
	private int t=0;
	
	
	/*
	 * 대각선을 나타내는 리스트, 각각 크기가 2인 1차원배열로 하나의 대각선을 나타낸다.
	 */
	private List<Coord[]> diagonals = new ArrayList<>();
	
	
	/*
	 * 최대 4팀까지
	 */
	public Shogi(int tileRow, int tileColumn, Team... teams) {
		this.tile_row = tileRow;
		this.tile_column = tileColumn;
		this.width = tileRow + 1;
		this.height = tileColumn + 1;
		
		double[] centerCoord = {tileRow/2D,tileColumn/2D};
		center = centerCoord;
		pieces = new iPiece[width][height];

		
		this.teams = teams;
		
		t=0;
		setCastles(teams);
		inititate();
	}
	
	private Coord king(Team team){
		Coord castle = castles.get(team);
		if(castle==null) return null;
		
		for(Coord cur : castle.squareRange(1)){
			iPiece p = get(cur);
			if(team.in(p) && p instanceof King){
				return cur;
			}
		}
		return null;
	}
	
	public boolean check(Team team){
		Coord king = king(team);
		if(king==null) return false;
		
		for(int y=0;y<height; y++){
			for(int x=0;x<width; x++){
				iPiece piece = pieces[x][y];
				if(piece==null || team.in(piece)) continue;
				
				if(canMoveTo(x, y, king.x, king.y))
					return true;
			}
		}
		return false;
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
		return pieces[x][y]!=null;
	}
	
	public Team turn(){
		return teams[t];
	}
	
	public void next(){
		t++;
		if(t>=teams.length) t=0;
	}
	

	public boolean move(int xi, int yi, int xf, int yf){
		if(!canMoveTo(xi, yi, xf, yf)) return false;
	
		iPiece pieceMove = pieces[xi][yi];
		iPiece pieceAbr = pieces[xf][yf];
	
		pieces[xf][yf] = pieceMove;
		pieces[xi][yi] = null;
		
		if(check(pieceMove.getTeam())){
			//undoes
			pieces[xf][yf] = pieceAbr;
			pieces[xi][yi] = pieceMove;
			return false;
		}
		
		next();
		return true;
	}
	
	public boolean anyInRoute(int xi, int yi, Predicate<Route> condition){
		iPiece piece = pieces[xi][yi];
		if(piece==null) return false;
		
		List<Route> routes = piece.findRoutes(this, xi, yi);
		return routes.stream().filter(condition).count()>0;
	}
	
	public boolean canMoveTo(int xi, int yi, int xf, int yf){
		return anyInRoute(xi, yi, r->r.isDest(xf, yf));
	}
	
	public boolean onRouteOf(Coord pieceLoc, Coord point){
		return anyInRoute(pieceLoc.x, pieceLoc.y, r->r.onRoute(point));
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
					
					pieces[x][y] = p;
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
		
			Coord castle = new Coord((int) Math.round(dx+center[0]), (int)Math.round(dy+center[1]));
			
			castles.put(team, castle);
			addDiagonal(castle.move(1, 1), castle.move(-1, -1));
			addDiagonal(castle.move(-1, 1), castle.move(1, -1));
		}
	}
	
	public void addDiagonal(Coord c1, Coord c2){
		if(!onBoard(c1) || !onBoard(c2) || c1.equals(c2)) return;
		
		Coord[]line = {c1,c2};
		diagonals.add(line);
	}
	
	public List<Coord[]> diagonals(){
		return diagonals;
	}
	
	public Map<Team, Coord> castles(){
		return castles;
	}
	
	public iPiece get(int x, int y) {
		return pieces[x][y];
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
