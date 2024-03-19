package clientTests;

import dataAccess.AuthAccess;
import org.junit.jupiter.api.*;
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
            serverFacade.createGame("Test Game", authResult.authToken());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void registerTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.register("Testuser1", "Testpassword", "fakeemail@yourmom.com");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void logoutTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            serverFacade.logout(authResult.authToken());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void listGamesTest(){
        try{
            serverFacade = new ServerFacade("http://localhost:8080");
            AuthAccess.AuthResult authResult = serverFacade.login("Testuser1", "Testpassword");
            System.out.println(serverFacade.listGames(authResult.authToken()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

}
