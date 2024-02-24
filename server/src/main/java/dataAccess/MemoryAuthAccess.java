package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthAccess implements AuthAccess {

    ArrayList<AuthResult> auths = new ArrayList<>();


    @Override
    public AuthResult createAuth(String username) {
        AuthResult result = new AuthResult(username, UUID.randomUUID().toString());
        auths.add(result);
        System.out.println(auths);
        return result;
    }

    @Override
    public AuthResult getAuth(String username) {
        for (AuthResult result : auths){
            if (result.username().equals(username)){
                return result;
            }
        }
        return null;
    }

    @Override
    public Boolean containsAuth(String authToken) {
        boolean authFound = false;
        for (AuthResult result : auths){
            if (result.authToken().equals(authToken)) {
                authFound = true;
                break;
            }
        }
        return authFound;
    }

    @Override
    public String getUsernameByAuth(String authToken) {
        for (AuthResult result : auths){
            if (result.authToken().equals(authToken)) {
                return result.username();
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
        auths.removeIf(result -> result.authToken().equals(authToken));
    }

    @Override
    public String toString() {
        return "MemoryAuthAccess{" +
                "auths=" + auths.toString() +
                '}';
    }
}
