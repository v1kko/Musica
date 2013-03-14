package uva.derp.Musica;

import java.io.*;
import java.net.*;

import android.R.integer;
import android.os.AsyncTask;


public class Backend {

	private String server;
	public boolean playing;
		
	public Backend(String server) {
		/* Create backend real OOP style */
		this.server = server;
		this.playing = false;
	}
	
	/*
	 * Return true on success, untrue on failure
	 */
	public void toggleplay() {
			if (this.playing)
				new Javakanker().execute(server, "/stop");
		    else 
		    	new Javakanker().execute(server, "/play");
			this.playing = !this.playing;
	}
	
}

class Javakanker extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
		if (params.length < 2)
			return "exception";
		String server = params[0];
		String request = params[1];
		
		try {
			
	
		URL url = new URL("http", server, 9042,request, null);
		
		HttpURLConnection conn = ((HttpURLConnection) url.openConnection());
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String result = "", line;
		while ((line = rd.readLine()) != null) {
			result += line;
		}
		rd.close();
		return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
	}
	
}
