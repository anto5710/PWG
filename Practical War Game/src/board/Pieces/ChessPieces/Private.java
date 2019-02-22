package board.Pieces.ChessPieces;

import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Unrecedable;
import board.Pieces.condition.XCross;
import board.Pieces.movable.FiniteMove;

public class Private extends FiniteMove{
	protected static final int[][] d1 = {{0,1}};
	protected static final int[][] d2 = {{1,0}};
	private static final int[][] dd1 = {{-1,1}};
	private static final int[][] dd2 = {{0,1}};
	
	public Private(Team team) {
		super(Pieces.PRIVATE, team);
		addGeneralMoves(new Unrecedable(this), d1, d2);
		addGeneralMoves(new XCross(this), dd1, dd2);
	}
	
}
