package chess;

import java.util.*;
import java.util.HashSet;

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
        switch (selfType) {
            case KING -> {
                return getKingMoves(board, myPosition);
            }
            case PAWN -> {
                return getPawnMoves(board, myPosition);
            }
            case ROOK -> {
                return getRookMoves(board, myPosition);
            }
            case QUEEN -> {
                return getQueenMoves(board, myPosition);
            }
            case BISHOP -> {
                return getBishopMoves(board, myPosition);
            }
            case KNIGHT -> {
                return getKnightMoves(board, myPosition);
            }
        }

        throw new RuntimeException("Error, piece does not exist");
    }

    /**
     *
     * @param board The current board
     * @param piecePosition The current position of the piece on the board
     * @return the list of valid moves that a king can make
     */
    public Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition piecePosition) {

        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getDiagDirections(piecePosition));
        directions.addAll(getHorVerDirections(piecePosition));
        HashSet<ChessMove> movelist = calculateMoves(directions, 1, board, piecePosition);

        System.out.println(movelist);
        return movelist;
    }

    /**
     *
     * @param board The current board
     * @param piecePosition The current position of the piece on the board
     * @return the list of valid moves that a queen can make
     */
    public Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getDiagDirections(piecePosition));
        directions.addAll(getHorVerDirections(piecePosition));

        HashSet<ChessMove> movelist = calculateMoves(directions, 8, board, piecePosition);


        return movelist;
    }

    /**
     *
     * @param board The current board
     * @param piecePosition The current position of the piece on the board
     * @return the list of valid moves that a bishop can make
     */
    public Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getDiagDirections(piecePosition));

        HashSet<ChessMove> movelist = calculateMoves(directions, 8, board, piecePosition);


        return movelist;
    }

    /**
     *
     * @param board The current board
     * @param piecePosition The current position of the piece on the board
     * @return the list of valid moves that a knight can make
     */
    public Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.add(new ChessPosition(piecePosition.rowPos+1, piecePosition.colPos+2));
        directions.add(new ChessPosition(piecePosition.rowPos-1, piecePosition.colPos+2));
        directions.add(new ChessPosition(piecePosition.rowPos+1, piecePosition.colPos-2));
        directions.add(new ChessPosition(piecePosition.rowPos-1, piecePosition.colPos-2));
        directions.add(new ChessPosition(piecePosition.rowPos+2, piecePosition.colPos+1));
        directions.add(new ChessPosition(piecePosition.rowPos-2, piecePosition.colPos+1));
        directions.add(new ChessPosition(piecePosition.rowPos+2, piecePosition.colPos-1));
        directions.add(new ChessPosition(piecePosition.rowPos-2, piecePosition.colPos-1));

        HashSet<ChessMove> movelist = calculateMoves(directions, 1, board, piecePosition);


        return movelist;
    }

    /**
     *
     * @param board The current board
     * @param piecePosition The current position of the piece on the board
     * @return the list of valid moves that a rook can make
     */
    public Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition piecePosition) {

        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getHorVerDirections(piecePosition));
        HashSet<ChessMove> movelist = calculateMoves(directions, 8, board, piecePosition);

        return movelist;
    }

    /**
     *
     * @param board The current board
     * @param piecePosition The current position of the piece on the board
     * @return the list of valid moves that a pawn can make
     */
    public Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        HashSet<ChessMove> movelist = new HashSet<ChessMove>();

        if(selfTeam == ChessGame.TeamColor.WHITE){
            directions.add(new ChessPosition(piecePosition.rowPos+1, piecePosition.colPos));
            if (piecePosition.rowPos == 2){
                movelist.addAll(calculateMoves(directions, 2, board, piecePosition));
            }else{
                movelist.addAll(calculateMoves(directions, 1, board, piecePosition));
            }
            directions.clear();
            directions.add(new ChessPosition(piecePosition.rowPos+1, piecePosition.colPos+1));
            directions.add(new ChessPosition(piecePosition.rowPos+1, piecePosition.colPos-1));
            movelist.addAll(calculateMoves(directions, 0, board, piecePosition));
            if (piecePosition.rowPos == 7){
                for (ChessMove move : movelist){
                    move.promotePiece = PieceType.QUEEN;
                }
            }
        }else {
            directions.add(new ChessPosition(piecePosition.rowPos - 1, piecePosition.colPos));
            if (piecePosition.rowPos == 7) {
                movelist.addAll(calculateMoves(directions, 2, board, piecePosition));
            } else {
                movelist.addAll(calculateMoves(directions, 1, board, piecePosition));
            }
            directions.clear();
            directions.add(new ChessPosition(piecePosition.rowPos - 1, piecePosition.colPos + 1));
            directions.add(new ChessPosition(piecePosition.rowPos - 1, piecePosition.colPos - 1));
            movelist.addAll(calculateMoves(directions, 0, board, piecePosition));

        }




        return movelist;
    }

    /**
     *
     * @param currentPos The current position of the piece
     * @return the positions above, below, to the right, and to the left
     */
    public Collection<ChessPosition> getHorVerDirections(ChessPosition currentPos) {
        HashSet<ChessPosition> output = new HashSet<ChessPosition>();

        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        output.add(new ChessPosition(currentRow + 1, currentCol));
        output.add(new ChessPosition(currentRow, currentCol - 1));
        output.add(new ChessPosition(currentRow - 1, currentCol));
        output.add(new ChessPosition(currentRow, currentCol + 1));

        return output;
    }

    /**
     *
     * @param currentPos
     * @return the coordinates in all diagonal directions
     */
    public Collection<ChessPosition> getDiagDirections(ChessPosition currentPos) {
        HashSet<ChessPosition> output = new HashSet<ChessPosition>();

        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        output.add(new ChessPosition(currentRow + 1, currentCol+1));
        output.add(new ChessPosition(currentRow+1, currentCol - 1));
        output.add(new ChessPosition(currentRow - 1, currentCol-1));
        output.add(new ChessPosition(currentRow-1, currentCol + 1));

        return output;
    }

    /**
     *
     * @param directions
     * @param max_distance
     * @param board
     * @param piecePosition
     * @return all valid moves in the directions and distance given
     */
    public HashSet<ChessMove> calculateMoves(ArrayList<ChessPosition> directions, int max_distance, ChessBoard board, ChessPosition piecePosition){
        int distance = 0;
        HashSet<ChessMove> output = new HashSet<ChessMove>();
        ChessPosition referencePosition = new ChessPosition(piecePosition.rowPos ,piecePosition.colPos);
        for (int i = 0; i < directions.size(); i++){
            boolean invalid = false;
            distance = 1;
            while (distance <= max_distance && !invalid) {
                referencePosition.colPos += directions.get(i).colPos - piecePosition.colPos;
                referencePosition.rowPos += directions.get(i).rowPos - piecePosition.rowPos;
                ChessPosition targetPosition = new ChessPosition(0 ,0);
                targetPosition.colPos = referencePosition.colPos;
                targetPosition.rowPos = referencePosition.rowPos;
                try {
                    board.getPiece(targetPosition);
                } catch (ArrayIndexOutOfBoundsException e){
                    invalid = true;
                }
                if (!invalid) {
                    if (board.getPiece(targetPosition) == null && targetPosition.isOnBoard()) {
                        if (selfType == PieceType.PAWN) {
                            if (targetPosition.getRow() == 8 && selfTeam == ChessGame.TeamColor.WHITE || targetPosition.getRow() == 1 && selfTeam == ChessGame.TeamColor.BLACK) {
                                output.add(new ChessMove(piecePosition, targetPosition, PieceType.QUEEN));
                                output.add(new ChessMove(piecePosition, targetPosition, PieceType.ROOK));
                                output.add(new ChessMove(piecePosition, targetPosition, PieceType.KNIGHT));
                                output.add(new ChessMove(piecePosition, targetPosition, PieceType.BISHOP));
                            } else {
                                output.add(new ChessMove(piecePosition, targetPosition, null));
                            }
                        }else{
                            output.add(new ChessMove(piecePosition, targetPosition, null));
                        }
                    } else if (board.getPiece(targetPosition).selfTeam != selfTeam){
                        if(selfType != PieceType.PAWN) {
                            output.add(new ChessMove(piecePosition, targetPosition, null));
                        }
                        invalid = true;
                    }else {
                        invalid = true;
                    }
                }
                distance += 1;
            }
            if (max_distance == 0){
                System.out.println("max distance is zero");
                referencePosition.colPos += directions.get(i).colPos - piecePosition.colPos;
                referencePosition.rowPos += directions.get(i).rowPos - piecePosition.rowPos;
                ChessPosition targetPosition = new ChessPosition(0 ,0);
                targetPosition.colPos = referencePosition.colPos;
                targetPosition.rowPos = referencePosition.rowPos;
                if (board.getPiece(referencePosition) != null) {
                    if (board.getPiece(targetPosition).selfTeam != selfTeam) {
                        System.out.println("enemy in range");
                        if (targetPosition.getRow() == 8 && selfTeam == ChessGame.TeamColor.WHITE || targetPosition.getRow() == 1 && selfTeam == ChessGame.TeamColor.BLACK) {
                            output.add(new ChessMove(piecePosition, targetPosition, PieceType.QUEEN));
                            output.add(new ChessMove(piecePosition, targetPosition, PieceType.BISHOP));
                            output.add(new ChessMove(piecePosition, targetPosition, PieceType.KNIGHT));
                            output.add(new ChessMove(piecePosition, targetPosition, PieceType.ROOK));
                        }else{
                            output.add(new ChessMove(piecePosition, targetPosition, null));
                        }
                        invalid = true;
                    }
                }
            }
            referencePosition.colPos = piecePosition.colPos;
            referencePosition.rowPos = piecePosition.rowPos;
        }
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return selfTeam == that.selfTeam && selfType == that.selfType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(selfTeam, selfType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "selfTeam=" + selfTeam +
                ", selfType=" + selfType +
                '}';
    }
}
