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
		return findRoutes(game,route,cur, move, c,false);
	}
	
	public Route findRoutes(Shogi game, Route route, Coord cur, Coord[] move, iRouteCondition c, boolean isDest) {
		int i = route.index()%move.length;
		
		cur = cur.move(move[i]);
		
		int state = Strider.CONTINUE;
		if(move.length<=1){
			isDest = true;
		}
		
		if(!game.onBoard(cur)){
			if(!isDest) return null;
			
			return route.destN==0? null : route;
		}
		

		if(!isDest){
			state *= c.canBePass(game, cur, route.origin);
			
			
		
		}else{
			state *= c.canBeDest(game, cur, route.origin);
			
			if(state!=Strider.FAILURE){
				route.destN++;
			}
		}
		
		if(state == Strider.FAILURE){
			return isDest? route : null;
		}
		
		route.addCoord(cur);
		
		if(state==Strider.COMPLETE ){
			if(!isDest){
				isDest = true;
				route.destN = 0;
			}else{
				return route;
			}
			System.out.println(cur.x + ", " + cur.y);
		}

		
		
		return findRoutes(game, route, cur, move, c, isDest);
	}
	
}
