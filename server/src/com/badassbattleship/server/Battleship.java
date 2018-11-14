/**
 * Authors: Tobi Schweiger <tschwei@siue.edu>
 *     		Michael Bracy
 *     		Eric Welch
 * License: Under GLWTS public license (see repo).
 * Purpose: Main point for battleship serer.
 */

package com.badassbattleship.server;

import static spark.Spark.*;

import com.google.gson.Gson;
import spark.Filter;

public class Battleship {

	public static void main(String[] args) {
		// JSON renderer
		Gson gson = new Gson();

		// we can get rid of this later, it's just for testing and allowing CORS
		after((Filter) (request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET,POST");
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