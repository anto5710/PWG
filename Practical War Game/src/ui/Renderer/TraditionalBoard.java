package ui.Renderer;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.Coord;
import board.Formation;
import board.Shogi;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.iPiece;
import board.Pieces.condition.Route;
import ui.Util;

public class TraditionalBoard extends RenderableBoard{
			
	/**
	 * serialID
	 */
	private static final long serialVersionUID = -3313278079860331378L;
	private final Color BOARD_COLOR = new Color(239, 192, 98);
	protected Color GRID_COLOR = Color.BLACK;


	// length에 대한 비율값
//	private final double prev_size = 0.02;

	private Map<Pieces, Double> sizes = new HashMap<>();
	
	private double getSize(Pieces pclass){
		return sizes.containsKey(pclass)? sizes.get(pclass):1;
	}
	
	//	int sq = 20;
//	
//	private final double board_border = 0.005*sq*0.24; 
//	private final double border = 0.0025*sq*0.24;
//	private final double piece_size = 0.004*sq*0.11; 
//	private final double char_size = 0.0068*sq*0.11; 
//	private final double prev_size = 0.002*sq*0.11;		
	
	
//	int sq =1;
	
	protected static Team[]teams = {
			new Team("漢", Color.RED), 
			new Team("楚", Color.BLUE)};
				
	
	public TraditionalBoard(){
		this(Formation.HEEH, teams);
		sizes.put(Pieces.PRIVATE, 0.8);
		sizes.put(Pieces.TACTICIAN, 0.8);
		sizes.put(Pieces.KING, 1.2);
//		game = new ImageRenderer("resource").generate("e.png", sq);
	}
	
	public TraditionalBoard(Formation f, Team...teams) {
		super(f, teams);
	}
	
	public void renderGame(Graphics2D g){
//		System.out.println(game.width + "2sd "+ game.height);
		
		for(int y=0;y< game.height;y++){
			for(int x=0; x<game.width; x++){
				
				iPiece piece = game.get(x,y);
				if(piece != null){
					int [] pixel = toPixel(x,y);
					drawPiece(g, piece, pixel[0], pixel[1], stateOf(piece));
				}
			}
		}
	}
	
	private final Color COLOR_DEFAULT = Color.WHITE;
	private final Color COLOR_FOCUSED = new Color(255, 200, 200);
	private final Color COLOR_SELECTED = new Color(255, 150, 150);
	private final Color COLOR_PREVIEW = new Color(255, 200, 200, 80);
	private final Color COLOR_SHADE = Color.DARK_GRAY;

	private final Color COLOR_PASS = Color.RED;
	private final Color COLOR_DEST = new Color(219, 39, 39, 80);
	private final int PREVIEW = 3;
	
	private final double piece_size = 0.04; 
	private final double tint_t = 0.0415; 
	private final double shade_t = 0.007; 
	private final double char_size = 0.07; 
	
	private void updateFont(Graphics2D g, double scale){
		float newS =  szF(char_size*scale);
		if(g.getFont().getSize()!= newS){
			g.setFont(Util.getFont("Kaiti SC", Font.PLAIN, newS));
		}
	}
			
	public void drawPiece(Graphics2D g, iPiece piece, int px, int py, int state){
		double scale = getSize(piece.getPClass());
		
		String symbol = piece.getPClass().getSymbol();
		updateFont(g, scale);
		
		
		int r = szI(piece_size*scale);
		int tr = szI(tint_t*scale);

		int fs = szI(char_size*scale);
		int st = szI(shade_t*scale);
		
		Color color = getColorOfPieceWhen(state);
		Color tint = Util.blend(BOARD_COLOR, color);
		
		//shade
//		g.setColor(COLOR_SHADE);
//		Util.fillCircleAtCenter(g, px, py+st, r);
//
		//bevel
//		g.setColor(tint);
//		Util.fillCircleAtCenter(g, px, py, tr);
//		
//		g.setColor(color);
//		Util.fillCircleAtCenter(g, px, py, r);
		
		g.setColor(COLOR_SHADE);
		g.fillPolygon(Util.getNPolygon(px, py+st, 8, r, r));
		
		g.setColor(tint);
		g.fillPolygon(Util.getNPolygon(px, py, 8, tr, tr));
		
		g.setColor(color);
		g.fillPolygon(Util.getNPolygon(px, py, 8, r, r));
	
		g.setColor(piece.getTeam().getColor());	
		Util.drawStringJustified(g, symbol, px, py, fs);
		
		g.setColor(Color.white);
		Util.fillCircleAtCenter(g, px, py, 1);
	}
	
	public Color getColorOfPieceWhen(int state){
		switch (state) {
		case FOCUSED:
			return COLOR_FOCUSED;
			
		case SELECTED:
			return COLOR_SELECTED;
			
		case PREVIEW:
			return COLOR_PREVIEW;
			
		default:
			return COLOR_DEFAULT;
		}
	}
	
	
	public void renderRoutes(Graphics2D g){
		iPiece piece = focusedPiece();
		if(piece==null) return;
		
		List<Route> routes = piece.findRoutes(game, focusedCoord());
	
		for(Route r : routes){
			g.setColor(COLOR_DEST);
			for(Coord dest : r.getDests()){
				int []p = toPixel(dest);
				drawPiece(g, piece, p[0], p[1], PREVIEW);
			}

			g.setColor(COLOR_PASS);
			connectPoints(g, r.getAll());
		}
	}

	public void renderBoard(Graphics2D g){
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
		if(game.checkmate(game.turn())){
			g.setColor(Color.CYAN);
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
}