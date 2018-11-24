package com.badassbattleship.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Board {
	private static Logger logger = LoggerFactory.getLogger(Board.class);

	private int grid[][];
	private HashMap<Integer, Ship> ships;

	private int aliveShips;
	
	public Board() {
		grid = new int[GameSettings.SIZE][GameSettings.SIZE];
		ships = new HashMap<>();
		aliveShips = 0;

		// Fill with -1 to signal that it is not occupied
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid.length; j++) {
				grid[i][j] = -1;
			}
		}
	}
	
	public void createShip(Ship ship) throws Exception {
		if (aliveShips < GameSettings.NUM_SHIPS) {
			int id = aliveShips++;
			int size = ship.getSize();
			Position pos = ship.getStartPosition();
			ShipOrientation orient = ship.getOrientation();
	
			// Save the ship.
			ships.put(id, ship);

			// This will throw on invalid placement.
			placeShip(id, size, pos, orient);
		} else {
			throw new Exception("Too many ships being placed.");
		}
	}
	
	public void placeShip(int id, int size, Position pos, ShipOrientation orient) throws Exception {
		switch(orient) {
			case HORIZONTAL:
				for (int i = 0; i < size; i++) {
					if (grid[pos.getY()][pos.getX() + i] == -1) {
						grid[pos.getY()][pos.getX() + i] = id;
					} else {
						throw new Exception("Invalid placement");
					}
				}
				break;
			case VERTICAL:
				for (int i = 0; i < size; i++) {
					if (grid[pos.getY() + i][pos.getX()] == -1) {
						grid[pos.getY() + i][pos.getX()] = id;
					} else {
						throw new Exception("Invalid placement");
					}
				}
				break;
			default:
				break;
		}
	}

	public boolean placeHit(int x, int y) throws Exception {
		// return TRUE if successful hit, FALSE if unsuccessful
		return false;
	}
	
	public void printGridToConsole() {
		for (int i = 0; i < GameSettings.SIZE; i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < GameSettings.SIZE; j++) {
				row.append(String.format("[%2d]", grid[i][j]));
			}
			logger.info(row + "\n");
		}
	}

}
