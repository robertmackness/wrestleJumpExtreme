package com.chopstickphoenix.WrestleJumpExtreme.android;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.chopstickphoenix.WrestleJumpExtreme.InterfaceAdvertising;
import com.chopstickphoenix.WrestleJumpExtreme.InterfaceShareGeneral;
import com.chopstickphoenix.WrestleJumpExtreme.InterfaceSwarm;
import com.chopstickphoenix.WrestleJumpExtreme.Settings;
import com.chopstickphoenix.WrestleJumpExtreme.WrestleJumpExtreme;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmLeaderboard;
public class AndroidLauncher extends AndroidApplication implements InterfaceAdvertising, InterfaceShareGeneral, InterfaceSwarm  {

	protected View gameView;

	//Declare Ad stuff
	private InterstitialAd interstitial;
	private  AdRequest adRequest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		//deprecated cfg.useGL20 = false;
		cfg.useAccelerometer = true;
		cfg.useCompass = false;

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);


		//Initialise Admob
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId("ca-app-pub-3729576601854244/8677730417");
	    adRequest = new AdRequest.Builder().addTestDevice("D25AD0A36877106B87DC9865E9C5CD05").build();

		

		View gameView = createGameView(cfg);
		layout.addView(gameView);

		setContentView(layout);

		//swarm
		Swarm.setActive(this);



	}



	// Launch the actual JumprX Game
	private View createGameView(AndroidApplicationConfiguration cfg) {
		// Launching the game with the right interfaces loaded into it
		gameView = initializeForView(new WrestleJumpExtreme(this, this, this), cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		//params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		//params.addRule(RelativeLayout.BELOW, adView.getId());
		gameView.setLayoutParams(params);
		return gameView;
	}


	@Override
	public void onResume() {
		super.onResume();

		//Swarm leaderboard
		Swarm.setActive(this);

	}

	@Override
	public void onPause() {

		super.onPause();

		//Swarm
		Swarm.setInactive(this);
		

	}

	@Override
	public void onBackPressed() {
		//show an ad
		showAdInterstitial();
		
		super.onBackPressed();
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();

	}

	@Override
	public void shareToast(){
		
		this.runOnUiThread(new Runnable() {
			  public void run() {
				  Toast.makeText(AndroidLauncher.this.getApplicationContext(), "Creating snapshot", Toast.LENGTH_SHORT).show();
			  }
			});
	}
	
	@Override
	public void share(String fileHandle) {

		
		try {
		
		    File myFile = new File(fileHandle);
		    MimeTypeMap mime = MimeTypeMap.getSingleton();
		    String ext=myFile.getName().substring(myFile.getName().lastIndexOf(".")+1);
		    String type = mime.getMimeTypeFromExtension(ext);
		    Intent sharingIntent = new Intent("android.intent.action.SEND");
		    sharingIntent.setType(type);
		    sharingIntent.putExtra("android.intent.extra.STREAM",Uri.fromFile(myFile));   
		    startActivity(Intent.createChooser(sharingIntent,"Brag using"));
		}
		catch(Exception e){
		    System.out.println("" + e);
		} 
		
		
	/*	Settings.load(); //load highscores from the android preferences file

		// get available share intents
		List<Intent> targets = new ArrayList<Intent>();
		Intent template = new Intent(Intent.ACTION_SEND);
		template.setType("text/plain");
		List<ResolveInfo> candidates = this.getPackageManager().
				queryIntentActivities(template, 0);

		// remove facebook which has a broken share intent
		for (ResolveInfo candidate : candidates) {
			String packageName = candidate.activityInfo.packageName;
			if (!packageName.equals("com.facebook.katana")) {
				Intent target = new Intent(android.content.Intent.ACTION_SEND);
				target.setType("text/plain");
				target.putExtra(Intent.EXTRA_TEXT, "Who's up for a challenge?  >:)  \nHighscore: " + Settings.getHighScore() +
						"\nWrestle Jump \nhttp://goo.gl/Udacac");
				target.setPackage(packageName);
				targets.add(target);
			}
		}
		Intent chooser = Intent.createChooser(targets.remove(0),("Where do you want to brag? :)"));
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[]{}));
		startActivity(chooser);
	*/	
	
	}


	// Share Intent stuff above 

	@Override
	public void showSwarmLeaderboard(){
		if (Swarm.isLoggedIn()) {
			submitSwarmScore();
			//This pauses a second before showing the leaderboards, allowing the connection to re-start
			Timer.schedule(new Task(){
				@Override
				public void run() {
					Swarm.showLeaderboards();
				}
			}, 2f);


		} else { 
			initialiseSwarm();
		}
	}

	@Override
	public void submitSwarmScore() {

		if (Swarm.isLoggedIn()) {
			//load the score from preferences
			int highScore;
			Settings.load();
			highScore = Settings.getHighScore();
			//submit to Swarm
			SwarmLeaderboard.submitScore(18711, (int) highScore);
		}

	}

	public void initialiseSwarm() {
		//Swarm initialise stuff
		Swarm.setActive(this);
		Swarm.init(this, 15591, "ea786f7a5f13811a3e0501a993b0c014"); //context, app id, app key
		Swarm.setAllowGuests(true);

	}


	@Override
	public void showAdInterstitial() {
			 runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (interstitial.isLoaded()){
					 interstitial.show();
				} else {
					Log.v("AdMob", "Tried to show, no ad to show");
				}
			}
		}); 
		Log.v("AdMob", "Show ad"); 
	}


	@Override
	public void loadAdInterstitial() {
		 runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (!interstitial.isLoaded()){
					interstitial.loadAd(adRequest);
					Log.v("AdMob", "Load ad");
				} else {
					Log.v("AdMob", "Ad already loaded");
				}
			}
		}); 

		Log.v("AdMob", "Load ad"); 
	}
}


