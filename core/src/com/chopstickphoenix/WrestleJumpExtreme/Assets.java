
package com.chopstickphoenix.WrestleJumpExtreme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;

	
	public static Texture items;
	public static TextureRegion mainMenu;
	public static TextureRegion pauseMenu;
	public static TextureRegion ready;
	public static TextureRegion gameOver;
	public static TextureRegion highScoresRegion;
	public static TextureRegion logo;
	public static TextureRegion soundOn;
	public static TextureRegion soundOff;
	public static TextureRegion arrow;
	public static TextureRegion pause;
	public static TextureRegion spring;
	public static TextureRegion castle;
	public static TextureRegion generalShareRegion;
	public static TextureRegion swarmLeaderboardRegion;
	public static TextureRegion nextText;
	
	public static Animation burgerAnim;
	public static Animation wrestlerJump;
	public static Animation wrestlerFall;
	public static TextureRegion wrestlerHit;
	public static Animation flyingChair;
	public static TextureRegion platform;
	public static Animation breakingPlatform;
	public static BitmapFont font;

	
	//public static Music music;
	public static Sound jumpSound;
	public static Sound highJumpSound;
	public static Sound hitSound;
	public static Sound coinSound;
	public static Sound clickSound;
	public static Sound dundundunnn;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		background = loadTexture("data/background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);

		items = loadTexture("data/items.png");
		mainMenu = new TextureRegion(items, 0, 224, 300, 110);
		pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
		ready = new TextureRegion(items, 320, 224, 192, 32);
		gameOver = new TextureRegion(items, 352, 256, 160, 96);
		highScoresRegion = new TextureRegion(Assets.items, 214, 104, 266, 110 / 3); //267 was 300
		logo = new TextureRegion(items, 0, 352, 274, 142);
		soundOff = new TextureRegion(items, 0, 0, 64, 64);
		soundOn = new TextureRegion(items, 64, 0, 64, 64);
		arrow = new TextureRegion(items, 0, 64, 64, 64);
		pause = new TextureRegion(items, 64, 64, 64, 64);
		generalShareRegion = new TextureRegion(items, 414, 0, 150, 93);
		swarmLeaderboardRegion = new TextureRegion (items, 284, 439, 298, 100);
		nextText = new TextureRegion(items, 0, 524, 92, 32);
		
		spring = new TextureRegion(items, 128, 0, 32, 32);
		castle = new TextureRegion(items, 128, 64, 64, 64);
		burgerAnim = new Animation(0.2f, new TextureRegion(items, 128, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32),
			new TextureRegion(items, 192, 32, 32, 32), new TextureRegion(items, 224, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32));
		wrestlerJump = new Animation(0.2f, new TextureRegion(items, 0, 128, 32, 32), new TextureRegion(items, 32, 128, 32, 32));
		wrestlerFall = new Animation(0.2f, new TextureRegion(items, 64, 128, 32, 32), new TextureRegion(items, 96, 128, 32, 32));
		wrestlerHit = new TextureRegion(items, 128, 128, 32, 32);
		flyingChair = new Animation(0.2f, new TextureRegion(items, 0, 160, 32, 32), new TextureRegion(items, 32, 160, 32, 32));
		platform = new TextureRegion(items, 64, 160, 64, 16);
		breakingPlatform = new Animation(0.2f, new TextureRegion(items, 64, 160, 64, 16), new TextureRegion(items, 64, 176, 64, 16),
			new TextureRegion(items, 64, 192, 64, 16), new TextureRegion(items, 64, 208, 64, 16));

		font = new BitmapFont(Gdx.files.internal("data/font1.fnt"), Gdx.files.internal("data/font1_0.png"), false);
		font.setScale(0.75f, 0.75f);
		
		//music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
		//music.setLooping(true);
		//music.setVolume(0.5f);
		//if (Settings.soundEnabled) music.play();
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
		highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));
		

	}

	public static void playSound (Sound sound) {
		if (Settings.soundEnabled) sound.play(1);
	}
}
