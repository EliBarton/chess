package dataAccess;

import model.UserData;

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
    public void deleteAuth(String authToken) {
        System.out.println(auths);
        auths.values().remove(authToken);
        System.out.println(auths);
    }
}
