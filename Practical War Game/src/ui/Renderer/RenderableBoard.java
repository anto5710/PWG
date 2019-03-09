package ui.Renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import board.Coord;
import board.Formation;
import board.Team;

public abstract class RenderableBoard extends FocusableBoard{
	
	public RenderableBoard(Formation f, Team...teams) {
		super(f, teams);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		renderBoard(g2d);
		renderGame(g2d);
		renderRoutes(g2d);
	}

	public abstract void renderGame(Graphics2D g);
	public abstract void renderRoutes(Graphics2D g);
	public abstract void renderBoard(Graphics2D g);
	
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
	
	public void connectPoints(Graphics g, List<Coord>list){ 
		connectPoints(g, 0, list); 
	}
	private void connectPoints(Graphics g, int i, List<Coord>points){
		if(i>=points.size()-1) return;
		
		Coord c1 = points.get(i) , c2 = points.get(i+1);
		drawLineInCoord(g, c1.x, c1.y, c2.x, c2.y);
		connectPoints(g, i+1, points);
	}
	
}