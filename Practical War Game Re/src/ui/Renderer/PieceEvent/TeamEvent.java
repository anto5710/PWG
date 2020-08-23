package ui.Renderer.PieceEvent;

import board.ADual;
import board.Shogi;
import board.Team;

public class TeamEvent{
	public final ADual game;
	public final Team team;
	public TeamEvent(ADual game2, Team team) {
		this.game = game2;
		this.team = team;
	}
	
	public boolean check(){
		return game.check(team);
	}
	
	public boolean checkmate(){
		return game.checkmate(team);
	}
}
