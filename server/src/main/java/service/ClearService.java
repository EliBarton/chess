package service;

import dataAccess.UserAccess;

public class ClearService {
    public ClearService() {
    }

    public void clear(){
        UserAccess.clearData();
    }
}
