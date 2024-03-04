package dataAccess;

import dataAccess.exceptions.DataAccessException;

import java.sql.SQLException;
import java.util.UUID;

public class SqlAuthAccess implements AuthAccess{

    public SqlAuthAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthResult createAuth(String username) {
        AuthResult result = new AuthResult(username, UUID.randomUUID().toString());
        var statement = "INSERT INTO auth (username, authToken) VALUES ('"
                + result.username() + "', '" + result.authToken() + "')";
        System.out.println(statement);
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
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
