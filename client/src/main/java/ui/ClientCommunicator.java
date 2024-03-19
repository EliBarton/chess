package ui;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ClientCommunicator {

    public static void createGame() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:8080/name");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

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

    public static void login(){

    }

    public static void logout(){

    }

    public static void register(){

    }

}
