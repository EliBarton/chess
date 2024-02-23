package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;

public interface GameAccess {

    int createGame(String gameName, String authToken);

    GameData getGame(int id);

    ArrayList<GameData> listGames();

    String updateGame(int id);

    record GameIdResult(int gameID){}

    record JoinGameRequest(String playerColor, int gameID){}

}
