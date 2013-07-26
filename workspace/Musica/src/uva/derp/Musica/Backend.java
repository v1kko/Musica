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
	
	public Backend(Context cxt) {
		/* Create backend real OOP style */
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		password = prefs.getString("password", Settings.defaultpassword);
		port = Integer.parseInt(prefs.getString("port", Settings.defaultport));
		server = prefs.getString("server", Settings.defaultserver);
	}
	
	public void toggleplay() {
		if (this.playing)
			new PauseQuery().send();
		else
			new PlayQuery().send();
		this.playing = !this.playing;
	}

	public void prevtrack() {
		new PrevQuery().send();
	}
	
	public void nexttrack() {
		new NextQuery().send();
	}
	
	public void getvolume() {
		new GetVolumeQuery().send();
	}

	public void setvolume(Integer volume) {
		volume = volume < 0 ? 0 : volume > 100 ? 100 : volume;
		new SetVolumeQuery().send(volume);
	}
	
	public String[] getcurrentsongs() {
		new SongsQuery().send();
		// TODO Auto- method stub
		return null;
	}
	
	public String getcurrentsong() {
		new SongQuery().send();
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

class GetVolumeQuery extends Query {
	public void send() {
		execute(Backend.server, "/volume", "getvolume");
	}
}

class SongQuery extends GetVolumeQuery {
	public void send() {
		execute(Backend.server, "/currentsong", "currentsong");
		//super.send();
	}
}

class SongsQuery extends SongQuery {
	public void send() {
		execute(Backend.server, "/currentsongs", "currentsongs");
		//super.send();
	}
}

class PlayQuery extends SongQuery {
	public void send() {
		execute(Backend.server, "/play", "toggleplaybutton");
		////execute(Backend.server, "/currentsong", "currentsong");
		super.send();
	}
}

class PauseQuery extends SongQuery {
	public void send() {
		execute(Backend.server, "/pause", "toggleplaybutton");
		//super.send();
	}
}

class PrevQuery extends SongQuery {
	public void send() {
		execute(Backend.server, "/prev", "prev");
		//super.send();
	}
}

class NextQuery extends SongQuery {
	public void send() {
		execute(Backend.server, "/next", "next");
		//super.send();
	}
}

class SetVolumeQuery extends Query {
	public void send(Integer volume) {
		execute(Backend.server, "/volume/" + volume.toString(), "getvolume");
	}
}