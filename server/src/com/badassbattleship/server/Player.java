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
        if(name.length() > 20 || name.isEmpty()) throw new Exception("Name longer than 20 or empty.");
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
