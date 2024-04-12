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
        if (!authData.containsAuth(authToken)){
            return 0;
        }
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        String newGame = new Gson().toJson(new ChessGame());
        String statement;
        if (gameName != null) {
            statement = "INSERT INTO game (game_id, white_username, black_username, game_name, chess_game) VALUES ('"
                    + gameID + "', null, null, '" + gameName.replace("'", "''") + "', '" + newGame + "')";
        }else{
            return 0;
        }
        DatabaseManager.updateDatabase(statement);
        return gameID;
    }

    @Override
    public GameData getGame(int id) {
        var statement = """
            SELECT game_id, white_username, black_username, game_name, chess_game
            FROM game
            WHERE game_id = '
            """ + id + "'";
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("game_id");
        columnNames.add("white_username");
        columnNames.add("black_username");
        columnNames.add("game_name");
        columnNames.add("chess_game");
        ArrayList<String> gameData = DatabaseManager.queryDatabaseStringArray(statement, columnNames);
        if (!gameData.isEmpty()) {
            int gameID = Integer.parseInt(gameData.get(0));
            Gson gson = new Gson();
            ChessGame chessGame = gson.fromJson(gameData.get(4), ChessGame.class);
            return new GameData(gameID, gameData.get(1), gameData.get(2), gameData.get(3), chessGame);
        }
        return null;
    }

    @Override
    public ArrayList<SerializedGameData> listGames() {
        Gson gson = new Gson();
        ArrayList<SerializedGameData> serializedGames = new ArrayList<>();
        var statement = "SELECT * from game";
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("game_id");
        columnNames.add("white_username");
        columnNames.add("black_username");
        columnNames.add("game_name");
        columnNames.add("chess_game");
        ArrayList<String> row = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try(var rs = preparedStatement.executeQuery()){
                    while (rs.next()) {
                        for (String name : columnNames) {
                            row.add(rs.getString(name));
                        }
                        serializedGames.add(new SerializedGameData(Integer.parseInt(row.get(0)), row.get(1), row.get(2),
                                row.get(3), row.get(4)));
                        row.clear();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return serializedGames;
    }

    @Override
    public ChessGame updateGame(int id, String authToken, String playerColor) {
        if (!authData.containsAuth(authToken) || getGame(id) == null){
            return null;
        }
        Gson gson = new Gson();
        GameData gameData = getGame(id);
        ChessGame game = gameData.game();
        String username = authData.getUsernameByAuth(authToken);

        if (playerColor != null) {
            if (playerColor.equals("WHITE")) {

                var statement = """
                        UPDATE game SET white_username = '""" + username + """
                        ', chess_game = '""" + gson.toJson(game) +
                        """
                                ' WHERE game_id = '""" + id + "'";
                DatabaseManager.updateDatabase(statement);

            } else if (playerColor.equals("BLACK")) {
                var statement = """
                        UPDATE game SET black_username = '""" + username + """
                        ', chess_game = '""" + gson.toJson(game) + """
                        ' WHERE game_id = '""" + id + "'";
                DatabaseManager.updateDatabase(statement);
            }

        }
        return game;
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
                `chess_game` text NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(game_id),
                INDEX(white_username),
                INDEX(black_username),
                INDEX(game_name),
                INDEX(chess_game(255))
                )
                """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
}
