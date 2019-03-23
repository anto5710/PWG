package board.Pieces.condition;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.ChessPieces.Cannon;
import board.Pieces.movable.GeneralAction;

public class Striker extends Strider{

	public Striker(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		iPiece target = game.get(to);
		
		if(target==null) return GeneralAction.CONTINUE;
		
		if(target.sameTeam(piece)) return GeneralAction.FAILURE;
		
		return GeneralAction.COMPLETE;
	}
}
