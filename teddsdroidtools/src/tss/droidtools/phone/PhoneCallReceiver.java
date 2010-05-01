package tss.droidtools.phone;

import tss.droidtools.BaseReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
	
	private Handler sh;
	private Context c;
	private CreateCallAnswerActivityTask t = new CreateCallAnswerActivityTask();

	
	/**
	 * Call back which fires off when the phone changes state.  
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		c = context;  // TODO: possible clean up on this var
		debugOn = Hc.debugEnabled(c);
		
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
			c = context;	// stash the passed in context away in an instance variable so the runnable can access it
			sh = new Handler();
			sh.postDelayed(t, Hc.STARTUP_DELAY);

			//TODO: start a short lived service that will start up the thread instead of just spawing a delayed thread.
			// From the Android docs that describe broadcast receivers:
			
			// If onReceive() spawns the thread and then returns, the entire process, 
			// including the new thread, is judged to be inactive (unless other application 
			// components are active in the process), putting it in jeopardy of being killed. 
			// The solution to this problem is for onReceive() to start a service and let the 
			// service do the job, so the system knows that there is still active work being 
			// done in the process. 
		}
	}

	/**
	 * Runnable to start the call answer screens activity in the background.
	 * 
	 * Extra thanks to the MyLock project for showing me how to implement this activity start up via
	 * an async thread in the name of preventing ANR's. 
	 */
	class CreateCallAnswerActivityTask implements Runnable {

		@Override
		public void run() {
			logMe("CreateCallAnswerActivityTask#run call back called");

			/* Create a new intent to be used to start the call answer screen's activity */
			Intent callAnswerActivity = new Intent();
    		callAnswerActivity.setClassName("tss.droidtools.phone","tss.droidtools.phone.CallAnswerActivity");
    		
    		/* Set the new intent's flags.
    		 * 
    		 * FLAG_ACTIVITY_NEW_TASK 
    		 * must be passed to start the activity from this receiver.  If you don' pass
    		 * it you get a nice stack trace with an error message that says this flag needs to be set.
    		 *
    		 * FLAG_ACTIVITY_NO_USER_ACTION
    		 * This prevents the call to the onUserLeaveHint call back that normally gets called on the tasks currently 
    		 * being displayed on the screen from being called.  Needed to prevent the call answer screen from clearing
    		 * any notifications that task may have set which should remain until the user has acknowledged them.
    		 */
    		callAnswerActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
    		
    		/* start the call answer screen activity */
    		logMe("starting CallAnswerActivity");
        	c.startActivity(callAnswerActivity);
        	
        	/* clean up and go home */
        	sh.removeCallbacks(t);
        	sh = null;
		}
	}
	
	private void logMe(String s) {
		super.logMe("PhoneCallReceiver", s);
	}
}
