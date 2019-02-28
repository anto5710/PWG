package board.Pieces.condition.Xseries;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.condition.Striker;

public class XStriker extends Striker{

	public XStriker(iPiece piece) {
		super(piece);
	}
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		int castle = condize(game.inCastle(origin) && game.inCastle(to));
		return castle * super.canBeDest(game, to, origin);
	}
	
	
}
