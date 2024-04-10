package server;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;



public class Connection {
    public String name;
    public Session session;

    public String authToken;

    public Connection(String visitorName, String authToken, Session session) {
        this.name = visitorName;
        this.session = session;
        this.authToken = authToken;
    }

    public Connection() {

    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public static void sendError(RemoteEndpoint remote, String message) {
    }
}

