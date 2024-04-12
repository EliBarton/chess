package dataAccess.Access;

import dataAccess.exceptions.AuthAccess;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserAccess implements UserAccess {
    ArrayList<UserData> users = new ArrayList<>();

    AuthAccess authData;

    public MemoryUserAccess(AuthAccess authData) {
        this.authData = authData;
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public AuthAccess.AuthResult addUser(UserData user) {
        users.add(user);
        return authData.createAuth(user.username());
    }

    @Override
    public UserData getUser(String username) {
        for (UserData user : users){
            if (user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

}
