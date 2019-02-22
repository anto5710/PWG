package board.Pieces;

import java.lang.reflect.InvocationTargetException;

import board.Team;
import board.Pieces.ChessPieces.Cannon;
import board.Pieces.ChessPieces.Chariot;
import board.Pieces.ChessPieces.Elephant;
import board.Pieces.ChessPieces.Horse;
import board.Pieces.ChessPieces.King;
import board.Pieces.ChessPieces.Kingsman;
import board.Pieces.ChessPieces.Private;

public enum Pieces {
	HORSE("馬", Horse.class), 
	CHARIOT("車", Chariot.class),
	ELEPHANT("象", Elephant.class),
	CANNON("砲", Cannon.class),
	KINGSMAN("士", Kingsman.class),
	KING("王", King.class),
	PRIVATE("卒", Private.class);
	
	private final String symbol;
	
	private final Class<? extends iPiece> pieceClass;

	private Pieces(String symbol, Class <? extends iPiece> pieceClass){
		this.symbol = symbol;
		this.pieceClass = pieceClass;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public Class<? extends iPiece> getPieceClass() {
		return pieceClass;
	}

	public static Class<? extends iPiece> getPieceClassFrom(String symbol) {
		for(Pieces p : values())
			if(p.symbol.equals(symbol)) return p.pieceClass;
		
		System.err.println("   ds" + symbol);
		return null;
	}
	
	public static iPiece castFromSymbol(String symbol, Team team){
		Class<? extends iPiece> pClass = getPieceClassFrom(symbol);
		
		try {
			
			return (iPiece) pClass.getConstructor(Team.class).newInstance(team);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			
			e.printStackTrace();
		} 
		return null;
	}
	
//	public static String getSymbol(Class<? extends AbstractPiece> class1) {
//		for(Pieces c : Pieces.values()){
//			if(c.pieceClass.equals(class1)){
//				return c.symbol;
//			}
//		}
//		return null;
//	}
//	
	
}
