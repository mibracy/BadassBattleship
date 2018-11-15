/**
 * Author: Tobi Schweiger <tschwei@siue.edu>
 * License: Under GLWTS public license (see repo).
 * Purpose: Represents an active game participant.
 */

package com.badassbattleship.server;

public class Player {
    private String name;
    private Board board;

    public Player(String name) throws Exception {
        if(name.length() > GameSettings.NAME_MAX_LENGTH || name.isEmpty()) {
            throw new Exception(String.format("Name longer than %d or empty.", GameSettings.NAME_MAX_LENGTH));
        }
        this.name = name;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard(Board board) {
        return board;
    }

    public String getName() {
        return name;
    }

}
