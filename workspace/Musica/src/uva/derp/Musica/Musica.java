package uva.derp.Musica;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
	
	Backend be;
	static Musica callback;
	static Context cxt;
	private String[] currentsongs;
	private String currentsong;
	private SeekBar volume;
	public SeekBar time;
	public Thread thr;
	private ListView listView;
	static public AlertDialog wrongsettings;
	static public AlertDialog wrongserver;
	public int tottime;
	public int curindex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* env */
		cxt = this;
		be = new Backend(this); 
		Musica.callback = this;
		//be.toggleplay();
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
		
		/* Init */
		be.getvolume();
		be.getcurrentsongs();
		/*/Init */
		
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.musica, menu);
		return true;
	}
	
	public void settimebarpos(int curtime) {
		this.time.setProgress(curtime*1000/tottime);
	}
	
	public void settitlefocus() {
		try {
			listView.setEnabled(false);
			listView.requestFocusFromTouch();
			listView.setSelection(curindex);
		} catch (Exception e) {
			// if listView not yet initialized
		}
	}

	public void settitlefocus(String title) {
		if (!title.equals(this.currentsong)) {
			for (int i = 0; i < this.currentsongs.length; i++) {
		   		if (this.currentsongs[i].equals(title)) {
		   			curindex = i;
		   			break;
		   		}
		   	this.currentsong = title;
		   	}
		}
		settitlefocus();
	}

	@SuppressWarnings("deprecation")
	public void getcallback(String postexecute, String result) {
		if (result.equals("-1")) {
			wrongsettings.show();
			be.playing = false;
		}
		
		if (postexecute.equals("toggleplaybutton")) {
			Button play = (Button) findViewById(R.id.playbutton);
			if (be.playing)
				play.setBackgroundResource(R.drawable.pause_button_layer);
			else
				play.setBackgroundResource(R.drawable.play_button_layer);
			
		} else if (postexecute.equals("currentsongs")) {
			JSONArray json;
			
			try {
				json = new JSONArray(result);
			} catch (JSONException e) {
				return;
			}
			
			try {
				this.currentsongs = new String[json.length()];
				for (int i = 0; i < json.length(); i++)
					this.currentsongs[i] = json.getJSONArray(i).getString(2); 
			} catch (JSONException e) {
				return;
			}
			
			listView = (ListView) findViewById(R.id.currentsongs);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.currentsongs);
			listView.setAdapter(adapter);
		} else if (postexecute.equals("refreshprogress")) {
			JSONArray json; final int curtime; String cursong;
			try {
				json = new JSONArray(result);
				curtime = (int)Integer.parseInt(json.getString(0));
				this.tottime = (int)Integer.parseInt(json.getString(1));
				cursong = json.getString(2);
			} catch (Exception e) {
				return;
			}
			
			settitlefocus(cursong);
			settimebarpos(curtime);
			
			try {
				thr.stop();
			} catch (Exception e) {
				// if no thread initialized yet
			}
			
			if (be.playing) {
				this.thr = new Thread() {
					@Override
					public void run() {
						int i = curtime;
						int firsti = i;
						while (true) {
							try {
								Thread.sleep(1000);
								i++;
								Musica.callback.settimebarpos(i);
								// ask for newly changed shit after 10 seconds or when track is done
								if (i == Musica.callback.tottime + 1 || i == firsti + 10) {
									Musica.callback.be.getprogress();
									break;
								}
							} catch (Exception e) {
								break;
							}
						}
					}
				};
				this.thr.start();
			}
		} else if (postexecute.equals("getvolume")) {
			try {
				this.volume.setProgress(Integer.parseInt(result));
			} catch (Exception e) {
				return;
			}
		}
	}
}

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
