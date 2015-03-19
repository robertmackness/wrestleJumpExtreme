

package com.chopstickphoenix.WrestleJumpExtreme;

public class WrestlerCharacter extends DynamicGameObject {
	public static final int WRESTLER_STATE_JUMP = 0;
	public static final int WRESTLER_STATE_FALL = 1;
	public static final int WRESTLER_STATE_HIT = 2;
	public static final float WRESTLER_JUMP_VELOCITY = 20; //was 11
	public static final float WRESTLER_MOVE_VELOCITY = 50; //was 20
	public static final float WRESTLER_WIDTH = 0.8f;
	public static final float WRESTLER_HEIGHT = 0.8f;

	int state;
	float stateTime;

	public WrestlerCharacter (float x, float y) {
		super(x, y, WRESTLER_WIDTH, WRESTLER_HEIGHT);
		state = WRESTLER_STATE_JUMP;
		stateTime = 0;
		
	}

	public void update (float deltaTime) {
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;

		if (velocity.y > 0 && state != WRESTLER_STATE_HIT) {
			if (state != WRESTLER_STATE_JUMP) {
				state = WRESTLER_STATE_JUMP;
				stateTime = 0;
			}
		}

		if (velocity.y < 0 && state != WRESTLER_STATE_HIT) {
			if (state != WRESTLER_STATE_FALL) {
				state = WRESTLER_STATE_FALL;
				stateTime = 0;
			}
		}

		if (position.x < 0) position.x = World.WORLD_WIDTH;
		if (position.x > World.WORLD_WIDTH) position.x = 0;

		stateTime += deltaTime;
	}

	public void hitSquirrel () {
		velocity.set(0, 0);
		state = WRESTLER_STATE_HIT;
		stateTime = 0;
	}

	public void hitPlatform () {
		velocity.y = WRESTLER_JUMP_VELOCITY;
		state = WRESTLER_STATE_JUMP;
		stateTime = 0;
	}

	public void hitSpring () {
		velocity.y = WRESTLER_JUMP_VELOCITY * 1.5f;
		state = WRESTLER_STATE_JUMP;
		stateTime = 0;
	}
}
