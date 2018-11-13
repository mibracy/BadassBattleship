package com.badassbattleship.server;

import spark.Response;

public class ServerUtil {
    public static Object errorResponse(Response res, String message) {
        res.type("application/json");
        res.status(500);

        return new Error(message);
    }
}

class Error {
    private String error;

    Error(String error) {
        this.error = error;
    }
}