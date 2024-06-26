package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InvalidDataException;
import dataAccess.exceptions.UnauthorizedException;
import model.UserData;
import service.*;
import spark.*;

import java.util.ArrayList;


public class Server {
    private final AuthAccess authData;
    private final UserAccess userData;
    private final GameAccess gameData;

    private final WebSocket websocket;

    {
        try {
            authData = new SqlAuthAccess();
            userData = new SqlUserAccess(authData);
            gameData = new SqlGameAccess(authData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final ClearService clearService = new ClearService(userData, authData, gameData);
    private final RegisterService registerService = new RegisterService(userData);
    private final LoginoutService loginoutService = new LoginoutService(authData, userData);
    private final GameService gameService = new GameService(authData, gameData);

    public Server() {
        this.websocket = new WebSocket(gameService);
    }

    private record ErrorMessage(String message){}
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", websocket);
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);


        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        Gson gson = new Gson();
        UserData newUser = gson.fromJson(req.body(), UserData.class);
        AuthAccess.AuthResult result;
        try {
            result = registerService.register(newUser);
            res.status(200);
        }
        catch (DataAccessException e){
            res.status(403);
            return gson.toJson(new ErrorMessage("Error: Registration failed, " + e.getMessage()));
        }catch (InvalidDataException e){
            res.status(400);
            return gson.toJson(new ErrorMessage("Error: Registration failed, " + e.getMessage()));
        }

        return gson.toJson(result);
    }


    private Object clear(Request req, Response res) {
        clearService.clear();
        res.status(200);
        return "";
    }

    private Object login(Request req, Response res) {
        Gson gson = new Gson();
        UserAccess.LoginRequest userData = gson.fromJson(req.body(), UserAccess.LoginRequest.class);
        res.type("application/json");
        AuthAccess.AuthResult result;
        try{
            result = loginoutService.login(userData);
        }catch (DataAccessException | InvalidDataException e){
            res.status(401);
            return gson.toJson(new ErrorMessage("Error: Login failed, " + e.getMessage()));
        }
        return gson.toJson(result);
    }

    //Request body is empty
    private Object logout(Request req, Response res) {
        Gson gson = new Gson();
        try{
            loginoutService.logout(req.headers("Authorization"));
            res.status(200);
        }catch (DataAccessException | InvalidDataException e){
            System.out.println("Error: Logout failed, " + e.getMessage());
            res.status(401);
            return gson.toJson(new ErrorMessage("Error: Logout failed, " + e.getMessage()));
        }
        return gson.toJson(res.body());
    }

    private Object createGame(Request req, Response res) {
        Gson gson = new Gson();
        String auth = req.headers("Authorization");
        GameAccess.CreateGameRequest createGameRequest = gson.fromJson(req.body(), GameAccess.CreateGameRequest.class);
        String gameName = createGameRequest.gameName();
        System.out.println(gameName);
        GameAccess.GameIdResult result;
        try{
            result = new GameAccess.GameIdResult(gameService.createGame(gameName, auth));

        }catch (UnauthorizedException e){
            System.out.println(e.getMessage());
            res.status(401);
            return gson.toJson(new ErrorMessage("Error: Create game failed, " + e.getMessage()));
        }
        return gson.toJson(result);
    }

    private Object listGames(Request req, Response res) {
        Gson gson = new Gson();
        String auth = req.headers("Authorization");
        ArrayList<GameAccess.SerializedGameData> games;

        try{
            games = gameService.listGames(auth);
        }catch (UnauthorizedException e){
            res.status(401);
            return gson.toJson(new ErrorMessage("Error: Create game failed, " + e.getMessage()));
        }
        GameAccess.ListGamesResult result = new GameAccess.ListGamesResult("games", games);
        return gson.toJson(result);
    }

    private Object joinGame(Request req, Response res) {
        Gson gson = new Gson();
        String auth = req.headers("Authorization");
        GameAccess.JoinGameRequest joinRequest = gson.fromJson(req.body(), GameAccess.JoinGameRequest.class);
        String game;
        try{
            game = gameService.updateGame(joinRequest.gameID(), auth, joinRequest.playerColor());
        }catch (UnauthorizedException e){
            res.status(401);
            return gson.toJson(new ErrorMessage("Error: Update game failed, " + e.getMessage()));
        } catch (InvalidDataException e) {
            res.status(403);
            return gson.toJson(new ErrorMessage("Error: Update game failed, " + e.getMessage()));
        } catch (DataAccessException e){
            res.status(400);
            return gson.toJson(new ErrorMessage("Error: Update game failed, " + e.getMessage()));
        }
        return game;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
