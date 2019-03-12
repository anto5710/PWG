package board.Pieces;

import java.util.List;

import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.condition.Route;

public abstract class AbstractPiece implements iPiece{
	
	
	private final Pieces pclass;
	private final Team team;
	
	public AbstractPiece(Pieces pclass, Team team){
		this.pclass = pclass;
		this.team = team;
	}
	
	@Override
	public boolean sameTeam(iPiece piece) {
		return piece!=null && piece.getTeam()==team;
	}
	
	@Override
	public Pieces getPClass() {
		return pclass;
	}
	
	@Override
	public Team getTeam() {
		return team;
	}
	@Override
	public List<Route> findRoutes(Shogi game, int x, int y) {
		return findRoutes(game, new Coord(x,y));
	}
	

}
