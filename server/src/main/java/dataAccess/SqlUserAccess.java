package dataAccess;

import dataAccess.exceptions.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public class SqlUserAccess implements UserAccess{

    AuthAccess authData;

    public SqlUserAccess(AuthAccess authAccess) throws DataAccessException {
        this.authData = authAccess;
        configureDatabase();
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE TABLE user";
        DatabaseManager.updateDatabase(statement);
    }

    @Override
    public AuthAccess.AuthResult addUser(UserData user) {



        var statement = "INSERT INTO user (username, password, email) VALUES ('"
                + user.username() + "', '" + user.password() + "', '" + user.email() + "')";
        DatabaseManager.updateDatabase(statement);
        return authData.createAuth(user.username());
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    private final String[] createStatements = {
            """
                CREATE TABLE IF NOT EXISTS user(
                `id` int NOT NULL AUTO_INCREMENT,
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(username),
                INDEX(password),
                INDEX(email)
                )
                """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    private void printTable() {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    System.out.println(ps);
                    if (rs.next()) {
                        System.out.println(rs);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    }
