package uva.derp.Musica;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import uva.derp.Musica.Backend;
import uva.derp.Musica.Settings;

public class Musica extends Activity {
	
	static Musica callback;
	private Backend be;
	private Settings se;
	private String[] currentsongs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* env */
        be = new Backend("10.0.2.2"); 
        Musica.callback=this;
        be.toggleplay();
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
        /*/objects */
        
        /* listeners */
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
			JSONObject json;
				Log.d("DBUG", result);
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return;
			}
			try {
				JSONArray bla = (JSONArray) json.get("currentsongs");
				this.currentsongs = new String[bla.length()];
				for (int i = 0; i < bla.length(); i++){
					this.currentsongs[i]= bla.getString(i); 
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
	        ListView currentsongs = (ListView) findViewById(R.id.currentsongs);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.currentsongs);
	        currentsongs.setAdapter(adapter);
		}
	}
    
}
