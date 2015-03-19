
package com.chopstickphoenix.WrestleJumpExtreme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class World {
	public interface WorldListener {
		public void jump ();

		public void highJump ();

		public void hit ();

		public void coin ();
	}

	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 40; // was 20
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -40); //was 0, 12

	public final WrestlerCharacter wrestlerCharacter;
	public final List<Platform> platforms;
	public final List<SpringJump> springJumps;
	public final List<FlyingChair> flyingChairs;
	public final List<Hamburger> hamburgers;
	public TheArena theArena;
	public final WorldListener listener;
	public final Random rand;

	public float heightSoFar;
	public int score;
	public int state;

	public World (WorldListener listener) {
		this.wrestlerCharacter = new WrestlerCharacter(5, 5);
		this.platforms = new ArrayList<Platform>();
		this.springJumps = new ArrayList<SpringJump>();
		this.flyingChairs = new ArrayList<FlyingChair>();
		this.hamburgers = new ArrayList<Hamburger>();
		this.listener = listener;
		rand = new Random();
		generateLevel();

		this.heightSoFar = 0; 
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;
	}

	private void generateLevel () {
		float y = Platform.PLATFORM_HEIGHT / 2;
		float maxJumpHeight = WrestlerCharacter.WRESTLER_JUMP_VELOCITY * WrestlerCharacter.WRESTLER_JUMP_VELOCITY / (2 * -gravity.y);
		while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
			int type = rand.nextFloat() > 0.8f ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
			float x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH / 2;

			Platform platform = new Platform(type, x, y);
			platforms.add(platform);

			if (rand.nextFloat() > 0.9f && type != Platform.PLATFORM_TYPE_MOVING) {
				SpringJump springJump = new SpringJump(platform.position.x, platform.position.y + Platform.PLATFORM_HEIGHT / 2
					+ SpringJump.SPRING_HEIGHT / 2);
				springJumps.add(springJump);
			}

			if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.85f) {
				FlyingChair flyingChair = new FlyingChair(platform.position.x + rand.nextFloat(), platform.position.y
					+ FlyingChair.FLYING_CHAIR_HEIGHT + rand.nextFloat() * 2);
				flyingChairs.add(flyingChair);
			}

			if (rand.nextFloat() > 0.6f) {
				Hamburger hamburger = new Hamburger(platform.position.x + rand.nextFloat(), platform.position.y 
																+ Hamburger.HAMBURGER_HEIGHT + rand.nextFloat() * 3);
				hamburgers.add(hamburger);
			}

			y += (maxJumpHeight - 0.5f);
			y -= rand.nextFloat() * (maxJumpHeight / 3);
		}

		theArena = new TheArena(WORLD_WIDTH / 2, y);
	}

	public void update (float deltaTime, float accelX) {
		updateWrestlerCharacter(deltaTime, accelX);
		updatePlatforms(deltaTime);
		updateMovingEnemy(deltaTime);
		updateHamburgers(deltaTime);
		if (wrestlerCharacter.state != WrestlerCharacter.WRESTLER_STATE_HIT) checkCollisions();
		checkGameOver();
	}

	private void updateWrestlerCharacter (float deltaTime, float accelX) {
		if (wrestlerCharacter.state != WrestlerCharacter.WRESTLER_STATE_HIT && wrestlerCharacter.position.y <= 0.5f) wrestlerCharacter.hitPlatform();
		if (wrestlerCharacter.state != WrestlerCharacter.WRESTLER_STATE_HIT) wrestlerCharacter.velocity.x = -accelX / 10 * WrestlerCharacter.WRESTLER_MOVE_VELOCITY;
		wrestlerCharacter.update(deltaTime);
		heightSoFar = Math.max(wrestlerCharacter.position.y, heightSoFar);
	}

	private void updatePlatforms (float deltaTime) {
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			platform.update(deltaTime);
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
				platforms.remove(platform);
				len = platforms.size();
			}
		}
	}

	private void updateMovingEnemy (float deltaTime) {
		int len = flyingChairs.size();
		for (int i = 0; i < len; i++) {
			FlyingChair flyingChair = flyingChairs.get(i);
			flyingChair.update(deltaTime);
		}
	}

	private void updateHamburgers (float deltaTime) {
		int len = hamburgers.size();
		for (int i = 0; i < len; i++) {
			Hamburger hamburger = hamburgers.get(i);
			hamburger.update(deltaTime);
		}
	}

	private void checkCollisions () {
		checkPlatformCollisions();
		checkSquirrelCollisions();
		checkItemCollisions();
		checkCastleCollisions();
	}

	private void checkPlatformCollisions () {
		if (wrestlerCharacter.velocity.y > 0) return;

		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (wrestlerCharacter.position.y > platform.position.y) {
				if (wrestlerCharacter.bounds.overlaps(platform.bounds)) {
					wrestlerCharacter.hitPlatform();
					listener.jump();
					//if (rand.nextFloat() > 0.5f) {
					//	platform.pulverize();
					//}  THIS CODE IS TO TURN ON PULVERIZING PLATFORMS
					break;
				}
			}
		}
	}

	private void checkSquirrelCollisions () {
		int len = flyingChairs.size();
		for (int i = 0; i < len; i++) {
			FlyingChair flyingChair = flyingChairs.get(i);
			if (flyingChair.bounds.overlaps(wrestlerCharacter.bounds)) {
				wrestlerCharacter.hitSquirrel();
				listener.hit();
			}
		}
	}

	private void checkItemCollisions () {
		int len = hamburgers.size();
		for (int i = 0; i < len; i++) {
			Hamburger hamburger = hamburgers.get(i);
			if (wrestlerCharacter.bounds.overlaps(hamburger.bounds)) {
				hamburgers.remove(hamburger);
				len = hamburgers.size();
				listener.coin();
				score += Hamburger.HAMBURGER_SCORE + wrestlerCharacter.position.y*0.05;
			}

		}

		if (wrestlerCharacter.velocity.y > 0) return;

		len = springJumps.size();
		for (int i = 0; i < len; i++) {
			SpringJump springJump = springJumps.get(i);
			if (wrestlerCharacter.position.y > springJump.position.y) {
				if (wrestlerCharacter.bounds.overlaps(springJump.bounds)) {
					wrestlerCharacter.hitSpring();
					listener.highJump();
				}
			}
		}
	}

	private void checkCastleCollisions () {
		if (theArena.bounds.overlaps(wrestlerCharacter.bounds)) {
			state = WORLD_STATE_NEXT_LEVEL;
		}
	}

	private void checkGameOver () {
		 if (heightSoFar - 7.5 > wrestlerCharacter.position.y || wrestlerCharacter.position.y == 1) { //or statement extra
			state = WORLD_STATE_GAME_OVER;
		}
	}
}
