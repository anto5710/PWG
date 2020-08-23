package board.Pieces.condition;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class CastleDweller extends Strider{

	public CastleDweller(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBePass(ADual game, Coord to, Coord origin) {
		int state = condize(game.inCastle(piece.getTeam(), to));
		return state * super.canBePass(game, to, origin);
	}

	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		
		int state = condize(game.inCastle(piece.getTeam(), to));
		return state * super.canBeDest(game, to, origin);
	}
}
