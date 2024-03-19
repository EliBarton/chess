package ui;

import dataAccess.AuthAccess;
import dataAccess.GameAccess;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void createGame(String gameName, String auth) throws IOException, URISyntaxException {
        ClientCommunicator.createGame(serverUrl, gameName, auth);
    }

    public GameAccess.ListGamesResult listGames(String auth) throws URISyntaxException, IOException {
        return ClientCommunicator.listGames(serverUrl, auth);
    }

    public void joinGame(){

    }

    public AuthAccess.AuthResult login(String username, String password) throws IOException, URISyntaxException {
        return ClientCommunicator.login(serverUrl, username, password);
    }

    public void logout(String auth) throws IOException, URISyntaxException {
        ClientCommunicator.logout(serverUrl, auth);
    }

    public AuthAccess.AuthResult register(String username, String password, String email) throws IOException, URISyntaxException {
        return ClientCommunicator.register(serverUrl, username, password, email);
    }

}
