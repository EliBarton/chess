package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
record CreateGameRequest(String gameName){}
record GameIdResult(int gameID){}
record JoinGameRequest(String playerColor, int gameID){}

record SerializedGameData(int gameID, String whiteUsername, String blackUsername,
                          String gameName, String game){}

record ListGamesResult(String message, ArrayList<SerializedGameData> games){}


record AuthResult(String username, String authToken){}
record LoginRequest(String username, String password) {}
public class HttpCommunicator {

    public static int createGame(String serverUrl, String gameName, String auth) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);

        String reqData = new Gson().toJson(createGameRequest);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }


        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            GameIdResult result = new Gson().fromJson(inputStreamReader, GameIdResult.class);
            return result.gameID();
        }
    }

    public static ListGamesResult listGames(String serverUrl, String auth) throws URISyntaxException, IOException {
        URI uri = new URI(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);


        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, ListGamesResult.class);
        }
    }

    public static ChessGame joinGame(String serverUrl, String auth, String color, int gameID) throws IOException, URISyntaxException {

        URI uri = new URI(serverUrl + "/game");
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);

        JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID);
        String reqData = gson.toJson(joinGameRequest);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }
        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            ChessGame game = gson.fromJson(inputStreamReader, ChessGame.class);
            return game;
        }
    }

    public static AuthResult login(String serverUrl, String username, String password) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/session");
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        LoginRequest loginRequest = new LoginRequest(username, password);
        String reqData = gson.toJson(loginRequest);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }
        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthResult auth = gson.fromJson(inputStreamReader, AuthResult.class);
            return auth;
        }
    }

    public static void logout(String serverUrl, String auth) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/session");
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);
        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            System.out.println(gson.fromJson(inputStreamReader, Map.class));
        }
    }

    public static AuthResult register(String serverUrl, String username, String password, String email) throws IOException, URISyntaxException {
        Gson gson = new Gson();
        URI uri = new URI(serverUrl + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        UserData userData = new UserData(username, password, email);
        String reqData = gson.toJson(userData);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }
        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            //System.out.println(gson.fromJson(inputStreamReader, Map.class));
            AuthResult auth = gson.fromJson(gson.fromJson(inputStreamReader, Map.class).toString(), AuthResult.class);
            return auth;
        }
    }

    public static void clear(String serverUrl) throws IOException, URISyntaxException {
        Gson gson = new Gson();
        URI uri = new URI(serverUrl + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
        }


    }

}
