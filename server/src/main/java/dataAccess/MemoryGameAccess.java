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
        System.out.println(games);
        System.out.println(serializedGames);
        return serializedGames;
    }

    @Override
    public ChessGame updateGame(int id, String authToken, String playerColor) {

        GameData gameData = getGame(id);
        ChessGame game = gameData.game();
        String username = authData.getUsernameByAuth(authToken);

        if (playerColor != null) {
            if (playerColor.equals("WHITE")) {
                setGame(id, getGame(id).updateWhiteUsername(username));
            } else if (playerColor.equals("BLACK")) {
                setGame(id, getGame(id).updateBlackUsername(username));

            }

        }
        return game;
    }

    private void setGame(int id, GameData game){
        for (int i = 0; i < games.size(); i++){
            if (games.get(i).gameID() == id){
                games.remove(i);
                games.set(i-1, game);
                System.out.println(games.size());
                return;
            }
        }
    }


}
