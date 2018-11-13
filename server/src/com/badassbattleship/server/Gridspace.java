package com.badassbattleship.server;

public class Gridspace {
	private Ship battleship;
	private boolean hasShip;

	public Gridspace() {
		this.hasShip = false;
	}

	public boolean isShip() {
		return hasShip;
	}
	
	public void setShip(Ship battleship) {
		this.battleship = battleship;
	}

}
