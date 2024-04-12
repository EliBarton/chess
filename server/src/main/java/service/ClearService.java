package service;

import dataAccess.AuthAccess;
import dataAccess.GameAccess;
import dataAccess.MemoryUserAccess;
import dataAccess.UserAccess;

public class ClearService {
    private final UserAccess userData;
    private final AuthAccess authData;

    private final GameAccess gameData;
    public ClearService(UserAccess userData, AuthAccess authData, GameAccess gameData) {
        this.userData = userData;
        this.authData = authData;
        this.gameData = gameData;
    }


    public void clear(){
        userData.clear();
        authData.clear();
        gameData.clear();
    }
}
