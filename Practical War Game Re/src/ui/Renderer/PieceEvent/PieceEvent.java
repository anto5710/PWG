package ui.Renderer.PieceEvent;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class PieceEvent{
	public final iPiece piece;
	public final Coord coord;
	public final ADual game;
	public PieceEvent(ADual game2, iPiece piece, Coord coord){
		this.piece = piece;
		this.coord = coord;
		this.game = game2;
	}
	
	
}
