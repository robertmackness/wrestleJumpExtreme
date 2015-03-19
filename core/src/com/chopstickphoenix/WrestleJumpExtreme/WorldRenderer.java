
package com.chopstickphoenix.WrestleJumpExtreme;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	World world;
	OrthographicCamera cam;
	SpriteBatch batch;

	public WorldRenderer (SpriteBatch batch, World world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		this.batch = batch;
	}

	public void render () {
		if (world.wrestlerCharacter.position.y > cam.position.y) cam.position.y = world.wrestlerCharacter.position.y;
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		renderBackground();
		renderObjects();
	}

	public void renderBackground () {
		batch.disableBlending();
		batch.begin();
		batch.draw(Assets.backgroundRegion, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH,
			FRUSTUM_HEIGHT);
		batch.end();
	}

	public void renderObjects () {
		batch.enableBlending();
		batch.begin();
		renderWrestler();
		renderPlatforms();
		renderItems();
		renderFlyingChairs();
		renderCastle();
		batch.end();
	}

	private void renderWrestler () {
		TextureRegion keyFrame;
		switch (world.wrestlerCharacter.state) {
		case WrestlerCharacter.WRESTLER_STATE_FALL:
			keyFrame = Assets.wrestlerFall.getKeyFrame(world.wrestlerCharacter.stateTime, Animation.ANIMATION_LOOPING);
			break;
		case WrestlerCharacter.WRESTLER_STATE_JUMP:
			keyFrame = Assets.wrestlerJump.getKeyFrame(world.wrestlerCharacter.stateTime, Animation.ANIMATION_LOOPING);
			break;
		case WrestlerCharacter.WRESTLER_STATE_HIT:
		default:
			keyFrame = Assets.wrestlerHit;
		}

		float side = world.wrestlerCharacter.velocity.x < 0 ? -1 : 1;
		if (side < 0)
			batch.draw(keyFrame, world.wrestlerCharacter.position.x + 0.5f, world.wrestlerCharacter.position.y - 0.5f, side * 1, 1);
		else
			batch.draw(keyFrame, world.wrestlerCharacter.position.x - 0.5f, world.wrestlerCharacter.position.y - 0.5f, side * 1, 1);
	}

	private void renderPlatforms () {
		int len = world.platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = world.platforms.get(i);
			TextureRegion keyFrame = Assets.platform;
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
				keyFrame = Assets.breakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
			}

			batch.draw(keyFrame, platform.position.x - 1, platform.position.y - 0.25f, 2, 0.5f);
		}
	}

	private void renderItems () {
		int len = world.springJumps.size();
		for (int i = 0; i < len; i++) {
			SpringJump springJump = world.springJumps.get(i);
			batch.draw(Assets.spring, springJump.position.x - 0.5f, springJump.position.y - 0.5f, 1, 1);
		}

		len = world.hamburgers.size();
		for (int i = 0; i < len; i++) {
			Hamburger hamburger = world.hamburgers.get(i);
			TextureRegion keyFrame = Assets.burgerAnim.getKeyFrame(hamburger.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, hamburger.position.x - 0.5f, hamburger.position.y - 0.5f, 1, 1);
		}
	}

	private void renderFlyingChairs () {
		int len = world.flyingChairs.size();
		for (int i = 0; i < len; i++) {
			FlyingChair flyingChair = world.flyingChairs.get(i);
			TextureRegion keyFrame = Assets.flyingChair.getKeyFrame(flyingChair.stateTime, Animation.ANIMATION_LOOPING);
			float side = flyingChair.velocity.x < 0 ? -1 : 1;
			if (side < 0)
				batch.draw(keyFrame, flyingChair.position.x + 0.5f, flyingChair.position.y - 0.5f, side * 1, 1);
			else
				batch.draw(keyFrame, flyingChair.position.x - 0.5f, flyingChair.position.y - 0.5f, side * 1, 1);
		}
	}

	private void renderCastle () {
		TheArena TheArena = world.theArena;
		batch.draw(Assets.castle, TheArena.position.x - 1, TheArena.position.y - 1, 2, 2);
	}
}
