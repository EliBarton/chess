package dataAccess;

import model.UserData;

public interface AuthAccess {

    AuthResult createAuth(String username);

    AuthResult getAuth(String username);

    void deleteAuth(String username);

    record AuthRequest(String username){}

    record AuthResult(String username, String authToken){}

}
