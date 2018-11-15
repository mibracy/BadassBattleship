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
			ShipOrientation orientation = ship.getOrientation();
			
			//set ship to all gridspaces with battleship
			if (size == 2 && orientation == ShipOrientation.HORIZONTAL) {
				grid[row][col].setShip(battleship);
				grid[row][col + 1].setShip(battleship);
			}
			if (size == 2 && orientation == ShipOrientation.VERTICAL) {
				grid[row][col].setShip(battleship);
				grid[row - 1][col].setShip(battleship);
			}
			
		}
	}
}
