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
			int size = ship.getSize();
			Position pos = ship.getStartPosition();
			ShipOrientation orient = ship.getOrientation();
			
			if (verifyPlacement(id,size,pos,orient) == true) {
				// Save the ship.
				ships.put(id, ship);
				
				placeShip(id, size, pos, orient);
			}
			else {
				aliveShips--;
			}
			

		}
	}
	
	public void placeShip(int id, int size, Position pos, ShipOrientation orient) {
		switch(orient) {
			case HORIZONTAL:
				for (int i = 0; i < size; i++) {
					grid[pos.getX() + i][pos.getY()] = id;
				}
				break;
			case VERTICAL:
				for (int i = 0; i < size; i++) {
					grid[pos.getX()][pos.getY() + i] = id;
				}
				break;
			default:
				break;
		}
	}
	
	public boolean verifyPlacement(int id, int size, Position pos, ShipOrientation orient) {
		switch(orient) {
			case HORIZONTAL:
				for (int i = 0; i < size; i++) {
					try {
						if (grid[pos.getX() + i][pos.getY()] != -1) {
							return false;
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
						return false;
					}
				}
				break;
			case VERTICAL:
				for (int i = 0; i < size; i++) {
					try {
						if (grid[pos.getX()][pos.getY() + i] != -1) {
							return false;
						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
						return false;
					}
				}
				break;
			default:
				break;
		}	
		
		return true;
	}

}
