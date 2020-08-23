package board.Pieces.movable;

import java.util.List;

import board.Coord;
import board.Shogi;
import board.Pieces.condition.Route;

public interface Movable {
	public List<Route> findRoutes(Shogi game, Coord origin);	
	
}
