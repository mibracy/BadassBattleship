package com.badassbattleship.server;

public class Board {
	private static final int SIZE = 10;
	private static final int NUMSHIPS = 5;
	
	private Gridspace grid[][];
	private int aliveShips;
	
	public Board() {
		grid = new Gridspace[SIZE][SIZE];
		aliveShips = 0;
	}
	
	public void createShip(ShipType type, ShipOrientation orientation, int row, int col) {
		if (aliveShips < NUMSHIPS) {
			aliveShips++;
			Ship battleship = new Ship(type);
			
			//set ship to all gridspaces with battleship
			if (type.getSize() == 2 && orientation == ShipOrientation.HORIZONTAL) {
				grid[row][col].setShip(battleship);
				grid[row][col + 1].setShip(battleship);
			}
			if (type.getSize() == 2 && orientation == ShipOrientation.VERTICAL) {
				grid[row][col].setShip(battleship);
				grid[row - 1][col].setShip(battleship);
			}
			
		}
	}

	private boolean boundsCheck(int x, int y, ShipOrientation orientation, ShipType type) {
		return true; //not implemented
	}

}
