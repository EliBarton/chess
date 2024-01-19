package chess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        
    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public Object[][] board = new Object[8][8];
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //throw new RuntimeException("Not implemented");
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return (ChessPiece) board[position.getRow()-1][position.getColumn()-1];
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < 8; i++){
            ArrayList<ChessPiece> rowPieces;
            switch (i) {
                case 0:
                    rowPieces = createBasePieces(ChessGame.TeamColor.WHITE);
                    for (int k = 0; k < 8; k++) {
                        board[i][k] = rowPieces.get(k);
                    }
                    break;
                case 1:
                    rowPieces = createPawns(ChessGame.TeamColor.WHITE);
                    for (int k = 0; k < 8; k++) {
                        board[i][k] = rowPieces.get(k);
                    }
                    break;
                case 6:
                    rowPieces = createPawns(ChessGame.TeamColor.BLACK);
                    for (int k = 0; k < 8; k++) {
                        board[i][k] = rowPieces.get(k);
                    }
                    break;
                case 7:
                    rowPieces = createBasePieces(ChessGame.TeamColor.BLACK);
                    for (int k = 0; k < 8; k++) {
                        board[i][k] = rowPieces.get(k);
                    }
                    break;
                default:
                    for (int k = 0; k < 8; k++) {
                        board[i][k] = null;
                    }
            }
        }
        //throw new RuntimeException("Not implemented");
    }

    private ArrayList<ChessPiece> createBasePieces(ChessGame.TeamColor team){
        ArrayList<ChessPiece> baseRow = new ArrayList<ChessPiece>();
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.ROOK));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.KNIGHT));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.BISHOP));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.QUEEN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.KING));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.BISHOP));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.KNIGHT));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.ROOK));
        return baseRow;
    }

    private ArrayList<ChessPiece> createPawns(ChessGame.TeamColor team){
        ArrayList<ChessPiece> baseRow = new ArrayList<ChessPiece>();
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        baseRow.add(new ChessPiece(team, ChessPiece.PieceType.PAWN));
        return baseRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }
}
