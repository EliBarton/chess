package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import ui.EscapeSequences.*;

import static ui.EscapeSequences.*;

public class GameBoard {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 1;

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

        for (int row = 1; row < BOARD_SIZE; ++row){
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
        for (int column = 0; column < BOARD_SIZE; ++column){
            if ((row +column) % 2 == 1) {
                out.print(SET_BG_COLOR_DARK_GREY);}
            else{
                out.print(SET_BG_COLOR_LIGHT_GREY);}
            out.print(WHITE_BISHOP);
            out.print(RESET_BG_COLOR);
        }
    }
}
