package com.chopstickphoenix.WrestleJumpExtreme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen extends ScreenAdapter {
	//private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 1.0f);
	//private static final float WORLD_TO_SCREEN = 1.0f / 100.0f;
	//private static final float SCENE_WIDTH = 12.80f;
	//private static final float SCENE_HEIGHT = 7.20f;

	private OrthographicCamera guiCam;
	//private Viewport viewport;
	private SpriteBatch batch;
	private Texture splashTexture;
	private WrestleJumpExtreme game;
	private Sprite sprite;

	public SplashScreen(WrestleJumpExtreme game) {
		this.game = game;
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);


		//moved from draw method
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		game.batcher.setProjectionMatrix(guiCam.combined);


		splashTexture = new Texture(Gdx.files.internal("data/splash.png"));
		sprite= new Sprite (splashTexture);
		Assets.playSound(Assets.clickSound);
	}


	public void draw () {


		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(sprite, 0, 0, 320, 480);
		game.batcher.end();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();


	}



	public void update(float delta) {
		if (delta > 0.1f) delta = 0.1f;

		if(Gdx.input.justTouched()) {
			Assets.playSound(Assets.clickSound);
			Settings.load();

			if (Settings.getHighScore() == 0){

				Settings.addScore(1);
				Settings.save();
				game.setScreen(new HelpScreen(game));

			} else {
				game.setScreen(new MainMenuScreen(game)); }

		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		splashTexture.dispose();
	}


}
