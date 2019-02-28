package board.Pieces.movable;


import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Route;
import board.Pieces.condition.Strider;
import board.Pieces.condition.iRouteCondition;


public class InfiniteMove extends GeneralMove {

	public InfiniteMove(Pieces pclass, Team team) {
		super(pclass, team);
	}
	
	@Override
	public Route findRoute(Shogi game, Route route, Coord cur, Coord[] move, iRouteCondition c) {
		route.destN=0;
		return findRoutes(game,route,cur, move,c,false);
	}
	
//	public Route findRoute(Shogi game, Route route, Coord cur, Coord[] move, iRouteCondition c, boolean isDest) {
//		int i = route.index()%move.length;
//		cur = cur.move(move[i]);
//		
////		boolean isDest = route.destN > 0;
//		
//		int state = Strider.condize(game.onBoard(cur)); 
//		
//		if(state!=Strider.FAILURE){
//			if(isDest){
//				state *= c.canBeDest(game, cur, route.origin);
//			} else {
//				state *= c.canBePass(game, cur, route.origin);
//			}		
//		}	
//		
//		if(state == Strider.FAILURE){
//			return isDest ? route : null;
//			
//		}
//		route.addCoord(cur);
//		
//		if(state==Strider.COMPLETE){
//			if(isDest) return route;
//				
//			route.plusDest();
//		}
//		return findRoutes(game, route, cur, move, c, isDest);
//	}
	
	public Route findRoutes(Shogi game, Route route, Coord cur, Coord[] move, iRouteCondition c, boolean isDest) {
		int i = route.index()%move.length;
		cur = cur.move(move[i]);
		
		int state = Strider.condize(game.onBoard(cur));
		if(move.length<=1){
			isDest = true;
		}		
		if(state!=Strider.FAILURE){
			if(!isDest){
				state *= c.canBePass(game, cur, route.origin);	
			}else{
				state *= c.canBeDest(game, cur, route.origin);		
			}
		}
		
		if(state == Strider.FAILURE){
			return route.destN==0? null : route;
		}
		
		route.addCoord(cur);
		if(isDest) route.plusDest();
		
		if(state==Strider.COMPLETE){
			if(isDest){
				return route;
			}
			
			isDest = true;
			route.destN = 0;				
		}

		return findRoutes(game, route, cur, move, c, isDest);
	}
}
