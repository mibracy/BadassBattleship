/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Manages matches, lets users joins, generates match IDs, and removes them.
 */

package com.badassbattleship.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.UUID;

/**
 * Singleton controller that allows for managing matches through the API.
 */
public class MatchController {

    private static Logger logger = LoggerFactory.getLogger(MatchController.class);
    private static MatchController instance = null;

    // Stores active matches.
    private HashMap<UUID, Match> matches;

    private MatchController() {
        logger.info("Match controller has started.");
        matches = new HashMap<>();
    }

    public static MatchController getInstance() {
        if(instance == null) {
            instance = new MatchController();
        }
        return instance;
    }

    /**
     * Responds to route to create brand new request.
     * It requires the first player's name so that the second player can join.
     * @param req
     * @param res
     * @return Responds with the UUID of the new match for the other player to join.
     */
    public Object newMatch(Request req, Response res) {
        String playerName = req.queryParams("name");

        UUID matchID = UUID.randomUUID();

        Match newMatch = new Match(matchID, playerName);

        matches.put(matchID, newMatch);

        logger.info("Created new match with ID {} and player 1 named {}", matchID, playerName);

        return newMatch;
    }

    /**
     * Responds to route to join a match.
     * @param req
     * @param res
     * @return Returns match info if successful or otherwise error.
     */
    public Object joinMatch(Request req, Response res) {
        String playerName = req.queryParams("name");

        //todo: catch malformed identifier
        UUID matchID = UUID.fromString(req.queryParams("id"));

        if(matches.containsKey(matchID)) {
            Match match = matches.get(matchID);
            Player player = match.addPlayer(playerName);

            // We can actually join
            if(player != null) {
                logger.info("Added player {} to {}!", playerName, matchID);
                return match;
            }

            // Match is full
            logger.error("Could not add player {} to match ID {}", playerName, matchID);
            return "{ \"error\": \"Could not add player to match\" }";
        }

        // todo: make error method
        res.type("application/json");
        res.status(500);
        return "{ \"error\": \"Invalid Match ID\" }";
    }

    /**
     * Responds to route that contains status of matches.
     * @param req
     * @param res
     * @return
     */
    public Object status(Request req, Response res) {
        return "not implemented";
    }

    /**
     * Responds to route to leave a match.
     * @param req
     * @param res
     * @return
     */
    public String leaveMatch(Request req, Response res) {
        return "not implemented";
    }

}
