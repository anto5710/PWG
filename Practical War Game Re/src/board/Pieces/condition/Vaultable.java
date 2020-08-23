package board.Pieces.condition;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.ChessPieces.Cannon;
import board.Pieces.movable.GeneralAction;

public class Vaultable extends Strider{

	public Vaultable(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBePass(ADual game, Coord to, Coord origin) {
		iPiece pAt = game.get(to);
		
		if(pAt==null) return GeneralAction.CONTINUE;
		if(pAt instanceof Cannon) return GeneralAction.FAILURE;
		
		return GeneralAction.COMPLETE;
	}
	
	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		iPiece target = game.get(to);
		
		if(target==null) return GeneralAction.CONTINUE;
		
		if(target.sameTeam(piece) || target instanceof Cannon) return GeneralAction.FAILURE;
		
		return GeneralAction.COMPLETE;
	}
}
