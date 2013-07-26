package uva.derp.Musica;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    private EditTextPreference server;
    private EditTextPreference port;
    private EditTextPreference password;
    public static String defaultserver = "10.0.2.2";
    public static String defaultport = "9042";
    public static String defaultpassword = "password";

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the XML preferences file
        addPreferencesFromResource(R.xml.preferences);
        // Get a reference to the preferences
        server   = (EditTextPreference) getPreferenceScreen().findPreference("server");
        port     = (EditTextPreference) getPreferenceScreen().findPreference("port");
        password = (EditTextPreference) getPreferenceScreen().findPreference("password");
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();
        // Setup the initial values
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        server.setSummary(sharedPreferences.getString("server", defaultserver));
        port.setSummary(sharedPreferences.getString("port", defaultport));
        password.setSummary(sharedPreferences.getString("password", defaultpassword));
        
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    
    public void onBackPressed() {
    	Musica.callback.be = new Backend(Musica.cxt);
        Musica.callback.be.getcurrentsongs();
    	super.onBackPressed();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Let's do something a preference value changes
        server.setSummary(sharedPreferences.getString("server", defaultserver));
        port.setSummary(sharedPreferences.getString("port", defaultport));
        password.setSummary(sharedPreferences.getString("password", defaultpassword));
    }
}