package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage{

    private Class<?> game;
    public LoadGame(ServerMessageType type, Class<?> game) {
        super(type);
        this.game = game;
    }

    public Class<?> getGame() {
        return game;
    }

    public void setGame(Class<?> game) {
        this.game = game;
    }
}
