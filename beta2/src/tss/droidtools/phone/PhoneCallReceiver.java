package tss.droidtools.phone;

import tss.droidtools.BaseReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
*
* Broadcast receiver that reacts to an incoming call and starts up the
* activity which will allow the user to answer the call with the camera
* button.
*
* credit goes to the following open source projects for showing a guy 
* like me how this technique works.  They're the smart ones who were cool
* enough to share the technique via an open source app:
* 
* MyLock - http://code.google.com/p/mylockforandroid/
* auto-answer - http://code.google.com/p/auto-answer/		
* 
* @author tedd
*
*/
public class PhoneCallReceiver extends BaseReceiver {
	
	/**
	 * Call back which fires off when the phone changes state.  
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		debugOn = Hc.debugEnabled(context);
		
		/* make sure the feature is enabled */
		boolean enabled = context.getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_PHONE_TOOLS_KEY, false);
		if (!enabled) 
		{
			logMe("feature disabled. ");
			return;
		} 

		/* examine the state of the phone that caused this receiver to fire off */
		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) 
		{
			
			logMe("the phone is ringing, scheduling creation call answer screen activity");
			Intent i = new Intent(context, CallAnswerIntentService.class);
			i.putExtra("delay", Hc.STARTUP_DELAY);
			context.startService(i);
			logMe("started, time to go back to listening");
		}
		return;
	}
	
	private void logMe(String s) {
		super.logMe("PhoneCallReceiver", s);
	}
}