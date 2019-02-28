package ui.Renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;

import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.iPiece;
import board.Pieces.condition.Route;
import ui.Util;

public class GamePanel extends JPanel{
	private final Color BOARD_COLOR = new Color(239, 192, 98);
	private final Color GRID_COLOR = Color.BLACK;
	protected Shogi game;
	
	boolean centered = true;
	
	private Team[]teams = {
			new Team("漢", Color.RED), 
			new Team("楚", Color.BLUE)};
	
	public GamePanel() {
		game = new Shogi(8, 9, teams);
		
		
		
		addMouseListener(new MouseAdapter() {	
			@Override
			public void mouseClicked(MouseEvent e) {
				whenBoardClicked(e);
			}
		});
		addMouseMotionListener(new MouseAdapter() {	
			@Override
			public void mouseMoved(MouseEvent e) {
				whenMouseMoved(e);
			}
		});
	}
	
	private void tryMove(Coord to){
		game.move(focused_piece.x, focused_piece.y, to.x, to.y);
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		renderBoard(g);
		renderGame(g);
		renderRoutes(g);
	}

	public void renderRoutes(Graphics g){
		iPiece piece = focusedPiece();
		if(piece==null) return;
		
		List<Route> routes = piece.findRoutes(game, focused_piece);
	
		for(Route r : routes){
			g.setColor(COLOR_DEST);
			for(Coord pass : r.getDests()){
				int []p = toPixel(pass);
				Util.fillCircleAtCenter(g, p[0], p[1], szI(prev_size));
			}

			g.setColor(COLOR_PASS);
			connectPoints(g, r.getAll());
		}
	}

	public void renderBoard(Graphics g){
		drawBackdrop(g);
		drawGrids(g);
		drawCastles(g);
		drawDiagonals(g);
	}
	
	public void drawBackdrop(Graphics g){
		g.setColor(BOARD_COLOR);
		if(game.check(game.turn())){ 
			g.setColor(Color.RED);
		}
			
		g.fill3DRect(tx(), ty(), boardLength(), boardLength(), true);
	}
	
	public void drawGrids(Graphics g){
		g.setColor(GRID_COLOR);
		drawLinesWithin(g, 0, 0, game.width, game.height, true);
		drawLinesWithin(g, 0, 0, game.width, game.height, false);
	}

	public void drawCastles(Graphics g){
		for(Team team: game.castles().keySet()){
			Coord castle = game.castles().get(team);
			int []pixel = toPixel(castle);
							
			g.setColor(team.getColor());
			Util.fillCircleAtCenter(g, pixel, 5);
		}
	}
	
	public void drawDiagonals(Graphics g){
		g.setColor(GRID_COLOR);
		for(Coord[] line : game.diagonals()){
			drawLineInCoord(g, line[0].x, line[0].y, line[1].x, line[1].y);
		}
	}
	
	public void drawLineInCoord(Graphics g, int x1, int y1, int x2, int y2){
		int[] c1_inPixels = toPixel(x1, y1);
		int[] c2_inPixels = toPixel(x2, y2);
		g.drawLine(c1_inPixels[0], c1_inPixels[1], c2_inPixels[0], c2_inPixels[1]);
	}
	
	public void drawLinesWithin(Graphics g, int x, int y, int X, int Y, boolean colums){
		for(int a=0; a < (colums ? X : Y); a++){
			int x1 = colums?a : x; 
			int y1 = colums?y : a;
			
			int x2 = colums ? a : X - 1;
			int y2 = colums ? Y - 1 : a; 
			
			drawLineInCoord(g, x1, y1, x2, y2);
		}
	}
	
	public void renderGame(Graphics g){
		g.setFont(new Font("Times New Roman", Font.PLAIN, szI(char_size)));
		
		for(int y=0;y<game.height;y++){
			for(int x=0; x<game.width; x++){
				
				iPiece piece = game.get(x,y);
				if(piece != null){
					int [] pixel = toPixel(x,y);
					int state = focusedPiece() == piece ? FOCUSED : NONE;
					state *= fixed ? SELECTED : 1;
					
					drawPiece(g, boardLength(), piece, pixel[0], pixel[1], state);
				}
			}
		}
	}
	
	public void connectPoints(Graphics g, List<Coord>list){ connectPoints(g, 0, list); }
	private void connectPoints(Graphics g, int i, List<Coord>points){
		if(i>=points.size()-1) return;
		
		Coord c1 = points.get(i) , c2 = points.get(i+1);
		drawLineInCoord(g, c1.x, c1.y, c2.x, c2.y);
		connectPoints(g, i+1, points);
	}

	private Coord focused_piece;
	private boolean fixed = false;

	
	private void defocus(){
		focused_piece = null;
		repaint();
	}
	
	private void focus(Coord piece){
		focused_piece = piece;
		repaint();
	}
	
	private boolean pieceFocused(){
		return focused_piece!=null;
	}
	
	protected iPiece focusedPiece(){
		if(!pieceFocused()) return null;
		
		return game.get(focused_piece);
	}
	
	private void whenMouseMoved(MouseEvent e) {
		if(fixed) return;
		
		Coord pointing = toCoord(e.getX(), e.getY());
		
		//나갔으면
		if(!game.onBoard(pointing)){
			defocus(); return;
		}
			
		if(pieceFocused()){
			if(!pointing.equals(focused_piece) &&
			   !game.onRouteOf(focused_piece, pointing)){ // went out
				defocus();
			}
		}else if(game.turn().in(game.get(pointing))){ // own's piece is there
			focus(pointing);
		} 
	}
	
	private void whenBoardClicked(MouseEvent e){
		if(!pieceFocused()) return;
		Coord clicked = toCoord(e.getX(), e.getY());
		
		if(fixed){
			fixed = false;
		}else if(clicked.equals(focused_piece)){
			fixed = true; return; 
		}
		repaint();
		
		System.out.println("moved!");
		tryMove(clicked);
	}
	
	private final int NONE = 0;
	private final int FOCUSED = 1;
	private final int SELECTED = 2;
	
	private final Color COLOR_PASS = Color.RED;
	private final Color COLOR_DEST = new Color(219, 39, 39, 80);
	
	private final Color COLOR_DEFAULT = Color.WHITE;
	private final Color COLOR_FOCUSED = new Color(255, 200, 200);
	private final Color COLOR_SELECTED = Color.red;
	
	public void drawPiece(Graphics g, int boardLength, iPiece piece, int px, int py, int state){
		String symbol = piece.getPClass().getSymbol();
		g.setColor(Color.WHITE);
		
		switch (state) {
		case FOCUSED:
			g.setColor(COLOR_FOCUSED);
				
			break;
		case SELECTED:
			g.setColor(COLOR_SELECTED);
		
			break;
		default:
			g.setColor(COLOR_DEFAULT);
			break;
		}
		
		int r = szI(piece_size);
		Util.fillCircleAtCenter(g, px, py, r);
		g.setColor(piece.getTeam().getColor());
		Util.drawStringJustified(g, symbol, px, py, szI(char_size));
	}
	

	
	private final double board_border = 0.05; // length에 대한 비율값
	private final double border = 0.025;
	private final double piece_size = 0.04; 
	private final double char_size = 0.063; 
	private final double prev_size = 0.02;
	
	public Coord toCoord(int px, int py){
		int x = (int)Math.round((px-(boardLength()*board_border+tx()))/tileWidth());
		int y = (int)Math.round((py-(boardLength()*board_border+ty()))/tileHeight());
		return new Coord(x,y);
	}

	private int[] toPixel(Coord pass) {
		return toPixel(pass.x, pass.y);
	}
	
	public int[] toPixel(int[]coord){
		return toPixel(coord[0], coord[1]);
	}
	
	public int[] toPixel(int x, int y){
		int px = tx() + (int)(tileWidth()*x + boardLength()*board_border);
		int py = ty() + (int)(tileHeight()*y + boardLength()*board_border);
		
		int[]pixel = {px, py};
		return pixel;
	}
	
//	public int charRadius(){
//		return (int) (boardLength() * char_size);
//	}
//	
//	public int prevRadius(){
//		return (int) (boardLength() * )
//	}
//	
//	public double pieceRadius(){
//		return boardLength() * piece_size;
//	}
	
//
//	private int border(){
//		return (int)(boardLength() * border);
//	}
	
	public double sz(double value){
		return value * boardLength();
	}

	public int szI(double value){
		return (int) sz(value);
	}
	
	public int boardLength(){	
		return (int) (Math.min(getWidth(), getHeight())*(1-2*border));
	}
	
	public double tileWidth(){
		return boardLength()*(1-board_border*2)/(game.tile_row);
	}
	
	public double tileHeight(){
		return boardLength()*(1-board_border*2)/(game.tile_column);
	}
	
	public int getBorderedW(){
		return (int) (getWidth()*(1-2*border));
	}
	
	public int getBorderedH(){
		return (int) (getHeight()*(1-2*border));
	}
	
	private int tx(){
		return szI(border) + (centered ? (getBorderedW() - boardLength())/2 : 0);
	}
	
	private int ty(){
		return szI(border) + (centered ? (getBorderedH() - boardLength())/2 : 0);
	}

	
}