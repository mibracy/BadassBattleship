package com.badassbattleship.server;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ship {
    @Expose
	private int size;

    @Expose
	private ShipOrientation orientation; // 0 for horizontal 1 for vertical

    @Expose
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
    @Expose
	@SerializedName("0")
	HORIZONTAL,
    @Expose
	@SerializedName("1")
	VERTICAL
}
class Position {
    @Expose
	private int x;
    @Expose
	private int y;
	public int getX() { 
		return x; 
	}
	public int getY() {
		return y;
	}
}