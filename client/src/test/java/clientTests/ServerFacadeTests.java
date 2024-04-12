package clientTests;

import chess.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    private static ServerMessageObserver observer;

    private static int port;
    record CreateGameRequest(String gameName){}
    record GameIdResult(int gameID){}
    record JoinGameRequest(String playerColor, int gameID){}

    record SerializedGameData(int gameID, String whiteUsername, String blackUsername,
                              String gameName, String game){}

    record ListGamesResult(String message, ArrayList<ui.SerializedGameData> games){}


    record AuthResult(String username, String authToken){}
    record LoginRequest(String username, String password) {}

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var portTemp = server.run(0);
        port = portTemp;
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port, observer);
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void drawRow(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        GameBoard.draw(ChessGame.TeamColor.WHITE, board, null);
        System.out.println(board.getPiece(new ChessPosition(1, 1)));
    }

    @Test
    public void gameTest(){
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        GameBoard.draw(ChessGame.TeamColor.WHITE, board, null);
        ChessPosition startPos = new ChessPosition(2, 1);
        System.out.println(board.getPiece(startPos));
        try {
            board.makeMove(new ChessMove(startPos, new ChessPosition(3, 1), null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        GameBoard.draw(ChessGame.TeamColor.WHITE, board, null);
    }

    @Test
    public void createGame(){
        try{
            serverFacade = new ServerFacade("http://localhost:" + port, observer);
            ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            int gameID = serverFacade.createGame("Test Game", authResult.authToken());
            Assertions.assertNotEquals(0, gameID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void createGameBadAuth(){
        Assertions.assertThrows(RuntimeException.class, () -> {
        try{
            serverFacade = new ServerFacade("http://localhost:" + port, observer);
            ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");

            serverFacade.createGame("Test Game", "1234");
        } catch (IOException e) {

            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        });
    }
    @Test
    public void registerTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:" + port, observer);
            ui.AuthResult authResult = serverFacade.register("Testuser1", "Testpassword", "fakeemail@yourmom.com");
            Assertions.assertNotNull(authResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void registerTestAlreadyExists(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                serverFacade = new ServerFacade("http://localhost:" + port, observer);
                ui.AuthResult authResult = serverFacade.register("Testuser2", "Testpassword", "fakeemail@yourmom.com");
                serverFacade.register("Testuser2", "Testpassword", "fakeemail@yourmom.com");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void logoutTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:" + port, observer);
            ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            serverFacade.logout(authResult.authToken());
            Assertions.assertThrows(IOException.class,() -> serverFacade.listGames(authResult.authToken()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void logoutTestNotLoggedIn(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                serverFacade = new ServerFacade("http://localhost:" + port, observer);
                ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
                serverFacade.logout(authResult.authToken());
                serverFacade.logout(authResult.authToken());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void listGamesTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:" + port, observer);
            ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");

            System.out.println(serverFacade.listGames(authResult.authToken()));
            Assertions.assertNotNull(serverFacade.listGames(authResult.authToken()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void listGamesTestBadAuth(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                serverFacade = new ServerFacade("http://localhost:" + port, observer);
                ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
                serverFacade.logout(authResult.authToken());
                System.out.println(serverFacade.listGames(authResult.authToken()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void joinGameTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:" + port, observer);
            ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            int id = serverFacade.createGame("Test Game", authResult.authToken());
            serverFacade.joinGame(authResult.authToken(), "WHITE", id, "Testuser1");
            Assertions.assertNotNull(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void joinGameTestBadAuth(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                serverFacade = new ServerFacade("http://localhost:" + port, observer);
                ui.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
                int id = serverFacade.createGame("Test Game", authResult.authToken());
                serverFacade.logout(authResult.authToken());
                serverFacade.joinGame(authResult.authToken(), "WHITE", id, "Testuser1");
                Assertions.assertNotNull(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
