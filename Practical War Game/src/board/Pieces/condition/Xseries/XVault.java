package board.Pieces.condition;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class XVault extends Vaultable{

	public XVault(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(Shogi game, Coord to, Coord origin) {
		int castle = condize(game.isCastle(origin) || game.isCastle(to));
		return super.canBeDest(game, to, origin) * castle;
	}

}
