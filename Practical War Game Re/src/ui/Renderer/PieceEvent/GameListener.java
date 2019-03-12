package ui.Renderer.PieceEvent;

public interface GameListener {
	public void defended(TeamEvent e);
	public void check(TeamEvent e);
	public void checkmate(TeamEvent e);
}
