package edu.cmu.sv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class EmailActivity extends Activity {
	
		 /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        String email_id = getIntent().getStringExtra("email_id");
	        email(email_id);
	       
	    }
		private void email(String email) {
				String[] recipients = new String[]{email, "",};
				
		        Intent emailIntent = new Intent(Intent.ACTION_SEND);
		        emailIntent.setType("text/plain");
		        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
		        finish();
		        
		        try {
		            
		        	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
		       
		        } catch (android.content.ActivityNotFoundException ex) {
		            
		        	Toast.makeText(EmailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		       
		        }
		}
		
}
