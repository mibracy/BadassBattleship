package com.badassbattleship.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badassbattleship.server.Ship;

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
				grid[i][j] = GameSettings.CELL_FREE;
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
		}
	}
	
	public void placeShip(int id, int size, Position pos, ShipOrientation orient) throws Exception {
		switch(orient) {
			case HORIZONTAL:
				for (int i = 0; i < size; i++) {
					if (grid[pos.getY()][pos.getX() + i] == GameSettings.CELL_FREE) {
						grid[pos.getY()][pos.getX() + i] = id;
					} else {
						throw new Exception("Invalid placement");
					}
				}
				break;
			case VERTICAL:
				for (int i = 0; i < size; i++) {
					if (grid[pos.getY() + i][pos.getX()] == GameSettings.CELL_FREE) {
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
	
	public void printGridToConsole() {
		for (int i = 0; i < GameSettings.SIZE; i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < GameSettings.SIZE; j++) {
				row.append(String.format("[%2d]", grid[i][j]));
			}
			logger.info(row + "\n");
		}
	}

	/**
	 * Returns a GameState enum which determines if a hit is a miss, hit, duplicate,
	 * or game-ending condition.
	 * @param x Hit X
	 * @param y Hit Y
	 * @return GameState
	 */
	public GameState calloutShot(int x, int y) {
		if(grid[y][x] == GameSettings.CELL_FREE) {
			grid[y][x] = GameSettings.CELL_MISS;
			//change the display here to a miss color
			return GameState.MISS;
		}
		else if(grid[y][x] == GameSettings.CELL_MISS || grid[y][x] == GameSettings.CELL_HIT) {
			return GameState.DUPLICATE_HIT;
		}
		else {
			//This should get the ship in the hashmap and update its hit count. Then it changes
			//the grid to reflect the hit and returns true.
			ships.get(grid[y][x]).successfulHit();

			if((ships.get(grid[y][x])).checkIfShipDestroyed()) {
				aliveShips--;
			}

			grid[y][x] = GameSettings.CELL_HIT;

			if(aliveShips == 0) {
				// We are done! Let MatchController figure out the rest from here.
				return GameState.GAME_OVER;
			}

			return GameState.HIT;
		}
	}
}
enum GameState {
	HIT,
	MISS,
	DUPLICATE_HIT,
	GAME_OVER
}