
package com.chopstickphoenix.WrestleJumpExtreme;

public class Hamburger extends GameObject {
	public static final float HAMBURGER_WIDTH = 0.5f;
	public static final float HAMBURGER_HEIGHT = 0.8f;
	public static final int HAMBURGER_SCORE = 10;


	float stateTime;

	public Hamburger (float x, float y) {
		super(x, y, HAMBURGER_WIDTH, HAMBURGER_HEIGHT);
		stateTime = 0;
	}

	public void update (float deltaTime) {
		stateTime += deltaTime;
	}
}
