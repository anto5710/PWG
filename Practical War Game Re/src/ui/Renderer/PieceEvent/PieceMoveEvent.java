package ui.Renderer.PieceEvent;

import board.ADual;
import board.Coord;
import board.Shogi;
import board.Pieces.iPiece;

public class PieceMoveEvent extends PieceEvent{
	public final iPiece target;
	public final Coord from;
	
	public PieceMoveEvent(ADual game, iPiece piece, Coord to, iPiece target, Coord from){
		super(game, piece, to);
		this.from = from;
		this.target = target;
	}
}
