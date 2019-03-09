package ui.Renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import board.Team;
import board.Pieces.Pieces;
import board.Pieces.iPiece;
import ui.FileExplorer;
import ui.MainFrame;
import ui.Util;

public class VisualBoard extends TraditionalBoard{
	private FileExplorer mainFolder = new FileExplorer(Util.getJARdir(MainFrame.class)).goChild("PWG");
	private HashMap<String, BufferedImage> visuals = new HashMap<>();
	private BufferedImage background;
	private BufferedImage check;
	private BufferedImage checkmate;
	
	public VisualBoard() {
		super();
		GRID_COLOR = Color.WHITE;
		registerVisuals();	
		getBackBoard();
	}
	
	private void getBackBoard(){
		FileExplorer dir = mainFolder.goChild("Background");
		if(dir.contains("background.jpg")){
			background = dir.laodImage("background.jpg");
		}
		if(dir.contains("check.jpg")){
			check = dir.laodImage("check.jpg");
		}
		if(dir.contains("checkmate.jpg")){
			checkmate = dir.laodImage("checkmate.jpg");
		}
	}
	
	private void registerVisuals(){
		FileExplorer dir = mainFolder.goChild("Pieces");
		for(Team team : teams){
			for(Pieces p: Pieces.values()){
				String imgLoc = team+p.getSymbol() + ".png";
				
				if(dir.contains(imgLoc)){
					visuals.put(team.getName()+p.getSymbol(),  dir.laodImage(imgLoc));
				}
			}
		}
	}
	
	private boolean registered(iPiece p){
		return visuals.containsKey(p.getTeam().getName()+p.getPClass().getSymbol());
	}
	
	private final double piece_size = 0.065; 
	
	
	
	@Override
	public void drawBackdrop(Graphics g) {
		if(background==null){
			super.drawBackdrop(g); return;
		}
		
		BufferedImage background = this.background;
		if(game.check(game.turn())) background = check;
		
		if(game.checkmate(game.turn())) background = checkmate;
		
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
		if(!registered(piece)){
			super.drawPiece(g, piece, px, py, state); return;
		}
		AffineTransform t = new AffineTransform();
		
		BufferedImage img = visuals.get(piece.getTeam().getName()+piece.getPClass().getSymbol());
		int r = szI(piece_size);
		int m = Math.min(img.getWidth(), img.getHeight());
		
		t.translate(px-r, py-(1D*r/m*img.getHeight()));
		t.scale(2D*r/m, 2D*r/m);
		
		g.drawImage(img, t, null);
	};
}
