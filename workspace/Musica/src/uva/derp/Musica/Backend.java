package uva.derp.Musica;

import java.io.*;
import java.net.*;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class Backend {

	private String server;
	static int port;
	static String password;
	public boolean playing;
		
	public Backend(Context cxt) {
		/* Create backend real OOP style */
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		password = prefs.getString("password", "password");
		port = Integer.parseInt(prefs.getString("port", "9042"));
		server = prefs.getString("server", "10.1.1.2");
	}
	
	/*
	 * Return true on success, untrue on failure
	 */
	public void toggleplay()  {
			if (this.playing)
				new Query().execute(server, "/pause", "toggleplaybutton");
		    else 
		    	new Query().execute(server, "/play", "toggleplaybutton");
			this.playing = !this.playing;
			this.always();
	}
	
	private void always() {
			this.getcurrentsongs();
			this.getvolume();
	}

	public void prevtrack()  {
			new Query().execute(server, "/prev", "prev");
			this.always();
	}
	
	public void nexttrack()  {
			new Query().execute(server, "/next", "next");
			this.always();
	}
	
	public void getvolume()  {
			new Query().execute(server, "/volume", "getvolume");
	}

	public void setvolume(Integer noise)  {
		noise = noise < 0 ? 0 : noise > 100 ? 100 : noise;
			new Query().execute(server, "/volume/" + noise.toString(), "getvolume");
			this.always();
	}
	
	public String[] getcurrentsongs() {
		new Query().execute(server, "/getcurrentsongs", "currentsongs");
		// TODO Auto- method stub
		return null;
	}
}

class Query extends AsyncTask<String, Integer, String> {

	String postexecute = "";

	@Override
	protected String doInBackground(String... params) {
		String pass = "pass=" + Backend.password;
		if (params.length < 2)
			return "exception";
		String server = params[0];
		String request = params[1];
		if (params.length > 2)
			this.postexecute = params[2];
		
		try{
		URL url = new URL("http", server, Backend.port ,request + "?" + pass, null);
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
		} catch (FileNotFoundException e){
			e.printStackTrace();
			return "exception1";
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
	}
	
	protected void onPostExecute(String result) {
		Musica.callback.getcallback(postexecute, result);
	}

}
