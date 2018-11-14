/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Represents a match.
 */

package com.badassbattleship.server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Match {

    private UUID id;

    private MatchStatus status;
    private LocalDateTime created;

    // We need to make sure we never add more than 2 players. Better data structure for that?
    private HashMap<String, Player> players;

    private String playerTurn; // determine who's turn it is

    public Match(UUID id) {
        this.players = new HashMap<>();
        this.id = id;
        this.status = MatchStatus.READY_BUT_WAITING_OPPONENT;
        this.created = LocalDateTime.now();
    }

    public Player addPlayer(String playerName) throws Exception {
        if(players.size() < 2) {
            Player player = new Player(playerName);

            // Make sure no name duplicates
            if(!players.containsKey(playerName)) {
                players.put(playerName, player);

                if (players.size() == 2) {
                    start();
                }

                return player;
            }
        }
        return null;
    }

    private void start() {
        // First player in players gets the firs turn
        this.playerTurn = players.entrySet().iterator().next().getKey();

        // Update the status.
        this.status = MatchStatus.PLAYING;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public MatchStatus getStatus() {
        return status;
    }

}

enum MatchStatus {
    READY_BUT_WAITING_OPPONENT,
    PLAYING,
    ABANDONED,
    WON,
    UNKNOWN_ERROR
}