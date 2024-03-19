package ui;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void createGame() throws IOException, URISyntaxException {
        ClientCommunicator.createGame(serverUrl);
    }

    public void listGames(){

    }

    public void joinGame(){

    }

    public void login(String username, String password) throws IOException, URISyntaxException {
        ClientCommunicator.login(serverUrl, username, password);
    }

    public void logout(){

    }

    public void register(String username, String password, String email) throws IOException, URISyntaxException {
        ClientCommunicator.register(serverUrl, username, password, email);
    }

}
