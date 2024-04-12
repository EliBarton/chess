package service;


import dataAccess.exceptions.AuthAccess;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InvalidDataException;
import dataAccess.Access.UserAccess;
import model.UserData;

public class RegisterService {
    private final UserAccess userData;
    public RegisterService(UserAccess userData) {
        this.userData = userData;
    }

    public AuthAccess.AuthResult register(UserData user) throws DataAccessException, InvalidDataException {
        if (userData.getUser(user.username()) != null){
            throw new DataAccessException("User already exists");
        }
        else if (user.username() == null || user.password() == null || user.email() == null){
            throw new InvalidDataException("Invalid user information");
        }
        else{
            return userData.addUser(user);
        }
    }

}
