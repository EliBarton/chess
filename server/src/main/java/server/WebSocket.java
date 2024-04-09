package server;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.serverMessages.ServerMessage;

@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class WebSocket {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WebSocket.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }

    public void sendMessage() {
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);

    }
}
