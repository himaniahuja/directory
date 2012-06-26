package edu.cmu.sv;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PhoneCallActivity extends ListActivity {
	 /** Called when the activity is first created. */
	ListView listView = null;
	ListAdapter adapter = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_list);
        
        @SuppressWarnings("unchecked")
		HashMap<String,String> phone_list = (HashMap<String, String>) getIntent().getSerializableExtra("phone_list");
        
        
		if (phone_list.isEmpty()){
			TextView text = new TextView(this);
        	// some parameters like text size, color, and font are set here
        	setEmptyView(text);
		}
		
		else {
			
			//Set<String> set = phone_list.keySet();
			//set.iterator();
			ArrayList<String> ph_list = new ArrayList<String>();
			
			if(phone_list.containsKey("Mobile"))
				ph_list.add("Cellphone   " + phone_list.get("Mobile"));
			if(phone_list.containsKey("Home"))
 	            ph_list.add("Home        " + phone_list.get("Home"));
			if(phone_list.containsKey("Work"))
  	            ph_list.add("Work        " + phone_list.get("Work"));
			if(phone_list.containsKey("Google Voice"))
  	            ph_list.add("Google Voice" + phone_list.get("Google Voice"));
			
			/*//String[] ph_numbers =  new String[phone_list.size()];
        
        	for (int l=0;l<ph_numbers.length;l++){
        		if(phone_list.containsKey("Mobile"))
     	        	ph_numbers[0] = "Cellphone   " + phone_list.get("Mobile");
     	        if(phone_list.containsKey("Home"))
     	            ph_numbers[0] = "Home        " + phone_list.get("Home");
     	        if(phone_list.containsKey("Work"))
     	            ph_numbers[0] = "Work        " + phone_list.get("Work");
     	        if(phone_list.containsKey("Google Voice"))
     	            ph_numbers[0] = "Google Voice" + phone_list.get("Google Voice");
        		
        	}*/
	       
	        
	        listView = getListView();
	       
			adapter = new ArrayAdapter<String>(this,R.layout.phone_list_item , ph_list);
			
			listView.setAdapter(adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
			   public void onItemClick(AdapterView<?> parent, View view,
				       int position, long id) {
					   
				   	String itemText = getListView().getItemAtPosition(position).toString();
				   	System.out.println(itemText.substring(12));
				   	call(itemText.substring(12));
				   	
				    }
			});
        }
    }
	private void setEmptyView(TextView text) {
		// TODO Auto-generated method stub
		
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
