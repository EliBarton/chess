package server;

import com.google.gson.Gson;
import dataAccess.*;
import model.UserData;
import service.ClearService;
import service.LoginoutService;
import service.RegisterService;
import spark.*;


public class Server {
    private final AuthAccess authData = new MemoryAuthAccess();
    private final UserAccess userData = new MemoryUserAccess(authData);
    private final ClearService clearService = new ClearService(userData);
    private final RegisterService registerService = new RegisterService(userData);
    private final LoginoutService loginoutService = new LoginoutService(authData, userData);
    private record errorMessage(String message){}
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        Gson gson = new Gson();
        UserData newUser = gson.fromJson(req.body(), UserData.class);
        AuthAccess.AuthResult result;
        try {
            result = registerService.register(newUser);
            res.status(200);
        }
        catch (DataAccessException e){
            res.status(403);
            return gson.toJson(new errorMessage("Error: Registration failed, " + e.getMessage()));
        }catch (InvalidDataException e){
            res.status(400);
            return gson.toJson(new errorMessage("Error: Registration failed, " + e.getMessage()));
        }

        return gson.toJson(result);
    }


    private Object clear(Request req, Response res) {
        clearService.clear();
        res.status(200);
        return "";
    }

    private Object login(Request req, Response res) {
        Gson gson = new Gson();
        UserAccess.LoginRequest userData = gson.fromJson(req.body(), UserAccess.LoginRequest.class);
        AuthAccess.AuthResult result;
        try{
            result = loginoutService.login(userData);
        }catch (DataAccessException e){
            res.status(401);
            return gson.toJson(new errorMessage("Error: Login failed, " + e.getMessage()));
        }catch (InvalidDataException e){
            res.status(401);
            return gson.toJson(new errorMessage("Error: Login failed, " + e.getMessage()));
        }
        return gson.toJson(result);
    }

    private Object logout(Request req, Response res) {
        Gson gson = new Gson();
        String authData = gson.fromJson(req.body(), String.class);
        try{
            loginoutService.logout(authData);
            res.status(200);
        }catch (DataAccessException | InvalidDataException e){
            System.out.println("Error: Logout failed, " + e.getMessage());
            //res.status(401);
            return gson.toJson(new errorMessage("Error: Logout failed, " + e.getMessage()));
        }
        return gson.toJson("");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
