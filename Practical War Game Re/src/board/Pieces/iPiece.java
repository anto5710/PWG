package board.Pieces;

import java.util.List;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.condition.Route;

public interface iPiece {
	public Team getTeam();
	public boolean sameTeam(iPiece piece);
	public Pieces getPClass();
	public List<Route> findRoutes(ADual game, int x, int y);
	public List<Route> findRoutes(ADual game, Coord coord);
}
