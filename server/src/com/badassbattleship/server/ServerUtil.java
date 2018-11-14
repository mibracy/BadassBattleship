/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Contains various methods for server management.
 */

package com.badassbattleship.server;

import spark.Response;

public class ServerUtil {
    public static Object errorResponse(Response res, String message) {
        return errorResponse(res, message, 500);
    }
    public static Object errorResponse(Response res, String message, int statusCode) {
        res.type("application/json");
        res.status(statusCode);

        return String.format("{ \"error\": \"%s\" }", message);
    }
}