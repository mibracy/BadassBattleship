/**
 * Authors:		Tobi Schweiger <tschwei@siue.edu>
 * 				
 * Description:	Main entry point for Battleship game.
 * 				This starts the Spark API server and handles the endpoints.
 * 				The rest of the game logic is handled somewhere else.
 */

package com.badassbattleship.server;

import java.util.logging.LogManager;

import static spark.Spark.*;

public class Battleship {

	public static void main(String[] args) {
		
		// This is an example of a REST end point.
		// We will use this to output info about ships and placements etc...
		
		post("/match/new", (req, res) -> {

			return "";
		});

		path("/api", () -> {
			path("/match", () -> {
				post("/new", MatchManager.getInstance()::newMatch);
				post("/join", (req, res) -> {
					return "";
				});
			});
		});
	}

}