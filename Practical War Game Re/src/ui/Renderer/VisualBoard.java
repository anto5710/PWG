package ui.Renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import board.ADual;
import board.Team;
import board.Pieces.iPiece;
import ui.SoundPlayer;
import ui.Renderer.PieceEvent.GameListener;
import ui.Renderer.PieceEvent.PieceListener;
import ui.Renderer.PieceEvent.PieceMoveEvent;
import ui.Renderer.PieceEvent.TeamEvent;

public class VisualBoard extends TraditionalBoard{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2336433524400985985L;

	//private SoundPlayer player = new SoundPlayer();
	public AssetLoader loader = new AssetLoader(teams);
	
	private SoundPlayer bgm = new SoundPlayer();
	private File now_playing = null;
	
	public VisualBoard(ADual game) {
		super(game);
//		GRID_COLOR = Color.white;
		
		addPieceListener(new PieceListener() {		
			@Override
			public void pieceMoved(PieceMoveEvent e) {
				playEffect(e.piece, "place");		
			}
			
			@Override
			public void pieceKilled(PieceMoveEvent e) {
				playEffect(e.piece, "kill");
			}
		});
		
		addGameListener(new GameListener() {
			
			@Override
			public void check(TeamEvent e) {
				changeBGM(loader.check);
			}
			
			@Override
			public void checkmate(TeamEvent e) {
				changeBGM(loader.checkmate);
			}

			@Override
			public void defended(TeamEvent e) {
				bgm.stop();
				now_playing = null;
			}
		});
	}

	private void changeBGM(File sound){
		if(now_playing!=null && now_playing!=sound) bgm.stop();
		
		if(sound!=null && now_playing!=sound){
			bgm.play(sound);
			now_playing = sound; 
		}
	}
	
	private void playEffect(iPiece piece, String state){
		File sound = loader.getSoundWhen(piece, state); 
		if(sound!=null) new SoundPlayer().play(sound);
	}
	
	private final double piece_size = 0.065; 	
	
	@Override
	public void drawBackdrop(Graphics g) {
		Team team = game.turn();
		
		String status = game.getStatus(team).getName();
		BufferedImage background = loader.getBackground(status);
		if(background==null){
			super.drawBackdrop(g); return;
		}
		g.clipRect(tx(), ty(), boardLength(), boardLength());
		renderImageAtCenter((Graphics2D) g, background, centerX(), centerY(), boardLength()/2D);
		g.setClip(null);
	}
	
	public void drawPiece(Graphics2D g, iPiece piece, int px, int py, int state) {	
//		if(!registered(piece)){		
		
		
		BufferedImage img = loader.getVisualOf(piece.getPClass(), piece.getTeam().getName());
		if(img==null){
			super.drawPiece(g, piece, px, py, state); return;
		}
		Point2D org = new Point(px, py);
		Point2D rev = null;
		
		rev = perspective().transform(org, rev);
		if(rev!=null){
			px = (int) rev.getX();
			py = (int) rev.getY();
		}
		int radius = szI(getSize(piece.getPClass())*piece_size);
		renderImageAtCenter(g, img, px, py, radius);
	}
	
	@Override
	public void drawRawPiece(Graphics2D g, iPiece piece, int px, int py, int state) {	
		BufferedImage img = loader.getVisualOf(piece.getPClass(), piece.getTeam().getName());
		if(img==null){
			super.drawRawPiece(g, piece, px, py, state); return;
		}
		int radius = szI(getSize(piece.getPClass())*piece_size);
		renderImageAtCenter(g, img, px, py, radius);
	}

	public void renderImageAtCenter(Graphics2D g, BufferedImage img, double px, double py, double r){
		AffineTransform t = new AffineTransform();
		
		double min = Math.min(img.getWidth(), img.getHeight());
		double cx = px-r/min*img.getWidth();
		double cy = py-r/min*img.getHeight();
		
		t.translate(cx, cy);		
		t.scale(2D*r/min, 2D*r/min);
		g.drawImage(img, t, null);
	}
}
