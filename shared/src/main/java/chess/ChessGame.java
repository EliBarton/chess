package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {


    TeamColor turn = TeamColor.WHITE;
    ChessBoard gameBoard = new ChessBoard();

    public ChessGame() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    /*
    Invalid if:
    King gets moved to check

     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        HashSet<ChessMove> output = new HashSet<>();
        if (piece == null){
            return null;
        }
        ArrayList<ChessMove> baseMoves = new ArrayList<>(piece.pieceMoves(gameBoard, startPosition));
        ChessBoard currentBoard;
        System.out.println(gameBoard);
        for (ChessMove baseMove : baseMoves) {
            currentBoard = gameBoard.clone();
            baseMoves = new ArrayList<>(piece.pieceMoves(currentBoard, startPosition));
            try {
                gameBoard.makeMove(baseMove);

                if (!isInCheck(piece.getTeamColor())){
                    //System.out.println("added new move to list from " + piece + ": " + baseMove);
                    output.add(baseMove);
                }

                //System.out.println(gameBoard + "\n\n" + currentBoard);
            } catch (InvalidMoveException e) {
                System.err.println(e);
                //System.out.println(gameBoard + "\n\n" + currentBoard);
            }
            gameBoard = currentBoard.clone();
            setTeamTurn(TeamColor.WHITE);
        }

        /*for (ChessMove baseMove : baseMoves) {
            currentBoard = gameBoard.clone();
            try {
                makeMove(baseMove);
            }catch (InvalidMoveException e){
                continue;
            }
            if (isInCheck(getTeamTurn())) {
                System.out.println("king is in check by " + piece.getPieceType() + " at " + startPosition);
                continue;
            }
            output.add(baseMove);
            gameBoard = currentBoard.clone();
        }*/
        return output;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();

        ChessBoard oldBoard = gameBoard.clone();
        try {
            if (gameBoard.getPiece(startPos).getTeamColor() != getTeamTurn()){
                throw new InvalidMoveException("Piece moved out of turn");
            }
            gameBoard.makeMove(move);
            if (isInCheck(getTeamTurn())){
                throw new InvalidMoveException("King moved into check");
            }
        }catch (InvalidMoveException e){
            System.err.println(e.getMessage());
            gameBoard = oldBoard.clone();
            throw new InvalidMoveException(e.getMessage());
        }
        if (getTeamTurn() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }else{
            setTeamTurn(TeamColor.WHITE);
        }


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<HashSet<ChessMove>> enemyEndMoves = new ArrayList<>();
        ArrayList<ChessPosition> enemyEndPositions = new ArrayList<>();
        ChessPosition kingPos = null;
        for (int i = 0; i < gameBoard.board.length; i++){
            for (int k = 0; k < gameBoard.board[i].length; k++){
                ChessPiece square = gameBoard.board[i][k];

                if (square != null){
                    if (square.getPieceType() == ChessPiece.PieceType.KING){
                        System.out.println("King at");
                        System.out.println(new ChessPosition(i+1, k+1));
                        //kingPos = new ChessPosition(i+1, k+1);
                    }
                    if (square.getTeamColor() != teamColor){
                        //System.out.println("It is " + teamColor + "'s turn.");
                        //System.out.println(square.getTeamColor() + " is attempting to attack the king");
                        //System.out.println(square.pieceMoves(gameBoard, new ChessPosition(i+1, k+1)));
                        enemyEndMoves.add(square.pieceMoves(gameBoard, new ChessPosition(i+1, k+1)));
                    }else{
                        if (square.getPieceType() == ChessPiece.PieceType.KING){

                            kingPos = new ChessPosition(i+1, k+1);
                        }
                    }
                }
            }
        }
        for (HashSet<ChessMove> moveList : enemyEndMoves){
            for (ChessMove move : moveList){
                enemyEndPositions.add(move.getEndPosition());
            }
        }
        if (kingPos == null){
            System.out.println("The " + teamColor + "'s enemy king is nowhere to be found");
        }
        for (ChessPosition endPos : enemyEndPositions){
            //System.out.println(endPos + " " + kingPos);
            if (endPos.equals(kingPos)){
                System.out.println("king is in check");
                return true;
            }
        }
        System.out.println("The king is not in check at " + kingPos);
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * In Check and no valid moves to make
     * Try all moves, see if they get you out of check
     * else return true
     *
     * method1: Clone board for every move, check if is in check.
     * method2: allow for an undo of a move. Apply move, check if is in check, undo, continue.
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            ChessPosition kingPos = null;
            for (int i = 0; i < gameBoard.board.length; i++){
                for (int k = 0; k < gameBoard.board[i].length; k++){
                    ChessPiece square = gameBoard.board[i][k];
                    if (square != null){
                        if (square.getTeamColor() == teamColor){
                            if (square.getPieceType() == ChessPiece.PieceType.KING){
                                kingPos = new ChessPosition(i+1, k+1);
                            }
                        }
                    }
                }
            }
            return validMoves(kingPos).isEmpty();
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            for (int i = 0; i < gameBoard.board.length; i++){
                for (int k = 0; k < gameBoard.board[i].length; k++){
                    ChessPiece square = gameBoard.board[i][k];
                    if (square != null){
                        if (square.getTeamColor() == teamColor){
                            if (!validMoves(new ChessPosition( i+1, k+1)).isEmpty()){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
