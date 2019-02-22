package board.Pieces.condition;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class CastleDweller extends Strider{

	public CastleDweller(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		int state = condize(game.inCastle(to));
		return state * super.canBeDest(game, to, origin);
	}

	@Override
	public int canBePass(Shogi game, Coord to, Coord origin) {
		int state = condize(game.inCastle(to));
		return state * super.canBePass(game, to, origin);
	}
}
