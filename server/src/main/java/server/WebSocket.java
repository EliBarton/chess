package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InvalidDataException;
import dataAccess.exceptions.UnauthorizedException;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import service.GameService;
import spark.Spark;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class WebSocket {
    public final HashSet<WebSocketSession> sessions = new HashSet<>();

    public final HashSet<Connection> connections = new HashSet<>();

    private final GameService gameService;

    public WebSocket(GameService gameService) {
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        //System.out.printf("Received: %s", msg);
        UserGameCommand command = readJson(msg, UserGameCommand.class);

            var conn = getSession(session);
            if (conn != null) {
                try {
                    switch (command.getCommandType()) {
                        case JOIN_PLAYER -> join((WebSocketSession) conn, msg);
                        case JOIN_OBSERVER -> observe((WebSocketSession) conn, msg);
                        case MAKE_MOVE -> move((WebSocketSession) conn, msg);
                        case LEAVE -> leave((WebSocketSession) conn, msg);
                        case RESIGN -> resign((WebSocketSession) conn, msg);
                    }
                } catch (IOException e) {
                    sendError(e.getMessage(), conn);
                }
            } else {
                Connection.sendError(session.getRemote(), "unknown user");
            }


    }


    private UserGameCommand readJson(String msg, Class c) {
        Gson gson = new Gson();
        return (UserGameCommand) gson.fromJson(msg, c);
    }

    public void join(WebSocketSession c, String msg) throws IOException {
        JoinPlayer joinPlayer = (JoinPlayer) readJson(msg, JoinPlayer.class);
        String myName = gameService.getAuthData().getUsernameByAuth(joinPlayer.authToken);
        joinPlayer.setName(myName);
        if (gameService.getGameData().getGame(joinPlayer.getGameID()) == null){
            sendError("game doesn't exist", c);
            return;
        }
        if (joinPlayer.getPlayerColor().equals("WHITE")) {
            String whiteUsername = gameService.getGameData().getGame(joinPlayer.getGameID()).whiteUsername();
            if (whiteUsername == null){
                sendError("player slot empty", c);
                return;
            }else if (!whiteUsername.equals(myName)){
                sendError("player slot already taken", c);
                return;
            }
        } else if (joinPlayer.getPlayerColor().equals("BLACK")){
            String blackUsername = gameService.getGameData().getGame(joinPlayer.getGameID()).blackUsername();
            if (blackUsername == null){
                sendError("player slot empty", c);
                return;
            }else if (!blackUsername.equals(myName)){
                sendError("player slot already taken", c);
                return;
            }
        }else{
            sendError("invalid team", c);
            return;
        }

        Connection connection = new Connection(joinPlayer.getName(), joinPlayer.authToken, c);
        connections.add(connection);
        sendGame(joinPlayer.getGameID(), joinPlayer.authToken);
        broadcastMessage(joinPlayer.getGameID(), joinPlayer.getName() + " has joined the game.", joinPlayer.authToken);

    }

    public void observe(WebSocketSession c, String msg) throws IOException {
        JoinObserver joinObserver = (JoinObserver) readJson(msg, JoinObserver.class);
        String myName = gameService.getAuthData().getUsernameByAuth(joinObserver.authToken);
        joinObserver.setName(myName);
        if (gameService.getGameData().getGame(joinObserver.getGameID()) == null){
            sendError("game doesn't exist", c);
            return;
        }
        if (myName == null){
            sendError("problem with authToken", c);
            return;
        }

        Connection connection = new Connection(joinObserver.getName(), joinObserver.authToken, c);
        connections.add(connection);
        sendGame(joinObserver.getGameID(), joinObserver.authToken);
        broadcastMessage(joinObserver.getGameID(), joinObserver.getName() + " is observing the game.", joinObserver.authToken);

    }

    public void move(WebSocketSession c, String msg) throws IOException {
        MakeMove makeMove = (MakeMove) readJson(msg, MakeMove.class);
        String myName = gameService.getAuthData().getUsernameByAuth(makeMove.authToken);

        try {
            gameService.getGameData().getGame(makeMove.getGameID()).game().makeMove(makeMove.getMove());
        } catch (InvalidMoveException e) {
            sendError("invalid move", c);
            return;
        }

        sendGame(makeMove.getGameID(), makeMove.authToken);
        broadcastMessage(makeMove.getGameID(), myName + " made a move.", makeMove.authToken);
    }

    public void leave(WebSocketSession c, String msg){

    }

    public void resign(WebSocketSession c, String msg){

    }

    private Session getSession(Session session){
        if (sessions.contains(session)){
            return session;
        }
        return null;
    }

    @OnWebSocketConnect
    public void onConnect(Session session){
        sessions.add((WebSocketSession) session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int number, String message){
        sessions.remove((WebSocketSession) session);

    }

    private Connection getConnection(Session session){
        for (Connection connection : connections){
            if (connection.session.equals(session)){
                return connection;
            }
        }
        return null;
    }

    private void sendNotification(int gameID, String msg, String authToken) throws IOException {
        var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, msg);
        for (Connection connection : connections){
            if (connection.authToken.equals(authToken)){
                connection.session.getRemote().sendString(new Gson().toJson(notification));
            }
        }
    }

    private void sendGame(int gameID, String authToken) throws IOException {
        ChessGame game = gameService.getGameData().getGame(gameID).game();
        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
        for (Connection connection : connections){
            if (connection.authToken.equals(authToken)){
                System.out.println("sent game: " + loadGame.getGame());
                connection.session.getRemote().sendString(new Gson().toJson(loadGame));
            }
        }
    }

    private void broadcastMessage(int gameID, String msg, String exceptThisAuthToken) throws IOException {
        var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, msg);
        for (Connection connection : connections){
            if (!connection.authToken.equals(exceptThisAuthToken)){
                if (connection.session.isOpen()) {
                    connection.session.getRemote().sendString(new Gson().toJson(notification));
                }
            }
        }
    }

    private void sendError(String msg, Session session) throws IOException {
        System.out.println("sent error" + msg);
        var error = new Error(ServerMessage.ServerMessageType.ERROR, msg);
        session.getRemote().sendString(new Gson().toJson(error));

    }

}
