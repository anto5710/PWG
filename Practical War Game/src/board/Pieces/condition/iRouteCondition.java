package board.Pieces.condition;

import board.Coord;
import board.Shogi;

public interface iRouteCondition {
	
	public int canBePass(Shogi game, Coord to, Coord origin);
	public int canBeDest(Shogi game, Coord to, Coord origin);
}
