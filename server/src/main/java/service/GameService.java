package service;

import dataAccess.AuthAccess;
import dataAccess.GameAccess;
import dataAccess.UnauthorizedException;

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


}
