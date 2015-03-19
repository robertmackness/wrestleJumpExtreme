
package com.chopstickphoenix.WrestleJumpExtreme;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MainMenuScreen extends ScreenAdapter {
	WrestleJumpExtreme game;
	OrthographicCamera guiCam;
	Rectangle soundBounds;
	Rectangle playBounds;
	Rectangle highscoresBounds;
	Rectangle helpBounds;
	Rectangle swarmLeaderboardBounds;
	Vector3 touchPoint;
	public final Random rand;
	InterfaceAdvertising InterfaceAdvertising;
	boolean adRoll;


	public MainMenuScreen (WrestleJumpExtreme game) {

		this.game = game;
		rand = new Random();
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(10, 240 + 18, 300, 36);
		highscoresBounds = new Rectangle(10, 240 - 18, 300, 36);
		helpBounds = new Rectangle(10, 240 - 18 - 36, 300, 36);
		swarmLeaderboardBounds = new Rectangle(guiCam.viewportWidth/2 - 260/2, 132, 260, 32);

		//touch inputlistener
		touchPoint = new Vector3();

		//google dice roller for ads
		adRoll = false;

		//pre-load an ad
		WrestleJumpExtreme.InterfaceAdvertising.loadAdInterstitial();

	}

	public void update () {


		//handles touch input for menu bounds
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (playBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				//was previously GameScreen before testing
				game.setScreen(new GameScreen(game));
				return;
			}
			if (highscoresBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new HighscoresScreen(game));
				return;
			}
			if (helpBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new HelpScreen(game));
				return;
			}
			if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Settings.soundEnabled = !Settings.soundEnabled;
				//if (Settings.soundEnabled)
				//	Assets.music.play();
				//else
				//	Assets.music.pause();
			}
			if (swarmLeaderboardBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);

				//TODO submit score and launch the swarm leaderboard
				WrestleJumpExtreme.interfaceSwarm.showSwarmLeaderboard();

				return;
			}
		}
	}

	public void draw () {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		game.batcher.setProjectionMatrix(guiCam.combined);

		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		game.batcher.end();

		game.batcher.enableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
		game.batcher.draw(Assets.mainMenu, 10, 200 - 15, 300, 110); //y was 200 - 110
		game.batcher.draw(Assets.swarmLeaderboardRegion, guiCam.viewportWidth/2 - 260/2, 100, 260, 72);
		game.batcher.draw(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 0, 0, 64, 64);
		game.batcher.end();	

		//checks if ad has been shown yet on this instance of MainMenu. If it's true then it doesn't execute the code
		// this stop endless ads being shown. There's also two rolls with different timers to trick the user the the
		// referenced method

		if (MainMenuScreen.this.adRoll == false) {
			beginAdRoll();
		}
	}

	//rolls one random between 0 to 100, if it's >85 then ad is shown
	private void beginAdRoll() {
		Random rand2;
		rand2 = new Random();
		int r2;
		r2 = rand2.nextInt(100);

		if (r2 >= 85){ //TODO MACKNESS change R2 >= 1 back to 85
			WrestleJumpExtreme.InterfaceAdvertising.showAdInterstitial();
			MainMenuScreen.this.adRoll = true;
		}
		MainMenuScreen.this.adRoll = true;
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void pause () {
		Settings.save();
	}
}
