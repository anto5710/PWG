package board.Pieces.condition;

import board.ADual;
import board.Coord;
import board.Shogi;

public interface iRouteCondition {
	
	public int canBePass(ADual game, Coord to, Coord origin);
	public int canBeDest(ADual game, Coord to, Coord origin);
}
