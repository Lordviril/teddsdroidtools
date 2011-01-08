package tss.droidtools.phone.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Configuration activity generic check box on click listener
 * that will update the config preference and display a toast
 * message.
 * 
 * @author tedd
 *
 */
public class ConfigCheckBoxOnClickListener implements OnClickListener {

	private String preferenceKey;
	private String toastMsg;
	private SharedPreferences preferences;
	private Context configActivityContext;
	
	public ConfigCheckBoxOnClickListener(String key,String toastMsg,
			SharedPreferences p, Context c) {
		this.preferenceKey = key; 
		this.toastMsg = toastMsg;
		this.preferences = p;
		this.configActivityContext = c;
	}
	
	/**
	 * On click event call back handler that updates the preference
	 * for the selected preference and displays the toast.
	 */
	@Override
	public void onClick(View v) {
		
		CheckBox checkBox = (CheckBox)v;
		String status = " Enabled";
		
        if ( checkBox.isChecked() ) {
        	preferences.edit().putBoolean(preferenceKey, true).commit();
        } else {
        	preferences.edit().putBoolean(preferenceKey, false).commit();
        	status = " Disabled";
        }
        
        Toast.makeText(configActivityContext, toastMsg + status, Toast.LENGTH_SHORT).show();
	}
}