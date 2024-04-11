package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {

    private int gameID;
    private String name;
    public Leave(String authToken, int gameID, String name) {
        super(authToken);
        this.gameID = gameID;
        this.name = name;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
