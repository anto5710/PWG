package board.Pieces.movable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import board.Coord;
import board.Team;
import board.Pieces.AbstractPiece;
import board.Pieces.Pieces;
import board.Pieces.condition.iRouteCondition;

public abstract class GeneralMove extends AbstractPiece{
	protected final Map<Coord[], iRouteCondition> moves = new HashMap<>();
	private final int [][] rotations = {{1,1},{-1,-1},{1,-1},{-1,1}};
									// 원방향, 대칭, 수평대칭, 수직대칭
	
	public GeneralMove(Pieces pclass, Team team) {
		super(pclass, team);
	}

	public void addGeneralMoves(iRouteCondition c, int[][]...init_moves){
		if(init_moves==null) return;
		
		for(int [][] move : init_moves){
			addGeneralMove(c, Coord.listfrom2D(move));
		}
	}
	
	public void addGeneralMove(iRouteCondition c, Coord[]init_move){
		for(int[]rot : rotations){
			Coord[] newMove = Coord.multiplyAll(init_move, rot);			
			addMove(newMove, c);
		}
	}
	
	public void addMove(Coord[]move, iRouteCondition condition){
		for(Coord[]move1: moves.keySet()){
			if(Arrays.equals(move, move1)) return;
		}
		moves.put(move, condition);
	}
}
