package no.hvl.dat110.ac.restservice;

import com.google.gson.Gson;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {

    static AccessLog accesslog = null;
    static AccessCode accesscode = null;

    public static void main(String[] args) {

        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8090);
        }

        // objects for data stored in the service

        accesslog = new AccessLog();
        accesscode = new AccessCode();

        after((req, res) -> {
            res.type("application/json");
        });

        // for basic testing purposes
        get("/accessdevice/hello", (req, res) -> {

            Gson gson = new Gson();

            return gson.toJson("IoT Access Control Device");
        });

        // TODO: implement the routes required for the access control service
        // as per the HTTP/REST operations describined in the project description

        post("/accessdevice/log/", (req, res) ->
                accesslog.get(
                        accesslog.add(
                                new Gson().fromJson(
                                        req.body(),
                                        AccessMessage.class)
                                        .getMessage()))
        );

        get("/accessdevice/log/", (req, res) -> accesslog.toJson());

        get("/accessdevice/log/:id",
                (req, res) -> new Gson().toJson(accesslog.get(Integer.parseInt(req.params("id"))))
        );

        put("/accessdevice/code",
                (request, response) -> accesscode = new Gson().fromJson(request.body(), AccessCode.class));

        get("/accessdevice/code", (request, response) ->
                new Gson().toJson(accesscode));

        delete("/accessdevice/log/", (request, response) -> {
            accesslog.clear();
            return accesslog.toJson();
        });
    }

}
