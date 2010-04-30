package tss.droidtools.phone;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
public class CallAnswerActivity extends Activity {
	protected CallAnswerActivityReceiver r;
	private Boolean debugOn;
	private ActivityManager am;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debugOn = Hc.debugEnabled(getApplicationContext());
		logMe("onCreate called");
		registerReciever();
		setContentView(R.layout.callanswerscreen);
		Button returnToCallScreen = (Button) findViewById(R.id.returnToCallScreen);
		returnToCallScreen.setOnClickListener(new OnClickListener() {
          	public void onClick(View v){
          		logMe("returnToCallScreen onClick event");
          		finishHim();
          	}
		});
		

		am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		Button rejectCall = (Button) findViewById(R.id.rejectCallButton);
		rejectCall.setOnClickListener(new OnClickListener() {
          	public void onClick(View v){
          		logMe("rejectCall onClick event");
          		List<RunningAppProcessInfo> ps = am.getRunningAppProcesses();
          		for (RunningAppProcessInfo p : ps) {
          			
          			logMe(" processName "+p.processName);
          			logMe(" pid "+p.pid);
          			String[] l = p.pkgList;
          			for (int x = 0; x < l.length; x++)
          				logMe("   pkgList["+x+"]: "+l[x]);
          			//logMe(" pkgList "+p.pkgList);
          			logMe(" object: " +p);
          		}
          		
          		// i've got a shotgun...
          		am.restartPackage("com.android.providers.telephony");
          		// and you aint got one...
          		am.restartPackage("com.android.phone");
          		finishHim();
          	}
		});
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
		
		//TODO: add a check to make sure the phone is still ringing in the case
		//      that the caller hung up before the activity started.
	}
	
	@Override
	protected void onPause() {
		logMe("onPause");
		unHookReceiver();
		super.onPause();
	}

	@Override
	protected void onStop() {
		logMe("onStop");
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
		//logMe("dispatchKeyEvent called with "+event);
		
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_FOCUS:
			/* this event occurs when you press down lightly on the camera button
			 * e.g. auto focus.  The event happens a lot even when you press down
			 * 
			 * hard (as the button is on its way down to the "hard press").
			 */
			
			//logMe("KEYCODE_FOCUS ignoring it");
			
			/* returning true to consume the event and prevent further processing of it by other apps */ 
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
		if (r != null) {
			logMe("receiver is already registered, skipping.");
			return;
		}
		logMe("registering PHONE_STATE receiver");
		r = new CallAnswerActivityReceiver();
		// kill this activity once the phone is picked up.
		this.registerReceiver(r, new IntentFilter("android.intent.action.PHONE_STATE"));
		
		
	}
	public void unHookReceiver() {
		logMe("unhooking the reciever");
		if (r != null) {
			unregisterReceiver(r);
		} else {
			logMe("whoops! reciever was allready null...");
		}
		r = null;
		logMe("done");
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
		 */
		KeyEvent fakeHeadsetPress =	new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
		Intent fakeHeadsetIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		
		fakeHeadsetIntent.putExtra(Intent.EXTRA_KEY_EVENT, fakeHeadsetPress);
		logMe("broadcasting ACTION_MEDIA_BUTTION intent with a KEYCODE_HEADSETHOOK code on an ACTION_DOWN action");


		//unHookReceiver();
		
		sendOrderedBroadcast(fakeHeadsetIntent, null);
		moveTaskToBack(true);		
		finish();		
	}
	

	
	/** shut off if call was not answered */
	private class CallAnswerActivityReceiver extends BroadcastReceiver {
		public CallAnswerActivityReceiver() {
			logMe("created a CallAnswerActivityReceiver for this task");
		}
		@Override
		public void onReceive(Context c, Intent i) {
			String phone_state = i.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (!phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				logMe("received "+phone_state+", time to go bye bye, thanks for playing!");
				finishHim();
			}
		} 
	}
	
	
	private void logMe(String s) {
		if (debugOn) Log.d(Hc.LOG_TAG, Hc.PRE_TAG + "CallAnswerActivity" + Hc.POST_TAG + " "+ s);
	}
}