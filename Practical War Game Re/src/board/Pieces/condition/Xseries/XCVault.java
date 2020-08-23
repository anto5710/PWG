package board.Pieces.condition.Xseries;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;
import board.Pieces.condition.CVaultable;
import board.Pieces.condition.Vaultable;

public class XCVault extends CVaultable{

	public XCVault(iPiece piece) {
		super(piece);
	}
	
	@Override
	public int canBeDest(ADual game, Coord to, Coord origin) {
		return condize(game.onADiagonal(origin, to)) * super.canBeDest(game, to, origin);
	}

}
