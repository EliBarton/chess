package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    PieceType selfType;

    ChessGame.TeamColor selfTeam;
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
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return selfType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> output = new HashSet<ChessMove>();
        switch (selfType){
            case BISHOP -> output.addAll(getBishopMoves(myPosition, board));
            case ROOK -> output.addAll(getRookMoves(myPosition, board));
            case QUEEN -> output.addAll(getQueenMoves(myPosition, board));
            case KING -> output.addAll(getKingMoves(myPosition, board));
            case PAWN -> output.addAll(getPawnMoves(myPosition, board));
        }
        return output;
    }


    private ArrayList<ChessPosition> getDiagMoves(ChessPosition startPos){
        ArrayList<ChessPosition> output = new ArrayList<ChessPosition>();

        output.add(new ChessPosition(startPos.getRow() + 1, startPos.getColumn() + 1));
        output.add(new ChessPosition(startPos.getRow() + 1, startPos.getColumn() - 1));
        output.add(new ChessPosition(startPos.getRow() - 1, startPos.getColumn() + 1));
        output.add(new ChessPosition(startPos.getRow() - 1, startPos.getColumn() - 1));

        return output;
    }

    private ArrayList<ChessPosition> getHorVerMoves(ChessPosition startPos){
        ArrayList<ChessPosition> output = new ArrayList<ChessPosition>();

        output.add(new ChessPosition(startPos.getRow() + 1, startPos.getColumn()));
        output.add(new ChessPosition(startPos.getRow() - 1, startPos.getColumn()));
        output.add(new ChessPosition(startPos.getRow(), startPos.getColumn() + 1));
        output.add(new ChessPosition(startPos.getRow(), startPos.getColumn() - 1));

        return output;
    }

    private HashSet<ChessMove> getBishopMoves(ChessPosition startPos, ChessBoard board){
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();

        directions.addAll(getDiagMoves(startPos));

        HashSet<ChessMove> moveList = calculateMoves(startPos, 9, directions, board);
        return moveList;
    }

    private HashSet<ChessMove> getRookMoves(ChessPosition startPos, ChessBoard board){
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();

        directions.addAll(getHorVerMoves(startPos));

        HashSet<ChessMove> moveList = calculateMoves(startPos, 9, directions, board);
        return moveList;
    }

    private HashSet<ChessMove> getQueenMoves(ChessPosition startPos, ChessBoard board){
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();

        directions.addAll(getHorVerMoves(startPos));
        directions.addAll(getDiagMoves(startPos));

        HashSet<ChessMove> moveList = calculateMoves(startPos, 9, directions, board);
        return moveList;
    }

    private HashSet<ChessMove> getKingMoves(ChessPosition startPos, ChessBoard board){
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();

        directions.addAll(getHorVerMoves(startPos));
        directions.addAll(getDiagMoves(startPos));

        HashSet<ChessMove> moveList = calculateMoves(startPos, 2, directions, board);
        return moveList;
    }

    private HashSet<ChessMove> getPawnMoves(ChessPosition startPos, ChessBoard board){
        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        HashSet<ChessMove> output = new HashSet<ChessMove>();
        if (selfTeam == ChessGame.TeamColor.WHITE){
            directions.add(new ChessPosition(startPos.getRow()-1, startPos.getColumn()));
            if (startPos.getRow() == 2) {
                output.addAll(calculateMoves(startPos, 3, directions, board));
            }else{
                output.addAll(calculateMoves(startPos, 2, directions, board));
            }
        }else{
            directions.add(new ChessPosition(startPos.getRow()+1, startPos.getColumn()));
            if (startPos.getRow() == 7){
                output.addAll(calculateMoves(startPos, 3, directions, board));
            }else{
                output.addAll(calculateMoves(startPos, 2, directions, board));
            }
        }
        ChessPosition referencePos = new ChessPosition(0, 0);
        if (selfTeam == ChessGame.TeamColor.WHITE) {
            referencePos.rowPos = startPos.rowPos +1;
            referencePos.colPos = startPos.colPos +1;
            if (board.getPiece(referencePos) != null) {
                System.out.println("enemy in range of pwn");
                if (board.getPiece(referencePos).getTeamColor() != selfTeam) {
                    if (referencePos.rowPos == 8){
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.QUEEN));
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.KNIGHT));
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.BISHOP));
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.ROOK));
                    }else {
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), null));
                    }
                }
            }
            referencePos.rowPos = startPos.rowPos +1;
            referencePos.colPos = startPos.colPos -1;
        }else{
            referencePos.rowPos = startPos.rowPos -1;
            referencePos.colPos = startPos.colPos -1;
            if (board.getPiece(referencePos) != null) {
                if (board.getPiece(referencePos).getTeamColor() != selfTeam) {
                    if (referencePos.rowPos == 1){
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.QUEEN));
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.KNIGHT));
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.BISHOP));
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), PieceType.ROOK));
                    }else {
                        output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), null));
                    }
                }
            }
            referencePos.rowPos = startPos.rowPos -1;
            referencePos.colPos = startPos.colPos +1;
        }
        if (board.getPiece(referencePos) != null) {
            if (board.getPiece(referencePos).getTeamColor() != selfTeam) {
                output.add(new ChessMove(startPos, new ChessPosition(referencePos.rowPos, referencePos.colPos), null));
            }
        }
        return output;
    }

    private HashSet<ChessMove> calculateMoves(ChessPosition startPos, int max_distance, ArrayList<ChessPosition> directions, ChessBoard board){
        HashSet<ChessMove> output = new HashSet<ChessMove>();
        ChessPosition referencePos = new ChessPosition(0, 0);
        int distance = 0;
        for (int i = 0; i < directions.size(); i++){
            distance = 1;
            while (distance <= max_distance){
                boolean valid = true;
                ChessPosition targetPos;
                referencePos.colPos = directions.get(i).colPos + ((distance)*(startPos.colPos - directions.get(i).colPos));
                referencePos.rowPos = directions.get(i).rowPos + ((distance)*(startPos.rowPos - directions.get(i).rowPos));

                if (referencePos.colPos < 1 || referencePos.colPos > 8 || referencePos.rowPos < 1 || referencePos.rowPos > 8){
                    valid = false;
                }
                if (referencePos.equals(startPos)){
                    valid = false;
                }

                targetPos = referencePos.clone();

                if (valid){
                    if (board.getPiece(referencePos) != null) {
                        if (board.getPiece(referencePos).selfTeam != selfTeam) {
                            if (selfType != PieceType.PAWN) {
                                output.add(new ChessMove(startPos, targetPos, null));
                            }
                        }
                        break;

                    }
                    if (selfType != PieceType.PAWN) {
                        output.add(new ChessMove(startPos, targetPos, null));
                    }else if (selfTeam == ChessGame.TeamColor.WHITE && targetPos.rowPos == 8){
                        output.add(new ChessMove(startPos, targetPos, PieceType.KNIGHT));
                        output.add(new ChessMove(startPos, targetPos, PieceType.QUEEN));
                        output.add(new ChessMove(startPos, targetPos, PieceType.BISHOP));
                        output.add(new ChessMove(startPos, targetPos, PieceType.ROOK));
                    }else if (selfTeam == ChessGame.TeamColor.BLACK && targetPos.rowPos == 1) {
                        output.add(new ChessMove(startPos, targetPos, PieceType.KNIGHT));
                        output.add(new ChessMove(startPos, targetPos, PieceType.QUEEN));
                        output.add(new ChessMove(startPos, targetPos, PieceType.BISHOP));
                        output.add(new ChessMove(startPos, targetPos, PieceType.ROOK));
                    }else{
                        output.add(new ChessMove(startPos, targetPos, null));
                    }
                }

                distance += 1;
            }

        }


        return output;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return selfType == piece.selfType && selfTeam == piece.selfTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(selfType, selfTeam);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "selfType=" + selfType +
                ", selfTeam=" + selfTeam +
                '}';
    }
}
