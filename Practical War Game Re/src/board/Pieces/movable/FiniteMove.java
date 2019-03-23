package board.Pieces.movable;

import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Route;
import board.Pieces.condition.iRouteCondition;

public class FiniteMove extends GeneralAction{

	public FiniteMove(Pieces pclass, Team team) {
		super(pclass, team);
	}
	
	public Route findRoute(Shogi game, Route route, Coord cur, Coord[]move, iRouteCondition c){
		cur = cur.move(move[route.index()]);
		int state = GeneralAction.CONTINUE;
		boolean isDest = route.index()==move.length-1;
		
		if(!game.onBoard(cur)) return null;
		
		if(isDest){
			state *= c.canBeDest(game, cur, route.origin);
		}else{
			state *= c.canBePass(game, cur, route.origin);
		}
			
		if(state == GeneralAction.FAILURE){
			return null;
		}
		route.addCoord(cur);
		
		if(isDest) return route;
		return findRoute(game, route, cur, move, c);
	}
}
