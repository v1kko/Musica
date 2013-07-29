package uva.derp.Musica;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import uva.derp.Musica.Backend;
import uva.derp.Musica.Settings;
import uva.derp.Musica.extraClass;

public class Musica extends Activity {
	// set to true to perform 3x5 unittests on startup
	boolean performunittest = false;
	
	Backend be;
	static Musica callback;
	static Context cxt;
	private SeekBar volume;
	public SeekBar time;
	public Thread thr;
	private ListView listView;
	static public AlertDialog wrongsettings;
	static public AlertDialog wrongserver;
	public int tottime;
	public int curvol = -1;
	public int curindex = 0;
	public String[] currentsongs = null;
	public String currentsong = "-1";
	
	// Called on application launch
	// Initializes all UI elements and sets their corresponding actions
	// Requests server information afterwards
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* env */
		cxt = this;
		be = new Backend(this); 
		Musica.callback = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your server settings don't seem right, please change them");
		builder.setNeutralButton(
			"OK",
			
			new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(findViewById(R.id.settingsbutton).getContext(), Settings.class);
					startActivity(intent);
				}
			}
		);
		wrongsettings = builder.create();
		builder.setMessage("Your server doesn't support the whole API, please update");
		builder.setNeutralButton(
			"OK",
			
			new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			}
		);
		wrongserver = builder.create();
		/*/env */
				
		/* objects from xml */
		setContentView(R.layout.activity_musica);
		Button play     = (Button) findViewById(R.id.playbutton);
		Button next     = (Button) findViewById(R.id.nextbutton);
		Button prev     = (Button) findViewById(R.id.prevbutton);
		Button settings = (Button) findViewById(R.id.settingsbutton);
		Button listert  = (Button) findViewById(R.id.listbutton);
		volume          =(SeekBar) findViewById(R.id.volumebar);
		time            =(SeekBar) findViewById(R.id.timebar);
		volume.setMax(100);
		time.setMax(1000);
		time.setEnabled(false);
		/*/objects */
		
		/* listeners */
		volume.setOnSeekBarChangeListener(new extraClass());
		
		play.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					be.toggleplay();
				}
			}
		);
		
		listert.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					be.getcurrentsongs();
				}
			}
		);
		
		next.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					be.nexttrack();
				}
			}
		);
		
		prev.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					be.prevtrack();
				}
			}
		);
		
		settings.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), Settings.class);
					startActivity(intent);
				}
			}
		);
		/*/listeners*/
		
		be.getvolume();
		be.getcurrentsongs();
		
		if (performunittest)
			unittest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.musica, menu);
		return true;
	}
	
	// Sets timebar to given time position
	public void settimebarpos(int curtime) {
		this.time.setProgress(curtime*1000/tottime);
	}
	
	// Highlights current track
	public void settitlefocus() {
		try {
			listView.setEnabled(false);
			listView.requestFocusFromTouch();
			listView.setSelection(curindex);
		} catch (Exception e) {
			// prevent crash when listView not initialized
		}
	}

	// Highlights given track
	public void settitlefocus(String title) {
		if (!title.equals(this.currentsong)) {
			for (int i = 0; i < this.currentsongs.length; i++) {
		   		if (this.currentsongs[i].equals(title)) {
		   			curindex = i;
		   			break;
		   		}
		   	}
			this.currentsong = title;
		}
		settitlefocus();
	}

	// Invoked after Asynctasks from backend
	@SuppressWarnings("deprecation")
	public void getcallback(String postexecute, String result) {
		if (result.equals("-1")) {
			wrongsettings.show();
			be.playing = false;
		}
		
		if (postexecute.equals("toggleplaybutton")) {
			Button play = (Button) findViewById(R.id.playbutton);
			if (be.playing) {
				play.setBackgroundResource(R.drawable.pause_button_layer);
				// If playlist wasn't loaded, try loading it again if app set to play
			} else play.setBackgroundResource(R.drawable.play_button_layer);
			
		} else if (postexecute.equals("currentsongs")) {
			JSONArray json;
			
			try {
				json = new JSONArray(result);
			} catch (JSONException e) {
				return;
			}
			
			try {
				
				// if empty playlist, try invoking nexttrack to circumvent a musicapi bug
				if (json.length() == 0) {
					be.playing = false;
					be.nexttrack();
					safesleep();
					be.getcurrentsongs();
					return;
				}
				
				// fill currentsongs array
				this.currentsongs = new String[json.length()];
				for (int i = 0; i < json.length(); i++)
					this.currentsongs[i] = json.getJSONArray(i).getString(2); 
			} catch (JSONException e) {
				return;
			}
			
			// put songs into listview
			listView = (ListView) findViewById(R.id.currentsongs);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.currentsongs);
			listView.setAdapter(adapter);
		} else if (postexecute.equals("refreshprogress")) {
			JSONArray json;
			try {
				// extract song name, current time and total time
				json = new JSONArray(result);
				final int curtime = (int)Integer.parseInt(json.getString(0));
				this.tottime = (int)Integer.parseInt(json.getString(1));
				String cursong = json.getString(2);
				// cancel any current timebar progression
				try {
					thr.interrupt();
					thr.stop();
				} catch (Exception e) {}
				// highlight appropriate song and set timebar appropriately
				settitlefocus(cursong);
				settimebarpos(curtime);
				// if music is playing, start moving the timebar
				// slow phones/emulators will experience a timebar that moves too slow, since one second may be actually five
				// since the timebar refreshes with new server data every ten simulated seconds, this will get fixed periodically
				if (be.playing) {
					this.thr = new Thread() {
						@Override
						public void run() {
							int i = curtime;
							int firsti = i;
							while (true) {
								try {
									safesleep();
									i++;
									if (Thread.interrupted() || this.isInterrupted()) {
										break;
									// ask for newly changed data after 10 seconds or when track is probably done
									} else if (i == Musica.callback.tottime+1 || i == firsti+10) {
										Musica.callback.be.getprogress();
										break;
									}
									Musica.callback.settimebarpos(i);
								} catch (Exception e) {
									break;
								}
							}
						}
					};
					this.thr.start();
				}
			} catch (Exception e) {
			}
		} else if (postexecute.equals("getvolume")) {
			// saves volume and sets volumebar appropriately
			try {
				this.curvol = Integer.parseInt(result);
				this.volume.setProgress(Integer.parseInt(result));
			} catch (Exception e) {
				return;
			}
		}
	}
	
	// Starts unittests in separate thread
	public void unittest () {
		Thread t = new Thread() {
			@Override
			public void run() {
				while (currentsongs == null || currentsongs.length == 0) {
					Log.d("unittest","waiting for server to send playlist...");
					safesleep();
				}
				while (currentsong == "-1") {
					Log.d("unittest","waiting for server to send song info...");
					safesleep();
				}
				while (curvol == -1) {
					Log.d("unittest","waiting for server to send volume value...");
					safesleep();
				}
				if (test1() & test2() & test3())
					Log.d("unittest","all unittests succeeded");
				else
					Log.d("unittest","some unittests failed!");
			}
		};
		t.start();
	}
	
	// Chooses a random song in the list to move to
	// Will run up to 5 times
	public boolean test1() {
		if (be.playing) {
			be.toggleplay();
		}
		int beginindex = curindex;
		
		Random generator = new Random();
		for (int i = 0; i < 5; i++) {
			int roll = 0;
			while (roll == 0)
				roll = generator.nextInt(currentsongs.length) - curindex;
			
			Log.d("unittest","Attempting to go from song number "+curindex+" to song number "+(curindex+roll));
			safesleep();
			
			int beginroll = roll;
			while (roll != 0) {
				if (roll > 0) {
					be.nexttrack();
					roll--;
					Log.d("unittest","Invoking nexttrack() ("+(Math.abs(beginroll)-Math.abs(roll))+"/"+Math.abs(beginroll)+")");
					safesleep();
				} else {
					be.prevtrack();
					roll++;
					Log.d("unittest","Invoking prevtrack() ("+(Math.abs(beginroll)-Math.abs(roll))+"/"+Math.abs(beginroll)+")");
					safesleep();
				}
			}
			Log.d("unittest","Current song is "+curindex+".: "+currentsong+"\nExpected song is "+(beginindex+beginroll)+".: "+currentsongs[beginindex+beginroll]);
			if (!(currentsong.equals(currentsongs[beginindex+beginroll]) && curindex == beginindex+beginroll)) {
				Log.d("unittest","failure!");
				safesleep();
				return false;
			} else {
				Log.d("unittest","success!");
				beginindex += beginroll;
				safesleep();
			}
		}
		Log.d("unittest","unittest 1 finished successfully!");
		return true;
	}
	
	// Invokes toggleplay and checks if played field is updated appropriately
	// Also randomly invokes toggleplay between separate iterations sometimes for added randomness
	public boolean test2() {
		Random generator = new Random();
		for (int i = 0; i < 5; i++) {
			if (be.playing) {
				Log.d("unittest","currently playing, lets invoke toggleplay!");
				be.toggleplay();
				safesleep();
				if (be.playing) {
					Log.d("unittest","still playing: failure!");
					safesleep();
					return false;
				} else {
					Log.d("unittest","no longer playing: success!");
					safesleep();				}
			} else {
				Log.d("unittest","currently paused, lets invoke toggleplay!");
				safesleep();
				if (!(be.playing)) {
					Log.d("unittest","no longer playing: failure!");
					safesleep();
					return false;
				} else {
					Log.d("unittest","still playing: success!");
					safesleep();
				}
			}
			
			if (generator.nextBoolean()) {
				Log.d("unittest","DIVINE INTERVENTION! Lets blindly invoke toggleplay at random!");
				be.toggleplay();
				safesleep();
			}
		}
		Log.d("unittest","unittest 2 finished successfully!");
		safesleep();
		return true;
	}
	
	// Invokes setvolume to set the volume
	// Checks if volume has been updated accordingly with getvolume after one second
	public boolean test3() {
		Random generator = new Random();
		for (int i = 0; i < 5; i++) {
			int roll = generator.nextInt(100);
			Log.d("unittest","Lets invoke setvolume(" + roll +");");
			be.setvolume(roll);
			safesleep();
			be.getvolume();
			safesleep();
			if (roll == curvol) {
				Log.d("unittest","Volume was indeed changed to " + curvol + ": success!");
				safesleep();
			} else {
				Log.d("unittest","Volume was changed to " + curvol + " instead of " + roll + ": failure!");
				safesleep();
				return false;
			}
		}
		Log.d("unittest","unittest 3 finished successfully!");
		safesleep();
		return true;
	}
	
	// Macro for sleeping to prevent code cluttering due to mandatory try catch
	public void safesleep() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {}
	}
}

// Makes volumebar invoke setvolume when released
class extraClass implements OnSeekBarChangeListener{

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		Musica.callback.be.setvolume(seekBar.getProgress());
	}
}
