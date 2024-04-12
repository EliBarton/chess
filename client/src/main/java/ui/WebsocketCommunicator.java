package ui;

import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.net.URI;
import java.util.logging.Logger;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

public class WebsocketCommunicator extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
    public Session session;
    private final ServerMessageObserver observer;
    public WebsocketCommunicator(String url, ServerMessageObserver observer) throws Exception{
        this.observer = observer;
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                Gson gson = new Gson();
                ServerMessage msgOld = gson.fromJson(message, ServerMessage.class);
                ServerMessage msg = null;
                switch (msgOld.getServerMessageType()){
                    case LOAD_GAME -> {
                        msg = gson.fromJson(message, LoadGame.class);
                    }
                    case ERROR -> {
                        msg = gson.fromJson(message, Error.class);
                    }
                    case NOTIFICATION -> {
                        msg = gson.fromJson(message, Notification.class);
                    }
                }

                observer.notify(msg);
            }
        });
    }

    public void send(UserGameCommand msg) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(msg));
    }

    public void joinGame(String auth, String color, int gameID, String name) throws Exception {
        if (color.isEmpty()){
            UserGameCommand observeMessage = new JoinObserver(auth, gameID, name);
            observeMessage.setCommandType(JOIN_OBSERVER);
            send(observeMessage);
        }else {
            UserGameCommand joinMessage = new JoinPlayer(auth, gameID, color, name);
            joinMessage.setCommandType(JOIN_PLAYER);
            send(joinMessage);
        }
    }

    public void makeMove(String auth, int gameID, ChessMove move) throws Exception {
        UserGameCommand moveMessage = new MakeMove(auth, gameID, move);
        moveMessage.setCommandType(MAKE_MOVE);
        send(moveMessage);
    }

    public void leaveGame(String auth, int gameID, String name) throws Exception {
        UserGameCommand leaveMessage = new Leave(auth, gameID, name);
        leaveMessage.setCommandType(LEAVE);
        send(leaveMessage);
    }


    public void resign(String auth, int gameID, String name) throws Exception {
        UserGameCommand resignMessage = new Leave(auth, gameID, name);
        resignMessage.setCommandType(RESIGN);
        send(resignMessage);
    }
}
