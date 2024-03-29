package ui;

import chess.*;
import dataAccess.GameAccess;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    public static Scanner reader = new Scanner(System.in);
    public static ServerFacade serverFacade = new ServerFacade("http://localhost:8080");

    private static String auth;
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        startMenu();

    }

    public static void startMenu(){
        System.out.println("\n");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Quit");
        System.out.println("4. Help");
        System.out.println("\n");
        int input = reader.nextInt();

        switch (input){
            case 1 -> loginPrompt();
            case 2 -> registerPrompt();
            case 3 -> {}
            case 4 -> printHelpStart();
        }
    }

    private static void loginPrompt(){
        System.out.println("Enter your username: ");
        String username = reader.next();
        System.out.println("Enter your password: ");
        String password = reader.next();
        // if username or password is invalid, say so
        try {
            auth = serverFacade.login(username, password).authToken();
            postLoginMenu();
        } catch (IOException e) {
            System.out.println("There was an error logging in: " + e.getMessage());
            startMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            startMenu();
        }

    }

    private static void registerPrompt(){
        System.out.println("Enter your username: ");
        String username = reader.next();
        System.out.println("Enter your password: ");
        String password = reader.next();
        System.out.println("Enter your email: ");
        String email = reader.next();
        // if username or password or email is invalid, say so
        try{
            auth = serverFacade.register(username, password, email).authToken();
        } catch (IOException e) {
            System.out.println("There was an error with your registration: " + e.getMessage());
            startMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            startMenu();
        }
        postLoginMenu();
    }

    private static void printHelpStart(){
        System.out.println("Here are the options; type the number:");
        startMenu();
    }

    private static void printHelpPostLogin(){
        System.out.println("Here are the options; type the number:");
        postLoginMenu();
    }

    public static void postLoginMenu(){
        System.out.println("\n");
        System.out.println("1. Create Game");
        System.out.println("2. List Games");
        System.out.println("3. Join Game");
        System.out.println("4. Join Game as Observer");
        System.out.println("5. Logout");
        System.out.println("6. Help");
        System.out.println("\n");
        int input = reader.nextInt();

        switch (input){
            case 1 -> createGamePrompt();
            case 2 -> listGamesPrompt();
            case 3 -> joinGamePrompt();
            case 4 -> joinGameObserverPrompt();
            case 5 -> logoutPrompt();
            case 6 -> printHelpPostLogin();
        }
    }

    private static void createGamePrompt(){
        reader.nextLine();
        System.out.println("Enter game name: ");
        String gameName = reader.nextLine();
        try{
            serverFacade.createGame(gameName, auth);
            System.out.println("Game '" + gameName + "' created.");
            postLoginMenu();
        } catch (IOException e) {
            System.out.println("There was an error creating your game: " + e.getMessage());
            postLoginMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            postLoginMenu();
        }
    }

    private static void listGamesPrompt(){
        System.out.println("Current games:");
        try {
            GameAccess.ListGamesResult result = serverFacade.listGames(auth);
            for (GameAccess.SerializedGameData game : result.games()){
                System.out.println(game.gameID() + " - " + game.gameName());
                System.out.println("White: " + game.whiteUsername() + "   Black: " + game.blackUsername());
                System.out.println();
            }
            postLoginMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was an error listing the games: " + e.getMessage());
            postLoginMenu();
        } catch (IOException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            postLoginMenu();
        }
    }

    private static void joinGamePrompt(){
        System.out.println("Enter the number representing the game you want to join: ");
        int gameID = reader.nextInt();
        System.out.println("Enter the color you want to play as, WHITE or BLACK");
        String color = reader.next();
        try{
            serverFacade.joinGame(auth, color, gameID);
            GameBoard.draw();
        } catch (IOException e) {
            System.out.println("There was an error joining the game: " + e.getMessage());
            postLoginMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            postLoginMenu();
        }

    }

    private static void joinGameObserverPrompt(){
        System.out.println("Enter the number representing the game you want to observe: ");
        int gameID = reader.nextInt();
        try{
            serverFacade.joinGame(auth, null, gameID);
            GameBoard.draw();
        } catch (IOException e) {
            System.out.println("There was an error observing the game: " + e.getMessage());
            postLoginMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            postLoginMenu();
        }

    }

    private static void logoutPrompt(){
        try{
            serverFacade.logout(auth);
            System.out.println("You have been logged out.");
            startMenu();
        } catch (IOException e) {
            System.out.println("There was an error logging out: " + e.getMessage());
            postLoginMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            postLoginMenu();
        }
    }
}