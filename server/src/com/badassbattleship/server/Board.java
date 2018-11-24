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
	
	public void printGridToConsole() {
		for (int i = 0; i < GameSettings.SIZE; i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < GameSettings.SIZE; j++) {
				row.append(String.format("[%2d]", grid[i][j]));
			}
			logger.info(row + "\n");
		}
	}
	public boolean calloutAShot(int x, int y) {
		boolean response = false;
		//This method will return false is a shot is a miss and true if it is successful
		if(grid[y][x] == -1) {
			grid[y][x] = -3;
			//change the display here to a miss color
			response = false;
		}
		else if(grid[y][x] == -3 || grid[y][x] == -2) {
		//This is the case a shot is called out twice. I'm not sure how we get the input from
		//the player but we should ask the player for a new x and y and then re-call this function
		}
		else {
		//This should get the ship in the hashmap and update its hit count. Then it changes
		//the grid to reflect the hit and returns true.
			(ships.get(grid[y][x])).successfulHit();
			if((ships.get(grid[y][x])).checkIfShipDestroyed() == true) {
				aliveShips--;
			}
			if(aliveShips == 0) {
				//game over condition
			}
			grid[y][x] = -2;
			//change the display here to a hit color
			response = true;
		}
		return response;
	}
	}

