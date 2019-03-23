package board.Pieces.ChessPieces;


import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Strider;
import board.Pieces.condition.Striker;
import board.Pieces.condition.Xseries.XStriker;
import board.Pieces.movable.InfiniteMove;

public class Chariot extends InfiniteMove{
	private final int [][]d1  = {{1,0}};
	private final int [][]d2  = {{0,1}};
	private final int [][]dd1  = {{1,1}};
	private final int [][]dd2  = {{1,-1}};
	
	public Chariot(Team team) {
		super(Pieces.CHARIOT, team);
		addGeneralActions(new Striker(this), d1, d2);
		addGeneralActions(new XStriker(this), dd1, dd2);
	}
}


