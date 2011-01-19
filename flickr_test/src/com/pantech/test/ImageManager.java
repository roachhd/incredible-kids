package com.pantech.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageManager {
	
	//이미지 URL로 부터 비트맵 생성.
	static public Bitmap getImageBitmap(String imgUrl){
    	URL url = null;
    	Bitmap bm = null;
    	try {
    		Log.e("ImageManager", "imgUrl=" + imgUrl);
			url = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
//            bis.close();
//            is.close();
//            conn.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bm != null)
			Log.e("ImageManager", "width:" + bm.getWidth() + " height" + bm.getHeight());
		return bm;   	
    }
	public static Bitmap UrlToBitmap(String url){
		HttpGet httpRequest = null;
		try {
			URL myUrl = new URL(url);
			httpRequest = new HttpGet(myUrl.toURI());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse)httpclient.execute(httpRequest);
			
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			InputStream instream = bufHttpEntity.getContent();
			Bitmap bm = BitmapFactory.decodeStream(instream);
			return bm;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
