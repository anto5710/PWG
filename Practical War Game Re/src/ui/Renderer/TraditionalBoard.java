package ui.Renderer;

import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.ADual;
import board.Coord;
import board.Formation;
import board.Shogi;
import board.Team;
import board.Xiangqi;
import board.Pieces.Pieces;
import board.Pieces.iPiece;
import board.Pieces.condition.Route;
import multi.client.ui.MainFrame;
import ui.Renderer.PieceEvent.GameListener;
import ui.Renderer.PieceEvent.PieceListener;
import ui.Renderer.PieceEvent.PieceMoveEvent;
import ui.Renderer.PieceEvent.TeamEvent;
import ui.Renderer.fundament.RenderableBoard;
import ui.util.Util;

public class TraditionalBoard extends RenderableBoard{
			
	/**
	 * serialID
	 */
	private static final long serialVersionUID = -3313278079860331378L;
	private Color BOARD_COLOR = new Color(239, 192, 98);
	protected static Color GRID_COLOR = Color.BLACK;
	private String font = "Kaiti SC";

	// length에 대한 비율값
//	private final double prev_size = 0.02;

	private Map<Pieces, Double> sizes = new HashMap<>();
	
	protected double getSize(Pieces pclass){
		return (sizes.containsKey(pclass)? sizes.get(pclass):1)*8D/game.tile_column;
	}
	
//	protected FileExplorer mainFolder(){
//		return new FileExplorer(Util.getJARdir(MainFrame.class)).goChild("PWG");
//	}
	
	//	int sq = 20;
//	
//	private final double board_border = 0.005*sq*0.24; 
//	private final double border = 0.0025*sq*0.24;
//	private final double piece_size = 0.004*sq*0.11; 
//	private final double char_size = 0.0068*sq*0.11; 
//	private final double prev_size = 0.002*sq*0.11;		
	
	public static Team[]teams = {
			new Team("漢", Color.RED , 0), 
			new Team("楚", Color.BLUE, 1)
//			new Team("간", Color.GREEN),
//			new Team("D", Color.BLACK)
			};
				
	
//	public TraditionalBoard(){
//		this(Formation.HEEH, teams);
//		sizes.put(Pieces.PRIVATE, 0.8);
//		sizes.put(Pieces.TACTICIAN, 0.8);
//		sizes.put(Pieces.KING, 1.2);
//		game = new ImageRenderer("resource").generate("e.png", sq);
//	}
	
	public TraditionalBoard(ADual game) {
		super(game);
		sizes.put(Pieces.PRIVATE, 0.8);
		sizes.put(Pieces.TACTICIAN, 0.8);
		sizes.put(Pieces.KING, 1.2);
		
		addGameListener(new GameListener() {
			
			@Override
			public void defended(TeamEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void checkmate(TeamEvent e) {
				MainFrame.INSTANCE.over(e.team);
				
			}
			
			@Override
			public void check(TeamEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				whenMouseMoved(e);
			}
		});
		
		addPieceListener(new PieceListener() {
			
			@Override
			public void pieceMoved(PieceMoveEvent e) {
				try {
					if(MainFrame.INSTANCE.handler !=null){
						System.out.println("handler: "+MainFrame.INSTANCE.handler + "e: "+ e);
						MainFrame.INSTANCE.handler.sendMove(e.from, e.coord);
					}
				} catch (IOException e1) {
					
					
					e1.printStackTrace();
				}
				
				
			}
			
			@Override
			public void pieceKilled(PieceMoveEvent e) {
				try {
					if(MainFrame.INSTANCE.handler !=null){
						MainFrame.INSTANCE.handler.sendMove(e.from, e.coord);
					}
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
	}
	
	private final Color COLOR_DEFAULT = Color.WHITE;
	private final Color COLOR_FOCUSED = new Color(255, 200, 200);
	private final Color COLOR_SELECTED = new Color(255, 150, 150);
	private final Color COLOR_PREVIEW = new Color(255, 200, 200, 80);
	private final Color COLOR_SHADE = Color.DARK_GRAY;
	private final Color COLOR_MOVING = new Color(255, 200, 200, 60);

	private final Color COLOR_PASS = Util.setAlpha(Color.RED, 86);
	private final Color COLOR_DEST = new Color(219, 39, 39, 80);
		
	private final double piece_size = 0.04; 
	private final double bevel = 0.06; 
	private final double shade_t = 0.007; 
	private final double char_size = 0.07; //0.07 
	private final double dot = 0.01;
	
	private final int Npoly = 8;
	
	private void updateFont(Graphics2D g, float newS){
		if(g.getFont().getSize()!= newS){
			g.setFont(Util.getFont(font, Font.PLAIN, newS));
		}
	}
	
	public void drawRawPiece(Graphics2D g, iPiece piece, int px, int py, int state){
		double scale = getSize(piece.getPClass());
	
		
		String symbol = piece.getPClass().getSymbol(piece.getTeam().num, MainFrame.INSTANCE.gameType);		
		
		
		int fs = szI(char_size*scale);
		int fsIN = szI(char_size*scale*0.95);
		
		int r = szI((1-bevel)*piece_size*scale);
		int tr = szI(piece_size*scale);
		int st = szI(shade_t*scale);
		
		Color color = getColorOfPieceWhen(state);
		Color tint = Util.blend(BOARD_COLOR, color);
		tint = Util.setAlpha(tint, color.getAlpha());
		Color shade = Util.setAlpha(COLOR_SHADE, color.getAlpha());
				
		//shade
		g.setColor(shade);
		g.fillPolygon(Util.getNPolygon(px, py+st, Npoly, r, r));
		
		//bevel
		g.setColor(tint);
		g.fillPolygon(Util.getNPolygon(px, py, Npoly, tr, tr));
		
		//fill
		g.setColor(color);
		g.fillPolygon(Util.getNPolygon(px, py, Npoly, r, r));
		
		
		if(state==MOVING) return;
			
		// char bevel-in
		updateFont(g, fs);
		g.setColor(piece.getTeam().getColor().darker().darker());	
		Util.drawStringJustified(g, symbol, px, py, fs);
			
		// char
		updateFont(g, fsIN);
		g.setColor(piece.getTeam().getColor());	
		Util.drawStringJustified(g, symbol, px, py, fsIN);	
	}	
	
	
	public void drawPiece(Graphics2D g, iPiece piece, int px, int py, int state){
		double scale = getSize(piece.getPClass());
		
		Point2D org = new Point(px, py);
		Point2D rev = null;
		
		rev = perspective().transform(org, rev);
		if(rev!=null){
			px = (int) rev.getX();
			py = (int) rev.getY();
		}
		
		String symbol = piece.getPClass().getSymbol(piece.getTeam().num, MainFrame.INSTANCE.gameType);		
		
		int fs = szI(char_size*scale);
		int fsIN = szI(char_size*scale*0.95);
		
		int r = szI((1-bevel)*piece_size*scale);
		int tr = szI(piece_size*scale);
		int st = szI(shade_t*scale);
		
		Color color = getColorOfPieceWhen(state);
		Color tint = Util.blend(BOARD_COLOR, color);
		tint = Util.setAlpha(tint, color.getAlpha());
		Color shade = Util.setAlpha(COLOR_SHADE, color.getAlpha());
				
		//shade
		g.setColor(shade);
		g.fillPolygon(Util.getNPolygon(px, py+st, Npoly, r, r));
		
		//bevel
		g.setColor(tint);
		g.fillPolygon(Util.getNPolygon(px, py, Npoly, tr, tr));
		
		//fill
		g.setColor(color);
		g.fillPolygon(Util.getNPolygon(px, py, Npoly, r, r));
		
		
		if(state==MOVING) return;
			
		// char bevel-in
		updateFont(g, fs);
		g.setColor(piece.getTeam().getColor().darker().darker());	
		Util.drawStringJustified(g, symbol, px, py, fs);
			
		// char
		updateFont(g, fsIN);
		g.setColor(piece.getTeam().getColor());	
		Util.drawStringJustified(g, symbol, px, py, fsIN);	
	}	
	
	public Color getColorOfPieceWhen(int state){
		switch (state) {
		case FOCUSED:
			return COLOR_FOCUSED;			
		case SELECTED:
			return COLOR_SELECTED;	
		case PREVIEW:
			return COLOR_PREVIEW;
		case MOVING:
			return COLOR_MOVING;
		default:
			return COLOR_DEFAULT;
		}
	}
	
	public void whenMouseMoved(MouseEvent e){
		if(focusing()) repaint();
	}
	
	@Override
	public void renderRoutes(Graphics2D g){
		iPiece piece = focusedPiece();
		if(piece==null) return;
		
		List<Route> routes = piece.findRoutes(game, focusedCoord());
	
		for(Route r : routes){
			g.setColor(COLOR_DEST);
			for(Coord dest : r.getDests()){
				
				Point mouse = getMousePosition();
				if(mouse!=null){
					Coord mouseC = toCoord(mouse.x, mouse.y);
					
					if(mouseC.equals(dest)){	
//						rev = perspective().transform(mouse, rev);
//						mouse =  (Point) applyTransform(mouse);
						g.setTransform(new AffineTransform());
//						drawPiece(g, piece, (int)rev.getX(), (int)rev.getY(), PREVIEW);
						drawRawPiece(g, piece, mouse.x, mouse.y, PREVIEW);
						g.setTransform(perspective());
					}
				}
				g.setColor(COLOR_PASS);
				Util.fillCircleAtCenter(g, toPixel(dest), 10);
			}
			g.setStroke(new BasicStroke(3));
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
	
	private static Color dom = new Color(216, 155, 95);
	private static Color blend =Util.blend(GRID_COLOR, dom);
	
	public void drawGrids(Graphics2D g){
		
		g.setColor(blend);
		g.setStroke(new BasicStroke(3));
		
		if(game instanceof Xiangqi){
			drawLinesWithin(g, 0, 0, game.width, game.height/2, true);
			drawLinesWithin(g, 0, game.height/2, game.width, game.height, true);
			drawLinesWithin(g, 0, 0, game.width, game.height, false);
			
			g.setColor(GRID_COLOR);
			g.setStroke(new BasicStroke(1));
			
			drawLinesWithin(g, 0, 0, game.width, game.height/2, true);
			drawLinesWithin(g, 0, game.height/2, game.width, game.height, true);
			drawLinesWithin(g, 0, 0, game.width, game.height, false);
		}else{
			drawLinesWithin(g, 0, 0, game.width, game.height, true);
			drawLinesWithin(g, 0, 0, game.width, game.height, false);
			
			g.setColor(GRID_COLOR);
			g.setStroke(new BasicStroke(1));
			
			drawLinesWithin(g, 0, 0, game.width, game.height, true);
			drawLinesWithin(g, 0, 0, game.width, game.height, false);
		}
	}

	public void drawCastles(Graphics g){
		for(Team team: game.castles().keySet()){
			Coord castle = game.castles().get(team);
			int []pixel = toPixel(castle);		
			int dot = szI(this.dot);
			int dotb = szI(0.4*this.dot);
			
			g.setColor(GRID_COLOR);
			Util.fillCircleAtCenter(g, pixel, dot);
			
			g.setColor(team.getColor());
			Util.fillCircleAtCenter(g, pixel, dotb);
		}
	}
	
	public void drawDiagonals(Graphics g){
		
		for(Coord[] line : game.diagonals()){
			g.setColor(blend);
			((Graphics2D)g).setStroke(new BasicStroke(3));
			drawLineInCoord(g, line[0].x, line[0].y, line[1].x, line[1].y);
			
			g.setColor(GRID_COLOR);
			((Graphics2D)g).setStroke(new BasicStroke(1));
			drawLineInCoord(g, line[0].x, line[0].y, line[1].x, line[1].y);
		}
	}
}