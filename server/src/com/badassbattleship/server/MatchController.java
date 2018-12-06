/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Manages matches, lets users joins, generates match IDs, and removes them.
 */

package com.badassbattleship.server;

import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Singleton controller that allows for managing matches through the API.
 */
public class MatchController {

    private static Logger logger = LoggerFactory.getLogger(MatchController.class);
    private static MatchController instance = null;

    // Stores active matches.
    private HashMap<Integer, Match> matches;

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
        String shipsJSON = req.queryParams("ships");

        Random rnd = new Random();
        int matchID = rnd.nextInt(50000);

        try {
            Match newMatch = new Match(matchID);
            Player player = newMatch.addPlayer(playerName);

            if(player != null) {
                player.setBoard(BoardFactory.getInstance().fromJSON(shipsJSON));
                matches.put(matchID, newMatch);

                logger.info("Created new match with ID {} and player ({}), board named {}", matchID, player.getId(), playerName);

                return newMatch;
            }
       } catch(Exception ex) {
           logger.error("Player {} in {} error occurred: {}",
                    playerName, matchID, ex.getMessage());
        }

        return ServerUtil.errorResponse(res, "An internal error occurred.");
    }

    /**
     * Responds to route to join a match.
     * @param req
     * @param res
     * @return Returns match info if successful or otherwise error.
     */
    public Object joinMatch(Request req, Response res) {
        String playerName = req.queryParams("name");
        String shipsJSON = req.queryParams("ships");

        try {
            int matchID = Integer.parseInt(req.queryParams("id"));

            if (matches.containsKey(matchID)) {
                Match match = matches.get(matchID);

                if(match.getStatus() == MatchStatus.READY_BUT_WAITING_OPPONENT) {
                    Player player = match.addPlayer(playerName);

                    // We can actually join
                    if (player != null) {
                        player.setBoard(BoardFactory.getInstance().fromJSON(shipsJSON));

                        logger.info("Added player ({}), board {} to match {}!", player.getId(), playerName, matchID);
                        return match;
                    }
                }

                // Match is full
                logger.error("Could not add player, board {} to match ID {} (match full or invalid status).",
                        playerName, matchID);
                return ServerUtil.errorResponse(res, "Match full/invalid match status.");
            }
        } catch(Exception ex) {
            logger.error("Player {} in {} error occurred: {}",
                    playerName, req.queryParams("id"), ex.getMessage());
        }

        return ServerUtil.errorResponse(res, "An internal error occurred.");
    }

    /**
     * Responds to route that contains status of matches.
     * @param req
     * @param res
     * @return
     */
    public Object status(Request req, Response res) {
        try {
            int matchID = Integer.parseInt(req.queryParams("id"));
            UUID playerID = UUID.fromString(req.queryParams("player_id"));

            if (matches.containsKey(matchID)) {
                Match match = matches.get(matchID);
                Player player = match.getPlayer(playerID);

                if(player != null) {
                    if(match.getStatus() == MatchStatus.PLAYING) {
                        if (match.getTurn().equals(player.getId()) && player.getBoard().isHitRecently()) {
                            // Make sure to hide the next time it is requested
                            player.getBoard().setHitRecently(false);

                            match.setRecentHitPosition(player.getBoard().getRecentHitPosition());
                            match.setRecentHitState(player.getBoard().getRecentHitState());
                        } else {
                            match.setRecentHitPosition(null);
                            match.setRecentHitState(null);
                        }
                    }

                    // Hide the new player IDs, to prevent cheating
                    if (match.getNewPlayerId() != null) {
                        match.hideNewPlayerId();
                    }

                    return match;
                }
            }
        } catch(IllegalArgumentException ex) {
            logger.error("{} is not a valid match ID (illegal argument exception).",
                    req.queryParams("id"));
        }

        return ServerUtil.errorResponse(res, "Invalid match.");
    }

    /**
     * Responds to route that contains status of matches.
     * @param req
     * @param res
     * @return
     */
    public Object performHit(Request req, Response res) {
        try {
            int matchID = Integer.parseInt(req.queryParams("id"));
            UUID playerID = UUID.fromString(req.queryParams("player_id"));

            int x = Integer.parseInt(req.queryParams("x"));
            int y = Integer.parseInt(req.queryParams("y"));

            if (matches.containsKey(matchID)) {
                Match match = matches.get(matchID);

                if(match.getTurn().equals(playerID)) {
                    Player player = match.getPlayer(playerID);
                    Player opponent = match.getOpponent(playerID);

                    CellState hitStatus = opponent.getBoard().calloutShot(x, y);

                    if(hitStatus == CellState.GAME_OVER) {
                        matches.remove(matchID);
                    }

                    logger.info("{} ONTO {} (match: {}) performed hit ({},{}): {}", player.getName(), opponent.getName(), x, y, hitStatus);

                    match.nextTurn();

                    return new HitStatus(hitStatus);
                }
            }
        } catch(IllegalArgumentException ex) {
            logger.error("{} is not a valid match ID (illegal argument exception).",
                    req.queryParams("id"));
        } catch(Exception ex) {
            logger.error("Invalid hit or other error.");
        }

        return ServerUtil.errorResponse(res, "Invalid match, hit, or player.");
    }

    /**
     * Responds to route that contains status of matches.
     * @param req
     * @param res
     * @return
     */
    public Object leaveMatch(Request req, Response res) {
        try {
            int matchID = Integer.parseInt(req.queryParams("id"));
            UUID playerID = UUID.fromString(req.queryParams("id"));

            if (matches.containsKey(matchID)) {
                Match match = matches.get(matchID);
                if(match.getPlayer(playerID) != null) {
                    //todo: end match here and remove player, let other player know
                }
            }
        } catch (Exception ex) {
            logger.error("Invalid match or player");
        }

        return ServerUtil.errorResponse(res, "Invalid match or player.");
    }

}

// For serialization purposes
class HitStatus {
    @Expose
    public CellState status;

    public HitStatus(CellState status) {
        this.status = status;
    }
}
