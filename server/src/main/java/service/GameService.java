package service;

import dataAccess.AuthAccess;
import dataAccess.GameAccess;
import dataAccess.InvalidDataException;
import dataAccess.UnauthorizedException;
import model.GameData;

import java.util.ArrayList;

public class GameService {

    private AuthAccess authData;
    private GameAccess gameData;

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

    public ArrayList<GameData> listGames(String authToken) throws UnauthorizedException {
        if(authData.containsAuth(authToken)) {
            return gameData.listGames();
        }
        throw new UnauthorizedException("User not authorized");
    }

    public String updateGame(int id, String authToken) throws UnauthorizedException, InvalidDataException {
        if(!authData.containsAuth(authToken)) {
            throw new UnauthorizedException("User not authorized");
        } else if (gameData.getGame(id) == null) {
            throw new InvalidDataException("ID is invalid");
        } else{
            return gameData.updateGame(id);
        }

    }


}
