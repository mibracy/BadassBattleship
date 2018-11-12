package com.badassbattleship.server;

import javafx.util.Pair;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Match {

    private UUID id;

    private MatchStatus status;
    private LocalDateTime created;

    // We need to make sure we never add more than 2 players.
    private ArrayList<Player> players;

    public Match(UUID id, String playerName) {
        this.players = new ArrayList<>();
        this.id = id;
        this.status = MatchStatus.READY_BUT_WAITING_OPPONENT;
        this.created = LocalDateTime.now();

        addPlayer(playerName);
    }

    private void update() {
        if(players.size() == 2) {
            this.status = MatchStatus.PLAYING;
        }
    }

    public Player addPlayer(String playerName) {
        if(players.size() < 2) {
            Player player = new Player(playerName);
            players.add(player);

            update();
            return player;
        }
        return null;
    }

}

enum MatchStatus {
    READY_BUT_WAITING_OPPONENT,
    PLAYING,
    ABANDONED,
    WON,
    UNKNOWN_ERROR
}