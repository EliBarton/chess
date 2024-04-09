package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import chess.*;
import ui.EscapeSequences.*;

import static ui.EscapeSequences.*;

public class GameBoard {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 1;

    private static final String[] columnNames = {"\u2003a ", "\u2003b ", "\u2003c ", "\u2003d ", "\u2003e ", "\u2003f ", "\u2003g ", "\u2003h "};

    public static void draw(ChessGame.TeamColor color, ChessBoard board){

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (color == ChessGame.TeamColor.BLACK) {
            drawBlack(out, board);
        }else{
            drawWhite(out, board);
        }
        System.out.println();

    }

    private static void drawWhite(PrintStream out, ChessBoard board){
        drawBoard(out, 1, board);
    }

    private static void drawBlack(PrintStream out, ChessBoard board){
        drawBoard(out, -1, board);
    }

    private static void drawBoard(PrintStream out, int side, ChessBoard board){
        drawColumnNames(out, side);

        for (int row = 0; Math.abs(row) < Math.abs((BOARD_SIZE) * side); row -= side){
            int realRow;
            if (side == 1){
                realRow = row + BOARD_SIZE-1;
            }else{
                realRow = row;
            }
            out.print("\u2003" + (realRow + 1) + "\u2003");
            drawRowSquares(out, realRow, side, board);
            out.print("\u2003" + (realRow + 1) + "\u2003");
            out.println();
        }
        drawColumnNames(out, side);
    }

    private static void drawColumnNames(PrintStream out, int side) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY);
        for (int column = 0; Math.abs(column) < Math.abs(BOARD_SIZE * side); column += side){
            if (side == -1){
                out.print(columnNames[column + BOARD_SIZE-1]);
            }else{
                out.print(columnNames[column]);
            }
        }
        out.println(EMPTY);
    }

    private static void drawRowSquares(PrintStream out, int row, int side, ChessBoard board) {

        for (int column = 0; Math.abs(column) < Math.abs(BOARD_SIZE * side); column += side){
            int realColumn;
            if (side == -1){
                realColumn = column + BOARD_SIZE-1;
            }else{
                realColumn = column;
            }
            if ((row +realColumn) % 2 == 1) {
                out.print(SET_BG_COLOR_DARK_GREY);}
            else{
                out.print(SET_BG_COLOR_LIGHT_GREY);}
            if (board.getPiece(new ChessPosition(row + 1, realColumn+1)) == null){
                out.print(EMPTY_SQUARE);
            }else {
                if (board.getPiece(
                        new ChessPosition(row + 1, realColumn + 1)).getTeamColor() == ChessGame.TeamColor.WHITE){
                    switch (board.getPiece(new ChessPosition(row + 1, realColumn + 1)).getPieceType()) {
                        case ChessPiece.PieceType.PAWN -> out.print(WHITE_PAWN);
                        case ChessPiece.PieceType.QUEEN -> out.print(WHITE_QUEEN);
                        case ChessPiece.PieceType.KING -> out.print(WHITE_KING);
                        case ChessPiece.PieceType.BISHOP -> out.print(WHITE_BISHOP);
                        case ChessPiece.PieceType.KNIGHT -> out.print(WHITE_KNIGHT);
                        case ChessPiece.PieceType.ROOK -> out.print(WHITE_ROOK);
                    }
                } else {
                    switch (board.getPiece(new ChessPosition(row + 1, realColumn + 1)).getPieceType()) {
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
