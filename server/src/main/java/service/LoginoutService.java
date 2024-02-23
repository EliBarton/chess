package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.InvalidDataException;
import dataAccess.UserAccess;
import model.UserData;

public class LoginoutService {
    private final AuthAccess authData;
    private final UserAccess userData;
    public LoginoutService(AuthAccess authData, UserAccess userData) {
        this.authData = authData;
        this.userData = userData;
    }

    public AuthAccess.AuthResult login(UserAccess.LoginRequest request) throws DataAccessException, InvalidDataException {
        if (authData.getAuth(request.username()) == null || userData.getUser(request.username()) == null) {
            throw new DataAccessException("User doesn't exist");
        } else if (request.username() == null || request.password() == null) {
            throw new InvalidDataException("Invalid user information");
        } else if (!userData.getUser(request.username()).password().equals(request.password())){
            throw new InvalidDataException("Wrong password");
        }
        else {
            AuthAccess.AuthResult result = authData.createAuth(request.username());
            return result;
        }
    }

    public void logout(String authToken) throws DataAccessException, InvalidDataException {
        if (authData.containsAuth(authToken)) {
            authData.deleteAuth(authToken);
        } else if (authToken == null) {
            throw new InvalidDataException("Invalid authorization");
        }
        else {
            throw new DataAccessException("User doesn't exist or isn't logged in");
        }
    }
}
