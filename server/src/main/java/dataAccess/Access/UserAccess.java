package dataAccess.Access;

import dataAccess.exceptions.AuthAccess;
import model.UserData;

public interface UserAccess {
    void clear();
    AuthAccess.AuthResult addUser(UserData user);
    UserData getUser(String username);
    record LoginRequest(String username, String password) {}

}
