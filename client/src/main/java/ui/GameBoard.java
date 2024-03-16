package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import ui.EscapeSequences.*;

import static ui.EscapeSequences.*;

public class GameBoard {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 1;



    public static void draw(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBlack();
        drawWhite(out);
    }

    private static void drawWhite(PrintStream out){
        drawRows(out);
    }

    private static void drawBlack(){

    }

    private static void drawRows(PrintStream out){
        for (int square = 0; square < BOARD_SIZE; ++square){
            for (int column = 0; column < BOARD_SIZE; ++column){
                if ((square+column) % 2 == 1) {out.print(SET_BG_COLOR_BLACK);}
                else{out.print(SET_BG_COLOR_WHITE);}
                out.print(WHITE_BISHOP);
                out.print(RESET_BG_COLOR);
            }
            out.println();
        }

    }
}
