package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.UUID;

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
                System.out.println(user.username() + " == " + username);
                return user;
            }
        }
        return null;
    }

    @Override
    public void removeUser(String username) {

    }

    @Override
    public void updateUser(String username, UserData newUserData) {

    }
}
