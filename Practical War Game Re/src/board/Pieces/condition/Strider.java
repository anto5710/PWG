package board.Pieces.condition;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.movable.GeneralAction;

public class Strider implements iRouteCondition{
	protected final iPiece piece;
	public Strider(iPiece piece) {
		this.piece = piece;
	}
	
	@Override
	public int canBePass(ADual game, Coord to, Coord origin) {
		return condize(!game.isThereStone(to));
	}	

	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		iPiece target = game.get(to);
		return condize(target==null || !piece.sameTeam(target));
	}
	
	/*
	 * stands for conditionize, transfering boolean to int condition
	 */
	public static int condize(boolean result){
		return result? GeneralAction.CONTINUE : GeneralAction.FAILURE;
	}
}
