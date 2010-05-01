package tss.droidtools.phone;

import tss.droidtools.BaseActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
/**
 * Configuration screen for the camera button 
 * @author tedd
 *
 */
public class ConfigScreenActivity extends BaseActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
               
        // set the defaults
        if (!p.contains(Hc.PREF_PHONE_TOOLS_KEY))
        	p.edit().putBoolean(Hc.PREF_PHONE_TOOLS_KEY, false).commit();
        if (!p.contains(Hc.PREF_DEBUG_LOGGING_KEY))
        	p.edit().putBoolean(Hc.PREF_DEBUG_LOGGING_KEY, true).commit();

        Boolean enabled = p.getBoolean(Hc.PREF_PHONE_TOOLS_KEY, false);

        final CheckBox checkbox = (CheckBox) findViewById(R.id.cameraAnswerCheckBox);
        checkbox.setChecked(enabled);
        checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) 
                {
                	p.edit().putBoolean(Hc.PREF_PHONE_TOOLS_KEY, true).commit();
                    Toast.makeText(ConfigScreenActivity.this, "Feature Enabled", Toast.LENGTH_SHORT).show();
                } 
                else 
                {
                	p.edit().putBoolean(Hc.PREF_PHONE_TOOLS_KEY, false).commit();
                    Toast.makeText(ConfigScreenActivity.this, "Feature Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        Boolean debugLoggingEnabled = p.getBoolean(Hc.PREF_DEBUG_LOGGING_KEY, true);
        final CheckBox debugLoggingcheckbox = (CheckBox) findViewById(R.id.debugLoggingCheckBox);
        debugLoggingcheckbox.setChecked(debugLoggingEnabled);
        debugLoggingcheckbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) 
                {
                	p.edit().putBoolean(Hc.PREF_DEBUG_LOGGING_KEY, true).commit();
                    Toast.makeText(ConfigScreenActivity.this, "Debug Logging Enabled", Toast.LENGTH_SHORT).show();
                } 
                else 
                {
                	p.edit().putBoolean(Hc.PREF_DEBUG_LOGGING_KEY, false).commit();
                    Toast.makeText(ConfigScreenActivity.this, "Debug Logging Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });     
        
        
    }
}