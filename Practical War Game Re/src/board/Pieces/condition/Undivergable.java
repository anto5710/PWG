package board.Pieces.condition;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class Undivergable extends Unrecedable{

	public Undivergable(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		double cy = game.center[1];
		Coord castle = game.castles().get(piece.getTeam());
		
		Coord vec = to.subtract(origin);
		
		double to_dy = to.y - cy;
		double or_dy = castle.y - cy;
		
		boolean crossRiver = to_dy * or_dy < 0;
		
		int oppD = condize(vec.x == 0 || crossRiver);

		
			
		return oppD * super.canBeDest(game, to, origin);
	}
}
