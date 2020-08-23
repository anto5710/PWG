package board;

import java.util.ArrayList;
import java.util.HashMap;

import board.Pieces.MetaPieceSet;
import board.Pieces.Pieces;
import board.Pieces.iPiece;
import board.Pieces.ChessPieces.Chariot;

import board.Pieces.ChessPieces.Horse;
import board.Pieces.ChessPieces.King;
import board.Pieces.ChessPieces.Private;
import board.Pieces.ChessPieces.Tactician;
import board.Pieces.ChessPieces.Cseries.CCannon;
import board.Pieces.ChessPieces.Cseries.CChariot;
import board.Pieces.ChessPieces.Cseries.CElephant;
import board.Pieces.ChessPieces.Cseries.CPrivate;
import board.Pieces.ChessPieces.Cseries.CTactician;

public class Xiangqi extends Shogi{

	public Xiangqi(Formation formation, Team[] teams) {
		super(formation, teams);
	}
	
	@Override
	protected void formate(Formation formation){
		 castles = new HashMap<>();
		 diagonals =  new ArrayList<>();;
		AIs.put(Pieces.CANNON, CCannon.class);
		AIs.put(Pieces.CHARIOT, CChariot.class);
		AIs.put(Pieces.ELEPHANT, CElephant.class);
		AIs.put(Pieces.HORSE, Horse.class);
		AIs.put(Pieces.KING, King.class);
		AIs.put(Pieces.PRIVATE, CPrivate.class);
		AIs.put(Pieces.TACTICIAN, CTactician.class);
	
		
		setCastle(teams[1], new Coord(4,1));
		setCastle(teams[0], new Coord(4,8));
		
		for(Coord c : formation.meta().keySet()){
			MetaPieceSet meta = formation.meta().get(c);
			
			
				iPiece p =Pieces.cast(meta, teams, AIs); 			
				pieces[c.x][c.y] = p;
			
		}
		
		
	}

}
