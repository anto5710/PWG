package board.Pieces;

import java.util.List;
import java.util.Map;

import board.Coord;
import board.Shogi;
import board.Team;

public interface iPiece {
	public Team getTeam();
	public Pieces getPClass();
	public List<Route> findRoutes(Shogi game, int x, int y);
	public List<Route> findRoutes(Shogi game, Coord coord);
	
}
