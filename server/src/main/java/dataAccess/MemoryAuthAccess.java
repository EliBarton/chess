package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthAccess implements AuthAccess {

    HashMap<String, String> auths = new HashMap<>();


    @Override
    public AuthResult createAuth(String username) {
        AuthResult result = new AuthResult(username, UUID.randomUUID().toString());
        auths.put(result.username(), result.authToken());
        return result;
    }

    @Override
    public AuthResult getAuth(String username) {
        return new AuthResult(username, auths.get(username));
    }

    @Override
    public Boolean containsAuth(String authToken) {
        return auths.containsValue(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.values().remove(authToken);
    }
}
