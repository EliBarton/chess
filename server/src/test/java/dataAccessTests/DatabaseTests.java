package dataAccessTests;

import dataAccess.AuthAccess;
import dataAccess.SqlAuthAccess;
import dataAccess.SqlUserAccess;
import dataAccess.UserAccess;
import dataAccess.exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {

    @Test
    @DisplayName("User Test")
    public void userTest(){
        try {
            AuthAccess authAccess = new SqlAuthAccess();
            UserAccess userAccess = new SqlUserAccess(authAccess);
            userAccess.addUser(new UserData("Chessmaster", "chesspassword", "mom@yourmom.com"));
        } catch (DataAccessException e) {
            fail("Failed in creating database:" + e);
        }
    }

}
