package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

    private int gameID;
    private final String playerColor;
    private String name;
    public JoinPlayer(String authToken, int gameID, String playerColor, String name) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.name = name;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
