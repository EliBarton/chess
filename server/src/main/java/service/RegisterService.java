package service;


import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

public class RegisterService {
    private final UserAccess userData;
    public RegisterService(UserAccess userData) {
        this.userData = userData;
    }

    public UserAccess.LoginResult register(UserData user) throws DataAccessException {
        if (userData.getUser(user.username()) == null){
            return userData.addUser(user);
        }
        else{
            throw new DataAccessException("User already exists");
        }
    }

}
