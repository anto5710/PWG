package board.Pieces.ChessPieces;

import board.Team;

import board.Pieces.Pieces;
import board.Pieces.condition.CastleDweller;

import board.Pieces.condition.Xseries.XStrider;
import board.Pieces.movable.FiniteMove;

public class Tactician extends FiniteMove {
	private static final int[][] d1 = {{1,0}};
	private static final int[][] d2 = {{0,1}};
	private static final int[][] dd1 = {{1,1}};
	private static final int[][] dd2 = {{-1,1}};
	public Tactician(Team team) {
		super(Pieces.TACTICIAN, team);
		addGeneralMoves(new CastleDweller(this), d1, d2);
		addGeneralMoves(new XStrider(this), dd1, dd2);
	}

}
