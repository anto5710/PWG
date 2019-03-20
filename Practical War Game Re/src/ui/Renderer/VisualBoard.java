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
		
		String status = game.getStatus(team).getName();
		BufferedImage background = loader.getBackground(status);
		if(background==null){
			super.drawBackdrop(g); return;
		}
		g.clipRect(tx(), ty(), boardLength(), boardLength());
		renderImageAtCenter((Graphics2D) g, background, (int)Math.round(centerX()), (int)Math.round(centerY()), boardLength()/2);
		g.setClip(null);
	}
	
	public void drawPiece(Graphics2D g, iPiece piece, int px, int py, int state) {	
//		if(!registered(piece)){		
		BufferedImage img = loader.getVisualOf(piece.getPClass(), piece.getTeam().getName());
		if(img==null){
			super.drawPiece(g, piece, px, py, state); return;
		}
		int radius = szI(getSize(piece.getPClass())*piece_size);
		renderImageAtCenter(g, img, px, py, radius);
	};

	public void renderImageAtCenter(Graphics2D g, BufferedImage img, int px, int py, int r){
		AffineTransform t = new AffineTransform();
		
		double min = Math.min(img.getWidth(), img.getHeight());
		double cx = px-r/min*img.getWidth();
		double cy = py-r/min*img.getHeight();
		
		t.translate(cx, cy);		
		t.scale(2D*r/min, 2D*r/min);
		g.drawImage(img, t, null);
	}
}
