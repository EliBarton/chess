import chess.*;

import java.util.Scanner;

public class Main {
    public static Scanner reader;
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        startMenu();

    }

    public static void startMenu(){
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
        int input = reader.nextInt();

        switch (input){
            case 1 -> loginPrompt();
        }
    }

    private static void loginPrompt(){
        System.out.println("Enter your username: ");
        String username = reader.nextLine();

    }
}