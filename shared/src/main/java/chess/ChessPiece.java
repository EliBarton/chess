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

    public Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition piecePosition) {

        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getDiagDirections(piecePosition));
        directions.addAll(getHorVerDirections(piecePosition));
        ArrayList<ChessMove> movelist = calculateMoves(directions, 1, board, piecePosition);

//        int distance = 1;
//        int currentRow = piecePosition.getRow();
//        int currentCol = piecePosition.getColumn();
//        for (int i = 0; i < directions; i++) {
//            switch (i + 1) {
//                case 4:
//                    newDirection = new ChessPosition(currentRow + 1, currentCol);
//                    break;
//                case 5:
//                    newDirection = new ChessPosition(currentRow + 1, currentCol - 1);
//                    break;
//                case 6:
//                    newDirection = new ChessPosition(currentRow, currentCol - 1);
//                    break;
//                case 7:
//                    newDirection = new ChessPosition(currentRow - 1, currentCol - 1);
//                    break;
//                case 8:
//                    newDirection = new ChessPosition(currentRow - 1, currentCol);
//                    break;
//                case 1:
//                    newDirection = new ChessPosition(currentRow - 1, currentCol + 1);
//                    break;
//                case 2:
//                    newDirection = new ChessPosition(currentRow, currentCol + 1);
//                    break;
//                case 3:
//                    newDirection = new ChessPosition(currentRow + 1, currentCol + 1);
//                    break;
//                default:
//                    newDirection = new ChessPosition(currentRow, currentCol);
//                    System.out.println("Failed");
//                    System.out.println("expected a number 1-8. Instead got " + (i + 1));
//            }
//
//            directions.add(newDirection);
//        }
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
//            newDirection = new ChessPosition(i % 2, k % 2);
//            movelist.add(new ChessMove(piecePosition, newDirection, null));
//            i = Math.abs(i);
//            System.out.println(newDirection.toString());
//        }
        System.out.println(movelist);
        return movelist;
    }

    public Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getDiagDirections(piecePosition));
        directions.addAll(getHorVerDirections(piecePosition));

        ArrayList<ChessMove> movelist = calculateMoves(directions, 8, board, piecePosition);


        return movelist;
    }

    public Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getDiagDirections(piecePosition));

        ArrayList<ChessMove> movelist = calculateMoves(directions, 8, board, piecePosition);


        return movelist;
    }

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

        ArrayList<ChessMove> movelist = calculateMoves(directions, 1, board, piecePosition);


        return movelist;
    }

    public Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition piecePosition) {

        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.addAll(getHorVerDirections(piecePosition));
        ArrayList<ChessMove> movelist = calculateMoves(directions, 8, board, piecePosition);

        return movelist;
    }

    public Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> movelist = new ArrayList<ChessMove>();


        return movelist;
    }

    public ArrayList<ChessPosition> getHorVerDirections(ChessPosition currentPos) {
        ArrayList<ChessPosition> output = new ArrayList<ChessPosition>();

        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        output.add(new ChessPosition(currentRow + 1, currentCol));
        output.add(new ChessPosition(currentRow, currentCol - 1));
        output.add(new ChessPosition(currentRow - 1, currentCol));
        output.add(new ChessPosition(currentRow, currentCol + 1));

        return output;
    }
    public ArrayList<ChessPosition> getDiagDirections(ChessPosition currentPos) {
        ArrayList<ChessPosition> output = new ArrayList<ChessPosition>();

        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        output.add(new ChessPosition(currentRow + 1, currentCol+1));
        output.add(new ChessPosition(currentRow+1, currentCol - 1));
        output.add(new ChessPosition(currentRow - 1, currentCol-1));
        output.add(new ChessPosition(currentRow-1, currentCol + 1));

        return output;
    }

    public ArrayList<ChessMove> calculateMoves(ArrayList<ChessPosition> directions, int max_distance, ChessBoard board, ChessPosition piecePosition){
        int distance = 0;
        ArrayList<ChessMove> output = new ArrayList<ChessMove>();
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
                        output.add(new ChessMove(piecePosition, targetPosition, null));
                    } else if (board.getPiece(targetPosition).selfTeam != selfTeam){
                        output.add(new ChessMove(piecePosition, targetPosition, null));
                        invalid = true;
                    }else {
                        invalid = true;
                    }
                }
                distance += 1;
            }
            referencePosition.colPos = piecePosition.colPos;
            referencePosition.rowPos = piecePosition.rowPos;
        }
        return output;
    }
}
