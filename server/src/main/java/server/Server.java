package server;

import com.google.gson.Gson;
import spark.*;

import java.util.Map;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/", (req, res) -> "Hello World");
        Spark.awaitInitialization();
        return Spark.port();
    }

    private static void register() {
        Spark.post("/user/:user", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String string = request.params(":user");
                response.type("application/json");
                return new Gson().toJson(Map.of("user", string));
            }
        });
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
