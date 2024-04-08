package ui;

import chess.ChessGame;
import dataAccess.AuthAccess;
import dataAccess.GameAccess;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public int createGame(String gameName, String auth) throws IOException, URISyntaxException {
        return HttpCommunicator.createGame(serverUrl, gameName, auth);
    }

    public GameAccess.ListGamesResult listGames(String auth) throws URISyntaxException, IOException {
        return HttpCommunicator.listGames(serverUrl, auth);
    }

    public ChessGame joinGame(String auth, String color, int gameID) throws IOException, URISyntaxException {
        return HttpCommunicator.joinGame(serverUrl, auth, color, gameID);
    }

    public AuthAccess.AuthResult login(String username, String password) throws IOException, URISyntaxException {
        return HttpCommunicator.login(serverUrl, username, password);
    }

    public void logout(String auth) throws IOException, URISyntaxException {
        HttpCommunicator.logout(serverUrl, auth);
    }

    public AuthAccess.AuthResult register(String username, String password, String email) throws IOException, URISyntaxException {
        return HttpCommunicator.register(serverUrl, username, password, email);
    }

    public void clear() throws IOException, URISyntaxException {
        HttpCommunicator.clear(serverUrl);
    }
}
