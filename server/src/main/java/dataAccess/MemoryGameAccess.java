package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class MemoryGameAccess implements GameAccess {

    HashSet<GameData> games = new HashSet<>();
    @Override
    public int createGame(String gameName, String authToken) {
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        games.add(new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public GameData getGame(String id) {
        return null;
    }

    @Override
    public HashSet<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(String id, String gameString) {

    }
}
