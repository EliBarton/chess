package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryUserAccess implements UserAccess {
    ArrayList<UserData> users = new ArrayList<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public LoginResult addUser(UserData user) {
        users.add(user);
        return new LoginResult(user.username(), UUID.randomUUID().toString());
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

    @Override
    public void removeUser(String username) {

    }

    @Override
    public void updateUser(String username, UserData newUserData) {

    }
}
