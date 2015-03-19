
package com.chopstickphoenix.WrestleJumpExtreme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
	public static boolean soundEnabled = true;
	//public final static int[] highscores = new int[] {100, 80, 50, 30, 10};
	public final static String file = ".superjumper";
	public static Preferences prefs;

	
	public static void load () {
		prefs = Gdx.app.getPreferences("wrestleJumpHighScores");
		/* //try {
			//FileHandle filehandle = Gdx.files.external(file);
			
		//	String[] strings = filehandle.readString().split("\n");
			
					
			//soundEnabled = Boolean.parseBoolean(strings[0]);
			//for (int i = 0; i < 5; i++) {
			//	highscores[i] = Integer.parseInt(strings[i+1]);  //this was 
			//}
		//} catch (Throwable e) {
			// 
		//} */
			//Preferences prefs = Gdx.app.getPreferences("wrestleJumpHighScores");
			
			//if (!prefs.contains("wrestleJumpHighScores")) {
	       //     prefs.putInteger("wrestleJumpHighScores", 10);
			//}
	}

	public static void save () {
		/* try {
			FileHandle filehandle = Gdx.files.external(file);
			
			filehandle.writeString(Boolean.toString(soundEnabled)+"\n", false);
			for (int i = 0; i < 5; i++) {
				filehandle.writeString(Integer.toString(highscores[i])+"\n", true);
			}
		} catch (Throwable e) {
		} */
		
		
        prefs.flush();
        
	}
	
	public static int getHighScore() {
		
		return prefs.getInteger("wrestleJumpHighScores");
	}
	
	public static void addScore (int currentHighScore) {
		/* for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		} */
		
		prefs.putInteger("wrestleJumpHighScores", currentHighScore);
	}
}
