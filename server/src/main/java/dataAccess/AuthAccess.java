package dataAccess;

import model.UserData;

public interface AuthAccess {

    AuthResult createAuth(String username);

    AuthResult getAuth(String username);

    Boolean containsAuth(String authToken);

    String getUsernameByAuth(String authToken);

    void clear();

    void deleteAuth(String authToken);

    record AuthRequest(String username){}

    record AuthResult(String username, String authToken){}

}
