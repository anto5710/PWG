package ui.Renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import board.Team;
import board.Pieces.iPiece;

public class VisualBoard extends TraditionalBoard{	
	public VisualBoard() {
		super();
		GRID_COLOR = Color.CYAN;
	}

	private final double piece_size = 0.065; 	
	
	@Override
	public void drawBackdrop(Graphics g) {
		Team team = game.turn();
		String bg = "default";
		if(game.check(team)) bg = "check";
		
		if(game.checkmate(team)) bg = "checkmate";
		
		BufferedImage background = loader.getBackground(bg);
		if(background==null){
			super.drawBackdrop(g); return;
		}
		
		AffineTransform t = new AffineTransform();
		int m = Math.min(background.getWidth(), background.getHeight());
		int W= (int) (1D*boardLength()/m*background.getWidth());
		int H= (int) (1D*boardLength()/m*background.getHeight());
		
		t.translate(tx()-(W-boardLength())/2, ty()- (H-boardLength())/2);
		t.scale(1D*boardLength()/m, 1D*boardLength()/m);
		
		g.clipRect(tx(), ty(), boardLength(), boardLength());
		((Graphics2D)g).drawImage(background, t, null);
		g.setClip(null);
	}
	
	public void drawPiece(Graphics2D g, iPiece piece, int px, int py, int state) {
		
//		if(!registered(piece)){
//			super.drawPiece(g, piece, px, py, state); return;
//		}
//		
		BufferedImage img = loader.getVisualOf(piece.getPClass(), piece.getTeam().getName());
		if(img==null){
			super.drawPiece(g, piece, px, py, state); return;
		}
		
		renderImageAtCenter(g, img, px, py, szI(getSize(piece.getPClass())*piece_size));
	};

	public void renderImageAtCenter(Graphics2D g, BufferedImage img, int px, int py, int r){
		AffineTransform t = new AffineTransform();
		int m = Math.min(img.getWidth(), img.getHeight());
		double cx = px-r;
		double cy = py-(1D*r/m*img.getHeight());
		
		t.translate(cx, cy);		
		t.scale(2D*r/m, 2D*r/m);
		g.drawImage(img, t, null);
	}
}
