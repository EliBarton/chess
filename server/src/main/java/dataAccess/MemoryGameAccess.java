package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.Random;

public class MemoryGameAccess implements GameAccess {

    AuthAccess authData;

    public MemoryGameAccess(AuthAccess authData) {
        this.authData = authData;
    }


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
            if (game.gameID() == id){
                return game;
            }
        }
        return null;
    }

    @Override
    public ArrayList<SerializedGameData> listGames() {
        Gson gson = new Gson();
        ArrayList<SerializedGameData> serializedGames = new ArrayList<>();
        for (GameData gameData : games){
            serializedGames.add(new SerializedGameData(gameData.gameID(), gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), gson.toJson(gameData.game())));
        }
        return serializedGames;
    }

    @Override
    public ChessGame updateGame(int id, String authToken, String playerColor) {

        GameData gameData = getGame(id);
        ChessGame game = gameData.game();
        String username = authData.getUsernameByAuth(authToken);

        if (playerColor != null) {
            if (playerColor.equals("WHITE")) {
                gameData = getGame(id).updateWhiteUsername(username);
            } else if (playerColor.equals("BLACK")) {
                gameData = getGame(id).updateBlackUsername(username);
            }
        }
        return game;
    }


}
