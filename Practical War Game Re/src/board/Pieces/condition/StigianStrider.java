package board.Pieces.condition;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class StigianStrider extends Strider{

	public StigianStrider(iPiece piece) {
		super(piece);
	}

	@Override
	public int canBePass(ADual game, Coord to, Coord origin) {
		double cy = game.center[1];

		double to_dy = to.y - cy;
		double or_dy = origin.y - cy;
		
		int oppD = condize(to_dy * or_dy >= 0);

		return oppD * super.canBePass(game, to, origin);
	}
	
	

	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		double cx = game.center[0], cy = game.center[1];

		double to_dy = to.y - cy;
		double or_dy = origin.y - cy;
		
		int oppD = condize(to_dy * or_dy >= 0);

		return oppD * super.canBeDest(game, to, origin);
	}

	
}
