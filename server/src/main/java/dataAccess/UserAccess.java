package dataAccess;

import model.UserData;

public interface UserAccess {
    void clear();
    AuthAccess.AuthResult addUser(UserData user);
    UserData getUser(String username);
    void removeUser(String username);
    void updateUser(String username, UserData newUserData);

    record LoginRequest(String username, String password) {}

}
