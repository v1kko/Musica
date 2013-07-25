package uva.derp.Musica;

import java.io.*;
import java.net.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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
		server = prefs.getString("server", "10.0.2.2");
	}
	
	public void toggleplay() {
		if (this.playing)
			new Query().execute(server, "/pause", "toggleplaybutton");
		else
			new Query().execute(server, "/play", "toggleplaybutton");
		this.playing = !this.playing;
		this.always();
	}
	
	private void always() {
		this.getcurrentsongs();
		new Query().execute(server, "/currentsong", "currentsong");
		this.getvolume();
	}

	public void prevtrack() {
		new Query().execute(server, "/prev", "prev");
		new Query().execute(server, "/currentsong", "currentsong");
	}
	
	public void nexttrack() {
		new Query().execute(server, "/next", "next");
		new Query().execute(server, "/currentsong", "currentsong");
	}
	
	public void getvolume() {
		new Query().execute(server, "/volume", "getvolume");
	}

	public void setvolume(Integer volume) {
		volume = volume < 0 ? 0 : volume > 100 ? 100 : volume;
		new Query().execute(server, "/volume/" + volume.toString(), "getvolume");
	}
	
	public String[] getcurrentsongs() {
		new Query().execute(server, "/currentsongs", "currentsongs");
		new Query().execute(server, "/currentsong", "currentsong");
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
			return "-1";
		String server = params[0];
		String request = params[1];
		
		if (params.length > 2)
			this.postexecute = params[2];
		
		try {
			URL url = new URL("http", server, Backend.port ,request + "?" + pass, null);
			HttpURLConnection conn = ((HttpURLConnection) url.openConnection());
			conn.setRequestMethod("POST");
			
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(pass);
			dos.flush();
			dos.close();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String result = "", line;
			
			while ((line = rd.readLine()) != null)
				result += line;
			
			rd.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}
	
	protected void onPostExecute(String result) {
		Musica.callback.getcallback(postexecute, result);
	}
}