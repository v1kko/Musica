package uva.derp.Musica;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Musica extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        Button play = (Button) findViewById(R.id.playbutton);
        play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			((EditText) findViewById(R.id.currentsongs)).setText("Hello World");
				
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.musica, menu);
        return true;
    }
    
}