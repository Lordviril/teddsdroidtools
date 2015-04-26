# About #
Welcome!

This is an Android application I wrote earlier in the year to "fix" a few issues I was having with my new Droid and to have some fun tinkering around with the Android platform.  I love my Droid but I don't like having to use the touch screen to answer calls.  I like having a physical button to answer my phone with (go figure). Messing with the slider isn't always feasible... especially when my wife calls me while I'm on my way home from work in bumper-to-bumper interstate traffic.

I had originally hoped to be able to spend more time on it (still do) but work and life are keeping me busy for the time being.  In the meantime, the code is here for those who are interested in that sort of thing and all are welcome to do with it as they please. I consider this code and app to be "Beerware". It is free and open source. If you like it and we ever meet up at a bar, buy me a beer!

Of course, if you do end up learning from or reusing this code, honorable mentions are always appreciated. =)

# Features #

  * Answer Incoming Calls
Answer phone calls by pressing the camera button, track ball press or an on screen long press button.  Approximately 3 seconds after the phone starts ringing the app starts running and you will be able to answer with the phone's physical camera button.

A message will also be displayed on the screen along with a button that will return you to the stock phone app since this prompt does lock you out of the regular call screen.  You can also use the back button to exit back to the stock phone app.

  * Ignore / Reject Incoming Calls
Besides being able to answer a call, you can also reject incoming phone calls by long pressing button on the touch screen.

  * In-Call Screen Guard
I wrote this for my wife whose hair messes with the proximity sensor that shuts off your screen during calls causing accidental hangups, mutes, etc.  About 3 seconds after answering a call (or starting a new one) a screen will pop up over the normal in call screen which has a button in the center you can long press to exit out with.

When the screen shuts off the screen guard restarts (provided you are still on a call).

# Known Issues #
  * Does not work on Android 2.3
Yep, sure doesn't.  Google yanked the ability to use the permission that allows a 3rd party application like this one to change the phones' state (e.g. answer or reject an incoming call).  See this StackOverflow posting:

http://stackoverflow.com/questions/4715250/android-how-to-grant-modify-phone-state-permission-for-apps-ran-on-gingerbread

  * Damn! This thing is butt ugly!
This is probably the biggest complaint I've received about the app.  At some point I'll con one of my buddies with talent in the visual arts to whip up some snazzy looking icons. In the meantime, keep your phone away from any mirrors when answering calls with this app.

  * Answering with the trackball can cause ANR/Freeze up errors
I've received a small number error reports via the Android Market error reporting tool that point to the trackball code freaking out... especially with debug logging turned on. I do plan on fixing this when I get some time but it's happening so rarely that the fix is on the back burner.

  * Start up delay
This is actually intentional (and kind of has to be this way). The reason for the delay is that the stock phone app must finish starting up before the answer screen can start.  Otherwise, if my answer screen starts before the stock answer screen, the stock one ends up on top forcing mine into the background where it cannot react to the camera key press event.

I can probably optimize this a bit by starting it sooner, adding some periodic checks to see if the answer screen is currently active and bringing it to the front if it is not. My first attempts to do this caused more problems than they solved so I dropped it ;).  I ended up coming to the conclusion that, for the most part, by the time I fished my phone out of my pocket the answer screen was up anyway.

Regardless, letting the user set the initial delay is something I want to add.

  * Does not circumvent the security pattern feature
If you have a security pattern set you'll still have to enter that in first before you can answer the call (at least this was the behavior I observed as of Android 2.1). This pretty much defeats the purpose of the call answer/reject functionality so you probably won't find those features very handy.  The screen guard may still be useful.

  * The screen guard exit button takes too long
This is because it is long press button, not a regular tap button.  The long press is to guard against your cheek closing the screen guard and then hitting the mute or end button in the stock phone app.

You can also use the back button to get out of the screen guard.

# Programmatically Answering/Rejecting Calls #
WARNING: This no longer works (for the TelephonyService at least) as of Android 2.3.

If you're interested in seeing how to tap into the underlying telephony services to answer or reject an incoming call (something that isn't available in the stock Android SDK) I'd highly recommend starting with this codetastrophe blog about accessing the hidden system services:

http://blog.codetastrophe.com/2008/12/accessing-hidden-system-service-apis-in.html

Once you've done that, this source file will be a good place to start to see how I'm accessing the telephony services and hooking it up to the physical camera button and on screen buttons:

http://code.google.com/p/teddsdroidtools/source/browse/trunk/teddsdroidtools/src/tss/droidtools/phone/CallAnswerActivity.java

# My Thanks #
I'd like to say thank you to the developers of the MyLock project for posting their code and collaborating with me on this.

MyLock - http://code.google.com/p/mylockforandroid/

I'd also like to thank the AutoAnswer project for blazing the trail (at least the trail I followed) on answering incoming calls on an Android phone.

AutoAnswer - http://code.google.com/p/auto-answer/