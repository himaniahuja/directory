package edu.cmu.sv;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneCallActivity extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String ph_number= getIntent().getStringExtra("phone_number");
        call(ph_number);
    }
	private void call(String ph) {
	    try {
	    	String num = "tel:" + ph;
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse(num));
	        startActivity(callIntent);
	        finish();
	        
	    
	    } catch (ActivityNotFoundException e) {
	        Log.e("CMUSV directory calling", "Call failed", e);
	    }
	}
	
}
