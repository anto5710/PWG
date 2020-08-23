package board.Pieces.ChessPieces.Cseries;

import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.CVaultable;
import board.Pieces.condition.Stocker;
import board.Pieces.condition.Striker;
import board.Pieces.condition.Vaultable;
import board.Pieces.condition.Xseries.XCVault;
import board.Pieces.condition.Xseries.XStocker;
import board.Pieces.condition.Xseries.XStriker;
import board.Pieces.condition.Xseries.XVault;
import board.Pieces.movable.FiniteMove;
import board.Pieces.movable.InfiniteMove;

public class CCannon extends InfiniteMove{
	private static final int[][] d2 = {{1,0},{1,0}}; 
	private static final int[][] d1 = {{0,1},{0,1}};
	
	private static final int[][] dd1 = {{1,1},{1,1}};
	private static final int[][] dd2 = {{1,-1},{1,-1}};
	
	
	private final int [][]cd1  = {{1,0}};
	private final int [][]cd2  = {{0,1}};
	private final int [][]cdd1  = {{1,1}};
	private final int [][]cdd2  = {{1,-1}};
	
	
	
	public CCannon(Team team) {
		super(Pieces.CANNON, team);
		addGeneralActions(new CVaultable(this), d1, d2);
		addGeneralActions(new Stocker(this), cd1, cd2);
	}

}
