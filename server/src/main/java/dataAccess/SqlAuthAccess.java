package dataAccess;

import dataAccess.exceptions.DataAccessException;

import java.sql.SQLException;
import java.util.UUID;

import static dataAccess.DatabaseManager.*;

public class SqlAuthAccess implements AuthAccess{

    public SqlAuthAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthResult createAuth(String username) {
        AuthResult result = new AuthResult(username, UUID.randomUUID().toString());
        var statement = "INSERT INTO auth (username, authToken) VALUES ('"
                + result.username() + "', '" + result.authToken() + "')";
        DatabaseManager.updateDatabase(statement);
        return result;
    }



    @Override
    public AuthResult getAuth(String username) {
        var statement = "SELECT authToken FROM auth WHERE username = '" + username + "';";
        String authToken = DatabaseManager.queryDatabaseString(statement, "authToken");
        return new AuthResult(username, authToken);
    }

    @Override
    public Boolean containsAuth(String authToken) {
        var statement = "SELECT EXISTS(SELECT authToken FROM auth WHERE authToken = '" + authToken + "')";
        return queryDatabaseExists(statement);
    }



    @Override
    public String getUsernameByAuth(String authToken) {
        var statement = "SELECT username FROM auth WHERE authToken = '" + authToken + "'";
        return DatabaseManager.queryDatabaseString(statement, "username");
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE TABLE auth";
        DatabaseManager.updateDatabase(statement);
    }

    @Override
    public void deleteAuth(String authToken) {
        var statement = "DELETE FROM auth WHERE authToken = '" + authToken + "'";
        DatabaseManager.updateDatabase(statement);
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
