package com.badassbattleship.server;

import java.util.HashMap;

public class Board {
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

			// Save the ship.
			ships.put(id, ship);

			int size = ship.getSize();
			Position pos = ship.getStartPosition();
			ShipOrientation orientation = ship.getOrientation();


		}
	}

}
