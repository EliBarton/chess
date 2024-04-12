package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {

    private int gameID;
    private String name;
    public Leave(String authToken, int gameID, String name) {
        super(authToken);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
