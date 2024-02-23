package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameAccess {

    int createGame(String gameName, String authToken);

    GameData getGame(String id);

    HashSet<GameData> listGames();

    void updateGame(String id, String gameString);

    record GameIdResult(int gameID){}

}
