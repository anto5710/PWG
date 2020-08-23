package board.Pieces;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import board.Team;
import board.Pieces.ChessPieces.Cannon;
import board.Pieces.ChessPieces.Chariot;
import board.Pieces.ChessPieces.Elephant;
import board.Pieces.ChessPieces.Horse;
import board.Pieces.ChessPieces.King;
import board.Pieces.ChessPieces.Tactician;
import board.Pieces.ChessPieces.Private;

public enum Pieces {
//	HORSE("馬", Horse.class), 
//	CHARIOT("車", Chariot.class),
//	ELEPHANT("象", Elephant.class),
//	CANNON("砲", Cannon.class),
//	TACTICIAN("士", Tactician.class),
//	KING("王", King.class),
//	PRIVATE("卒", Private.class);
//	
	HORSE("馬", "马", "馬", "馬"), 
	CHARIOT("車", "车", "俥", "車"),
	ELEPHANT("象", "象","相","象"),
	CANNON("砲", "包", "炮", "砲"),
	TACTICIAN("士","士","仕", "士"),
	KING("漢", "楚", "帥", "將"),
	PRIVATE("兵", "卒", "兵", "卒");
	
	
	
	private final String [] symbols;
	
	private Pieces(String ... symbols){
		this.symbols = symbols;
	}
	
	@Override
	public String toString() {
		return symbols[0];
	}
	
	public String getSymbol(int team, int game){
		return symbols[game + team];
	}
	
	public String getSymbol() {
		return symbols[0];
	}

	public static Pieces from(String symbol) {
		for(Pieces p : values())
			if(p.getSymbol().equals(symbol)) return p;
		
		System.err.println("   ds" + symbol);
		return null;
	}
	
	
	public static iPiece cast(MetaPieceSet meta, Team[] teams, Map<Pieces, Class<? extends iPiece>> aIs) {
		Pieces type = from(meta.symbol);
		Team team = teams[meta.team];
		
		if(type == null || !aIs.containsKey(type)) return null;
		
		Class<? extends iPiece> pclass = aIs.get(type);
		System.out.println("sdsclasss   "+ pclass);
		
		
		if(type == null || team == null || pclass == null) return null;
		
		
		try {
			System.out.println("Concstt");
			
			return pclass.getConstructor(Team.class).newInstance(team);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {	
			e.printStackTrace();
		}
		return null;
	}
	
	
//	public Class<? extends iPiece> getPieceClass() {
//		return pieceClass;
//	}
//
//	public static Class<? extends iPiece> getPieceClassFrom(String symbol) {
//		for(Pieces p : values())
//			if(p.symbol.equals(symbol)) return p.pieceClass;
//		
//		System.err.println("   ds" + symbol);
//		return null;
//	}
//	
//	public static iPiece castFromSymbol(String symbol, Team team){
//		Class<? extends iPiece> pClass = getPieceClassFrom(symbol);
//		
//		try {
//			
//			return (iPiece) pClass.getConstructor(Team.class).newInstance(team);
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			
//			e.printStackTrace();
//		} 
//		return null;
//	}
	
	public static Pieces random(){
		return values()[(int) (Math.random()*values().length)];	
	}


	
}

