package board;

import java.util.Arrays;
import java.util.HashMap;

import board.Pieces.MetaPieceSet;
import ui.Assert;




public enum Formation {
	HEEH(	"車馬象士+士象馬車,"+
			"++++王++++,"+
			"+砲+++++砲+,"+
			"卒+卒+卒+卒+卒,"+	
			"+++++++++,"+
			"卒+卒+卒+卒+卒,"+
			"+砲+++++砲+,"+
			"++++王++++,"+
			"車馬象士+士象馬車", 
			
			"111111111,"+
			"111111111,"+
			"111111111,"+
			"111111111,"+	
			"+++++++++,"+
			"000000000,"+
			"000000000,"+
			"000000000,"+
			"000000000"),
	
	EHHE(	"車馬象士+士象馬車,"+
			"++++王++++,"+
			"+砲+++++砲+,"+
			"卒+卒+卒+卒+卒,"+	
			"+++++++++,"+
			"卒+卒+卒+卒+卒,"+
			"+砲+++++砲+,"+
			"++++王++++,"+
			"車象馬士+士馬象車", 
			
			"111111111,"+
			"111111111,"+
			"111111111,"+
			"111111111,"+	
			"+++++++++,"+
			"000000000,"+
			"000000000,"+
			"000000000,"+
			"000000000"),
	
	HEEH_ROTATED(
			"車++卒+卒++車,"+
			"馬+砲+++砲+馬,"+
			"象++卒+卒++象,"+
			"士+++++++士,"+
			"+王+卒+卒+王+,"+
			"士+++++++士,"+
			"象++卒+卒++象,"+
			"馬+砲+++砲+馬,"+
			"車++卒+卒++車",
			
			
			"1111+0000,"+
			"1111+0000,"+
			"1111+0000,"+
			"1111+0000,"+	
			"1111+0000,"+
			"1111+0000,"+
			"1111+0000,"+
			"1111+0000,"+
			"1111+0000,");
	
	private HashMap<Coord, MetaPieceSet> meta = new HashMap<>(); 
	public final int W;
	public final int H;
	
	private Formation(String forma, String teamSet){
		int[] rows_size = dimensionOf(forma.split(","));
		int[] team_rows_size = dimensionOf(teamSet.split(","));
		
		System.out.println(Arrays.toString(rows_size));
		System.out.println(Arrays.toString(team_rows_size));
	
		Assert.throwIF(!Arrays.equals(rows_size, team_rows_size), "The Dimension of a Formation and its Team Set Must Be Equivalent.");
		W = rows_size[0];
		H = rows_size[1];	
		
		init(forma, teamSet);
	}
	
	private int[] dimensionOf(String[] lines){
		int H = lines.length;
		int minW = lines[0].length();
		for(String line: lines){
			if(line!=null && line.length()<minW) minW = line.length();
		}
		int [] dimension = {minW, H};
		return dimension;
	}
	
	public HashMap<Coord, MetaPieceSet> meta(){
		return meta;
	}
	
	private void init(String forma, String teamSet){
		String[]rows = forma.split(",");
		String[]team_rows = teamSet.split(",");
		
		for(int y=0; y<H; y++){
			for(int x=0; x<W; x++){
				
				String symbol = ""+rows[y].charAt(x);
				if(symbol.equals("+")) continue;				
				
				int team = Integer.parseInt(""+team_rows[y].charAt(x));
				meta.put(new Coord(x,y), new MetaPieceSet(symbol, team));
			}
		}
	}
}
