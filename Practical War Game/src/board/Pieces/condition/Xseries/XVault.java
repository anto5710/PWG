package board.Pieces.condition.Xseries;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.condition.Vaultable;

public class XVault extends Vaultable{

	public XVault(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		int castle = condize(game.onADiagonal(to, origin));
		return super.canBeDest(game, to, origin) * castle;
	}

}
