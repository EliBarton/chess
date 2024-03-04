package dataAccess;

import dataAccess.exceptions.DataAccessException;

import java.sql.SQLException;

public class SqlAuthAccess implements AuthAccess{
    @Override
    public AuthResult createAuth(String username) {
        return null;
    }

    @Override
    public AuthResult getAuth(String username) {
        return null;
    }

    @Override
    public Boolean containsAuth(String authToken) {
        return null;
    }

    @Override
    public String getUsernameByAuth(String authToken) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void deleteAuth(String authToken) {

    }

    private final String[] createStatements = {
            """
                CREATE TABLE IF NOT EXISTS auth(
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(256) NOT NULL,
                `authToken` varchar(256) NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(username),
                INDEX(authToken)
                )
                """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
}
