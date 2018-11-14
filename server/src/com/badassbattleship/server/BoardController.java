package com.badassbattleship.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates and creates game boards.
 */
public class BoardController {
    private static Logger logger = LoggerFactory.getLogger(MatchController.class);
    private static BoardController instance = null;

    private BoardController() {
        logger.info("Board controller has started.");
    }

    public static BoardController getInstance() {
        if(instance == null) {
            instance = new BoardController();
        }
        return instance;
    }

    public Board createBoardFromJSON(String json) throws Exception {
        logger.info("Validating and creating board from JSON... " + json);
        return null;
    }

}
