package com.badassbattleship.server;

import java.util.UUID;

public class Player {

    private String name;
    private UUID id;

    public Player(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

}
