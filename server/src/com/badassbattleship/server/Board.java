package com.badassbattleship.server;

public class Board {
	private Gridspace grid[][];
	private int aliveShips;
	
	public Board() {
		grid = new Gridspace[GameSettings.SIZE][GameSettings.SIZE];
		aliveShips = 0;
	}
	
	public void createShip(Ship ship) {
		if (aliveShips < GameSettings.NUM_SHIPS) {
			aliveShips++;

			int size = ship.getSize();
			Position pos = ship.getStartPosition();
			ShipOrientation orientation = ship.getOrientation();
			
			//set ship to all gridspaces with battleship

			//todo: instead of directly adding a ship object to a gridspace, just add the ID of the ship!!

			if (size == 2 && orientation == ShipOrientation.HORIZONTAL) {
				grid[pos.getX()][pos.getY()].setShip(ship);
				grid[pos.getX()][pos.getY()].setShip(ship);
			}
			if (size == 2 && orientation == ShipOrientation.VERTICAL) {
				grid[pos.getX()][pos.getY()].setShip(ship);
				grid[pos.getX() - 1][pos.getY()].setShip(ship);
			}
			
		}
	}
}
