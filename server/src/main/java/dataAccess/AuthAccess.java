package dataAccess;

import model.UserData;

public interface AuthAccess {

    AuthResult createAuth(String username);

    AuthResult getAuth(String username);

    Boolean containsAuth(String authToken);

    void deleteAuth(String authToken);

    record AuthRequest(String username){}

    record AuthResult(String username, String authToken){}

}
