package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameAccess {

    int createGame(String gameName, String authToken);

    GameData getGame(int id);

    ArrayList<SerializedGameData> listGames();

    ChessGame updateGame(int id, String authToken, String playerColor);

    record GameIdResult(int gameID){}

    record JoinGameRequest(String playerColor, int gameID){}

    record SerializedGameData(int gameID, String whiteUsername, String blackUsername,
                              String gameName, String game){}

}
