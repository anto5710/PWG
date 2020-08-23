package board.Pieces.movable;


import board.ADual;
import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Route;
import board.Pieces.condition.Strider;
import board.Pieces.condition.iRouteCondition;


public class InfiniteMove extends GeneralAction {

	public InfiniteMove(Pieces pclass, Team team) {
		super(pclass, team);
	}
	
	@Override
	public Route findRoute(ADual game, Route route, Coord cur, Coord[] move, iRouteCondition c) {
		route.destN=0;
		return findRoute(game,route,cur, move,c,false);
	}
	
	public Route findRoute(ADual game, Route route, Coord cur, Coord[] move, iRouteCondition c, boolean isDest) {
		int i = route.index()%move.length;
		cur = cur.add(move[i]);
		
		int state = Strider.condize(game.onBoard(cur));
		if(move.length<=1){
			isDest = true;
		}		
		
		if(state!=GeneralAction.FAILURE){
			if(!isDest){
				state *= c.canBePass(game, cur, route.origin);	
			}else{
				state *= c.canBeDest(game, cur, route.origin);		
			}
		}
		
		if(state == GeneralAction.FAILURE){
			return route.destN==0? null : route;
		}
		
		if(state != GeneralAction.PASS){
			route.addCoord(cur);
			if(isDest) route.plusDest();
		}
		
		if(state==GeneralAction.COMPLETE){
			if(isDest){
				return route;
			}
			
			isDest = true;
			route.destN = 0;				
		}

		return findRoute(game, route, cur, move, c, isDest);
	}
}
