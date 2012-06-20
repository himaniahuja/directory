
package edu.cmu.sv;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SmsActivity extends ListActivity {
	
	
	  ListView listView = null;
	  ListAdapter adapter = null;
	  @Override
      public void onCreate(Bundle savedInstanceState) {
         
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.main);
	      
		  setContentView(R.layout.phone_list);
	        
		  @SuppressWarnings("unchecked")
		  HashMap<String,String> phone_list = (HashMap<String, String>) getIntent().getSerializableExtra("sms_list");
        
        
		  if (phone_list.isEmpty()){
				TextView text = new TextView(this);
	        	// some parameters like text size, color, and font are set here
	        	setEmptyView(text);
		  }
			
		else {
			
			ArrayList<String> ph_list = new ArrayList<String>();
			
			if(phone_list.containsKey("Mobile"))
				ph_list.add("Cellphone   " + phone_list.get("Mobile"));
			if(phone_list.containsKey("Home"))
 	            ph_list.add("Home        " + phone_list.get("Home"));
			if(phone_list.containsKey("Work"))
  	            ph_list.add("Work        " + phone_list.get("Work"));
			if(phone_list.containsKey("Google Voice"))
  	            ph_list.add("Google Voice" + phone_list.get("Google Voice"));
			
			listView = getListView();
	       
			adapter = new ArrayAdapter<String>(this,R.layout.phone_list_item , ph_list);
			
			listView.setAdapter(adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
			   public void onItemClick(AdapterView<?> parent, View view,
				       int position, long id) {
					   
				   	String itemText = getListView().getItemAtPosition(position).toString();
				   	System.out.println(itemText.substring(12));
				   	sms(itemText.substring(12));
				   	
				    }
			});
		}
    }		
	  
		  

	private void sms(String ph) {
		
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ph)));
			finish();
    	
	}

	private void setEmptyView(TextView text) {
		// TODO Auto-generated method stub
		
	}
}
