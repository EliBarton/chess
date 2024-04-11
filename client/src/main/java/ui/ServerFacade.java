package ui;

import chess.ChessGame;
import dataAccess.AuthAccess;
import dataAccess.GameAccess;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URISyntaxException;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

public class ServerFacade {

    private final String serverUrl;
    private final String websocketUrl;

    WebsocketCommunicator ws;

    public ServerFacade(String url) throws Exception {
        serverUrl = "http://" + url;
        websocketUrl = "ws://" + url + "/connect";
    }

    public int createGame(String gameName, String auth) throws IOException, URISyntaxException {
        return HttpCommunicator.createGame(serverUrl, gameName, auth);
    }

    public GameAccess.ListGamesResult listGames(String auth) throws URISyntaxException, IOException {
        return HttpCommunicator.listGames(serverUrl, auth);
    }

    public ChessGame joinGame(String auth, String color, int gameID) throws Exception {
        ChessGame game = HttpCommunicator.joinGame(serverUrl, auth, color, gameID);
        ws = new WebsocketCommunicator(websocketUrl);
        ws.joinGame(auth, color, gameID);
        return game;
    }

    public AuthAccess.AuthResult login(String username, String password) throws Exception {
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
