
package com.chopstickphoenix.WrestleJumpExtreme;

import java.nio.ByteBuffer;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class HighscoresScreen extends ScreenAdapter {
	WrestleJumpExtreme game;
	OrthographicCamera guiCam;
	Rectangle backBounds;
	Rectangle shareBounds;
	Vector3 touchPoint;
	String highScores;
	float xOffset = 0;
	public  Random rand;
	FileHandle fh;
	

	public HighscoresScreen (WrestleJumpExtreme game) {
		this.game = game;

		Settings.load();

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		backBounds = new Rectangle(0, 0, 64, 64);
		shareBounds = new Rectangle((guiCam.viewportWidth/2 - 150/2), 60, 150, 60);
		touchPoint = new Vector3();
		highScores = "" + Settings.getHighScore();
		/* for (int i = 0; i < 5; i++) {
			highScores[i] = i + 1 + ". " + Settings.highscores[i]; */
		xOffset = Math.max(Assets.font.getBounds(highScores).width, xOffset);
		//}
		//xOffset = 140 - xOffset / 2 + Assets.font.getSpaceWidth() / 2;
	}

	public void update () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (backBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}

			if (shareBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				WrestleJumpExtreme.interfaceShareGeneral.shareToast();		
		         try{
		              
		                fh = new FileHandle(Gdx.files.getExternalStoragePath() + "JumperScore" + ".png");
		                System.out.println(fh);
		                fh.delete();
		                Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		                PixmapIO.writePNG(fh, pixmap);
		                pixmap.dispose();
		            }catch (Exception e){
		                System.out.println(e);
		            }
		        
				WrestleJumpExtreme.interfaceShareGeneral.share(fh.toString());
				
				return;
			}
		}
	}


	private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
	    final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);
	    ByteBuffer pixels = pixmap.getPixels();
	    final int numBytes = w * h * 4;
	    byte[] lines = new byte[numBytes];
	    if (yDown) {
	        // Flip the pixmap upside down
	        int numBytesPerLine = w * 4;
	        for (int i = 0; i < h; i++) {
	            pixels.position((h - i - 1) * numBytesPerLine);
	            pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
	        }
	        pixels.clear();
	        pixels.put(lines);
	    }
	    return pixmap;
	}


	public void draw () {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();

		game.batcher.setProjectionMatrix(guiCam.combined);
		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		game.batcher.end();

		game.batcher.enableBlending();
		game.batcher.begin();
		//draw textures from Assets class
		game.batcher.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
		game.batcher.draw(Assets.highScoresRegion, 10, 250, 300, 110 / 2); // was 10, 360 - 16, 267, 33
		game.batcher.draw(Assets.generalShareRegion, (guiCam.viewportWidth/2 - 150/2), 60, 150, 60);
		//general share is 98 by 98 pixels on assets class texture items


		//draw the font
		Assets.font.setScale(3);
		Assets.font.draw(game.batcher, highScores, (guiCam.viewportWidth/2F-Assets.font.getBounds(highScores).width/2F), 230);
		Assets.font.setScale(0.75f, 0.75f);


		game.batcher.draw(Assets.arrow, 0, 0, 64, 64);
		game.batcher.end();
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}



}

