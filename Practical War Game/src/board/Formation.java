package board;

import java.util.HashMap;

import board.Pieces.MetaPieceSet;




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
			"000000000");
	
	private HashMap<Coord, MetaPieceSet> meta = new HashMap<>(); 
	public final int W;
	public final int H;
	
	private Formation(String forma, String teamSet){
		String [] rows = forma.split(",");
		
		W = rows[0].length();
		H = rows.length;
		
		init(forma, teamSet);
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
//				
				int team = Integer.parseInt(""+team_rows[y].charAt(x));
				meta.put(new Coord(x,y), new MetaPieceSet(symbol, team));
//				
			}
		}
	}
}
