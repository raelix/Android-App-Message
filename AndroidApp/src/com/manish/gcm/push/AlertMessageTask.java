package com.manish.gcm.push;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;


public class AlertMessageTask extends AsyncTask<String, String, String> {
	    String toastMessage;

	    @Override
	    protected String doInBackground(String... params) {
	        toastMessage = params[0];
	        return toastMessage;
	    }

	    protected void OnProgressUpdate(String... values) { 
	        super.onProgressUpdate(values);
	    }
	   // This is executed in the context of the main GUI thread
	    @Override
		protected void onPostExecute(String result){
	    	new AlertDialog.Builder(MainActivity.my)
	    	.setTitle("Information")
	    	.setMessage(result)
	    	.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    	    public void onClick(DialogInterface dialog, int which) { 
	    	        // continue with delete
	    	    }
	    	 })
	    	
	    	 .show();
	    }}


		