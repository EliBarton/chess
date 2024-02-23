package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MemoryGameAccess implements GameAccess {

    ArrayList<GameData> games = new ArrayList<>();
    @Override
    public int createGame(String gameName, String authToken) {
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        games.add(new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public GameData getGame(int id) {
        for (GameData game : games){
            if (String.valueOf(game.gameID()).equals(id)){
                return game;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return games;
    }

    @Override
    public String updateGame(int id) {
        Gson serializer = new Gson();
        ChessGame game = getGame(id).game();

        return serializer.toJson(game);
    }
}
