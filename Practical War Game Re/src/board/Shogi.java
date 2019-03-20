package board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import board.Pieces.MetaPieceSet;
import board.Pieces.Pieces;

import board.Pieces.iPiece;
import board.Pieces.ChessPieces.King;
import board.Pieces.condition.Route;
import ui.Assert;

public class Shogi {
	public final int tile_row, tile_column; //칸
	public final int width, height;  // 좌표
	public iPiece[][] pieces;
	
	private Map<Team, Coord> castles = new HashMap<>();
	public final double [] center; //배열의 좌표 아님
	private Team []teams;
	private int nthTeam=0;
	
	
	/*
	 * 대각선을 나타내는 리스트, 각각 크기가 2인 1차원배열로 하나의 대각선을 나타낸다.
	 */
	private List<Coord[]> diagonals = new ArrayList<>();
	
	
	/*
	 * 최대 4팀까지
	 */
	public Shogi(Formation formation, Team... teams) {
		Assert.throwIF(teams.length>4, "No more than 4 teams");
		
		this.width = formation.W;
		this.height = formation.H;
		this.tile_row = width-1;
		this.tile_column = height-1;
		
		double[] centerCoord = {tile_row/2D,tile_column/2D};
		center = centerCoord;
		pieces = new iPiece[width][height];

		this.teams = teams;
		nthTeam=0;
//		setCastles();
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
		
		nthTeam=0;
//		setCastles();
//		System.out.println(width + "d2 "+ height);
		this.pieces = p;
	}

	public Status getStatus(Team team){
		if(!castles.containsKey(team)) return null;
		
		if(check(team)){
			return checkmate(team) ? Status.CHECKMATE : Status.CHECK;
		}
		return Status.NORMAL;
	}
	
	private Coord findKing(Team team){
		for(Coord c : getCastle(team).squareRange(1)){
			iPiece p = pieces[c.x][c.y];
			if(p instanceof King && team.in(p)) return c;
		}
		return null;
	}
	
	public boolean check(Team team){
		Coord king = findKing(team);
		if(king==null) return false;
	
		return anyPiece((p,pLoc)-> !team.in(p) && onDest(pLoc.x, pLoc.y, king.x, king.y));
	}
	
	public boolean checkmate(Team team){
		if(!check(team)) return false;
		return !anyPiece((p,pLoc)-> team.in(p) && defendable(pLoc.x, pLoc.y));
	}
	
	private boolean defendable(int xi, int yi){
		iPiece piece = get(xi, yi);
		if(piece==null) return false;
			
		Team team = piece.getTeam();
		return anyInRoute(xi, yi, r-> r.anyDests
											(d->!checkIFmoved(team, xi, yi, d.x, d.y)));
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
		return teams[nthTeam];
	}
	
	public void next(){
		nthTeam++;
		nthTeam%=teams.length;
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
	
	public boolean trymove(int xi, int yi, int xf, int yf){		
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
			if(p instanceof King){ 
				setCastle(p.getTeam(), c);
			}
		}
	}

//	private void setCastles(){
//		double xr = center[0] - 1;
//		double yr = center[1] -1;		
//	
//		Polygon p = Util.getNPolygon(center[0], center[1], teams.length, xr, yr);
//		for(int i=0; i<teams.length; i++){
//			Coord castle = new Coord(p.xpoints[i], p.ypoints[i]);
//			
//			setCastle(teams[i], castle);
//		}
//	}
	
	private void setCastle(Team team, Coord castle) {
		if(castle!=null && Arrays.asList(teams).contains(team)) {
			System.out.println("castle" + team);
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
	
	public boolean onADiagonal(Coord...coords){
		nextDiagonal: for(Coord[] line: diagonals()){ 
			Coord c1 = line[0];
			Coord c2 = line[1];
	
			for(Coord c : coords){
				if(!c.between(c1, c2) || !Coord.onALine(c1, c, c2)){					
					continue nextDiagonal;
				}
			}
			return true;
		}
		return false;
	}
		
	public Map<Team, Coord> castles(){
		return castles;
	}
	
	public iPiece get(int x, int y) {
		return pieces[x][y];
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

	public Coord getCastle(Team team){
		return castles.get(team);
	}
	
	public boolean inCastle(Team team, Coord to) {
		Coord castle = getCastle(team);
		return castle!=null && castle.squareRange(1).contains(to);
	}
}
