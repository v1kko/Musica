package uva.derp.Musica;

import java.io.*;
import java.net.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class Backend {

	public static String server;
	static int port;
	static String password;
	public boolean playing;
	
	// Constructor
	public Backend(Context cxt) {
		/* Create backend real OOP style */
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		password = prefs.getString("password", Settings.defaultpassword);
		port = Integer.parseInt(prefs.getString("port", Settings.defaultport));
		server = prefs.getString("server", Settings.defaultserver);
	}
	
	// Pauses playback if playing and vice versa
	public void toggleplay() {
		if (this.playing)
			new PauseQuery().send();
		else
			new PlayQuery().send();
		this.playing = !this.playing;
		this.getvolume();
		this.getprogress();
	}

	// Return to previous track
	public void prevtrack() {
		new PrevQuery().send();
		this.getprogress();
	}
	
	// Skip to next track
	public void nexttrack() {
		new NextQuery().send();
		this.getprogress();
	}
	
	// Synchronize volume with server
	public void getvolume() {
		new GetVolumeQuery().send();
	}
	
	// Synchronize current song and song time with server
	public void getprogress() {
		new ProgressQuery().send();
	}

	// Send new volume value to server
	public void setvolume(Integer volume) {
		volume = volume < 0 ? 0 : volume > 100 ? 100 : volume;
		new SetVolumeQuery().send(volume);
		Musica.callback.settitlefocus();
	}
	
	// Synchronize playlist with server
	public String[] getcurrentsongs() {
		new SongsQuery().send();
		this.getprogress();
		return null;
	}
	
}

// Class for performing requests and listening to answers outside of UI thread
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
			URL url = new URL("http", server, Backend.port, request + "?" + pass, null);
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
	
	// Invokes getcallback with possible third argument after receiving answer to query
	protected void onPostExecute(String result) {
		Musica.callback.getcallback(postexecute, result);
	}
}

class GetVolumeQuery extends Query {
	public void send() {
		execute(Backend.server, "/volume", "getvolume");
	}
}

class ProgressQuery extends Query {
	public void send() {
		execute(Backend.server, "/progress", "refreshprogress");
	}
}

class SongsQuery extends Query {
	public void send() {
		execute(Backend.server, "/currentsongs", "currentsongs");
	}
}

class PlayQuery extends Query {
	public void send() {
		execute(Backend.server, "/play", "toggleplaybutton");
	}
}

class PauseQuery extends Query {
	public void send() {
		execute(Backend.server, "/pause", "toggleplaybutton");
	}
}

class PrevQuery extends Query {
	public void send() {
		execute(Backend.server, "/prev", "prev");
	}
}

class NextQuery extends Query {
	public void send() {
		execute(Backend.server, "/next", "next");
	}
}

class SetVolumeQuery extends Query {
	public void send(Integer volume) {
		execute(Backend.server, "/volume/" + volume.toString(), "getvolume");
	}
}