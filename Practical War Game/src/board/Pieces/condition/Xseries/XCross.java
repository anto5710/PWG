package board.Pieces.condition.Xseries;


import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.condition.CastleDweller;

public class XCross extends CastleDweller{

	public XCross(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		return condize(game.isCastle(origin) || game.isCastle(to)) * super.canBeDest(game, to, origin);
	}
	
}
