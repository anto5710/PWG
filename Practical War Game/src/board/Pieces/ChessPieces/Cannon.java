package board.Pieces.ChessPieces;

import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Vaultable;
import board.Pieces.condition.Xseries.XVault;
import board.Pieces.movable.InfiniteMove;


public class Cannon extends InfiniteMove {
	private static final int[][] d2 = {{1,0},{1,0}}; 
	private static final int[][] d1 = {{0,1},{0,1}};
	
	private static final int[][] dd1 = {{1,1},{1,1}};
	private static final int[][] dd2 = {{1,-1},{1,-1}};
	public Cannon(Team team) {
		super(Pieces.CANNON, team);
		addGeneralMoves(new Vaultable(this), d1, d2);
		addGeneralMoves(new XVault(this), dd1, dd2);
	}


}
