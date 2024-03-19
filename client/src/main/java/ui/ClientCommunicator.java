package ui;

import com.google.gson.Gson;
import dataAccess.GameAccess;
import dataAccess.UserAccess;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ClientCommunicator {

    public static void createGame(String serverUrl) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        GameAccess.CreateGameRequest createGameRequest = new GameAccess.CreateGameRequest("test game");
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

    public static void listGames(){

    }

    public static void joinGame(){

    }

    public static void login(String serverUrl, String username, String password) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        UserAccess.LoginRequest loginRequest = new UserAccess.LoginRequest(username, password);
        String reqData = new Gson().toJson(loginRequest);
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

    public static void logout(){

    }

    public static void register(String serverUrl, String username, String password, String email) throws IOException, URISyntaxException {
        URI uri = new URI(serverUrl + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        UserData userData = new UserData(username, password, email);
        String reqData = new Gson().toJson(userData);
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

}
