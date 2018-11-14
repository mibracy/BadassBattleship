package com.badassbattleship.server;


public class Ship {
	private boolean destroyed;
	private ShipType type;
	private ShipOrientation orientation; // 0 for horizontal 1 for vertical
	private int hits;
	
	public Ship(ShipType type) {
		this.type = type;
		this.destroyed = false;
		this.hits = 0;
	}
	
	public void hit() {
		this.hits++;
	}

	public boolean isDestroyed() {
		return destroyed;
	}
	
	private void setDestroyed() {
		this.destroyed = true;
	}

	public void destroyedCheck() {
		if (hits >= type.getSize()) {
			setDestroyed();
		}
	}

	public ShipOrientation getOrientation() {
		return orientation;
	}

	public void setOrientation(ShipOrientation orientation) {
		this.orientation = orientation;
	}

}
enum ShipOrientation {
	HORIZONTAL,
	VERTICAL
}
enum ShipType {
	UNDEFINED(0),
	CARRIER(5),
	BATTLESHIP(4),
	CRUISER(3),
	SUBMARINE(3),
	DESTROYER(2);

	private int size;

	ShipType(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}