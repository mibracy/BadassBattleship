package com.badassbattleship.server;


public class Ship {
	private boolean destroyed;
	private int size;
	private int orientation; // 0 for horizontal 1 for vertical
	private int hits;
	
	public Ship(int size) {
		this.size = size;
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
		if (hits >= size) {
			setDestroyed();
		}
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

}
