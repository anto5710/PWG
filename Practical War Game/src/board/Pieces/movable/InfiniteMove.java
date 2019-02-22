package board.Pieces.movable;

import java.util.ArrayList;

import java.util.List;

import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.Route;

public abstract class InfiniteMove extends GeneralMove {

	public InfiniteMove(Pieces chariot, Team team) {
		super(chariot, team);
	}

	@Override
	public List<Route> findRoutes(Shogi game, Coord origin) {
		List<Route> routes = new ArrayList<>();
		
		return routes;
	}
}
