package dataAccessTests;

import dataAccess.*;
import dataAccess.exceptions.DataAccessException;
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
            userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com"));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

    @Test
    @DisplayName("New Game Test")
    public void createGameTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            String authToken = userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com")).authToken();
            GameAccess gameAccess = new SqlGameAccess(authAccess);
            gameAccess.createGame("My new chess game", authToken);
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
