package dataAccess;

import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InvalidDataException;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

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
        if (getUser(user.username()) != null){
            return null;
        }
        var statement = "INSERT INTO user (username, password, email) VALUES ('"
                + user.username() + "', '" + user.password() + "', '" + user.email() + "')";
        DatabaseManager.updateDatabase(statement);
        return authData.createAuth(user.username());
    }

    @Override
    public UserData getUser(String username) {
        var statement = "SELECT username, password, email FROM user WHERE username = '" + username + "';";
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("username");
        columnNames.add("password");
        columnNames.add("email");
        ArrayList<String> user = DatabaseManager.queryDatabaseStringArray(statement, columnNames);
        if (user.size() == 0){
            return null;
        }else {
            return new UserData(user.get(0), user.get(1), user.get(2));
        }
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
    }
