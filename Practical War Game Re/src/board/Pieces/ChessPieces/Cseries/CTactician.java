package board.Pieces.ChessPieces.Cseries;

import board.Team;

import board.Pieces.Pieces;
import board.Pieces.condition.CastleDweller;

import board.Pieces.condition.Xseries.XStrider;
import board.Pieces.movable.FiniteMove;

public class CTactician extends FiniteMove {
	private static final int[][] d1 = {{1,0}};
	private static final int[][] d2 = {{0,1}};
	private static final int[][] dd1 = {{1,1}};
	private static final int[][] dd2 = {{-1,1}};
	public CTactician(Team team) {
		super(Pieces.TACTICIAN, team);
//		addGeneralActions(new CastleDweller(this), d1, d2);
		addGeneralActions(new XStrider(this), dd1, dd2);
	}

}
