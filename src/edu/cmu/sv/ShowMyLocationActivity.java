package edu.cmu.sv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ShowMyLocationActivity extends MapActivity {

	double currentLatitude;
	double currentLongitude;
	Location currentLocation;
	TextView addressText;
	MapController mapController;
	MapView mapview;
	LocationManager locationManager;
	LocationListener locationListener;
	GeoPoint geopoint;


	class MapOverlay extends com.google.android.maps.Overlay
	{
	    @Override
	    public boolean draw(Canvas canvas, MapView mapView, 
	    boolean shadow, long when) 
	    {
	        super.draw(canvas, mapView, shadow);                   

	        //---translate the GeoPoint to screen pixels---
	        Point screenPts = new Point();
	        mapView.getProjection().toPixels(geopoint, screenPts);

	        //---add the marker---
	        Bitmap bmp = BitmapFactory.decodeResource(
	            getResources(), R.drawable.pin3);            
	        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
	        return true;
	    }
	} 
	
	public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.location);
    	
    	mapview = (MapView)findViewById(R.id.mapview);
    	mapview.setBuiltInZoomControls(true);
		mapController = mapview.getController();
	    mapController.setZoom(16);
    	
    	addressText = (TextView)findViewById(R.id.addressText);
    	
        
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        
        locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				makeUseOfNewLocation(location);
				//Toast.makeText(getApplicationContext(), "New Locationdd", Toast.LENGTH_LONG).show();
				
				
				
			}

			private void makeUseOfNewLocation(Location location) {
				
				
				double lon = (double) (location.getLongitude() * 1E6);
				double lat = (double) (location.getLatitude() * 1E6);
				
				int lontitue = (int)lon;
				int latitute = (int)lat;
				
				geopoint = new GeoPoint(latitute, lontitue);
				mapController.animateTo(geopoint);
				
				currentLongitude = location.getLongitude();
				currentLatitude = location.getLatitude();
				getAddress();
				
				//---Add a location marker---
			    MapOverlay mapOverlay = new MapOverlay();
			    List<Overlay> listOfOverlays = mapview.getOverlays();
			   // listOfOverlays.clear();
			    listOfOverlays.add(mapOverlay);  
			    
			   // mapview.invalidate();			 
			//	locationManager.removeUpdates(locationListener);
			//	locationManager = null;
				
				
				// Setting the current location in the backend. 
				String email = CMUSVDirectoryActivity.USER_EMAIL.split("@")[0];
				System.out.println(email);
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://cmusvdirectory.appspot.com/Location_Post");
				
				
				try {
				    // Add your data
				    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				    nameValuePairs.add(new BasicNameValuePair("email", email));
				    nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(currentLatitude)));
				    nameValuePairs.add(new BasicNameValuePair("long", String.valueOf(currentLongitude)));
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				    // Execute HTTP Post Request
				    HttpResponse response = httpclient.execute(httppost);
				    Logger.getAnonymousLogger().info("response : " + response);
				} catch (ClientProtocolException e) {
				    e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
		
		
	}
	
	void getAddress(){
	        try{
	            Geocoder gcd = new Geocoder(this, Locale.getDefault());
	            List<Address> addresses = 
	                gcd.getFromLocation(currentLatitude, currentLongitude,1);
	            if (addresses.size() > 0) {
	                StringBuilder result = new StringBuilder();
	                for(int i = 0; i < addresses.size(); i++){
	                    Address address =  addresses.get(i);
	                    int maxIndex = address.getMaxAddressLineIndex();
	                    for (int x = 0; x <= maxIndex; x++ ){
	                        result.append(address.getAddressLine(x));
	                        result.append("\n");
	                    } 
	                }
	                addressText.setText(result.toString());
	            }
	        }
	        catch(IOException ex){
	            addressText.setText(ex.getMessage().toString());
	        }
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	 
	void updateLocation(Location location){
        currentLocation = location;
        currentLatitude = currentLocation.getLatitude();
        currentLongitude = currentLocation.getLongitude();
	}
}
		

	
	 


