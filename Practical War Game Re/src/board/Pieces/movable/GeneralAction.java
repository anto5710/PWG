package board.Pieces.movable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.AbstractPiece;
import board.Pieces.Pieces;
import board.Pieces.condition.Route;
import board.Pieces.condition.iRouteCondition;
import ui.Assert;

public abstract class GeneralAction extends AbstractPiece{
	private final Map<Coord[], iRouteCondition> actions = new HashMap<>();
	private final int [][] rotations = {{1,1},{-1,-1},{1,-1},{-1,1}};
									// 원방향, 대칭, 수평대칭, 수직대칭

	public final static int FAILURE = 0;
	public final static int CONTINUE= 1;
	public final static int COMPLETE = 2;
	
	public GeneralAction(Pieces pclass, Team team) {
		super(pclass, team);
	}

	public void addGeneralActions(iRouteCondition c, int[][]...src_actions){
		if(src_actions==null) return;
		
		for(int [][] action : src_actions){
			addGeneralAction(c, Coord.listfrom2D(action));
		}
	}
	
	public void addGeneralAction(iRouteCondition c, Coord[]src_action){
		Assert.throwIF(src_action==null, "The action of a piece cannot be null");
		
		for(int[]rot : rotations){
			Coord[] newaction = Coord.multiplyAll(src_action, rot);			
			addAction(newaction, c);
		}
	}
	
	public void addAction(Coord[]action, iRouteCondition condition){
		if(containsAction(action)) return;
		actions.put(action, condition);
	}
	
	private boolean containsAction(Coord[]action){
		if(action==null) return false;
		
		for(Coord[]action1: actions.keySet()){
			if(Arrays.equals(action, action1)) return true; 
		}
		return false;
	}
	
	@Override
	public final List<Route> findRoutes(Shogi game, Coord origin) {	
		List<Route> routes = new ArrayList<>();
		
//		System.out.println("size: "+ actions.keySet().size());
		for(Coord [] action : actions.keySet()){
			iRouteCondition condition = actions.get(action);
			Route r = findRoute(game, new Route(origin), origin, action, condition);
			if(r!=null) 
				routes.add(r);
		}
		return routes;
	}
	
	public abstract Route findRoute(Shogi game, Route route, Coord cur, Coord[]action, iRouteCondition c);
}
