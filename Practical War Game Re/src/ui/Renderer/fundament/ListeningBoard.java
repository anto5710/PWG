package ui.Renderer.fundament;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

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


public class ListeningBoard extends JPanel{
	protected Shogi game;
	protected boolean centered = true;

	private List<PieceListener> pieceLs = new ArrayList<>();
	private List<GameListener> gameLs = new ArrayList<>();
	
	public ListeningBoard(Formation f, Team...teams) {
		Assert.test(f==null || teams.length>4 || teams.length==0, "Invalid formation or invalid number of teams");
		game = new Shogi(f,teams);
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