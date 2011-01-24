package com.incrediblekids.activities;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.incrediblekids.network.HttpCall;
import com.incrediblekids.util.Const;
import com.incrediblekids.util.ImageManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class ItemPicActivity extends Activity implements View.OnTouchListener{
	
	private final String TAG = "ItemPicActivity";
	private final String FLICKR_KEY = "edce333ce82dfc77c47fce4bfb7a2803";
	private final String FLICKR_SEC = "e48bbb5063ef5943";
	private final String FLICKR_UID = "24882827@N04";
	
	private ProgressDialog m_LoadindDialog = null;
	private ViewFlipper m_flipper;

	private float m_fAtDown;
	private float m_fAtUp;
	private Bitmap m_aBitmap[] = null;
	private ArrayList <String> m_ImageUrlArr = null;

	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		 
		setContentView(R.layout.item_pic_layout);
		
		//Check whether network connection is available or not.
/*		if (!NetworkConnInfo.IsWifiAvailable(this) && !NetworkConnInfo.Is3GAvailable(this))
		{
			Toast.makeText(this, "Data network is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}*/
	
		final String term = intent.getStringExtra(Const.ITEM_NAME);
		m_LoadindDialog = ProgressDialog.show(this,"Loading",
				"Downloading images related with "+ term, true, true);	
		
		Thread thread = new Thread(new Runnable() {
			public void run(){	
				m_ImageUrlArr = getImageURLs(term, 4, 1);
				m_aBitmap = new Bitmap[m_ImageUrlArr.size()];
				for (int count=0; count < m_ImageUrlArr.size(); count++){
					Log.e(TAG, m_ImageUrlArr.get(count));
					m_aBitmap[count] = ImageManager.getImageBitmap((m_ImageUrlArr.get(count)));
				}
				mainHandler.sendEmptyMessage(0);
			} 
		}); 
		thread.start();
		
	}
	private Handler mainHandler = new Handler() {
		public void handleMessage(Message msg) {
			m_LoadindDialog.dismiss();
			m_flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
			m_flipper.setOnTouchListener(ItemPicActivity.this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			for (int i=0; i < m_ImageUrlArr.size(); i++){
				ImageView iv = new ImageView(ItemPicActivity.this);
				iv.setLayoutParams(layoutParams);
				iv.setImageBitmap(m_aBitmap[i]);
				m_flipper.addView(iv);
			}
		}
	};
	
	//Get Image from flickr
	public ArrayList <String> getImageURLs(String searchText, int perPage, int pageNum){
		Log.e(TAG, "getImageURLs for term " + searchText);
		ArrayList <String> urlArray = new ArrayList <String>();
		Transport rest;
		try {
			rest = new REST();
			String [] a = new String[1];
			a[0] = new String(searchText);
			Flickr flickr=new Flickr(FLICKR_KEY, FLICKR_SEC, rest);
			SearchParameters searchParams=new SearchParameters();
			searchParams.setTagMode("all");
			searchParams.setUserId(FLICKR_UID);
			searchParams.setTags(a);

			PhotosInterface photosInterface=flickr.getPhotosInterface();
			PhotoList photoList = photosInterface.search(searchParams,perPage,pageNum);
			
			if(photoList!=null){

				for(int i=0;i<photoList.size();i++){
					Log.e(TAG, "photo");
					Photo photo=(Photo)photoList.get(i);
					urlArray.add(photo.getLargeUrl());
				}
			}
			else{
				Log.e(TAG, "photo list is null");
			}
			Log.e(TAG, "getPhotosInterface() photoList.size = "+photoList.size());
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urlArray;
	}
	
	private ArrayList <String> getImageURLsFromGoogle(String searchText, String pic_source){
		String response = HttpCall.execute("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&as_sitesearch=" + pic_source + "&q="+searchText);
		ArrayList <String> result = new ArrayList<String>();
		JSONObject root	= null;
		JSONObject responseData = null;
		JSONArray resultArray = null;
		try {
			root = new JSONObject(response);
			responseData = root.getJSONObject("responseData");
			resultArray = responseData.getJSONArray("results");
			
			for (int i=0; i < resultArray.length(); i++){
				result.add(resultArray.getJSONObject(i).getString("url"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v != m_flipper) return false;		

		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			m_fAtDown = event.getX();
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			m_fAtUp = event.getX(); 

			if( m_fAtUp < m_fAtDown ) {
				m_flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				m_flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));
				m_flipper.showNext();
				
			}
			else if (m_fAtUp > m_fAtDown){
				m_flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				m_flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				m_flipper.showPrevious();			
			}
			turnNaviOn(m_flipper.getDisplayedChild());
		}		
		return true;
	}
	
	private void turnNaviOn(int index){
		ImageView iv1 = (ImageView)findViewById(R.id.nav_1);
		ImageView iv2 = (ImageView)findViewById(R.id.nav_2);
		ImageView iv3 = (ImageView)findViewById(R.id.nav_3);
		ImageView iv4 = (ImageView)findViewById(R.id.nav_4);
		switch(index){
		case 0:			
			iv1.setBackgroundColor(Color.RED);
			iv2.setBackgroundColor(Color.TRANSPARENT);
			iv3.setBackgroundColor(Color.TRANSPARENT);
			iv4.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 1:
			iv2.setBackgroundColor(Color.RED);
			iv1.setBackgroundColor(Color.TRANSPARENT);
			iv3.setBackgroundColor(Color.TRANSPARENT);
			iv4.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 2:
			iv3.setBackgroundColor(Color.RED);
			iv1.setBackgroundColor(Color.TRANSPARENT);
			iv2.setBackgroundColor(Color.TRANSPARENT);
			iv4.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 3:
			iv4.setBackgroundColor(Color.RED);
			iv1.setBackgroundColor(Color.TRANSPARENT);
			iv2.setBackgroundColor(Color.TRANSPARENT);
			iv3.setBackgroundColor(Color.TRANSPARENT);
			break;
		default:
			break;
				
		}
	}
}