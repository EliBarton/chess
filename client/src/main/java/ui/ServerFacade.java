package ui;

import chess.ChessGame;
import chess.ChessMove;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URISyntaxException;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

public class ServerFacade {

    private final String serverUrl;
    private final String websocketUrl;
    private final ServerMessageObserver observer;

    WebsocketCommunicator ws;

    public ServerFacade(String url, ServerMessageObserver observer) throws Exception {
        serverUrl = "http://" + url;
        websocketUrl = "ws://" + url + "/connect";
        this.observer = observer;
    }

    public int createGame(String gameName, String auth) throws IOException, URISyntaxException {
        return HttpCommunicator.createGame(serverUrl, gameName, auth);
    }

    public ListGamesResult listGames(String auth) throws URISyntaxException, IOException {
        return HttpCommunicator.listGames(serverUrl, auth);
    }

    public void joinGame(String auth, String color, int gameID, String name) throws Exception {
        HttpCommunicator.joinGame(serverUrl, auth, color, gameID);
        ws = new WebsocketCommunicator(websocketUrl, observer);
        ws.joinGame(auth, color, gameID, name);

    }

    public void makeMove(String auth, int gameID, ChessMove move) throws Exception {
        ws.makeMove(auth, gameID, move);
    }

    public AuthResult login(String username, String password) throws Exception {
        return HttpCommunicator.login(serverUrl, username, password);
    }

    public void logout(String auth) throws IOException, URISyntaxException {
        HttpCommunicator.logout(serverUrl, auth);
    }

    public AuthResult register(String username, String password, String email) throws IOException, URISyntaxException {
        return HttpCommunicator.register(serverUrl, username, password, email);
    }

    public void clear() throws IOException, URISyntaxException {
        HttpCommunicator.clear(serverUrl);
    }
}
