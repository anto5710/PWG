package board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import board.Pieces.AbstractPiece;
import board.Pieces.MetaPieceSet;
import board.Pieces.Pieces;

import board.Pieces.iPiece;
import board.Pieces.ChessPieces.Cannon;
import board.Pieces.ChessPieces.Chariot;
import board.Pieces.ChessPieces.Elephant;
import board.Pieces.ChessPieces.Horse;
import board.Pieces.ChessPieces.King;
import board.Pieces.ChessPieces.Private;
import board.Pieces.ChessPieces.Tactician;
import board.Pieces.ChessPieces.Cseries.CElephant;
import board.Pieces.condition.Route;
import ui.Assert;
import ui.Renderer.PieceEvent.GameListener;
import ui.util.Util;

public class Shogi extends ADual {
	static Map<Pieces, Class<? extends iPiece>> AIs = new HashMap<>();
	


	
	public Shogi(Formation formation, Team[]teams) {
		super(formation, teams);
	}
	
	public Shogi(iPiece[][] p, Team[] teams){
		super(p, teams);
	}

	protected Map<Team, Coord> castles;
		
	/*
	 * 대각선을 나타내는 리스트, 각각 크기가 2인 1차원배열로 하나의 대각선을 나타낸다.
	 */
	protected List<Coord[]> diagonals;


	public Status getStatus(Team team){
		Assert.throwIF(!castles.containsKey(team), "No such team:"+team); 
		if(check(team)){
			return checkmate(team) ? Status.CHECKMATE : Status.CHECK;
		}
		return Status.NORMAL;
	}
	
	private Coord findKing(Team team){
		Assert.throwIF(getCastle(team)==null, "No Castle Found for team: "+ team.getName());
		
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
		return check(team) && !anyPiece((p,pLoc)-> team.in(p) && defendable(pLoc.x, pLoc.y));
	}
	
	private boolean defendable(int xi, int yi){
		iPiece piece = get(xi, yi);
		if(piece==null) return false;
			
		Team team = piece.getTeam();
		return anyInRoute(xi, yi, r-> r.anyDests
											(d->!checkIFmoved(team, xi, yi, d.x, d.y)));
	}
		
	public boolean anyInRoute(int xi, int yi, Predicate<Route>condition){
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

	@Override
	protected void formate(Formation formation){
		 castles = new HashMap<>();
		 diagonals =  new ArrayList<>();;
		AIs.put(Pieces.CANNON, Cannon.class);
		AIs.put(Pieces.CHARIOT, Chariot.class);
		AIs.put(Pieces.ELEPHANT, Elephant.class);
		AIs.put(Pieces.HORSE, Horse.class);
		AIs.put(Pieces.KING, King.class);
		AIs.put(Pieces.PRIVATE, Private.class);
		AIs.put(Pieces.TACTICIAN, Tactician.class);
	
		
		for(Coord c : formation.meta().keySet()){
			MetaPieceSet meta = formation.meta().get(c);
			iPiece p =Pieces.cast(meta, teams, AIs); 			
			pieces[c.x][c.y] = p;
			if(p instanceof King){ 
				setCastle(p.getTeam(), c);
			}
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
		
	protected void setCastle(Team team, Coord castle) {
		if(castle!=null && Arrays.asList(teams).contains(team)) {
			System.out.println("castle" + team);
			castles.put(team, castle);
			addDiagonal(castle.add(1, 1), castle.add(-1, -1));
			addDiagonal(castle.add(-1, 1), castle.add(1, -1));
		}
	}

	public Map<Team, Coord> castles(){
		return castles;
	}
	
	public Coord getCastle(Team team){
		return castles.get(team);
	}
	
	public boolean inCastle(Team team, Coord to) {
		Coord castle = getCastle(team);
		
		for(Coord c : castle.squareRange(1)){
			if(c.equals(to))return castle!=null;
		}
		return false;
	}
}
