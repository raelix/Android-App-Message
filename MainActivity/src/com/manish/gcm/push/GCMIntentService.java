package com.manish.gcm.push;

import static com.manish.gcm.push.CommonUtilities.SENDER_ID;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
public static String reg ;
	public GCMIntentService() {
		super(SENDER_ID);
	}

	private static final String TAG = "===GCMIntentService===";

	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		reg = registrationId;
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.i(TAG, "unregistered = " + arg1);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "new message= ");
		String message = intent.getExtras().getString("message");
		generateNotification(context, message);
	}

	@Override
	protected void onError(Context arg0, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
CharSequence tickerText = message;//intent.getStringExtra("me");              // ticker-text
		long when = System.currentTimeMillis();         // notification time
		CharSequence contentTitle = ""+message; //intent.getStringExtra("me");  // message title

		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, tickerText, when);
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//		MainActivity.mDisplay.setText(message);
		notification.setLatestEventInfo(context, contentTitle, "", pendingIntent);
		notification.flags|=Notification.FLAG_INSISTENT|Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;
		notification.vibrate=new long[] {100L, 100L, 200L, 500L};
		notificationManager.notify(1, notification);
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		wl.acquire();
	}
}
