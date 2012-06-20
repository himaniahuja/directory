package edu.cmu.sv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CMUSVUtils {


	// Reading JSON data from back end.
	public static String readPeopleData(String uri) {
		URL url;
		StringBuffer jsonstring = null;
		HttpURLConnection connection; 

		try{
			url = new URL(uri);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(1000 * 5);
			InputStreamReader is = new InputStreamReader(connection
					.getInputStream());
			BufferedReader buff = new BufferedReader(is);
			jsonstring = new StringBuffer();
			String line = "";
			do {
				line = buff.readLine();
				if (line != null)
					jsonstring.append(line);
			} while (line != null);
		} 

		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonstring.toString().trim();
	}   
}
