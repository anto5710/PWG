package ui.Renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.naming.spi.DirStateFactory.Result;

import board.Coord;
import board.Shogi;
import board.Team;
import board.Pieces.Pieces;
import board.Pieces.iPiece;
import ui.FileExplorer;
import ui.MainFrame;
import ui.Util;

public class ImageRenderer {
	private FileExplorer loader;
	
	public ImageRenderer(String folder) {
		loader = new FileExplorer(Util.getJARdir(MainFrame.class)).goChild("PWG").goChild(folder);
	}

	public Shogi generate(String img, int scope){ 
		ArrayList<Team>teams = new ArrayList<>();
		
		BufferedImage bufImgs;
		iPiece[][] pieces;
		
		bufImgs = loader.laodImage(img);
		
		if(bufImgs==null) return null;
		
		Color[]temp = new Color[scope*scope];
		
		pieces = new iPiece[bufImgs.getWidth()/scope][bufImgs.getHeight()/scope];;
		
		for(int x=0; x< bufImgs.getWidth()-scope; x+=scope){
			for(int y=0; y< bufImgs.getHeight()-scope; y+=scope){
				
				temp = new Coord(x+scope/2,y+scope/2).squareRange(scope/2).stream().map(c->{
					return new Color( bufImgs.getRGB(c.x, c.y));
				}).toArray(size->new Color[size]);

				Color end = Util.blend(temp);
				
				Pieces d = Pieces.random();
				Team t = new Team("d", end);
				pieces[x/scope][y/scope] = Pieces.castFromSymbol(d.getSymbol(), t);
				
				teams.add(t);
			}
		}
		
		return new Shogi(pieces, teams.toArray(new Team[teams.size()]));
	}
	
}
