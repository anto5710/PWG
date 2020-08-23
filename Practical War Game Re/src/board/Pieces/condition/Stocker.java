package board.Pieces.condition;

import board.ADual;
import board.Coord;
import board.Pieces.iPiece;

public class Stocker extends Striker{

	public Stocker(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		return condize(!game.isThereStone(to)) * super.canBeDest(game, to, origin);
	}

}
