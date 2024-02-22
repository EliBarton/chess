package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryUserAccess;
import dataAccess.UserAccess;
import model.UserData;
import service.ClearService;
import service.RegisterService;
import spark.*;

import java.util.ArrayList;
import java.util.Map;


public class Server {
    private final UserAccess userData = new MemoryUserAccess();
    private final ClearService clearService = new ClearService(userData);
    private final RegisterService registerService = new RegisterService(userData);
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        Gson gson = new Gson();
        UserData newUser = gson.fromJson(req.body(), UserData.class);
        UserAccess.LoginResult result;
        try {
            result = registerService.register(newUser);
            res.status(200);
        }
        catch (DataAccessException e){
            res.status(401);
            return null;
        }

        return gson.toJson(result);
    }


    private Object clear(Request req, Response res) {
        clearService.clear();
        res.status(204);
        System.out.println("clear pressed");
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
