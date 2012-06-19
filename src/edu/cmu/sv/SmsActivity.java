package edu.cmu.sv;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class SmsActivity extends Activity {
	
	
	  @Override
      public void onCreate(Bundle savedInstanceState) {
         
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.main);
	      
	      // the destination number
          String number = getIntent().getStringExtra("sms");
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number)));
          finish();
      }


}
