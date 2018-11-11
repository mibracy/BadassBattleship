package com.badassbattleship.server;

import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Manages matches, lets users joins, generates match IDs, and removes them.
 */
public class MatchManager {

    private static MatchManager instance = null;

    private HashMap<UUID, Match> matches;

    private MatchManager() {
        matches = new HashMap<>();
    }

    public static MatchManager getInstance() {
        if(instance == null) {
            instance = new MatchManager();
        }
        return instance;
    }

    public String newMatch(Request req, Response res) {
        String player1Name = req.params("name");

        UUID matchID = UUID.randomUUID();
        matches.put(matchID, new Match(player1Name));

        return "first player name is " + player1Name + " and match ID is " + matchID.toString();
    }

}
