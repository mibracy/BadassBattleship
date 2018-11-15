package com.badassbattleship.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates and creates game boards.
 */
public class BoardFactory {
    private static Logger logger = LoggerFactory.getLogger(MatchController.class);
    private static BoardFactory instance = null;

    private BoardFactory() {
        logger.info("Board controller has started.");
    }

    public static BoardFactory getInstance() {
        if(instance == null) {
            instance = new BoardFactory();
        }
        return instance;
    }

    public Board fromJSON(String json) throws Exception {
        logger.info("Validating and creating board from JSON... ");

        Board board = new Board();

        Ship[] ships = Battleship.gson.fromJson(json, Ship[].class);

        for(Ship ship : ships) {
           board.createShip(ship);
        }

        return board;
    }

}