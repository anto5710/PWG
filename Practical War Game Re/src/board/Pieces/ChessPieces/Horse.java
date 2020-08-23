package board.Pieces.ChessPieces;



import board.Team;
import board.Pieces.Pieces;
import board.Pieces.condition.Strider;
import board.Pieces.movable.FiniteMove;

public class Horse extends FiniteMove{
	private static final int[][] d1 = {{0,1},{1,1}}, 
								 d2 = {{1,0},{1,1}};			
						
	public Horse(Team team) {
		super(Pieces.HORSE, team);
		addGeneralActions(new Strider(this), d1, d2);		
	}
}
