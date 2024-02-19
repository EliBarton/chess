package server;

import com.google.gson.Gson;
import model.UserData;
import spark.*;

import java.util.ArrayList;
import java.util.Map;


public class Server {
    private ArrayList<String> users = new ArrayList<>();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/user", this::listUsers);
        Spark.post("/user", this::register);
        Spark.delete("/user", this::deleteUser);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        Gson gson = new Gson();
        UserData newUser = gson.fromJson(req.body(), UserData.class);
        users.add(gson.toJson(newUser));
        return listUsers(req, res);
    }

    private Object listUsers(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("users", users));
    }

    private Object deleteUser(Request req, Response res) {
        users.remove(req.params(":user"));
        return listUsers(req, res);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
