package board;

import java.io.File;
import java.util.List;
import java.util.Map;

import board.Pieces.Pieces;
import board.Pieces.iPiece;
import ui.Assert;

public abstract class ADual {
	public int tile_row, tile_column; //칸
	public int width, height;  // 좌표
	public iPiece[][] pieces;
	public Team []teams;
	public int nthTeam=0;
	
	public final double [] center; //배열의 좌표 아님
	
	/*
	 * 최대 4팀까지
	 */
	public ADual (Formation formation, Team... teams) {
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
	
	public ADual (iPiece[][] p, Team[] teams) {		
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
	
	public boolean onBoard(int x, int y){
		return 0 <= x && x < width && 0 <= y && y < height; 
	}
	
	public boolean isThereStone(int x, int y){
		return pieces[x][y]!=null;
	}
	
	public iPiece get(Coord to) {
		return get(to.x, to.y);
	}
	
	public boolean onBoard(Coord coord) {
		return onBoard(coord.x, coord.y);
	}

	public boolean isThereStone(Coord coord) {
		return isThereStone(coord.x, coord.y);
	}
	
	public iPiece get(int x, int y) {
		return pieces[x][y];
	}
	
	public Team turn(){
		return teams[nthTeam];
	}
	
	public void next(){
		nthTeam++;
		nthTeam%=teams.length;
	}
	
	protected abstract void formate(Formation formation);
	public abstract boolean trymove(int xi, int yi, int xf, int yf);

	public abstract boolean onRoute(Coord focused_coord, Coord pointing);

	public abstract boolean check(Team team) ;

	public abstract boolean checkmate(Team team) ;

	public abstract Status getStatus(Team team);

	public abstract Map<Team, Coord> castles();

	public abstract List<Coord[]> diagonals();

	public abstract boolean inCastle(Team team, Coord to);

	public abstract boolean onADiagonal(Coord...origin);
}
