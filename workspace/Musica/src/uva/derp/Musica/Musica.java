package uva.derp.Musica;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import uva.derp.Musica.Backend;

public class Musica extends Activity {
	
	static Musica kanker;
	private Backend be;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* env shit */
        final Backend be = new Backend("10.0.2.2"); 
        this.be = be;
        Musica.kanker=this;
        /*/env */
        
        /* objects from xml */
        setContentView(R.layout.activity_musica);
        Button play   = (Button) findViewById(R.id.playbutton);
        Button next   = (Button) findViewById(R.id.nextbutton);
        Button prev   = (Button) findViewById(R.id.prevbutton);
        //Button rewind = (Button) findViewById(R.id.rewindbutton);
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
        /*/listeners*/
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.musica, menu);
        return true;
    }


	public void krijgkanker(String javakanker, String result) {
		if (javakanker.equals("toggleplaybutton")) {
			String result1 = be.playing ? "MUZIEK" : "STILTE";
			((EditText) findViewById(R.id.currentsongs)).setText(result1);
		}
	}
    
}
