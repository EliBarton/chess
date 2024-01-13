package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessGame.TeamColor selfTeam;
    public PieceType selfType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        selfTeam = pieceColor;
        selfType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return selfTeam;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return selfType;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (selfType){
            case KING -> {
                return getKingMoves(myPosition);
            }
            case PAWN -> {
                return getPawnMoves(myPosition);
            }
            case ROOK -> {
                return getRookMoves(myPosition);
            }
            case QUEEN -> {
                return getQueenMoves(myPosition);
            }
            case BISHOP -> {
                return getBishopMoves(myPosition);
            }
            case KNIGHT -> {
                return getKnightMoves(myPosition);
            }
        }

        throw new RuntimeException("Error, piece does not exist");
    }

    public Collection<ChessMove> getKingMoves(ChessPosition piecePosition){
        ArrayList<ChessMove> movelist = new ArrayList<ChessMove>();
        ChessPosition endPosition;
        int directions = 8;
        int distance = 1;
        int currentRow = piecePosition.getRow();
        int currentCol = piecePosition.getColumn();
        for (int i = 0; i < directions; i++){
            switch (i+1){
                case 1:
                    endPosition = new ChessPosition(currentRow+1, currentCol);
                case 2:
                    endPosition = new ChessPosition(currentRow+1, currentCol-1);
                case 3:
                    endPosition = new ChessPosition(currentRow, currentCol-1);
                case 4:
                    endPosition = new ChessPosition(currentRow-1, currentCol-1);
                case 5:
                    endPosition = new ChessPosition(currentRow-1, currentCol);
                case 6:
                    endPosition = new ChessPosition(currentRow-1, currentCol+1);
                case 7:
                    endPosition = new ChessPosition(currentRow, currentCol+1);
                case 8:
                    endPosition = new ChessPosition(currentRow+1, currentCol+1);
                default:
                    endPosition = new ChessPosition(currentRow, currentCol);
            }
            movelist.add(new ChessMove(piecePosition, endPosition, null));
        }
//        for (int i = 0; i < directions; i++){
//            int k = i + 1;
//            int row;
//            int col;
//            if(k % 4 > 1) {
//                k = -k;
//
//            }
//            if (i % 4 > 1){
//                i = -i;
//            }
//            if (i > 6) {
//                row = i % 2;
//                col = k % 2;
//            }
//            endPosition = new ChessPosition(i % 2, k % 2);
//            movelist.add(new ChessMove(piecePosition, endPosition, null));
//            i = Math.abs(i);
//            System.out.println(endPosition.toString());
//        }

        return movelist;
    }

    public Collection<ChessMove> getQueenMoves(ChessPosition piecePosition){
        ArrayList<ChessMove> movelist = new ArrayList<ChessMove>();


        return movelist;
    }

    public Collection<ChessMove> getBishopMoves(ChessPosition piecePosition){
        ArrayList<ChessMove> movelist = new ArrayList<ChessMove>();


        return movelist;
    }

    public Collection<ChessMove> getKnightMoves(ChessPosition piecePosition){
        ArrayList<ChessMove> movelist = new ArrayList<ChessMove>();


        return movelist;
    }

    public Collection<ChessMove> getRookMoves(ChessPosition piecePosition){
        ArrayList<ChessMove> movelist = new ArrayList<ChessMove>();
        ChessPosition endPosition;
        int directions = 4;
        boolean diagonal = true;
        int distance = 1;
        for (int i = 0; i < directions; i++) {
            int k = i + 1;
            int row;
            int col;
            if (k % 4 > 1) {
                k = -k;
            }
            if (i % 4 > 1) {
                i = -i;
            }
            if (i > 6) {
                row = i % 2;
                col = k % 2;
            }
            endPosition = new ChessPosition(i % 2, k % 2);
            movelist.add(new ChessMove(piecePosition, endPosition, null));
            //i = Math.abs(i);
        }
        return movelist;
    }

    public Collection<ChessMove> getPawnMoves(ChessPosition piecePosition){
        ArrayList<ChessMove> movelist = new ArrayList<ChessMove>();


        return movelist;
    }

}
