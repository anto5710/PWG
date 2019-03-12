package ui.Renderer.PieceEvent;

import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class PieceEvent{
	public final iPiece piece;
	public final Coord coord;
	public final Shogi game;
	public PieceEvent(Shogi game, iPiece piece, Coord coord){
		this.piece = piece;
		this.coord = coord;
		this.game = game;
	}
	
	
}
