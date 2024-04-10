package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

    private int gameID;
    private String playerColor;
    public JoinPlayer(String authToken, int gameID, String playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
