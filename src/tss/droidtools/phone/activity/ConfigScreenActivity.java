package tss.droidtools.phone.activity;

import tss.droidtools.phone.Hc;
import tss.droidtools.phone.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

/**
 * Configuration screen.
 *  
 * @author tedd
 *
 */
public class ConfigScreenActivity extends Activity {

	protected SharedPreferences preferences;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		preferences = getSharedPreferences(Hc.PREFSNAME, 0);
        setContentView(R.layout.main);

        renderConfigCheckbox(Hc.PREF_PHONE_TOOLS_KEY,true,R.id.phoneToolsCheckBox,"ALL Phone Tools have been");
        renderConfigCheckbox(Hc.PREF_CALL_ANSWER_TOOLS_KEY,true,R.id.callAnswerTools,"Call Answer Tools");
        renderConfigCheckbox(Hc.PREF_ANSWER_WITH_CAMERA_KEY,true,R.id.cameraAnswerCheckBox,"Camera Button Answer");
        renderConfigCheckbox(Hc.PREF_ANSWER_WITH_TRACKBALL_KEY,true,R.id.trackballAnswerCheckBox,"Trackball Answer");
        renderConfigCheckbox(Hc.PREF_ANSWER_WITH_BUTTON_KEY,true,R.id.touchscreenButtonAnswerCheckBox,"Touchscreen Button Answer");
        renderConfigCheckbox(Hc.PREF_ALLOW_REJECT_KEY,true,R.id.rejectCallsCheckBox,"Reject Call Feature");
        renderConfigCheckbox(Hc.PREF_SCREEN_GUARD_TOOLS_KEY,true,R.id.inCallScreenGuardCheckBox,"In-Call Screen Guard");
        renderConfigCheckbox(Hc.PREF_DEBUG_LOGGING_KEY,true,R.id.debugLoggingCheckBox,"Debug Logging");
        
    }
    
    /**
     * Renders a configurations check box, setting a default
     * value if its the first time this preference has been
     * referenced, with the users stored value and establishes
     * an onClick handler.
     * 
     * @param preferenceKey
     * @param defaultValue
     * @param viewId
     * @param toastMsg
     */
    private void renderConfigCheckbox(String preferenceKey, 
    		boolean defaultValue, int viewId, String toastMsg) {

    	// set a default value if one does not exist yet for the preference
        if (!preferences.contains(preferenceKey)) {
        	preferences.edit().putBoolean(preferenceKey, defaultValue).commit();
        }

        // get the current preference value and view component
        Boolean currentValue = preferences.getBoolean(preferenceKey, defaultValue);
        final CheckBox checkbox = (CheckBox) findViewById(viewId);
        
        // set the current setting and register the onClick call back
        checkbox.setChecked(currentValue);
        checkbox.setOnClickListener(new ConfigCheckBoxOnClickListener(preferenceKey, toastMsg, preferences, ConfigScreenActivity.this));  
    }
}