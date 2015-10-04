package com.manish.gcm.push;

import static com.manish.gcm.push.CommonUtilities.SENDER_ID;

import java.util.StringTokenizer;

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
	 * Si supporrà che esisteranno diversi tipi di ack : 
	 * 1-Notifica l'avvenuta registrazione e lo inserisce nel file xml,					ack &1 &usr &idkey
	 * 2-notifica l'avvenuta ricezione di un sms contenente il numero del mittente ,    ack &2 &mittenteNumero &messaggio
	 * 3-contiene il primo ack dal destinatario, 										ack &3 &destinatario
	 * 4-contiene il secondo ed ultimo ack dal destinatario  							ack &4 &destinatario
	 */
	private static void generateNotification(Context context, String message) {

		if(message.startsWith("ack")){
			StringTokenizer st = new StringTokenizer(message,"&");
			String first = st.nextToken(); 
			String second = st.nextToken(); 

			int value = Integer.parseInt(second);
			switch(value){
			case 1:
				String third = st.nextToken(); 
				String fourth = st.nextToken(); 
				String ack = String.valueOf(first);
				String user = String.valueOf(third);
				String key = String.valueOf(fourth);
				new AlertMessageTask().execute("ack: "+value+" user: "+user+" key: "+key);
				break;
			case 2:
				third = st.nextToken(); 
				fourth = st.nextToken(); 
				ack = String.valueOf(first);
				String tel = String.valueOf(third);
				message = String.valueOf(fourth);
				generaNotifica(context,"num: "+tel+":"+message);//qui bisogna interpellare il database e andare ad inserire il messaggio sul id con numero di telefono = telefono
				break;
			case 3:
				third = st.nextToken(); 
				fourth = st.nextToken(); 
				ack = String.valueOf(first);
				String numero = String.valueOf(third);
				generaNotifica(context,"num: "+numero+":"+"messaggio spedito");//qui bisogna aggiungere una spunta poichè il messaggio spedito al "numero" è stato spedito ma non ancora ricevuto
				break;
			case 4:
				third = st.nextToken(); 
				fourth = st.nextToken(); 
				ack = String.valueOf(first);
				numero = String.valueOf(third);
				generaNotifica(context,"num: "+numero+":"+"messaggio spedito");//stessa cosa xò bisogna aggiungere due spunte poichè il messaggio spedito al "numero" è stato anche ricevuto e notificato
				break;
			}
		}


	}


	private static void generaNotifica(Context context,String message){
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = message;
		long when = System.currentTimeMillis();
		CharSequence contentTitle = ""+message; 
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, tickerText, when);
		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.putExtra("messaggio", message);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		//		MainActivity.mDisplay.setText(message);
		notification.setLatestEventInfo(context, contentTitle, "", pendingIntent);
		notification.flags|=Notification.FLAG_ONLY_ALERT_ONCE|Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;
		notification.vibrate=new long[] {100L, 100L, 200L, 500L};
		notificationManager.notify(1, notification);
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		wl.acquire();
	}
}
