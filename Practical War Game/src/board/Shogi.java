package board;


import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import board.Formation.MetaPieceSet;
import board.Pieces.Pieces;
import board.Pieces.iPiece;
import board.Pieces.ChessPieces.King;
import board.Pieces.condition.Route;
import ui.Assert;
import ui.Util;
import ui.Renderer.TraditionalBoard;


public class Shogi {
	public final int tile_row, tile_column; //칸
	public final int width, height;  // 좌표
	public iPiece[][] pieces;
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
	public Shogi(Formation formation, Team... teams) {
		Assert.test(teams.length>4, "No more than 4 teams");
		
		this.width = formation.W;
		this.height = formation.H;
		this.tile_row = width-1;
		this.tile_column = height-1;
		
		double[] centerCoord = {tile_row/2D,tile_column/2D};
		center = centerCoord;
		pieces = new iPiece[width][height];

		
		this.teams = teams;
		t=0;
		setCastles();
		formate(formation);
	}
	
	public Shogi(iPiece[][] p, Team[] teams) {
		
		this.tile_row = p.length-1;
		this.tile_column = p[0].length-1;
		this.width = tile_row + 1;
		this.height = tile_column + 1;
		
		double[] centerCoord = {tile_row/2D,tile_column/2D};
		center = centerCoord;
		pieces = new iPiece[width][height];

		
		this.teams = teams;
		
		t=0;
		setCastles();
//		System.out.println(width + "d2 "+ height);
		this.pieces = p;
	}

	private Coord findKing(Team team){
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
		Coord king = findKing(team);
		if(king==null) return false;
	
		return anyPiece((p,c)->{
			return !team.in(p) && anyInRoute(c.x, c.y, r->r.isDest(king.x, king.y));
		});
	}
	
	public boolean checkmate(Team team){
		if(!check(team)) return false;
		
		return !anyPiece((p,c)->{
			return team.in(p) && defendable(c.x, c.y);	
		});
	}
	
	private boolean defendable(int xi, int yi){
		iPiece piece = get(xi, yi);
		if(piece==null) return false;
			
		Team team = piece.getTeam();
		for(Route r: piece.findRoutes(this, xi, yi)){
			if(r.getDests().stream().filter(d->!checkIFmoved(team, xi, yi, d.x, d.y)).count()>0) return true;			
		}
		return false;
	}
		
	public boolean anyCoord(List<Coord>c, Predicate<Coord> condition){
		return c.stream().filter(condition).count()>0;
	}
	
	public boolean anyInRoute(int xi, int yi, Predicate<Route> condition){
		iPiece piece = pieces[xi][yi];
		if(piece==null) return false;
		
		List<Route> routes = piece.findRoutes(this, xi, yi);
		return routes.stream().filter(condition).count()>0;
	}
	
	public boolean onDest(int xi, int yi, int xf, int yf){
		return anyInRoute(xi, yi, r->r.isDest(xf, yf));
	}
	
	public boolean onRoute(Coord pieceLoc, Coord point){
		return anyInRoute(pieceLoc.x, pieceLoc.y, r->r.onRoute(point));
	}

	private boolean anyPiece(BiFunction<iPiece, Coord, Boolean> filter){
		for(int y=0;y<height; y++)
			for(int x=0;x<width; x++){
				
				iPiece piece = pieces[x][y]; 
				
				if(piece!=null && filter.apply(piece, new Coord(x,y))){
					return true;
				}
			}
		return false;
	}
	
	public Team turn(){
		return teams[t];
	}
	
	public void next(){
		t++;
		t%=teams.length;
	}

	private boolean checkIFmoved(Team team, int xi, int yi, int xf, int yf){
		iPiece toMove = pieces[xi][yi];
		iPiece abg = pieces[xf][yf];
		
		pieces[xf][yf] = pieces[xi][yi];
		pieces[xi][yi] =null;
		
		boolean result = check(team);
		
		pieces[xf][yf] = abg;
		pieces[xi][yi] = toMove;
		
		return result;
	}
	
	public boolean move(int xi, int yi, int xf, int yf){		
		if(!canMoveTo(xi, yi, xf, yf)) return false;
		
		pieces[xf][yf] = pieces[xi][yi];
		pieces[xi][yi] =null;
		next();
		return true;
	}
	
	public boolean canMoveTo(int xi, int yi, int xf, int yf){
		iPiece toMove = pieces[xi][yi];		
		if(toMove==null) return false;
		
		if(!onDest(xi, yi, xf, yf)) return false;
		
		return !checkIFmoved(toMove.getTeam(), xi, yi, xf, yf);
	}
	
	public boolean onBoard(int x, int y){
		return 0 <= x && x < width && 0 <= y && y < height; 
	}
	
	public boolean isThereStone(int x, int y){
		return pieces[x][y]!=null;
	}
	
	private void formate(Formation formation){
		for(Coord c : formation.meta().keySet()){
			MetaPieceSet meta = formation.meta().get(c);
			iPiece p = Pieces.castFromSymbol(meta.symbol, teams[meta.team]);
			
			pieces[c.x][c.y] = p;
		}
	}
	
	public boolean isCastle(int x, int y){
		return castles.values().stream().filter(coord -> coord.x==x && coord.y==y).count() == 1;
	}
	
	private void setCastles(){
		double xr = tile_row - center[0] - 1;
		double yr = tile_column - center[1] -1;		
	
		Polygon p = Util.getNPolygon(center[0], center[1], teams.length, xr, yr);
		for(int i=0; i<teams.length; i++){
			Coord castle = new Coord(p.xpoints[i], p.ypoints[i]);
			
			castles.put(teams[i], castle);
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
	
	public boolean onADiagonal(Coord...coords){
		for(Coord[] line: diagonals()){ 
			Coord c1 = line[0];
			Coord c2 = line[1];
	
			boolean lineI = true;
			for(Coord c : coords){
				if(c1.equals(c) || c2.equals(c)) continue;
					
				if(!c.between(c1, c2) || c1.slope(c)!=c.slope(c2)){
					lineI = false; break;
				}
			}
			if(lineI){
				System.out.println("sdsd");
				return true;
			}
		}
		return false;
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
