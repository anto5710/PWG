package ui.Renderer.PieceEvent;

import board.Shogi;
import board.Team;

public class TeamEvent{
	public final Shogi game;
	private final Team team;
	public TeamEvent(Shogi game, Team team) {
		this.game = game;
		this.team = team;
	}
	
	public boolean check(){
		return game.check(team);
	}
	
	public boolean checkmate(){
		return game.checkmate(team);
	}
}
