package service;

import dataAccess.MemoryUserAccess;
import dataAccess.UserAccess;

public class ClearService {
    private final UserAccess userData;
    public ClearService(UserAccess userData) {
        this.userData = userData;
    }


    public void clear(){
        userData.clear();
    }
}
