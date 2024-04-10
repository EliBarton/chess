package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.concurrent.ConcurrentHashMap;

@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class WebSocket {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        System.out.printf("Received: %s", msg);
        UserGameCommand command = readJson(msg);

        var conn = getConnection(command.authToken, session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join(conn, msg);
                case JOIN_OBSERVER -> observe(conn, msg);
                case MAKE_MOVE -> move(conn, msg);
                case LEAVE -> leave(conn, msg);
                case RESIGN -> resign(conn, msg);
            }
        } else {
            Connection.sendError(session.getRemote(), "unknown user");
        }

    }

    private server.Connection getConnection(String authToken, Session session) {
        return new Connection();
    }

    private UserGameCommand readJson(String msg) {
        Gson gson = new Gson();
        return gson.fromJson(msg, UserGameCommand.class);
    }

    public void join(Connection c, String msg){

    }

    public void observe(Connection c, String msg){

    }

    public void move(Connection c, String msg){

    }

    public void leave(Connection c, String msg){

    }

    public void resign(Connection c, String msg){

    }

}
