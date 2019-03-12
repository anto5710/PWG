package board.Pieces.ChessPieces;


import board.Team;
import board.Pieces.Pieces;
import board.Pieces.movable.InfiniteMove;

public class Chariot extends InfiniteMove{

	public Chariot(Team team) {
		super(Pieces.CHARIOT, team);
	}
}
