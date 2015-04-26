# About #
My original homepage before updating it starting in the fall of '10.

Welcome!

These are some Android applications that I am working on for various reasons, primarily just exploring the Android platform for fun, and want to share with the world. I don't have any grand plans for these apps like getting rich and famous off of them.  I'm just a programmer and I like to tinker with my phone.

Hopefully it will do something useful for you that makes your life a little easier. Please do let me know if the app behaves badly or you have an enhancement idea.  My spare time is limited but I do intended to work on this for a while.

I consider this app to be "Beerware". It is free and open source.  If you like using the app and we ever meet up at a bar, buy me a beer : )

# Current Features #
It now has two features:

  * Call Answer
Answer phone calls by pressing the camera button, track ball press or an on screen long press button.  Handy for Motorola Droid owners who want to answer phone calls without having to use the touch screen slider to answer incoming calls.  (Probably not as useful for those who have a physical button to press to answer).

Approximately 3 seconds after the phone starts ringing the app starts running and you will be able to answer with the phone's physical camera button.  A message will also be displayed on the screen along with a button that will return you to the phone's regular incoming call screen (the prompt does lock you out of the regular call screen until you answer or return out of it).

Credit goes to the MyLock and AutoAnswer projects for figuring out the technique used underneath the hood and making their code available to guys like me.  This is just my take on the functionality they hammered out first.  Please check out their projects here:

MyLock - http://code.google.com/p/mylockforandroid/

AutoAnswer - http://code.google.com/p/auto-answer/

  * Call Ignore / Reject
Reject incoming phone calls by long pressing button on the touch screen.

Credit goes to the guys at codetastrophe for publishing this article about accessing the underlying Android service processes:

http://blog.codetastrophe.com/2008/12/accessing-hidden-system-service-apis-in.html

  * In-Call Screen Guard (new in beta4)
I wrote this for my wife whose hair messes with the proximity sensor that shuts off your screen during calls causing accidental hangups, mutes, etc.  About 3 seconds after answering a call (or starting a new one) a screen will pop up over the normal in call screen which has a button in the center you can long press to exit out with.  When the screen goes dark, the screen guard will restart as long as you are still in a call.

**Security Pattern**
For those of you who use the phones security pattern to unlock feature, if your phone was asleep (and thus requiring a security pattern to get back in) the security pattern screen will be displayed first before the screen guard. At least that's what it does on my Droid running Android 2.1 =)

### Beta3 Notice ###
The underlying techniques used to answer and reject calls have changed in the beta3 release.

Answering calls in the first two beta releases relied on a built hook within the Android phone application that enabled users to answer calls via a button on their headset, what I've been referring to as the HEADSETHOOK technique.  While it works, some media applications that are coded to use that feature effectively prevent this technique from working.  So it was not an ideal solution, but it worked (especially for me since I didn't have any of those media apps installed ;)

"Obliterating" (rejecting) calls, added in beta2, utilized the same SDK feature that the task killer applications use to restart the telephony and phone applications.  This works great but it a) prevented the call from being recorded in the call log and b) temporarily turned your cell service off while those apps restarted.  So it is kinda like killing a fish with a sledgehammer. Messy.  Effective.  And fun!  Those restarts actually happen quite fast so it was't too big a deal but it was still not the greatest solution.

In this beta3 release, the app now calls into the telephony process to answer and reject the calls in pretty much the same manner that the regular Android phone application uses. So far it has worked great for me in testing on my Droid and in the emulators. So I'm comfortable releasing the change into the market as both features behave better this way with (hopefully) less interference from outside apps and negative impacts on the users experience.

The downside to doing this is Google absolutely does not support regular old app developers like me doing this and future releases could break this apps functionality.  If/when that ever happens this app will have to fragment to support the differences in the versions of the OS.  Hopefully, they'll add the ability to answer and reject calls as a part of their supported SDK so guys like me don't have to "find a way" to make it work.

Curious developers interested in seeing how this works are encouraged to check out the source code.  This source file will be a good place to start:

http://code.google.com/p/teddsdroidtools/source/browse/tags/teddsdroidtools/beta3/src/tss/droidtools/phone/CallAnswerActivity.java

Please do email teddsdroidtools@gmail.com if the app doesn't work, FC's, freaks out or generally behaves badly for you and let me know about it (I'll probably ask you to install Log Collector so you can email me logs:)

# Release History #
**beta4.1** - based on user feed back, I updated verbiage on in-call screen to better indicate it requires a long press and removed unnecessary finish() call in its long click listener left over from development to speed up its response time.

**beta4** - Added in-call screen guard (for Gina :).  Added AIDL call to silence the ringer when answering calls based on user feedback.

**beta3** - Switch to AIDL approach for answering and rejecting calls. Config and answer screen updates.

**beta2** - Added trackball press and on screen answer buttons. Added call reject feature using restart package technique. Switched to service based start up of the answer activity.

**beta1** - Initial release. Answer calls with camera button using the "headsethook" technique.

# Upcoming Features #
Things that I want to add in the days to come:

  * Didn't Work
This will be a feature/app/library that will enable users to email logging to the developer.  I'd really like to make this something that's easy to integrate into any Android app so other developers can use it too.

  * I Was Here
A little app that is basically a button to push that will store the date and time, your current location and the ability to add notes to stored locations.  Can be used for manually noting when you arrived/left at a location (like work) or when you started/stopped doing something.  I'm just weird and would like to have something like that.

  * Notepad
Just an implementation of the Android notepad tutorial with some of the bugs worked out that I use for jotting down quick notes (where I parked, food orders, etc).