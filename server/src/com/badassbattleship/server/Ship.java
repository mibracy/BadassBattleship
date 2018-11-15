package com.badassbattleship.server;


import com.google.gson.annotations.SerializedName;

public class Ship {
	private int size;
	private ShipOrientation orientation; // 0 for horizontal 1 for vertical
	private Position position; // Used for serialization - changing this does not change position on board!
	
	public Ship(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public Position getStartPosition() {
		return position;
	}

	public ShipOrientation getOrientation() {
		return orientation;
	}

	public void setOrientation(ShipOrientation orientation) {
		this.orientation = orientation;
	}
}

enum ShipOrientation {
	@SerializedName("0")
	HORIZONTAL,
	@SerializedName("1")
	VERTICAL
}
class Position {
	private int x;
	private int y;
	public int getX() { return x; }
	public int getY() {
		return y;
	}
}