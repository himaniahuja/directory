package edu.cmu.sv;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CMUSVPersonActivity extends Activity {

		String phone_number = null;
	
	  @SuppressWarnings("unchecked")
	@Override
		public void onCreate(Bundle savedInstanceState) {
	    	
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.profile);
		  
		  	// http://stackoverflow.com/questions/6343166/android-os-networkonmainthreadexception
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		  
		 
		  try {
			//JSONObject jsonObjPerson = new JSONObject(getIntent().getStringExtra("person"));
			
			HashMap<String, String> person = new HashMap<String, String>();
			person = (HashMap<String, String>) getIntent().getSerializableExtra("person");
			
			// GET NAME 
			TextView text = new TextView(this); 

		    text = (TextView)findViewById(R.id.name_on_profile);
		    
		    text.setText(person.get("human_name"));
		    
		    // GET PHONE NUMBER AND CALL
		    TextView telephone = new TextView(this); 
		    
		    if (person.containsKey("Mobile"))
		    	phone_number = person.get("Mobile");
		    	
		    else if(person.containsKey("Home"))
		    	phone_number = person.get("Mobile");
		    else if(person.containsKey("Work"))
		    	phone_number = person.get("Work");
		    else if(person.containsKey("Google Voice"))
		    	phone_number = person.get("Google Voice");
		    else
		    	phone_number = "Not Available";
		    
		    telephone = (TextView)findViewById(R.id.telephone); 
		    telephone.setText(phone_number);
		    
		    telephone.setOnClickListener(new View.OnClickListener() {
		    	public void onClick(View view) {	    	         
	    			   Intent intent = new Intent(CMUSVPersonActivity.this, PhoneCallActivity.class);
	    			   Bundle b1 = new Bundle();
	    			   b1.putString("phone_number", phone_number);
	    		   	   intent.putExtras(b1);
	    		   	   startActivityForResult(intent, 0);
	    	    }
	    	  });
		    
		    // On click for telephone button
		    
		    final Button phone_button = (Button) findViewById(R.id.button_phone);
	    	phone_button.setOnClickListener(new View.OnClickListener() {
	              public void onClick(View v) {
	            	  Intent intent = new Intent(CMUSVPersonActivity.this, PhoneCallActivity.class);
	    			   Bundle b4 = new Bundle();
	    			   b4.putString("phone_number", phone_number);
	    		   	   intent.putExtras(b4);
	    		   	   startActivityForResult(intent, 0);
	              }
	        });
	    	
	    	// SMS button
	    	  final Button sms_button = (Button) findViewById(R.id.button_sms);
	    	  final String sms_number = phone_number;
	    	  sms_button.setOnClickListener(new View.OnClickListener() {
	              public void onClick(View v) {
	            	  Intent intent = new Intent(CMUSVPersonActivity.this, SmsActivity.class);
	    			   Bundle b3 = new Bundle();
	    			   b3.putString("sms", sms_number);
	    		   	   intent.putExtras(b3);
	    		   	   startActivityForResult(intent, 0);
	              }
	        });
			  
	    	// SMS text
			  TextView sms_text = new TextView(this); 
			    
			  sms_text = (TextView)findViewById(R.id.sms_text); 
			  sms_text.setText(phone_number);
			    
			  sms_text.setOnClickListener(new View.OnClickListener() {
				  public void onClick(View view) {	    	         
					  Intent intent = new Intent(CMUSVPersonActivity.this, SmsActivity.class);
					  Bundle b6 = new Bundle();
					  b6.putString("sms", phone_number);
					  intent.putExtras(b6);
					  startActivityForResult(intent, 0);
				  }
		      });
			  
			// GET EMAIL ID AND SEND EMAIL
		    TextView email = new TextView(this); 
		    final String final_Email = person.get("email");
		    String email_id = final_Email.split("@")[0];

		    email = (TextView)findViewById(R.id.email_person); 
		    email.setText(email_id);
		    
		    email.setOnClickListener(new View.OnClickListener() {
		    	public void onClick(View view) {
	    	         
	    			   Intent intent = new Intent(CMUSVPersonActivity.this, EmailActivity.class);
	    			   Bundle b2 = new Bundle();
	    			   b2.putString("email_id", final_Email);
	    		   	   intent.putExtras(b2);
	    		   	   startActivityForResult(intent, 0);
	    	    }
	    	  });
		    
		    // On click for email button
		    
		     final Button email_button = (Button) findViewById(R.id.button_email);
	    	 
	    	 email_button.setOnClickListener(new View.OnClickListener() {
	              public void onClick(View v) {
	            	  Intent intent = new Intent(CMUSVPersonActivity.this, EmailActivity.class);
	    			   Bundle b5 = new Bundle();
	    			   b5.putString("email_id", final_Email);
	    		   	   intent.putExtras(b5);
	    		   	   startActivityForResult(intent, 0);
	              }
	          });

	    	  
		   // GET IMAGE
		    Object content = null;
		    
		    // types of URL:
		    
		    // http://s3.amazonaws.com/cmusv-rails-production/people/photo/603/profile/AlexanderKurilin.jpg
		    // /images/staff/AbeIshihara.jpg
		    // http://cmusv-rails-production.s3.amazonaws.com/REV_a7e4c77154cc1546c4baa9e8d56927e255bc696b/images/students/2012/SE/AJCave.jpg
		    // http://cmusv-rails-production.s3.amazonaws.com/REV_a7e4c77154cc1546c4baa9e8d56927e255bc696b/images/staff/AbeIshihara.jpg
		    
		    String input_uri = person.get("image_uri");
		    String final_uri;
		    if (input_uri.contains("http"))
		    	final_uri = input_uri;
		    else 
		    	final_uri = "http://cmusv-rails-production.s3.amazonaws.com/REV_a7e4c77154cc1546c4baa9e8d56927e255bc696b" + input_uri;
		    
		    URL url = new URL(final_uri);
		    content = url.getContent();
			
		    InputStream is = (InputStream)content;
		    Drawable image = Drawable.createFromStream(is, "src");
			
		    ImageView imgView = new ImageView(this);
            imgView = (ImageView)findViewById(R.id.image_view);
            imgView.setImageDrawable(image);
		  	
		    } catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		  
		  	  catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
		  		}
	  }

}	