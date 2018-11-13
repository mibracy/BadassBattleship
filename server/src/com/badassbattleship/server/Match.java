/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Represents a match.
 */

package com.badassbattleship.server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Match {

    private UUID id;

    private MatchStatus status;
    private LocalDateTime created;

    // We need to make sure we never add more than 2 players. Better data structure for that?
    private ArrayList<Player> players;

    private String playerTurn;

    public Match(UUID id, String playerName) {
        this.players = new ArrayList<>();
        this.id = id;
        this.status = MatchStatus.READY_BUT_WAITING_OPPONENT;
        this.created = LocalDateTime.now();

        addPlayer(playerName);
    }

    public Player addPlayer(String playerName) {
        if(players.size() < 2) {
            Player player = new Player(playerName);
            players.add(player);

            // We are ready for the match to start!
            start();

            return player;
        }
        return null;
    }

    private void start() {
        if(players.size() == 2) {
            this.playerTurn = players.get(0).getName();
            this.status = MatchStatus.PLAYING;
        }
    }

}

enum MatchStatus {
    READY_BUT_WAITING_OPPONENT,
    PLAYING,
    ABANDONED,
    WON,
    UNKNOWN_ERROR
}