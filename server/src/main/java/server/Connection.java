package server;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(name, that.name) && Objects.equals(session, that.session) && Objects.equals(authToken, that.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, session, authToken);
    }
}

