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

    public void createGame() throws IOException, URISyntaxException {
        ClientCommunicator.createGame();
    }

    public void listGames(){

    }

    public void joinGame(){

    }

    public void login(){

    }

    public void logout(){

    }

    public void register(){

    }

}
