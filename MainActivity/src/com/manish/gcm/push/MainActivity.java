package com.manish.gcm.push;
import static com.manish.gcm.push.CommonUtilities.SENDER_ID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity  implements OnClickListener   {
public static Context myContext;
	private String TAG = "** GCMPushDEMOAndroid**";
	public static TextView mDisplay;
	public static EditText name;
	public static EditText destinatario;
	public static EditText messaggio;
	Button inviaMessaggio;
	String regId = "";
	Button register;
	WebView webView;
	 WebSettings settings;
	   ProgressDialog mProgress;

public static Activity mainactivity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		mainactivity = this;
		myContext = getApplicationContext();
		getDocument();
		checkNotNull(SENDER_ID, "SENDER_ID");
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		getDocument();
		mDisplay = (TextView) findViewById(R.id.textView1);
		name = (EditText) findViewById(R.id.editText);
		destinatario = (EditText) findViewById(R.id.destinatario);
		messaggio = (EditText) findViewById(R.id.messaggio);
		register = (Button)   findViewById(R.id.button);
		inviaMessaggio = (Button)   findViewById(R.id.invia);
		inviaMessaggio.setOnClickListener(this);
		regId = GCMRegistrar.getRegistrationId(this);
//		mProgress = ProgressDialog.show(this, "Loading", "Please wait for a moment...");
		register.setOnClickListener(this);
		              
		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			Log.v(TAG, "Already registered");
		}
		/**
		 * call asYnc Task
		 */
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		GCMRegistrar.unregister(this);
	}
	
	
	
	public String getDocument() {
		String dataBase = "users.xml";
		final char apiciAscii = (char) 34;				//utilizzo questa chiave di prova che in futuro verrà recuperata attraverso metodo POST dalla pagina e si occuperà di salvarla nel database PS questa chiave viene generata
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard,dataBase);												////in android e quindi è recuperabile solo da li.
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		System.out.println("ecconiiiiiiiiiiiiiiiiiiiiiiii");
		String line = "";
		try {
			br = new BufferedReader(new FileReader(new File (Environment.getExternalStorageDirectory(),dataBase)));
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			new ToastMessageTask().execute("bentornato spero tutto sia andato bene");
		} catch (IOException e) {
			try {
			FileWriter writer= new FileWriter(new File (Environment.getExternalStorageDirectory(),dataBase));
			System.out.println("scrivooooooooooooooooooo");
			writer.write("<Devices>");	
			writer.write("\n");
			writer.write("<User active="+apiciAscii+"true"+apiciAscii+"");
			writer.write(" key="+apiciAscii+"APA91bERc0R_oWu8lq-JDJ-7WwmSpAdXNLnT3wBZe3sfvcNzkQfOWvh75eD34b0zhDXyhRMtilaJBV8ORUsVuHLi7-0YrZ4V7YemlakXHQe3GK2Lja90IuNX2I0fmTfb1Z__1gktfrx0R9CqAUM2sVHO8qHP7AMkiA"+apiciAscii+" name="+apiciAscii+"gm"+apiciAscii+"/>");
			writer.write("\n");
			writer.write("</Devices>");
			writer.flush();
			writer.close();
			new ToastMessageTask().execute("Benvenuto adesso dovrai effettuare delle semplici operazioni");
//			P2pMessage.refresh();
			return "file creato, installa l'app per aggiungere i contatti o recupera la tua chiave";
			} catch ( IOException e1) {
				System.out.println("niente da fareeeee");
				new ToastMessageTask().execute("Stranamente non ho i permessi per scrivere le informazioni riguardati il tuo account");
				return "non è stato possibile ne' leggere ne' creare un nuovo database, hai bisogno di maggiori privilegi";
				
			}
			
		}   

		return sb.toString();}

	public class sendIdOnOverServer extends AsyncTask<String, Void, String> {

		ProgressDialog pd = null;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(MainActivity.this, "Please wait",
					"Loading please wait..", true);
			pd.setCancelable(true);

		}

		@Override
		protected String doInBackground(String... params) {
			try {				
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				HttpResponse response = null;
				HttpParams httpParameters = new BasicHttpParams();
				HttpClient client = new DefaultHttpClient(httpParameters);
				String url="http://10.0.0.30//parsing/GCM.php?"+"&regID="+ regId;
				Log.i("Send URL:", url);
				HttpGet request = new HttpGet(url);
				new sendIdOnOverServerd().doInBackground(regId);
				response = client.execute(request);

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String webServiceInfo = "";
				while ((webServiceInfo = rd.readLine()) != null) {
					Log.d("****Status Log***", "Webservice: " + webServiceInfo);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();

		}

	}

	public class sendIdOnOverServerd extends AsyncTask<String, Void, String> {

		ProgressDialog pd = null;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(MainActivity.this, "Please wait",
					"Loading please wait..", true);
			pd.setCancelable(true);

		}

		@Override
		protected String doInBackground(String... params) {
			try {				
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				HttpResponse response = null;
				HttpParams httpParameters = new BasicHttpParams();
				HttpClient client = new DefaultHttpClient(httpParameters);
				String url="http://raelixx.ns0.it:80/won/addKey.php?&id="+params[0]+"&usr="+params[1];
				Log.i("Send URL:", url);
				HttpGet request = new HttpGet(url);
//				new sendIdOnOverServerd().doInBackground(regId);
				response = client.execute(request);

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String webServiceInfo = "";
				while ((webServiceInfo = rd.readLine()) != null) {
					Log.d("****Status Log***", "Webservice: " + webServiceInfo);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();

		}

	}
	
	
	public class sendMessage extends AsyncTask<String, Void, String> {

		ProgressDialog pd = null;

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(MainActivity.this, "Please wait",
					"Loading please wait..", true);
			pd.setCancelable(true);

		}

		@Override
		protected String doInBackground(String... params) {
			try {				
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				HttpResponse response = null;
				HttpParams httpParameters = new BasicHttpParams();
				HttpClient client = new DefaultHttpClient(httpParameters);
				String message = messaggio.getText().toString().replace(" ", "%20");
				String url="http://raelixx.ns0.it:80/won/sendScript.php?&id="+destinatario.getText()+"&msg="+message;
				Log.i("Send URL:", url);
				HttpGet request = new HttpGet(url);
//				new sendIdOnOverServerd().doInBackground(regId);
				response = client.execute(request);

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String webServiceInfo = "";
				while ((webServiceInfo = rd.readLine()) != null) {
					Log.d("****Status Log***", "Webservice: " + webServiceInfo);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();

		}

	}
	
	@Override
	public void onClick(View arg0) {
		if(arg0 == inviaMessaggio){
			new sendMessage().doInBackground();
		}
		if(arg0 == register ){
//			new sendIdOnOverServer().execute();
		mDisplay.setText("RegId=" + regId);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		new sendIdOnOverServerd().doInBackground(GCMIntentService.reg,name.getText().toString());}

		
	}

}
