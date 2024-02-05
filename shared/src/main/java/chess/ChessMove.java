package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    public ChessPosition startPos;
    public ChessPosition endPos;
    public ChessPiece.PieceType promotePiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        startPos = startPosition;
        endPos = endPosition;
        promotePiece = promotionPiece;
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "startPos=" + startPos +
                ", endPos=" + endPos +
                ", promotePiece=" + promotePiece +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        if (promotePiece == null) {
            return Objects.equals(startPos, chessMove.startPos) && Objects.equals(endPos, chessMove.endPos);
        }else{
            return Objects.equals(startPos, chessMove.startPos) && Objects.equals(endPos, chessMove.endPos) && promotePiece.equals(chessMove.promotePiece);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPos, endPos, promotePiece);
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPos;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPos;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotePiece;
        //throw new RuntimeException("Not implemented");
    }
}
