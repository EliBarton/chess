package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import model.UserData;

public class LoginoutService {
    private final AuthAccess authData;
    public LoginoutService(AuthAccess authData) {
        this.authData = authData;
    }

    public AuthAccess.AuthResult login(UserAccess.LoginRequest request) throws DataAccessException {
        if (authData.getAuth(request.username()) == null) {
            throw new DataAccessException("User doesn't exists");
        } else if (request.username() == null || request.password() == null) {
            throw new RuntimeException("Invalid user information");
        } else {
            return authData.createAuth(request.username());
        }
    }
}
