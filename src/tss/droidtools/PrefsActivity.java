package tss.droidtools;

import tss.droidtools.phone.Hc;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
			addPreferencesFromResource(R.xml.preferences);
			getPreferenceManager().setSharedPreferencesName(Hc.PREFSNAME);
	}
}
