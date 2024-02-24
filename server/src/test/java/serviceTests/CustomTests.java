package serviceTests;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

public class CustomTests {

    @Test
    @DisplayName("Clear Data")
    public void clearSuccess(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        UserData testUser2 = new UserData("Checkersdude",
                "mypassword1", "bestatcheckers@yourmom.com");
        UserData testUser3 = new UserData("minecraftzombie",
                "eeeaaeerhhhh", "brains@yourmom.com");
        testUserData.addUser(testUser1);
        testUserData.addUser(testUser2);
        testUserData.addUser(testUser3);

        ClearService clearService = new ClearService(testUserData);
        clearService.clear();
        assertNull(testUserData.getUser("Chessmaster"), "User was not deleted");
        assertNull(testUserData.getUser("Checkersdude"), "User was not deleted");
        assertNull(testUserData.getUser("minecraftzombie"), "User was not deleted");
    }

    @Test
    @DisplayName("Register Success")
    public void registerUser(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        RegisterService registerService = new RegisterService(testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        try {
            registerService.register(testUser1);
        } catch (DataAccessException e) {
            fail("Registration failed during data access");
        } catch (InvalidDataException e){
            fail("invalid registration info");
        }
        assertNotNull(testUserData.getUser("Chessmaster"));
    }

    @Test
    @DisplayName("Register Twice")
    public void registerUserAgain(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        RegisterService registerService = new RegisterService(testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        try {
            registerService.register(testUser1);
        } catch (DataAccessException e) {
            fail("Registration failed during data access");
        } catch (InvalidDataException e){
        fail("invalid registration info");
    }
        try {
            registerService.register(testUser1);
        } catch (DataAccessException ignored) {
            return;
        }catch (InvalidDataException e){
            fail("invalid registration info");
        }
        fail("User was able to register twice");
    }

    @Test
    @DisplayName("Login User")
    public void loginUser(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        LoginoutService loginoutService = new LoginoutService(authData, testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        testUserData.addUser(testUser1);

        UserAccess.LoginRequest loginRequest = new UserAccess.LoginRequest(testUser1.username(), testUser1.password());

        try {
            loginoutService.login(loginRequest);
        } catch (DataAccessException e) {
            fail("There was a failure in data access: " + e);
        } catch (InvalidDataException e) {
            fail("Invalid data was given:" + e);
        }

        //Add test that makes sure that the login was successful and valid
        System.out.println(authData.getAuth("Chessmaster").authToken());
        assertNotNull(authData.getAuth("Chessmaster").authToken());

    }

    @Test
    @DisplayName("Login Unregistered User")
    public void loginInvalidUser(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        LoginoutService loginoutService = new LoginoutService(authData, testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");

        UserAccess.LoginRequest loginRequest = new UserAccess.LoginRequest(testUser1.username(), testUser1.password());

        try {
            AuthAccess.AuthResult result = loginoutService.login(loginRequest);
            assertNull(result);
        } catch (DataAccessException e) {
        } catch (InvalidDataException e) {
        }
        assertNull(authData.getAuth("Chessmaster").authToken());
    }

    @Test
    @DisplayName("Logout user")
    public void logoutUser(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        RegisterService registerService = new RegisterService(testUserData);
        LoginoutService loginoutService = new LoginoutService(authData, testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        try {
            AuthAccess.AuthResult authResult = registerService.register(testUser1);
            loginoutService.logout(authResult.authToken());
        } catch (DataAccessException e) {
            fail("failed during data access");
        } catch (InvalidDataException e){
            fail("invalid registration info");
        }
        assertNotNull(testUserData.getUser("Chessmaster"));
        assertNull(authData.getAuth("Chessmaster").authToken());
    }

    @Test
    @DisplayName("Logout user that isn't even logged in")
    public void logoutUserNotLoggedIn(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        RegisterService registerService = new RegisterService(testUserData);
        LoginoutService loginoutService = new LoginoutService(authData, testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        try {
            AuthAccess.AuthResult authResult = registerService.register(testUser1);
            loginoutService.logout(authResult.authToken());
            loginoutService.logout(authResult.authToken());
        } catch (DataAccessException ignored) {
        } catch (InvalidDataException e){
            fail("invalid registration info");
        }
        assertNotNull(testUserData.getUser("Chessmaster"));
        assertNull(authData.getAuth("Chessmaster").authToken());
    }

    @Test
    @DisplayName("Create game")
    public void createGame(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess UserData = new MemoryUserAccess(authData);
        GameAccess gameData = new MemoryGameAccess(authData);
        RegisterService registerService = new RegisterService(UserData);
        LoginoutService loginoutService = new LoginoutService(authData, UserData);
        GameService gameService = new GameService(authData, gameData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        int gameID = 0;
        try {
            AuthAccess.AuthResult authResult = registerService.register(testUser1);
            gameID = gameService.createGame("My test game", authResult.authToken());

        } catch (DataAccessException ignored) {
            fail("data access failure");
        } catch (InvalidDataException e){
            fail("invalid registration info");
        } catch (UnauthorizedException e){
            fail("user not authorized");
        }
        assertNotNull(gameData.getGame(gameID));
    }

    @Test
    @DisplayName("Create game unauthorized")
    public void createGameUnauthorized(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess UserData = new MemoryUserAccess(authData);
        GameAccess gameData = new MemoryGameAccess(authData);
        RegisterService registerService = new RegisterService(UserData);
        LoginoutService loginoutService = new LoginoutService(authData, UserData);
        GameService gameService = new GameService(authData, gameData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        int gameID = 0;
        try {
            AuthAccess.AuthResult authResult = registerService.register(testUser1);
            gameID = gameService.createGame("My test game", authResult.authToken() + 1);
        } catch (DataAccessException ignored) {
            fail("data access failure");
        } catch (InvalidDataException e){
            fail("invalid registration info");
        } catch (UnauthorizedException ignored){
        }
        assertNull(gameData.getGame(gameID));
    }

    @Test
    @DisplayName("list games")
    public void listGames(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess UserData = new MemoryUserAccess(authData);
        GameAccess gameData = new MemoryGameAccess(authData);
        RegisterService registerService = new RegisterService(UserData);
        GameService gameService = new GameService(authData, gameData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        int gameID = 0;
        try {
            AuthAccess.AuthResult authResult = registerService.register(testUser1);
            gameID = gameService.createGame("My test game 1", authResult.authToken());
            gameID = gameService.createGame("My test game 2", authResult.authToken());
            gameID = gameService.createGame("My test game 3", authResult.authToken());
            gameID = gameService.createGame("My test game 4", authResult.authToken());
        } catch (DataAccessException ignored) {
            fail("data access failure");
        } catch (InvalidDataException e){
            fail("invalid registration info");
        } catch (UnauthorizedException e){
            fail(e);
        }
        System.out.println(gameData.listGames());
        assertNotNull(gameData.listGames());
    }

    @Test
    @DisplayName("list games unauthorized")
    public void listGamesUnauthorized(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess UserData = new MemoryUserAccess(authData);
        GameAccess gameData = new MemoryGameAccess(authData);
        RegisterService registerService = new RegisterService(UserData);
        GameService gameService = new GameService(authData, gameData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        int gameID = 0;
        try {
            AuthAccess.AuthResult authResult = registerService.register(testUser1);
            gameID = gameService.createGame("My test game 1", authResult.authToken() + 1);
            gameID = gameService.createGame("My test game 2", authResult.authToken() + 1);
            gameID = gameService.createGame("My test game 3", authResult.authToken() + 1);
            gameID = gameService.createGame("My test game 4", authResult.authToken() + 1);
        } catch (DataAccessException ignored) {
            fail("data access failure");
        } catch (InvalidDataException e){
            fail("invalid registration info");
        } catch (UnauthorizedException e){
        }
        assertTrue(gameData.listGames().isEmpty());
    }


    @Test
    @DisplayName("join game white")
    public void joinGameAsWhite(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess UserData = new MemoryUserAccess(authData);
        GameAccess gameData = new MemoryGameAccess(authData);
        RegisterService registerService = new RegisterService(UserData);
        GameService gameService = new GameService(authData, gameData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        int gameID = 0;
        Gson gson = new Gson();
        ChessGame gameJoined = null;
        try {
            AuthAccess.AuthResult authResult = registerService.register(testUser1);
            gameID = gameService.createGame("My test game", authResult.authToken());
            System.out.println(gameService.listGames(authResult.authToken()));
            String gameString = gameService.updateGame(gameID, authResult.authToken(), "WHITE");
            //System.out.println(gameService.updateGame(gameID, authResult.authToken(), "BLACK"));
            System.out.println(gameService.listGames(authResult.authToken()));
            gameJoined = gson.fromJson(gameString, ChessGame.class);
            
            System.out.println(gameJoined);
        } catch (DataAccessException ignored) {
            fail("data access failure");
        } catch (InvalidDataException e){
            fail("invalid registration info");
        } catch (UnauthorizedException e){
            fail("unauthorized");
        }
        assertEquals(gameData.getGame(gameID).game(), gameJoined);
        System.out.println();

    }


}