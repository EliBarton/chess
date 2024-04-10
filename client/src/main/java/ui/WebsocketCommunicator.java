package ui;

import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
    public Session session;
    public WebsocketCommunicator() throws Exception{
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }


}
