package service;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InvalidDataException;
import dataAccess.exceptions.UnauthorizedException;

import java.util.ArrayList;

public class GameService {

    private final AuthAccess authData;
    private final GameAccess gameData;

    public GameService(AuthAccess authData, GameAccess gameData) {
        this.authData = authData;
        this.gameData = gameData;
    }

    public int createGame(String gameName, String authToken) throws UnauthorizedException {
        int gameID;
        if(authData.containsAuth(authToken)){
            gameID = gameData.createGame(gameName,  authToken);
        }else {
            throw new UnauthorizedException("User not authorized");
        }
        return gameID;
    }

    public ArrayList<GameAccess.SerializedGameData> listGames(String authToken) throws UnauthorizedException {
        if(authData.containsAuth(authToken)) {
            return gameData.listGames();
        }
        throw new UnauthorizedException("User not authorized");
    }

    public String updateGame(int id, String authToken, String playerColor)
            throws UnauthorizedException, InvalidDataException, DataAccessException {


        checkForUpdateGameExceptions(id, authToken, playerColor);
        Gson gson = new Gson();

        if (playerColor == null){
            return gson.toJson(gameData.updateGame(id, authToken, null));
        }

        if (playerColor.equals("WHITE") || playerColor.equals("BLACK")){
            return gson.toJson(gameData.updateGame(id, authToken, playerColor));
        } else {
            return gson.toJson(gameData.updateGame(id, authToken, null));
        }


    }

    private void checkForUpdateGameExceptions(int id, String authToken, String playerColor)
            throws UnauthorizedException, InvalidDataException, DataAccessException {
        if(!authData.containsAuth(authToken)) {
            throw new UnauthorizedException("User not authorized");
        } else if (gameData.getGame(id) == null) {
            throw new DataAccessException("ID is invalid");
        }
        if (gameData.getGame(id).whiteUsername() != null){
            if (playerColor.equals("WHITE")){
                throw new InvalidDataException("White is already taken");
            }
        }else if (gameData.getGame(id).blackUsername() != null) {
            if (playerColor.equals("BLACK")) {
                throw new InvalidDataException("Black is already taken");
            }
        }
    }


}
