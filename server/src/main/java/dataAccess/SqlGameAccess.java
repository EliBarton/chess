package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.exceptions.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class SqlGameAccess implements GameAccess {

    AuthAccess authData;

    public SqlGameAccess(AuthAccess authAccess) throws DataAccessException {
        authData = authAccess;
        configureDatabase();
    }

    @Override
    public int createGame(String gameName, String authToken) {
        //Still need to check if authtoken is correct...
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        String newGame = new Gson().toJson(new ChessGame());
        var statement = "INSERT INTO game (game_id, white_username, black_username, game_name, game) VALUES ('"
                + gameID + "', null, null, '" + gameName + "', '" + newGame + "')";
        DatabaseManager.updateDatabase(statement);

        return 0;
    }

    @Override
    public GameData getGame(int id) {
        return null;
    }

    @Override
    public ArrayList<SerializedGameData> listGames() {
        return null;
    }

    @Override
    public ChessGame updateGame(int id, String authToken, String playerColor) {
        return null;
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE TABLE game";
        DatabaseManager.updateDatabase(statement);
    }

    private final String[] createStatements = {
            """
                CREATE TABLE IF NOT EXISTS game(
                `id` int NOT NULL AUTO_INCREMENT,
                `game_id` int NOT NULL,
                `white_username` varchar(256),
                `black_username` varchar(256),
                `game_name` varchar(256) NOT NULL,
                `game` text NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(game_id),
                INDEX(white_username),
                INDEX(black_username),
                INDEX(game_name),
                INDEX(game(255))
                )
                """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
}
