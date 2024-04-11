package server;

import com.google.gson.Gson;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import service.GameService;
import spark.Spark;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class WebSocket {
    public final HashSet<WebSocketSession> sessions = new HashSet<>();

    private final GameService gameService;

    public WebSocket(GameService gameService) {
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        //System.out.printf("Received: %s", msg);
        UserGameCommand command = readJson(msg);

        var conn = getSession(session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join((WebSocketSession) conn, msg);
                case JOIN_OBSERVER -> observe((WebSocketSession) conn, msg);
                case MAKE_MOVE -> move((WebSocketSession) conn, msg);
                case LEAVE -> leave((WebSocketSession) conn, msg);
                case RESIGN -> resign((WebSocketSession) conn, msg);
            }
        } else {
            Connection.sendError(session.getRemote(), "unknown user");
        }

    }


    private UserGameCommand readJson(String msg) {
        Gson gson = new Gson();
        return gson.fromJson(msg, UserGameCommand.class);
    }

    public void join(WebSocketSession c, String msg){
        System.out.println("A request to join the game was received: " + msg);

    }

    public void observe(WebSocketSession c, String msg){

    }

    public void move(WebSocketSession c, String msg){

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


}
