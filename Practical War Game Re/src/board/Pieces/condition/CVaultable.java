package board.Pieces.condition;



import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.ChessPieces.Cannon;
import board.Pieces.movable.GeneralAction;

public class CVaultable extends Strider{

	public CVaultable(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBePass(ADual game, Coord to, Coord origin) {
		iPiece pAt = game.get(to);
		
		if(pAt==null) return GeneralAction.CONTINUE;
		
		return GeneralAction.COMPLETE;
	}
	
	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		iPiece target = game.get(to);
		
		if(target==null) return GeneralAction.PASS;
		
		if(target.sameTeam(piece) || target instanceof Cannon) return GeneralAction.FAILURE;
		
		return GeneralAction.COMPLETE;
	}
}
