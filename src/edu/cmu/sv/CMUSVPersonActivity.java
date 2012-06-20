package edu.cmu.sv;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CMUSVPersonActivity extends Activity {

		String phone_number = null;
		ListAdapter adapter = null;
		ListView lView = null;
		HashMap<String,String> phone_list = null;
		HashMap<String, String> location_info = null;
	
	  @SuppressWarnings("unchecked")
	@Override
		public void onCreate(Bundle savedInstanceState) {
	    	
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.profile);
		  
		  	// http://stackoverflow.com/questions/6343166/android-os-networkonmainthreadexception
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		  
		 
		  try {
			
			HashMap<String, String> person = (HashMap<String, String>) getIntent().getSerializableExtra("person");
			
			
		    // Get Image of person
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
		  	
		   
			// Get Name Of Person
			TextView text = new TextView(this); 

		    text = (TextView)findViewById(R.id.name_on_profile);
		    
		    text.setText(person.get("human_name"));
		    
		    
		    // Get the phone numbers and pass to phone activity
		    
		    phone_list = new HashMap<String,String>();
		    
		    if (person.containsKey("Mobile")){
		    	phone_list.put("Mobile", person.get("Mobile"));
		    }
		    
		    if (person.containsKey("Home")){
		    	phone_list.put("Home", person.get("Home"));
		    }  
		    
		    if (person.containsKey("Work")){
		    	phone_list.put("Work", person.get("Work"));
		    }
		    
		    if (person.containsKey("Google Voice")){
		    	phone_list.put("Google Voice", person.get("Google Voice"));
		    }
		    
		    // On click for telephone button
		    
		    final Button phone_button = (Button) findViewById(R.id.button_phone);
	    	phone_button.setOnClickListener(new View.OnClickListener() {
	              public void onClick(View v) {
	            	  Intent intent = new Intent(CMUSVPersonActivity.this, PhoneCallActivity.class);
	    			   Bundle b4 = new Bundle();
	    			   b4.putSerializable("phone_list", phone_list);
	    		   	   intent.putExtras(b4);
	    		   	   startActivityForResult(intent, 0);
	              }
	        });
		   
	   
		    // SMS button
		    final Button sms_button = (Button) findViewById(R.id.button_sms);
		    final HashMap<String, String> sms_list = phone_list;
		    sms_button.setOnClickListener(new View.OnClickListener() {
		          public void onClick(View v) {
		               Intent intent = new Intent(CMUSVPersonActivity.this, SmsActivity.class);
		    		   Bundle b3 = new Bundle();
		    		   b3.putSerializable("sms_list", sms_list);
		   		   	   intent.putExtras(b3);
		   		   	   startActivityForResult(intent, 0);
		              }
		        });	
		    	
			    
			// On click for email button
		    final String final_Email = person.get("email");
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
	
		    // on click location button
		    String email = person.get("email");
		    String email_id = person.get("email").split("@")[0];
		    
		    Logger.getAnonymousLogger().info(email);
		    Logger.getAnonymousLogger().info(email_id);
		    String locationData = CMUSVUtils.readPeopleData("http://cmusvdirectory.appspot.com/Location.json?limit=1&email=" + email_id);
		    Logger.getAnonymousLogger().info(locationData);
	    	JSONArray locationJson = null;
	    	
    	
    		locationJson = new JSONArray(locationData);
    		if (locationJson.length() !=0){
    			
    			JSONObject childJSONObject = locationJson.getJSONObject(0);
    		
	    		Double lat = childJSONObject.getDouble("lat");
	    		Double lon = childJSONObject.getDouble("long");
	    		
	    		//Json returns "DateTime": "2012-06-20 16:39:43.720440"
	    		String datetime = childJSONObject.getString("DateTime");
	    		
	    		location_info = new HashMap<String, String> ();
	    		location_info.put("lat", String.valueOf(lat));
	    		location_info.put("lon", String.valueOf(lon));
	    		location_info.put("email", email);
	    		location_info.put("datetime", datetime);
	    		location_info.put("human_name", person.get("human_name"));
	    		
	    		// On click for location button
			}
    			
		    final Button button_pin = (Button) findViewById(R.id.button_pin);
	    	button_pin.setOnClickListener(new View.OnClickListener() {
	              public void onClick(View v) {
	            	  Intent intent = new Intent(CMUSVPersonActivity.this, ShowUserLocation.class);
	    			  if(location_info !=null) {
		            	  Bundle b10 = new Bundle();
		    			  b10.putSerializable("location_info", location_info);
		    		   	  intent.putExtras(b10);
	    			  }
	    			  startActivityForResult(intent, 0);
	    			  
	              }
	        });
    	
    
    
	  } catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  
  	  catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
  		}
  
  	  catch (JSONException e2) {
  		e2.printStackTrace();
  		
  	  }
  }
}	


/*
 // Get the phone numbers and pass to phone activity
 
 ArrayList<HashMap<String,String>> phone_list = new ArrayList<HashMap<String,String>>();
 
 if (person.containsKey("Mobile")){
 	HashMap<String,String> mobile = new HashMap<String,String>();
 	mobile.put("label", "Mobile");
 	mobile.put("detail", person.get("Mobile"));
 	phone_list.add(mobile);
 }
 if (person.containsKey("Home")){
 	HashMap<String,String> home = new HashMap<String,String>();
 	home.put("label", "Home");
 	home.put("detail", person.get("Home"));
 	phone_list.add(home);
 }
 if (person.containsKey("Work")){
 	HashMap<String,String> work = new HashMap<String,String>();
 	work.put("label", "Work");
 	work.put("detail", person.get("Work"));
 	phone_list.add(work);
 }
 if (person.containsKey("Google Voice")){
 	HashMap<String,String> gVoice = new HashMap<String,String>();
 	gVoice.put("label", "Google Voice");
 	gVoice.put("detail", person.get("Google Voice"));
 	phone_list.add(gVoice);
 }
 
 */
 
/*adapter = new SimpleAdapter(this,phone_list, R.layout.phone_list_item, 
		  							new String[] {"label","detail", "",""},
		  							new int[] {R.id.one_item,R.id.detail_item, R.id.button_phone_list,R.id.button_sms_list,});
setListAdapter(adapter); 
lView = getListView();

lView.setOnItemClickListener(new OnItemClickListener() {
	   public void onItemClick(AdapterView<?> parent, View view,
 	       int position, long id) {
		   
		   switch(view.getId()){
           case R.id.one_item:
           case R.id.detail_item:
           case R.id.button_phone_list:
           case R.id.button_sms_list:
         	  HashMap<String, String> number = (HashMap<String, String>) lView.getItemAtPosition(position);
	    		   phone_number = number.get("detail");
	    		   Intent intent = new Intent(CMUSVPersonActivity.this, PhoneCallActivity.class);
 			   Bundle b1 = new Bundle();
 			   b1.putString("phone_number", phone_number);
 		   	   intent.putExtras(b1);
 		   	   startActivityForResult(intent, 0);
               break;  
        }     
		   
 		  
	}
});
	*/


/* // GET PHONE NUMBER AND CALL
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
	  */
	
	
	// GET EMAIL ID AND SEND EMAIL
	
/* TextView email = new TextView(this); 
 final String final_Email = person.get("email");
 String email_id = final_Email.split("@")[0];
*/
/* email = (TextView)findViewById(R.id.email_person); 
 email.setText(email_id);
 
 email.setOnClickListener(new View.OnClickListener() {
 	public void onClick(View view) {
	         
			   Intent intent = new Intent(CMUSVPersonActivity.this, EmailActivity.class);
			   Bundle b2 = new Bundle();
			   b2.putString("email_id", final_Email);
		   	   intent.putExtras(b2);
		   	   startActivityForResult(intent, 0);
	    }
	  });*/
	

