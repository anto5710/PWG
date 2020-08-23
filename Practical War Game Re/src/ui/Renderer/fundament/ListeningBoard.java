package ui.Renderer.fundament;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import board.ADual;
import board.Coord;
import board.Formation;
import board.Shogi;
import board.Team;
import board.Pieces.iPiece;
import ui.Assert;
import ui.Renderer.PieceEvent.GameListener;
import ui.Renderer.PieceEvent.PieceListener;
import ui.Renderer.PieceEvent.PieceMoveEvent;
import ui.Renderer.PieceEvent.TeamEvent;


public abstract class ListeningBoard extends JPanel{
	public ADual game;

	private List<PieceListener> pieceLs = new ArrayList<>();
	private List<GameListener> gameLs = new ArrayList<>();
	
	public ListeningBoard(ADual game) {
		this.game = game;
	}
		
	public void tryMove(Coord from, Coord to){
		iPiece target = game.get(to);
		iPiece toMove = game.get(from);

		boolean roadKill = game.isThereStone(to);
		boolean moved = game.trymove(from.x, from.y, to.x, to.y);

		if(moved){
			PieceMoveEvent e = new PieceMoveEvent(game, toMove, to, target, from);
			
			if(roadKill){
				pieceLs.forEach(l->l.pieceKilled(e));
			}else pieceLs.forEach(l->l.pieceMoved(e));
			repaint();
		}
		
		updateTideOfWar();
	}
	
	public void updateTideOfWar(){
		Team turn = game.turn();
		TeamEvent e = new TeamEvent(game, turn);
		if(game.check(turn)){
			gameLs.forEach(l->l.check(e));
			
			if(game.checkmate(turn)) gameLs.forEach(l->l.checkmate(e));
		}else{
			gameLs.forEach(l->l.defended(e));
		}
	}
	
	public void addPieceListener(PieceListener listener){
		if(listener!=null) pieceLs.add(listener);
	}
	
	public void addGameListener(GameListener listener){
		if(listener!=null) gameLs.add(listener);
	}
	
	
}