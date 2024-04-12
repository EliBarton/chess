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
public class ChessPiece implements Cloneable {

    ChessGame.TeamColor selfTeam;

    PieceType selfType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        selfTeam = pieceColor;
        selfType = type;
    }

    @Override
    public ChessPiece clone() {
        try {
            ChessPiece clone = (ChessPiece) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return selfTeam == piece.selfTeam && selfType == piece.selfType;
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
            case KNIGHT -> output = getKnightMoves(myPosition, board);
            case KING -> output = getKingMoves(myPosition, board);
            case QUEEN -> output = getQueenMoves(myPosition, board);
            case BISHOP -> output = getBishopMoves(myPosition, board);
            case ROOK -> output = getRookMoves(myPosition, board);
            case PAWN -> output = getPawnMoves(myPosition, board);
        }
        return output;
    }

    public HashSet<ChessMove> getKnightMoves(ChessPosition startPos, ChessBoard board){
        HashSet<ChessMove> output = new HashSet<ChessMove>();

        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();
        directions.add(new ChessPosition(startPos.getRow()+2, startPos.getColumn()+1));
        directions.add(new ChessPosition(startPos.getRow()+2, startPos.getColumn()-1));
        directions.add(new ChessPosition(startPos.getRow()-2, startPos.getColumn()+1));
        directions.add(new ChessPosition(startPos.getRow()-2, startPos.getColumn()-1));
        directions.add(new ChessPosition(startPos.getRow()+1, startPos.getColumn()+2));
        directions.add(new ChessPosition(startPos.getRow()+1, startPos.getColumn()-2));
        directions.add(new ChessPosition(startPos.getRow()-1, startPos.getColumn()+2));
        directions.add(new ChessPosition(startPos.getRow()-1, startPos.getColumn()-2));

        output = calculateMoves(startPos, board, directions, 1);

        return output;
    }
    public HashSet<ChessMove> getKingMoves(ChessPosition startPos, ChessBoard board){
        HashSet<ChessMove> output = new HashSet<ChessMove>();

        ArrayList<ChessPosition> directions = getDiagDirections(startPos);
        directions.addAll(getHorVerDirections(startPos));

        output = calculateMoves(startPos, board, directions, 1);

        return output;
    }
    public HashSet<ChessMove> getQueenMoves(ChessPosition startPos, ChessBoard board){
        HashSet<ChessMove> output = new HashSet<ChessMove>();

        ArrayList<ChessPosition> directions = getDiagDirections(startPos);
        directions.addAll(getHorVerDirections(startPos));

        output = calculateMoves(startPos, board, directions, 8);

        return output;
    }

    public HashSet<ChessMove> getRookMoves(ChessPosition startPos, ChessBoard board){
        HashSet<ChessMove> output = new HashSet<ChessMove>();

        ArrayList<ChessPosition> directions = getHorVerDirections(startPos);

        output = calculateMoves(startPos, board, directions, 8);

        return output;
    }
    public HashSet<ChessMove> getBishopMoves(ChessPosition startPos, ChessBoard board){
        HashSet<ChessMove> output = new HashSet<ChessMove>();

        ArrayList<ChessPosition> directions = getDiagDirections(startPos);

        output = calculateMoves(startPos, board, directions, 8);

        return output;
    }
    public HashSet<ChessMove> getPawnMoves(ChessPosition startPos, ChessBoard board){
        HashSet<ChessMove> output = new HashSet<ChessMove>();

        ArrayList<ChessPosition> directions = new ArrayList<ChessPosition>();

        if (selfTeam == ChessGame.TeamColor.WHITE){
            directions.add(new ChessPosition(startPos.getRow() + 1, startPos.getColumn()));
            if (startPos.getRow() == 2){
                output.addAll(calculateMoves(startPos, board, directions, 2));
                addValidMovesWhitePawn(startPos, board, output);
            } else if (startPos.getRow() == 7) {
                ChessPosition referencePos = new ChessPosition(startPos.getRow()+1, startPos.getColumn()+1);
                addValidDiagMove(startPos, board, output, directions, referencePos);
                referencePos = new ChessPosition(startPos.getRow()+1, startPos.getColumn()-1);
                addValidDiagMove(startPos, board, output, directions, referencePos);
            }else{
                output.addAll(calculateMoves(startPos, board, directions, 1));
                addValidMovesWhitePawn(startPos, board, output);
            }
        }else{
            directions.add(new ChessPosition(startPos.getRow() - 1, startPos.getColumn()));
            if (startPos.getRow() == 7){
                output.addAll(calculateMoves(startPos, board, directions, 2));
                addValidMovesBlackPawn(startPos, board, output);
            } else if (startPos.getRow() == 2) {
                ChessPosition referencePos = new ChessPosition(startPos.getRow()-1, startPos.getColumn()+1);
                addValidDiagMove(startPos, board, output, directions, referencePos);
                referencePos = new ChessPosition(startPos.getRow()-1, startPos.getColumn()-1);
                addValidDiagMove(startPos, board, output, directions, referencePos);
            }else{
                output.addAll(calculateMoves(startPos, board, directions, 1));
                addValidMovesBlackPawn(startPos, board, output);
            }
        }

        return output;
    }

    private void addValidMovesWhitePawn(ChessPosition startPos, ChessBoard board, HashSet<ChessMove> output) {
        ChessPosition referencePos = new ChessPosition(startPos.getRow()+1, startPos.getColumn()+1);
        if (referencePos.isOnBoard()) {
            addCloneMove(startPos, board, output, referencePos);
        }
        referencePos = new ChessPosition(startPos.getRow()+1, startPos.getColumn()-1);
        if (referencePos.isOnBoard()) {
            addCloneMove(startPos, board, output, referencePos);
        }
    }

    private void addValidMovesBlackPawn(ChessPosition startPos, ChessBoard board, HashSet<ChessMove> output) {
        ChessPosition referencePos = new ChessPosition(startPos.getRow()-1, startPos.getColumn()-1);
        if (referencePos.isOnBoard()) {
            addCloneMove(startPos, board, output, referencePos);
        }
        referencePos = new ChessPosition(startPos.getRow()-1, startPos.getColumn()+1);
        if (referencePos.isOnBoard()) {
            addCloneMove(startPos, board, output, referencePos);
        }
    }

    private void addCloneMove(ChessPosition startPos, ChessBoard board, HashSet<ChessMove> output, ChessPosition referencePos) {
        if (board.getPiece(referencePos) != null) {
            if (board.getPiece(referencePos).selfTeam != selfTeam) {
                output.add(new ChessMove(startPos, referencePos.clone(), null));
            }
        }
    }

    private void addValidPromotions(ChessPosition startPos, ChessBoard board, HashSet<ChessMove> output, ChessPosition referencePos) {
        if (referencePos.isOnBoard()) {
            if (board.getPiece(referencePos) != null) {
                if (board.getPiece(referencePos).selfTeam != selfTeam) {
                    output.add(new ChessMove(startPos, referencePos.clone(), PieceType.QUEEN));
                    output.add(new ChessMove(startPos, referencePos.clone(), PieceType.ROOK));
                    output.add(new ChessMove(startPos, referencePos.clone(), PieceType.BISHOP));
                    output.add(new ChessMove(startPos, referencePos.clone(), PieceType.KNIGHT));
                }
            }
        }
    }


    private void addValidDiagMove(ChessPosition startPos, ChessBoard board, HashSet<ChessMove> output, ArrayList<ChessPosition> directions, ChessPosition referencePos) {
        addValidPromotions(startPos, board, output, referencePos);
        output.addAll(calculateMoves(startPos, board, directions, 1));
    }

    public HashSet<ChessMove> calculateMoves(ChessPosition startPos, ChessBoard board, ArrayList<ChessPosition> directions, int maxDistance){
        HashSet<ChessMove> output = new HashSet<ChessMove>();
        int distance = 0;
        ChessPosition targetPos = new ChessPosition(0, 0);

        for (int i = 0; i < directions.size(); i++){
            distance = 0;
            ChessPosition referencePos = new ChessPosition(0, 0);
            Boolean valid = true;

            while (distance < maxDistance){
                distance += 1;
                referencePos.rowPos = startPos.getRow() + (distance * (directions.get(i).getRow() - startPos.getRow()));
                referencePos.colPos = startPos.getColumn() + (distance * (directions.get(i).getColumn() - startPos.getColumn()));

                targetPos = referencePos.clone();
                if (targetPos.rowPos < 1 || targetPos.rowPos > 8 || targetPos.colPos < 1 || targetPos.colPos > 8){
                    break;
                }
                if (board.getPiece(targetPos) != null) {
                    if (board.getPiece(targetPos).getTeamColor() == selfTeam) {
                        break;
                    } else {
                        distance = 8;
                        if (selfType == PieceType.PAWN){
                            break;
                        }
                    }
                }
                if (getPieceType() != PieceType.PAWN) {
                    output.add(new ChessMove(startPos, targetPos, null));
                }else{
                    if (getTeamColor() == ChessGame.TeamColor.WHITE){
                        if (targetPos.rowPos == 8){
                            addPromotionMoves(startPos, output, targetPos);
                            break;
                        }
                        output.add(new ChessMove(startPos, targetPos, null));
                    }else{
                        if (targetPos.rowPos == 1){
                            addPromotionMoves(startPos, output, targetPos);
                        }
                        output.add(new ChessMove(startPos, targetPos, null));
                    }
                }
            }

        }


    return output;
    }

    private void addPromotionMoves(ChessPosition startPos, HashSet<ChessMove> output, ChessPosition targetPos) {
        output.add(new ChessMove(startPos, targetPos, PieceType.BISHOP));
        output.add(new ChessMove(startPos, targetPos, PieceType.QUEEN));
        output.add(new ChessMove(startPos, targetPos, PieceType.ROOK));
        output.add(new ChessMove(startPos, targetPos, PieceType.KNIGHT));
    }


    public ArrayList<ChessPosition> getHorVerDirections(ChessPosition startPos){
        ArrayList<ChessPosition> output = new ArrayList<ChessPosition>();

        output.add(new ChessPosition(startPos.getRow()+1, startPos.getColumn()));
        output.add(new ChessPosition(startPos.getRow(), startPos.getColumn()+1));
        output.add(new ChessPosition(startPos.getRow()-1, startPos.getColumn()));
        output.add(new ChessPosition(startPos.getRow(), startPos.getColumn()-1));

        return output;
    }

    public ArrayList<ChessPosition> getDiagDirections(ChessPosition startPos){
        ArrayList<ChessPosition> output = new ArrayList<ChessPosition>();

        output.add(new ChessPosition(startPos.getRow()+1, startPos.getColumn()+1));
        output.add(new ChessPosition(startPos.getRow()-1, startPos.getColumn()+1));
        output.add(new ChessPosition(startPos.getRow()-1, startPos.getColumn()-1));
        output.add(new ChessPosition(startPos.getRow()+1, startPos.getColumn()-1));

        return output;
    }

}
