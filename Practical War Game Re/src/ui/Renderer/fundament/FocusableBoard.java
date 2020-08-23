package ui.Renderer.fundament;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import board.ADual;
import board.Coord;
import board.Formation;
import board.Team;
import board.Pieces.iPiece;
import multi.client.ui.MainFrame;


public class FocusableBoard extends DynamicBoard{
	public final static int NONE = 0;
	public final static int FOCUSED = 1;
	public final static int SELECTED = 2;
	public final static int PREVIEW = 3;
	public final static int MOVING = 4;
	
	public FocusableBoard(ADual game) {
		super(game);

		addMouseListener(new MouseAdapter() {	
			
			@Override
			public void mouseClicked(MouseEvent e) {
				whenBoardClicked(e);
			}
		});
		addMouseMotionListener(new MouseAdapter() {	
			@Override
			public void mouseDragged(MouseEvent e) {
				whenBoardClicked(e);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				whenMouseMoved(e);
			}
		});
	}
	
	protected int stateOf(iPiece piece){
		int state = focusedPiece() == piece ? FOCUSED : NONE;
		return state * (fixed() ? SELECTED : 1);
	}
	
	protected Coord focusedCoord(){
		return focused_coord;
	}
	
	protected boolean fixed(){
		return fixed;
	}
	
	protected iPiece focusedPiece(){
		if(!focusing()) return null;
		
		return game.get(focused_coord);
	}

	private Coord focused_coord;
	private boolean fixed = false;
	
	private void defocus(){
		focused_coord = null;
		repaint();
	}
	
	private void focus(Coord piece){
		focused_coord = piece;
		repaint();
	}
	
	protected boolean focusing(){
		return focused_coord!=null;
	}

	private void whenMouseMoved(MouseEvent e) {
		if(fixed) return;
		Coord pointing = toCoord(e.getX(), e.getY());
		
		//나갔으면
		if(!game.onBoard(pointing)){
			defocus(); return;
		}
			
		if(focusing()){
			if(!pointing.equals(focused_coord) &&
			   !game.onRoute(focused_coord, pointing)){ // went out
				defocus();
			}
		}else if(game.turn().in(game.get(pointing))){ // own's piece is there
			focus(pointing);
		} 
	}
	
	private void whenBoardClicked(MouseEvent e){
		if(!focusing()) return;
		Coord clicked = toCoord(e.getX(), e.getY());
		
		if(fixed){
			fixed = false;	
		}else if(clicked.equals(focused_coord)){
			fixed = true;  
		}
		
		if(MainFrame.INSTANCE.handler == null || game.nthTeam == MainFrame.INSTANCE.team){
			tryMove(focused_coord, clicked);
		}
		repaint();
		System.out.println("moved!");
	}
}