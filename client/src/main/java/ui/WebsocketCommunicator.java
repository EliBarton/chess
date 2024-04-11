package ui;

import com.google.gson.Gson;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

public class WebsocketCommunicator extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
    public Session session;
    public WebsocketCommunicator(String url) throws Exception{
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(UserGameCommand msg) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(msg));
    }

    public void joinGame(String auth, String color, int gameID, String name) throws Exception {
        UserGameCommand joinMessage = new JoinPlayer(auth, gameID, color, name);
        joinMessage.setCommandType(JOIN_PLAYER);
        send(joinMessage);
    }


}
