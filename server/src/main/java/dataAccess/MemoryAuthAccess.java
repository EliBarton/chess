package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    public String getUsernameByAuth(String authToken) {
        for (Map.Entry<String, String> authInfo : auths.entrySet()){
            if (authInfo.getValue().equals(authToken)){
                return authInfo.getKey();
            }
        }
        return "Error getting username";
    }

    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.values().remove(authToken);
    }

    @Override
    public String toString() {
        return "MemoryAuthAccess{" +
                "auths=" + auths.toString() +
                '}';
    }
}
