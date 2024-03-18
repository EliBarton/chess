package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences.*;

import static ui.EscapeSequences.*;

public class GameBoard {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 1;

    private static final ChessBoard baseBoard = new ChessBoard();

    private static final String[] columnNames = {"\u2003a ", "\u2003b ", "\u2003c ", "\u2003d ", "\u2003e ", "\u2003f ", "\u2003g ", "\u2003h "};

    public static void draw(){

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBlack();
        drawWhite(out);
    }

    private static void drawWhite(PrintStream out){
        drawBoard(out);
    }

    private static void drawBlack(){

    }

    private static void drawBoard(PrintStream out){
        drawColumnNames(out);

        for (int row = 0; row < BOARD_SIZE; ++row){
            out.print("\u2003" + row + "\u2003");
            drawRowSquares(out, row);
            out.print("\u2003" + row + "\u2003");
            out.println();
        }
        drawColumnNames(out);
    }

    private static void drawColumnNames(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY);
        for (int column = 0; column < BOARD_SIZE; ++column){
            out.print(columnNames[column]);
        }
        out.println(EMPTY);
    }

    private static void drawRowSquares(PrintStream out, int row) {
        baseBoard.resetBoard();
        for (int column = 0; column < BOARD_SIZE; ++column){
            if ((row +column) % 2 == 1) {
                out.print(SET_BG_COLOR_DARK_GREY);}
            else{
                out.print(SET_BG_COLOR_LIGHT_GREY);}
            if (baseBoard.getPiece(new ChessPosition(row + 1, column+1)) == null){
                out.print(EMPTY_SQUARE);
            }else {
                if (baseBoard.getPiece(
                        new ChessPosition(row + 1, column + 1)).getTeamColor() == ChessGame.TeamColor.WHITE){
                    switch (baseBoard.getPiece(new ChessPosition(row + 1, column + 1)).getPieceType()) {
                        case ChessPiece.PieceType.PAWN -> out.print(WHITE_PAWN);
                        case ChessPiece.PieceType.QUEEN -> out.print(WHITE_QUEEN);
                        case ChessPiece.PieceType.KING -> out.print(WHITE_KING);
                        case ChessPiece.PieceType.BISHOP -> out.print(WHITE_BISHOP);
                        case ChessPiece.PieceType.KNIGHT -> out.print(WHITE_KNIGHT);
                        case ChessPiece.PieceType.ROOK -> out.print(WHITE_ROOK);
                    }
                } else {
                    switch (baseBoard.getPiece(new ChessPosition(row + 1, column + 1)).getPieceType()) {
                        case ChessPiece.PieceType.PAWN -> out.print(BLACK_PAWN);
                        case ChessPiece.PieceType.QUEEN -> out.print(BLACK_QUEEN);
                        case ChessPiece.PieceType.KING -> out.print(BLACK_KING);
                        case ChessPiece.PieceType.BISHOP -> out.print(BLACK_BISHOP);
                        case ChessPiece.PieceType.KNIGHT -> out.print(BLACK_KNIGHT);
                        case ChessPiece.PieceType.ROOK -> out.print(BLACK_ROOK);
                    }
                }

            }
            out.print(RESET_BG_COLOR);
        }
    }
}
