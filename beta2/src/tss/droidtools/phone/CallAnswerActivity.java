package tss.droidtools.phone;

import tss.droidtools.BaseActivity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
/**
 *
 * Activity that gets called by the broadcast receiver when the phone rings.
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
public class CallAnswerActivity extends BaseActivity {
	protected BroadcastReceiver r;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logMe("onCreate called");

		// turn receivers on prior to drawing screen.
		registerReciever();
		setContentView(R.layout.callanswerscreen);

		// return button
		Button returnToCallScreen = (Button) findViewById(R.id.returnToCallScreen);
		returnToCallScreen.setOnClickListener(new OnClickListener()	{
          	public void onClick(View v) {
          		logMe("returnToCallScreen onClick event");
          		finishHim();
          	}
		});
		

		// reject button
		
		Button rejectCall = (Button) findViewById(R.id.rejectCallButton);
		boolean enabled = getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_ALLOW_REJECT_KEY, false);
		if (enabled) {
			rejectCall.setOnLongClickListener(new OnLongClickListener() {
	          	public boolean onLongClick(View v){
	          		logMe("rejectCall onClick event");
	          		ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
	          		// i've got a shotgun...
	          		am.restartPackage("com.android.providers.telephony");
	          		// and you aint got one...
	          		am.restartPackage("com.android.phone");
	          		finishHim();
	          		return true;
	          	}
			});
		}
		else
		{
			rejectCall.setVisibility(View.GONE);			
		}
		
		
		// answer touch screen button
		Button answerButton = (Button) findViewById(R.id.answerCallButton);
		enabled = getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_ANSWER_WITH_BUTTON_KEY, false);
		if (enabled) {
			answerButton.setOnLongClickListener(new OnLongClickListener() {
	          	public boolean onLongClick(View v){
	          		logMe("touch screen answer button onClick event");
	          		answerCall();
	          		return true;
	          	}
			});
		}
		else
		{
			answerButton.setVisibility(View.GONE);			
		}		
		
	}
	
	@Override
	protected void onStart() {
		logMe("onStart");
		super.onStart();
	}


	//
	// putting the register/unhook of the reciever in
	// resume/pause instead of stop/start beause we cannot
	// guarantee that onStop will always be called (e.g.
	// system kills this activity because it needs memory).
	// 
	// This may be overkill for the answer screen.
	//
	@Override
	protected void onResume() {
		logMe("onResume");
		registerReciever();
		super.onResume();
		
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
			logMe("Hey! the phone is idle. stopping");
			finishHim();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		logMe("paused");
		unHookReceiver();
		if(!isFinishing()) {
			logMe("giggle...");
			Intent i = new Intent(getApplicationContext(), CallAnswerIntentService.class);
			i.putExtra("delay", Hc.RESTART_DELAY);
			startService(i);
		}

	}

	protected void onStop() {
		logMe("stopped, finishing? "+isFinishing());
		super.onStop();
	}
	
	protected void onDestroy() {
		logMe("destroyed");
		super.onStop();
	}

	/** track ball version for our friends with Nexus Ones */
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_MOVE:
			// gets called a LOT! ignore ;)
			return true;
		case MotionEvent.ACTION_DOWN:
			answerCall();
			return true;
		default:
			logMe("trackball event: "+event);
		}
		return super.dispatchTrackballEvent(event);
	}
	
	/** broadcast HEADSETHOOK when the camera button is pressed*/
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
		
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_FOCUS:
			/*
			 * this event occurs when you press down lightly on the camera button
			 * e.g. auto focus.  The event happens a lot even when you press down
			 * hard (as the button is on its way down to the "hard press").
			 * returning true to consume the event and prevent further processing of 
			 * it by other apps 
			 */ 
			return true;
			
		case KeyEvent.KEYCODE_CAMERA:
			answerCall();
			return true;
			
		default:
			logMe("Unknown key event: "+event);
			break;
		}
		
		return super.dispatchKeyEvent(event);
	}
	
	
	public void finishHim() {
		unHookReceiver();
		finish();
	}
	
	public void registerReciever() {
		if (r != null) return;

		logMe("registering PHONE_STATE receiver");
		r = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) 	{
				
				String phone_state = i.getStringExtra(TelephonyManager.EXTRA_STATE);
				if (!phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) 
				{
					logMe("received "+phone_state+", time to go bye bye, thanks for playing!");
					finishHim();
				}
			} 
		};
		
		this.registerReceiver(r, new IntentFilter("android.intent.action.PHONE_STATE"));
	}

	/** 
	 * unregister the broadcast receiver in charge of exiting out of the app when the phone
	 * goes into OFFHOOK or IDLE. 
	 */
	public void unHookReceiver() {

		if (r != null) 
		{
			logMe("unhooking the reciever");
			this.unregisterReceiver(r);
			r = null;
			logMe("done");
		}
	}
	
	private void answerCall() {
		/*
		 * The "magic" goes here.  
		 * 
		 * Programmatically mimic the press of the button on a head set used to answer
		 * an incoming call.  The recipe to do this is as follows:
		 * 
		 *  intent - ACTION_MEDIA_BUTTON
		 *  action - ACTION_DOWN
		 *  code   - KEYCODE_HEADSETHOOK
		 *  
		 *  Broadcasting that intent answers the phone =)
		 *  
		 *  However, if there are any other apps that are listening for 
		 *  ACTION_MEDIA_BUTTION intents and consuming them they won't get
		 *  to the Phone app.
		 *  
		 */
		KeyEvent fakeHeadsetPress =	new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
		Intent fakeHeadsetIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		
		fakeHeadsetIntent.putExtra(Intent.EXTRA_KEY_EVENT, fakeHeadsetPress);
		logMe("broadcasting ACTION_MEDIA_BUTTION intent with a KEYCODE_HEADSETHOOK code on an ACTION_DOWN action");
		
		sendOrderedBroadcast(fakeHeadsetIntent, null);
		moveTaskToBack(true);		
		finish();		
	}

	private void logMe(String s) {
		super.logMe("CallAnswerActivity", s);
	}
}