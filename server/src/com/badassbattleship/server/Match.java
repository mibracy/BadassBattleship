/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Represents a match.
 */

package com.badassbattleship.server;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Match {

    @Expose
    private UUID id;

    @Expose
    private MatchStatus status;

    @Expose
    private UUID turn; // TODO: this could probably be cleaned up into some local boolean logic!

    @Expose
    private UUID newPlayerId;

    private LocalDateTime created;

    // We need to make sure we never add more than 2 players. Better data structure for that?
    private HashMap<UUID, Player> players;

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
                players.put(player.getId(), player);

                this.newPlayerId = player.getId();

                if (players.size() == 2) {
                    start();
                }

                return player;
            }
        }
        return null;
    }

    public Player getPlayer(UUID playerId) {
        return players.get(playerId);
    }

    private void start() {
        // Assign current turn
        this.turn = (UUID)players.keySet().toArray()[0];

        // Update the status.
        this.status = MatchStatus.PLAYING;
    }

    public UUID getNewPlayerId() {
        return newPlayerId;
    }

    public void hideNewPlayerId() {
        this.newPlayerId = null;
    }

    public UUID getTurn() {
        return turn;
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