/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Contains various methods for server management.
 */

package com.badassbattleship.server;

import com.google.gson.annotations.Expose;
import spark.Response;

public class ServerUtil {
    public static Object errorResponse(Response res, String message) {
        return errorResponse(res, message, 500);
    }
    public static Object errorResponse(Response res, String message, int statusCode) {
        res.status(statusCode);

        return new Error(message); //gotta do it this way, otherwise formatting is borked
    }
}

// Serialization purposes
class Error {
    @Expose
    public String error;
    public Error(String error) {
        this.error = error;
    }
}