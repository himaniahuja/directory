package edu.cmu.sv;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

public class ShowMyLocationActivity extends MapActivity {

	double currentLatitude;
	double currentLongitude;
	Location currentLocation;
	TextView addressText;
	MapController mapController;
	MapView mapview;
	
	public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.location);
    	
    	mapview = (MapView)findViewById(R.id.mapview);
    	mapview.setBuiltInZoomControls(true);
		mapController = mapview.getController();
	    mapController.setZoom(16);
    	
    	addressText = (TextView)findViewById(R.id.addressText);
    	
        
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        
        LocationListener locationListener = new LocationListener() {
			
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
				
				GeoPoint geopoint = new GeoPoint(latitute, lontitue);
				mapController.animateTo(geopoint);
				
				currentLongitude = location.getLongitude();
				currentLatitude = location.getLatitude();
				getAddress();
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
		

	
	 


