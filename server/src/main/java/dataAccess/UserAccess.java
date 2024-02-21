package dataAccess;

import model.UserData;

public interface UserAccess {
    void clear();
    void addUser(UserData user);
    UserData getUser(String username);
    void removeUser(String username);
    void updateUser(String username, UserData newUserData);
}
