package clientTests;

import dataAccess.AuthAccess;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import server.Server;
import ui.GameBoard;
import ui.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
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
        GameBoard.draw();
    }

    @Test
    public void createGame(){
        try{
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            int gameID = serverFacade.createGame("Test Game", authResult.authToken());
            Assertions.assertNotEquals(0, gameID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void createGameBadAuth(){
        Assertions.assertThrows(RuntimeException.class, () -> {
        try{
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");

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
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.register("Testuser1", "Testpassword", "fakeemail@yourmom.com");
            Assertions.assertNotNull(authResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void registerTestAlreadyExists(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                serverFacade = new ServerFacade("http://localhost:8080");
                AuthAccess.AuthResult authResult = serverFacade.register("Testuser1", "Testpassword", "fakeemail@yourmom.com");
                serverFacade.register("Testuser1", "Testpassword", "fakeemail@yourmom.com");
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
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            serverFacade.logout(authResult.authToken());
            Assertions.assertThrows(IOException.class,() -> serverFacade.listGames(authResult.authToken()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void logoutTestNotLoggedIn(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                serverFacade = new ServerFacade("http://localhost:8080");
                AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
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
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            System.out.println(serverFacade.listGames(authResult.authToken()));
            Assertions.assertNotNull(serverFacade.listGames(authResult.authToken()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void joinGameTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            int id = serverFacade.createGame("Test Game", authResult.authToken());
            Assertions.assertNotNull(serverFacade.joinGame(authResult.authToken(), "WHITE", id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }


}
