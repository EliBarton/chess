package model;

import chess.ChessGame;
import com.google.gson.Gson;

public record GameData(int gameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) {
    public GameData updateWhiteUsername(String whiteName){
        return new GameData(gameID, whiteName, blackUsername, gameName, game);
    }
    public GameData updateBlackUsername(String blackName){
        return new GameData(gameID, whiteUsername, blackName, gameName, game);
    }


}
