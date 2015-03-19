
package com.chopstickphoenix.WrestleJumpExtreme;

public class FlyingChair extends DynamicGameObject {
	public static final float FLYING_CHAIR_WIDTH = 1;
	public static final float FLYING_CHAIR_HEIGHT = 0.6f;
	public static final float FLYING_CHAIR_VELOCITY = 3f;

	float stateTime = 0;

	public FlyingChair (float x, float y) {
		super(x, y, FLYING_CHAIR_WIDTH, FLYING_CHAIR_HEIGHT);
		velocity.set(FLYING_CHAIR_VELOCITY, 0);
	}

	public void update (float deltaTime) {
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x - FLYING_CHAIR_WIDTH / 2;
		bounds.y = position.y - FLYING_CHAIR_HEIGHT / 2;

		if (position.x < FLYING_CHAIR_WIDTH / 2) {
			position.x = FLYING_CHAIR_WIDTH / 2;
			velocity.x = FLYING_CHAIR_VELOCITY;
		}
		if (position.x > World.WORLD_WIDTH - FLYING_CHAIR_WIDTH / 2) {
			position.x = World.WORLD_WIDTH - FLYING_CHAIR_WIDTH / 2;
			velocity.x = -FLYING_CHAIR_VELOCITY;
		}
		stateTime += deltaTime;
	}
}
