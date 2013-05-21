package uva.derp.Musica;

import java.security.PublicKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.R.integer;
import android.media.MediaRouter.VolumeCallback;
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
	
	static Musica callback;
	static Context cxt;
	Backend be;
	private Settings se;
	private String[] currentsongs;
	private String currentsong;
	private SeekBar volume;
	private ListView listView;
	static public AlertDialog wrongsettings;
	static public AlertDialog wrongserver;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* env */
        cxt = this;
        be = new Backend(this); 
        Musica.callback=this;
        be.toggleplay();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your server settings don't seem right, please change them");
        builder.setNeutralButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
        		Intent intent = new Intent(findViewById(R.id.settingsbutton).getContext(), Settings.class);
        		startActivity(intent);
				
			}
		});
        wrongsettings = builder.create();
        builder.setMessage("Your server doesn't support the whole API, please update");
        builder.setNeutralButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
        wrongserver = builder.create();
        /*/env */
        
        /* Init */
        be.getcurrentsongs();
        /*/Init */
        
        /* objects from xml */
        setContentView(R.layout.activity_musica);
        Button play   = (Button) findViewById(R.id.playbutton);
        Button next   = (Button) findViewById(R.id.nextbutton);
        Button prev   = (Button) findViewById(R.id.prevbutton);
        Button settings   = (Button) findViewById(R.id.settingsbutton);
        volume = (SeekBar) findViewById(R.id.volumebar);
        volume.setMax(100);
        /*/objects */
        
        /* listeners */
        volume.setOnSeekBarChangeListener(new extraClass());
        
        play.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		be.toggleplay();
			}
		});
        next.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		be.nexttrack();
			}
		});
        prev.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		be.prevtrack();
			}
		});
        
        settings.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(v.getContext(), Settings.class);
        		startActivity(intent);
			}
		});
        /*/listeners*/
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.musica, menu);
        return true;
    }


	public void getcallback(String postexecute, String result) {
		if (result.equals("exception")){
			wrongsettings.show();
			be.playing = false;
		}
		if (result.equals("exception1")){
			wrongserver.show();
		 	be.playing = false;
		}
		if (postexecute.equals("toggleplaybutton")) {
	        Button play   = (Button) findViewById(R.id.playbutton);
			if (be.playing)
			{
		        play.setBackgroundResource(R.drawable.pause_button_layer);
			}
			else {
				play.setBackgroundResource(R.drawable.play_button_layer);
			}
		}
		if (postexecute.equals("currentsongs")) {
			JSONArray jason;
			try {
				jason = new JSONArray(result);
			} catch (JSONException e) {
							// TODO Auto-generated catch block
				e.printStackTrace();
				
				return;
			}
			try {
				this.currentsongs = new String[jason.length()];
				for (int i = 0; i < jason.length(); i++){
					this.currentsongs[i] = jason.getJSONArray(i).getString(2); 
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
	        listView = (ListView) findViewById(R.id.currentsongs);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.currentsongs);
	        listView.setAdapter(adapter);
		}
		if (postexecute.equals("currentsong")) {
			JSONArray json;
			try {
				json = new JSONArray(result);
				this.currentsong = (String) json.getString(2);
		        for(int i = 0 ; i < this.currentsongs.length ; i ++ ){
		       		if (this.currentsongs[i].equals(this.currentsong) )
		       		{
		       			Log.d("DBUG", Integer.toString(i));
		       			listView.setFocusable(true);
		       			listView.setFocusableInTouchMode(true);
		       			//Oh android .... 
		       			//listView.setItemChecked(i, true);
		       			listView.setSelection(i);
		       			
		       			//listView.requestChildFocus(listView.getChildAt(i), listView);
		       			//listView.performItemClick(listView, i, listView.getItemIdAtPosition(i));
		       			//listView.setActivated(true);
		       			//listView.getChildAt(i).performClick();
		       			//listView.performItemClick(listView.getAdapter().getView(i, null, null), i, listView.getAdapter().getItemId(i));
		       			listView.getChildAt(i).setBackgroundColor(0x6f6fff);
		       			//listView.getAdapter().getView(i, null, null).setBackgroundColor(0x6f6fff);
		       			//listView.invalidateViews();
		       		}
		       	}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		if (postexecute.equals("getvolume"))
		{
			try {
			this.volume.setProgress(Integer.parseInt(result));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}

class extraClass implements OnSeekBarChangeListener{

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		Musica.callback.be.setvolume(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
}
