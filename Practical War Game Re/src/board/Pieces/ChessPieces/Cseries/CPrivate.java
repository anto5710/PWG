package board.Pieces.ChessPieces.Cseries;


import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Undivergable;
import board.Pieces.condition.Unrecedable;
import board.Pieces.condition.Xseries.XStrider;
import board.Pieces.movable.FiniteMove;

public class CPrivate extends FiniteMove{
	protected static final int[][] d1 = {{0,1}};
	protected static final int[][] d2 = {{1,0}};
	private static final int[][] dd1 = {{-1,1}};
	private static final int[][] dd2 = {{0,1}};
	
	public CPrivate(Team team) {
		super(Pieces.PRIVATE, team);
		addGeneralActions(new Undivergable(this), d1, d2);
		addGeneralActions(new XStrider(this), dd1, dd2);
	}
	
}
