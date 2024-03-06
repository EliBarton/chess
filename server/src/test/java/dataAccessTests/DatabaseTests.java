package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import dataAccess.exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {

    @Test
    @DisplayName("User Test")
    public void userTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            assertNotNull(userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Duplicate User Test")
    public void userDuplicateTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            userAccess.addUser(new UserData("Chessmaster", "wonka", "mom@yourmom.com"));
            assertNull(userAccess.addUser(new UserData("Chessmaster", "wonkas", "moms@yourmom.com")));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Get User Test")
    public void getUserTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            UserData expected = new UserData("Chessmaster", "chesspassword", "mom@yourmom.com");
            userAccess.addUser(expected);
            assertEquals(expected, userAccess.getUser("Chessmaster"));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Get Nonexistent User Test")
    public void getNullUserTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            UserData expected = new UserData("Chessmaster", "chesspassword", "mom@yourmom.com");
            userAccess.addUser(expected);
            assertNull(userAccess.getUser("Chessmasta"));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }


    @Test
    @DisplayName("Get Game Test")
    public void getGameTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            int gameID = gameAccess.createGame("My new chess game", authToken);
            assertEquals(
                    new GameData(gameID, null, null, "My new chess game", new ChessGame()),
                    gameAccess.getGame(gameID));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Get Nonexistent Game Test")
    public void getNullGameTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            gameAccess.createGame("My new chess game", authToken);
            assertNull(gameAccess.getGame(12));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Create Game Test")
    public void createGameTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            assertNotEquals(0, gameAccess.createGame("My new chess game", authToken));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Unauthorized Create Game Test")
    public void createUnauthorizedGameTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            assertEquals(0, gameAccess.createGame("My new chess game", "12435"));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Join Game Test")
    public void joinGameTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            int id = gameAccess.createGame("My new chess game", authToken);
            assertNotNull(gameAccess.updateGame(id, authToken, "WHITE"));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Join Game Bad ID Test")
    public void joinGameBadIDTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            int id = gameAccess.createGame("My new chess game", authToken);
            gameAccess.updateGame(id, authToken, "WHITE");
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("List Games Test")
    public void listGamesTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            gameAccess.createGame("chess game 1", authToken);
            gameAccess.createGame("chess game 2", authToken);
            gameAccess.createGame("chess game 3", authToken);
            gameAccess.createGame("chess game 4", authToken);
            gameAccess.createGame("chess game 5", authToken);
            assertNotNull(gameAccess.listGames());
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Get Auth Test")
    public void getAuthTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            clearAll();
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            AuthAccess.AuthResult actual = authAccess.getAuth("Chessmaster");
            System.out.println(actual.authToken());
            assertEquals(authToken, actual.authToken());
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }

    }

    @Test
    @DisplayName("Get Auth Test")
    public void containsAuthTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            AuthAccess.AuthResult actual = authAccess.getAuth("Chessmaster");
            assertTrue(authAccess.containsAuth(authToken));
            System.out.println(actual.authToken());
            assertEquals(authToken, actual.authToken());
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Get Username By Auth Test")
    public void getUserByAuthTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            AuthAccess.AuthResult actual = authAccess.getAuth("Chessmaster");
            assertEquals("Chessmaster", authAccess.getUsernameByAuth(authToken));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("Remove Auth Test")
    public void removeAuthTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            authAccess.deleteAuth(authToken);
            assertEquals(new AuthAccess.AuthResult("Chessmaster", null), authAccess.getAuth("Chessmaster"));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("All clear Test")
    public void clearAllTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            var statement = "SELECT EXISTS(SELECT * FROM auth)";
            assertFalse(DatabaseManager.queryDatabaseExists(statement));
            statement = "SELECT EXISTS(SELECT * FROM user)";
            assertFalse(DatabaseManager.queryDatabaseExists(statement));
            statement = "SELECT EXISTS(SELECT * FROM game)";
            assertFalse(DatabaseManager.queryDatabaseExists(statement));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }




    @BeforeEach
    public void clearAll() throws DataAccessException {
        AuthAccess authAccess = new SqlAuthAccess();
        UserAccess userAccess = new SqlUserAccess(authAccess);
        GameAccess gameAccess = new SqlGameAccess(authAccess);
        authAccess.clear();
        userAccess.clear();
        gameAccess.clear();
    }
}
