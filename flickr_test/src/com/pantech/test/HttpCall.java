package com.pantech.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpCall {


	public static String execute(String query){
		StringBuilder sb = new StringBuilder();
		String response;
		try {
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (conn != null){
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){

					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					while(true){
						String line = br.readLine();
						if (line == null) break;
						sb.append(line);						
					}
					br.close();
				}
				else{
					conn.disconnect();
					return null;
				}
				conn.disconnect();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
		response = sb.toString();
		Log.e("WOORAM", response);
		return response;
	}
}
