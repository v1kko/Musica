package uva.derp.Musica;

import java.io.*;
import java.net.*;

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
	public void toggleplay()  {
			if (this.playing)
				new Javakanker().execute(server, "/stop", "toggleplaybutton");
		    else 
		    	new Javakanker().execute(server, "/play", "toggleplaybutton");
			this.playing = !this.playing;
	}
	
	public void prevtrack()  {
			new Javakanker().execute(server, "/prev", "prev");
	}
	
	public void nexttrack()  {
			new Javakanker().execute(server, "/next", "next");
	}

	public String[] getcurrentsongs() {
		new Javakanker().execute(server, "/getcurrentsongs", "currentsongs");
		// TODO Auto- method stub
		return null;
	}
}

class Javakanker extends AsyncTask<String, Integer, String> {

	String javakanker = "";

	@Override
	protected String doInBackground(String... params) {
		String pass = "pass=password";
		if (params.length < 2)
			return "exception";
		String server = params[0];
		String request = params[1];
		if (params.length > 2)
			this.javakanker = params[2];
		
		try{
		URL url = new URL("http", server, 9042,request, null);
		HttpURLConnection conn = ((HttpURLConnection) url.openConnection());
		conn.setRequestMethod("POST");
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		dos.writeBytes(pass);
		dos.flush();
		dos.close();
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
	
	protected void onPostExecute(String result) {
		Musica.kanker.krijgkanker(javakanker, result);
	}

}
