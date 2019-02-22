package board.Pieces.movable;

import java.util.ArrayList;
import java.util.List;
import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.Route;
import board.Pieces.condition.Strider;
import board.Pieces.condition.iRouteCondition;

public abstract class FiniteMove extends GeneralMove{

	public FiniteMove(Pieces pclass, Team team) {
		super(pclass, team);
	}
	
	@Override
	public final List<Route> findRoutes(Shogi game, Coord origin) {	
		List<Route> routes = new ArrayList<>();
		
		
		System.out.println("size: "+ moves.keySet().size());
		for(Coord [] move : moves.keySet()){
			Route r = findRoutes(game, new Route(origin), origin, move, moves.get(move));
			if(r!=null) 
				routes.add(r);
		}
		return routes;
	}
	
	public Route findRoutes(Shogi game, Route route, Coord cur, Coord[]move, iRouteCondition c){
		cur = cur.move(move[route.index()]);
		int state = Strider.CONTINUE;
		boolean isLast = route.index()==move.length-1;
		
		if(!game.onBoard(cur)) return null;
		
		if(isLast){
			state *= c.canBeDest(game, cur, route.origin);
		}else{
			state *= c.canBePass(game, cur, route.origin);
		}
			
		if(state == Strider.FAILURE){
			return null;
		}
		route.addCoord(cur);
		
		if(isLast) return route;
		return findRoutes(game, route, cur, move, c);
	}
}
