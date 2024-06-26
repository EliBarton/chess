package ui;

import chess.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Client implements ServerMessageObserver{
    public static Scanner reader = new Scanner(System.in);
    public static ServerFacade serverFacade;

    private static boolean awaitingMessage = false;
    private static boolean awaitingInput = false;

    private static ArrayList<Thread> threads = new ArrayList<>();


    public void init(){
        try {
            serverFacade = new ServerFacade("localhost:8080", this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String auth;
    private static String name = "";
    private static int gameID = 0;

    private static String color = null;

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
        name = username;
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
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        try {
            int input = reader.nextInt();
            switch (input){
                case 1 -> createGamePrompt();
                case 2 -> listGamesPrompt();
                case 3 -> joinGamePrompt();
                case 4 -> joinGameObserverPrompt();
                case 5 -> logoutPrompt();
                case 6 -> printHelpPostLogin();
            }
        }catch (Exception ignored){

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
            ListGamesResult result = serverFacade.listGames(auth);
            for (SerializedGameData game : result.games()){
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
        gameID = reader.nextInt();
        System.out.println("Enter the color you want to play as, WHITE or BLACK");
        color = reader.next();

        try{
            serverFacade.joinGame(auth, color, gameID, name);
            awaitMessage();
        } catch (IOException e) {
            System.out.println("There was an error joining the game: " + e.getMessage());
            postLoginMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            postLoginMenu();
        } catch (Exception e) {
            System.out.println("There was a problem: " + e.getMessage());
            postLoginMenu();
        }

    }

    private static void awaitMessage() {
        awaitingMessage = true;
        while (awaitingMessage){
            if (!awaitingMessage){
                break;
            }
        }
    }


    private static void joinGameObserverPrompt(){
        System.out.println("Enter the number representing the game you want to observe: ");
        gameID = reader.nextInt();
        try{
            serverFacade.joinGame(auth, "", gameID, name);
            awaitMessage();
        } catch (IOException e) {
            System.out.println("There was an error observing the game: " + e.getMessage());
            postLoginMenu();
        } catch (URISyntaxException e) {
            System.out.println("There was a problem accessing the server: " + e.getMessage());
            postLoginMenu();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    private static void gameplayMenu(ChessGame game, HashSet<ChessMove> highlightedMoves){
        awaitingInput = false;
        GameBoard.draw(color, game.getBoard(), highlightedMoves);
        System.out.println("1. Redraw Board");
        System.out.println("2. Leave Game");
        System.out.println("3. Make Move");
        System.out.println("4. Resign");
        System.out.println("5. Highlight Legal Moves");
        System.out.println("6. Help");
        System.out.println("\n");
        awaitingInput = true;
        getInput(color, game);
    }

    private static void getInput(String color, ChessGame game) {
        int input = 0;
        try{
            if (reader.hasNext()) {
                input = Integer.parseInt(reader.next());
                switch (input) {
                    case 0 -> {
                    }
                    case 1 -> gameplayMenu(game, null);
                    case 2 -> leaveGame();
                    case 3 -> makeMovePrompt(game);
                    case 4 -> resign();
                    case 5 -> highlightMovesPrompt(game);
                    case 6 -> printHelpGameplay();
                    default -> gameplayMenu(game, null);
                }
                return;
            }
        }catch (NumberFormatException e) {
            //gameplayMenu(game, null);
            return;
        }catch (Exception ignored){

        }
    }

    private static void printHelpGameplay(){
        System.out.println("There are several options to choose from. Type the number and press enter.:");
    }

    private static void leaveGame(){
        try {
            serverFacade.leaveGame(auth, gameID, name);
        } catch (Exception e) {
            System.out.println("error leaving game");
        }
        System.out.println("You have left the game");
        postLoginMenu();
    }

    private static void resign(){
        try {
            serverFacade.resign(auth, gameID, name);
        } catch (Exception e) {
            System.out.println("error resigning game");
        }
    }

    private static void makeMovePrompt(ChessGame game){
        System.out.println("Enter the position of the piece that you want to move:");
        String selectedPieceInput = reader.next();
        ChessPosition startPos = convertToChessPosition(selectedPieceInput);

        System.out.println("Enter the position you want to move the piece to:");
        String moveToInput = reader.next();
        ChessPosition endPos = convertToChessPosition(moveToInput);
        ChessMove move = checkForPromotion(new ChessMove(startPos, endPos, null), game);


        try {
            serverFacade.makeMove(auth, gameID, move);
        } catch (InvalidMoveException e) {
            System.out.println("Error: Move Invalid. Try again.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    // Converts the user's input into a Chess Position
    private static ChessPosition convertToChessPosition(String moveString){
        char columnChar = moveString.charAt(0);
        int column = columnChar - 'a' + 1;
        int row = Integer.parseInt(moveString.substring(1));
        return new ChessPosition(row, column);
    }

    private static ChessMove checkForPromotion(ChessMove move, ChessGame game){
        ChessPosition startPos = move.getStartPosition();
        ChessPiece piece = game.getBoard().getPiece(startPos);
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                if (startPos.getRow() == 7){
                    return choosePromotionMenu(move);
                }
            } else{
                if (startPos.getRow() == 2){
                    return choosePromotionMenu(move);
                }
            }

        }
        return move;
    }

    private static ChessMove choosePromotionMenu(ChessMove move){
        System.out.println("You get to promote your pawn! What do you want to promote it to?");
        System.out.println("1. Queen");
        System.out.println("2. Rook");
        System.out.println("3. Bishop");
        System.out.println("4. Knight");

        System.out.println("\n");
        int input = reader.nextInt();

        ChessMove newMove;

        switch (input) {
            case 2 -> newMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK);
            case 3 -> newMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP);
            case 4 -> newMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT);
            default -> newMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN);
        }
        return newMove;
    }

    private static void highlightMovesPrompt(ChessGame game){
        System.out.println("Enter the position of the piece to highlight the moves of:");
        while (awaitingMessage) {
            try {
                if (reader.hasNext()) {
                    String selectedPieceInput = reader.next();
                    ChessPosition piecePos = convertToChessPosition(selectedPieceInput);

                    HashSet<ChessMove> pieceMoves = (HashSet<ChessMove>) game.validMoves(piecePos);

                    gameplayMenu(game, pieceMoves);
                    return;
                }
            } catch (Exception ignored){

            }
        }
    }

    @Override
    public void notify(ServerMessage message) {
        awaitingMessage = false;
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((Notification) message).getMessage());
            case ERROR -> displayError(((Error) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGame) message).getGame());
            default -> System.out.println("error receiving message");
        }

    }

    private void displayNotification(String message){
        System.out.println(message);
    }

    private void loadGame(ChessGame game){
        threads.forEach(Thread::interrupt);
        Runnable runnable = () -> {
            gameplayMenu(game, null);
        };
        Thread thread = new Thread(runnable);
        threads.add(thread);
        thread.start();
    }

    private void displayError(String errorMessage){
        System.out.println("Error: " + errorMessage);
    }

}

