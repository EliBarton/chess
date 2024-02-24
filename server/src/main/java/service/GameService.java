package service;

import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;

import java.util.ArrayList;

public class GameService {

    private AuthAccess authData;
    private GameAccess gameData;

    public GameService(AuthAccess authData, GameAccess gameData) {
        this.authData = authData;
        this.gameData = gameData;
    }

    public int createGame(String gameName, String authToken) throws UnauthorizedException {
        int gameID;
        if(authData.containsAuth(authToken)){
            gameID = gameData.createGame(gameName,  authToken);
        }else {
            throw new UnauthorizedException("User not authorized");
        }
        return gameID;
    }

    public ArrayList<GameAccess.SerializedGameData> listGames(String authToken) throws UnauthorizedException {
        if(authData.containsAuth(authToken)) {
            return gameData.listGames();
        }
        throw new UnauthorizedException("User not authorized");
    }

    public String updateGame(int id, String authToken, String playerColor)
            throws UnauthorizedException, InvalidDataException, DataAccessException {


        checkForUpdateGameExceptions(id, authToken, playerColor);
        Gson gson = new Gson();
        System.out.println("Player color:            " + playerColor);
        System.out.println("The game:            " + gameData.getGame(id));

        if (playerColor == null){
            return gson.toJson(gameData.updateGame(id, authToken, null));
        }

        if (playerColor.equals("WHITE") || playerColor.equals("BLACK")){
            return gson.toJson(gameData.updateGame(id, authToken, playerColor));
        } else {
            return gson.toJson(gameData.updateGame(id, authToken, null));
        }

//        if(!authData.containsAuth(authToken)) {
//            throw new UnauthorizedException("User not authorized");
//        } else if (gameData.getGame(id) == null) {
//            throw new InvalidDataException("ID is invalid");
//        }else if (playerColor == null || playerColor.isEmpty()) {
//            return gson.toJson(gameData.updateGame(id, authToken, null));
//        } else if (playerColor.equals("WHITE")){
//            if (gameData.getGame(id).whiteUsername() == null){
//                return gson.toJson(gameData.updateGame(id, authToken, playerColor));
//            } else{
//                throw new DataAccessException("Colors taken");
//            }
//        } else if (playerColor.equals("BLACK")) {
//            if (gameData.getGame(id).blackUsername() == null){
//                return gson.toJson(gameData.updateGame(id, authToken, playerColor));
//            } else{
//                throw new DataAccessException("Colors taken");
//            }
//        } else{
//            throw new DataAccessException("Colors don't exist");
//        }

    }

    private void checkForUpdateGameExceptions(int id, String authToken, String playerColor)
            throws UnauthorizedException, InvalidDataException, DataAccessException {
        if(!authData.containsAuth(authToken)) {
            throw new UnauthorizedException("User not authorized");
        } else if (gameData.getGame(id) == null) {
            throw new DataAccessException("ID is invalid");
        }
        if (gameData.getGame(id).whiteUsername() != null){
            if (playerColor.equals("WHITE")){
                throw new InvalidDataException("White is already taken");
            }
        }else if (gameData.getGame(id).blackUsername() != null) {
            System.out.println("This should be Null: " + playerColor);
            if (playerColor.equals("BLACK")) {
                throw new InvalidDataException("Black is already taken");
            }
        }
    }


}
