package board.Pieces.ChessPieces;

import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Strider;
import board.Pieces.movable.FiniteMove;


public class Elephant extends FiniteMove {
	private final static int[][] d1 = {{0,1},{1,1},{1,1}};
	private final static int[][] d2 = {{1,0},{1,1},{1,1}};
	
	public Elephant(Team team) {
		super(Pieces.ELEPHANT, team);
		addGeneralActions(new Strider(this), d1, d2);
	}

}
