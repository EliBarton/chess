package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

public class CustomTests {

    @Test
    @DisplayName("Clear Data")
    public void clearSuccess(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
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
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        RegisterService registerService = new RegisterService(testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        try {
            registerService.register(testUser1);
        } catch (DataAccessException e) {
            fail("Registration failed during data access");
        }
        assertNotNull(testUserData.getUser("Chessmaster"));
    }

    @Test
    @DisplayName("Register Twice")
    public void registerUserAgain(){
        MemoryAuthAccess authData = new MemoryAuthAccess();
        UserAccess testUserData = new MemoryUserAccess(authData);
        RegisterService registerService = new RegisterService(testUserData);
        UserData testUser1 = new UserData("Chessmaster",
                "Chess123", "bestatchess@yourmom.com");
        try {
            registerService.register(testUser1);
        } catch (DataAccessException e) {
            fail("Registration failed during data access");
        }
        try {
            registerService.register(testUser1);
        } catch (DataAccessException ignored) {
            return;
        }
        fail("User was able to register twice");
    }
}