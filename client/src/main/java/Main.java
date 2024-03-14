import chess.*;

import java.util.Scanner;

public class Main {
    public static Scanner reader = new Scanner(System.in);
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        startMenu();

    }

    public static void startMenu(){
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
        System.out.println("4. Help");
        int input = reader.nextInt();

        switch (input){
            case 1 -> loginPrompt();
            case 2 -> registerPrompt();
            case 3 -> {}
            case 4 -> printHelp();
        }
    }

    private static void loginPrompt(){
        System.out.println("Enter your username: ");
        String username = reader.next();
        System.out.println("Enter your password: ");
        String password = reader.next();
        // if username or password is invalid, say so

    }

    private static void registerPrompt(){
        System.out.println("Enter your username: ");
        String username = reader.next();
        System.out.println("Enter your password: ");
        String password = reader.next();
        System.out.println("Enter your email: ");
        String email = reader.next();
        // if username or password or email is invalid, say so
    }

    private static void printHelp(){
        System.out.println("Here are the options:");
        startMenu();
    }


}