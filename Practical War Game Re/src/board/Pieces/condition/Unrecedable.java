package board.Pieces.condition;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class Unrecedable extends Strider{

	public Unrecedable(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		double cx = game.center[0], cy = game.center[1];
		Coord castle = game.castles().get(piece.getTeam());
		
		Coord expected_D = new Coord((int)cx, (int)cy).move(castle.multiply(-1)); 
		Coord actual_D = to.move(origin.multiply(-1));  
	
		Coord result = expected_D.multiply(actual_D.x, actual_D.y);
		int oppD = condize(result.x>=0 && result.y>=0);
		
		return oppD * super.canBeDest(game, to, origin);
	}
	
}
