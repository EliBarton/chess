package server;

import com.google.gson.Gson;
import model.UserData;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.ObjectRowWriterProcessor;
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
        Spark.delete("/db", this::clear);
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

    private Object clear(Request req, Response res) {
        users.clear();
        if (!users.isEmpty()){
            res.status(500);
            res.body("didn't work");
        }else{
            res.status(200);
            res.body(listUsers(req, res).toString());
        }
        return listUsers(req, res);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
