package board.Pieces.ChessPieces.Cseries;

import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.StigianStrider;
import board.Pieces.condition.Strider;
import board.Pieces.movable.FiniteMove;

public class CElephant extends FiniteMove{
	private final static int[][] d1 = {{1,1},{1,1}};
//	private final static int[][] d2 = {{1,1},{1,1}};
	
	public CElephant(Team team) {
		super(Pieces.ELEPHANT, team);
		addGeneralActions(new StigianStrider(this), d1);
	}
}
