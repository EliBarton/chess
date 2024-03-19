package ui;

import com.google.gson.Gson;
import dataAccess.AuthAccess;
import dataAccess.GameAccess;
import dataAccess.UserAccess;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ClientCommunicator {

    public static void createGame(String serverUrl, String gameName, String auth) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);
        GameAccess.CreateGameRequest createGameRequest = new GameAccess.CreateGameRequest(gameName);

        String reqData = new Gson().toJson(createGameRequest);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }


        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
        }
    }

    public static GameAccess.ListGamesResult listGames(String serverUrl, String auth) throws URISyntaxException, IOException {
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
            return new Gson().fromJson(inputStreamReader, GameAccess.ListGamesResult.class);
        }
    }

    public static void joinGame(){

    }

    public static AuthAccess.AuthResult login(String serverUrl, String username, String password) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/session");
        Gson gson = new Gson();
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");

        UserAccess.LoginRequest loginRequest = new UserAccess.LoginRequest(username, password);
        String reqData = gson.toJson(loginRequest);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }
        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            AuthAccess.AuthResult auth = gson.fromJson(inputStreamReader, AuthAccess.AuthResult.class);
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

    public static AuthAccess.AuthResult register(String serverUrl, String username, String password, String email) throws IOException, URISyntaxException {
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
            System.out.println(gson.fromJson(inputStreamReader, Map.class));
            AuthAccess.AuthResult auth = gson.fromJson(gson.fromJson(inputStreamReader, Map.class).toString(), AuthAccess.AuthResult.class);
            return auth;
        }
    }

}
