package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InvalidDataException;
import dataAccess.exceptions.UnauthorizedException;
import model.GameData;
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
import webSocketMessages.userCommands.*;

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
        ChessGame.TeamColor currentTurn = gameService.getGameData().getGame(makeMove.getGameID()).game().getTeamTurn();

        ChessGame game = gameService.getGameData().getGame(makeMove.getGameID()).game();

        if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK) || game.isInCheckmate(null)){
            sendError("game has ended with a checkmate", c);
            return;
        }
        if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK) ){
            sendError("game has ended with a stalemate", c);
            return;
        }

        if (currentTurn == ChessGame.TeamColor.WHITE){
            String whiteUsername = gameService.getGameData().getGame(makeMove.getGameID()).whiteUsername();
            if (!whiteUsername.equals(myName)){
                sendError("It's not your turn", c);
                return;
            }
        }else{
            String blackUsername = gameService.getGameData().getGame(makeMove.getGameID()).blackUsername();
            if (!blackUsername.equals(myName)){
                sendError("It's not your turn", c);
                return;
            }
        }

        try {
            gameService.getGameData().getGame(makeMove.getGameID()).game().makeMove(makeMove.getMove());
        } catch (InvalidMoveException e) {
            sendError("invalid move", c);
            return;
        }

        updateGame(makeMove.getGameID(), makeMove.authToken);
        broadcastMessage(makeMove.getGameID(), myName + " made a move.", makeMove.authToken);
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK) || game.isInCheckmate(null)){
            broadcastMessage(makeMove.getGameID(), myName + " has won the game.", makeMove.authToken);
            sendNotification(makeMove.getGameID(), "you have won the game.", makeMove.authToken);
        }
    }

    public void leave(WebSocketSession c, String msg) throws IOException {
        Leave leave = (Leave) readJson(msg, Leave.class);
        String myName = gameService.getAuthData().getUsernameByAuth(leave.authToken);
        leave.setName(myName);

        String whiteUsername = gameService.getGameData().getGame(leave.getGameID()).whiteUsername();
        if (whiteUsername.equals(myName)){
            gameService.getGameData().getGame(leave.getGameID()).updateWhiteUsername(null);
        }
        String blackUsername = gameService.getGameData().getGame(leave.getGameID()).blackUsername();
        if (blackUsername.equals(myName)){
            gameService.getGameData().getGame(leave.getGameID()).updateBlackUsername(null);
        }


        sessions.remove(c);
        for (Connection connection : connections){
            if (connection.session.equals(c)){
                connections.remove(connection);
                break;
            }
        }
        broadcastMessage(leave.getGameID(), leave.getName() + " has left the game.", leave.authToken);

    }

    public void resign(WebSocketSession c, String msg) throws IOException {
        Leave resign = (Leave) readJson(msg, Leave.class);
        String myName = gameService.getAuthData().getUsernameByAuth(resign.authToken);
        resign.setName(myName);
        try {
            gameService.updateGame(resign.getGameID(), resign.authToken, null);
        } catch (UnauthorizedException e) {
            sendError("invalid", c);
            return;
        } catch (InvalidDataException e) {
            sendError("invalid", c);
            return;
        } catch (DataAccessException e) {
            sendError("invalid", c);
            return;
        }

        sendNotification(resign.getGameID(), "you have resigned.", resign.authToken);
        broadcastMessage(resign.getGameID(), resign.getName() + " has resigned.", resign.authToken);

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

    private void updateGame(int gameID, String authToken) throws IOException {
        ChessGame game = gameService.getGameData().getGame(gameID).game();
        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
        for (Connection connection : connections){
            if (connection.session.isOpen()) {
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
