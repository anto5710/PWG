package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.Route;
import board.Pieces.iPiece;
import board.Pieces.ChessPieces.Cannon;
import board.Pieces.ChessPieces.Chariot;
import board.Pieces.ChessPieces.Elephant;
import board.Pieces.ChessPieces.Horse;

public class GamePanel extends JPanel{
	private final double border = 0.05; // length에 대한 비율값
	private final double piece_size = 0.04; 
	private final double char_size = 0.063; 
	
	private Shogi game;
	
	private final Color BOARD_COLOR = new Color(239, 192, 98);
	private final Color GRID_COLOR = Color.BLACK;
	
	private Coord selected;
	
	private void deselectPiece(){
		selected = null;
		repaint();
	}
	
	private boolean selectedPiece(){
		return selected!=null;
	}
	
	private iPiece getSelectedPiece(){
		if(!selectedPiece()) return null;
		
		return game.get(selected.x, selected.y);
	}
	
	private void selectPiece(Coord coord){
		selected = coord;
		repaint();
	}
	
	public GamePanel() {
		Team blue = new Team(("퍼랭이"), Color.BLUE);
		Team red = new Team(("빨갱이"), Color.RED);
	
		game = new Shogi(8, 9, red, blue);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {					
				whenBoardClicked(e);
			}
			
		});
	}
	
	private void tryMove(int x, int y){
		if(game.move(selected.x, selected.y, x, y)){
			
		}
	}
	
	private void whenBoardClicked(MouseEvent e){
		if(selectedPiece()){
			Coord p = toCoord(e.getX(), e.getY());
			if(!selected.equals(p)) tryMove(p.x, p.y);
			
			deselectPiece();return;
		}
		
		Coord p = toCoord(e.getX(), e.getY());
		if(game.isThereStone(p.x, p.y)){ // null이면 그 자리에 아무 말도 없다 
			selectPiece(p);
		}
	}
	
	public Coord toCoord(int px, int py){
		int x = (int)Math.round((px-boardLength()*border)/tileWidth());
		int y = (int)Math.round((py-boardLength()*border)/tileHeight());
		return new Coord(x,y);
	}
	
	public int[] toPixel(int[]coord){
		return toPixel(coord[0], coord[1]);
	}
	
	public int[] toPixel(int x, int y){
		int px = (int)(tileWidth()*x + boardLength()*border);
		int py = (int)(tileHeight()*y + boardLength()*border);
		
		int[]pixel = {px, py};
		return pixel;
	}
	
	public int charRadius(){
		return (int) (boardLength() * char_size);
	}
	
	public double pieceRadius(){
		return boardLength() * piece_size;
	}
	
	public int boardLength(){
		return Math.min(getWidth(), getHeight());
	}
	
	public double tileWidth(){
		return boardLength()*(1-border*2)/(game.tile_row);
		
	}
	
	public double tileHeight(){
		return boardLength()*(1-border*2)/(game.tile_column);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		renderBoard(g);
		renderGame(g);
		renderRoutes(g);
	}

	public void renderRoutes(Graphics g){
		if(!selectedPiece()) return;
		
		iPiece piece = getSelectedPiece();
		List<Route> routes = piece.findRoutes(game, selected);
		if(routes==null){
			return;
		}
		
		
		for(Route r : routes){
			g.setColor(new Color(219, 39, 39, 10));
			for(Coord pass : r.getPasses()){
				int []p = toPixel(pass);
				Util.fillCircleAtCenter(g, p[0], p[1], 20);
			}
	
			
			g.setColor(new Color(219, 39, 39, 80));
			for(Coord pass : r.getDests()){
				int []p = toPixel(pass);
				Util.fillCircleAtCenter(g, p[0], p[1], 20);
			}
		}
	}
	
	private int[] toPixel(Coord pass) {
		return toPixel(pass.x, pass.y);
	}

	public void renderBoard(Graphics g){
		g.setColor(BOARD_COLOR);
		g.fillRect(0, 0, boardLength(), boardLength());
		
		g.setColor(GRID_COLOR);
		drawLinesWithin(g, 0, 0, game.width, game.height, true);
		drawLinesWithin(g, 0, 0, game.width, game.height, false);
		drawCastles(g);
	}

	public void drawCastles(Graphics g){
		
		for(Team team: game.castles().keySet()){
			Coord castle = game.castles().get(team);
			int []pixel = toPixel(castle);
							
			g.setColor(team.getColor());
			Util.fillCircleAtCenter(g, pixel, 5);
			g.setColor(GRID_COLOR);
			drawLineInCoord(g, castle.x-1, castle.y-1, castle.x+1, castle.y+1);
			drawLineInCoord(g, castle.x+1, castle.y-1, castle.x-1, castle.y+1);
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
		g.setFont(new Font("Times New Roman", Font.PLAIN, (int)(boardLength() * char_size)));
		
		for(int y=0;y<game.height;y++){
			for(int x=0; x<game.width; x++){
				
				iPiece piece = game.get(x,y);
				if(piece != null){
					int [] pixel = toPixel(x,y);
					drawPiece(g, boardLength(), piece, pixel[0], pixel[1]);
				}
			}
		}
	}
	
	public void drawPiece(Graphics g, int boardLength, iPiece piece, int px, int py){
		int r = (int) pieceRadius();
		String symbol = piece.getPClass().getSymbol();
		g.setColor(Color.WHITE);
		
		if(selectedPiece() && piece==getSelectedPiece()){
			g.setColor(new Color(255, 200, 200));
		}
		Util.fillCircleAtCenter(g, px, py, r);
		g.setColor(piece.getTeam().getColor());
		Util.drawStringJustified(g, symbol, px, py, charRadius());
	}
	
}