package serviceTests;

import chess.*;
import dataAccess.DataAccessException;
import dataAccess.MemoryUserAccess;
import dataAccess.UserAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.RegisterService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CustomTests {

    @Test
    @DisplayName("Clear Data")
    public void clearSuccess(){
        UserAccess testUserData = new MemoryUserAccess();
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        UserData testUser2 = new UserData("Checkersdude",
                "mypassword1", "bestatcheckers@yourmom.com");
        UserData testUser3 = new UserData("minecraftzombie",
                "eeeaaeerhhhh", "brains@yourmom.com");
        testUserData.addUser(testUser1);
        testUserData.addUser(testUser2);
        testUserData.addUser(testUser3);

        ClearService clearService = new ClearService(testUserData);
        clearService.clear();
        assertNull(testUserData.getUser("Chessmaster"), "User was not deleted");
        assertNull(testUserData.getUser("Checkersdude"), "User was not deleted");
        assertNull(testUserData.getUser("minecraftzombie"), "User was not deleted");
    }

    @Test
    @DisplayName("Register Success")
    public void registerUser(){
        UserAccess testUserData = new MemoryUserAccess();
        RegisterService registerService = new RegisterService(testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        try {
            registerService.register(testUser1);
        } catch (DataAccessException e) {
            fail("Registration failed during data access");
        }
    }
}