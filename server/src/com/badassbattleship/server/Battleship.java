/**
 * Authors: Tobi Schweiger <tschwei@siue.edu>
 *     		Michael Bracy
 *     		Eric Welch
 * License: Under GLWTS public license (see repo).
 * Purpose: Main point for battleship serer.
 */

package com.badassbattleship.server;

import static spark.Spark.*;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Filter;

public class Battleship {

	// JSON parser for this project
	public static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting().create();

	public static void main(String[] args) {
		// JSON renderer

		// we can get rid of this later, it's just for testing and allowing CORS
		after((Filter) (request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET,POST");
			response.type("application/json");
		});

		// Todo (Tobi): turn these into POSTs!
		// register game routes
		path("/api", () -> {
			path("/match", () -> {
				get("/new", MatchController.getInstance()::newMatch, gson::toJson);
				get("/join", MatchController.getInstance()::joinMatch, gson::toJson);
				get("/status", MatchController.getInstance()::status, gson::toJson);
			});
		});
	}

}