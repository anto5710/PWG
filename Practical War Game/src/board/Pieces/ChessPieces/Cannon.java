package board.Pieces.ChessPieces;

import board.Team;
import board.Pieces.Pieces;
import board.Pieces.movable.InfiniteMove;


public class Cannon extends InfiniteMove {

	public Cannon(Team team) {
		super(Pieces.CANNON, team);
	}


}
