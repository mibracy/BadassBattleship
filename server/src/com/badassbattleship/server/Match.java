package com.badassbattleship.server;

import javafx.util.Pair;

public class Match {

    private MatchStatus status;

    public Match(String playerName) {
        addPlayer(playerName);
    }

    public void addPlayer(String playerName) {

    }

}

enum MatchStatus {
    READY_BUT_WAITING_OPPONENT,
    ABANDONED,
    WON,
    UNKNOWN_ERROR
}