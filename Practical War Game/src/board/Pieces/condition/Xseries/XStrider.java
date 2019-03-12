package board.Pieces.condition.Xseries;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.condition.Strider;

public class XStrider extends Strider{

	public XStrider(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		return condize(game.onADiagonal(origin, to)) * super.canBeDest(game, to, origin);
	}
	
}
