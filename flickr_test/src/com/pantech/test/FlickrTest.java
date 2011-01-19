package com.pantech.test;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.Transport;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;

public class FlickrTest extends Activity implements View.OnTouchListener{
	/** Called when the activity is first created. */
	ViewFlipper flipper;
	// 터치 이벤트 발생 지점의 x좌표 저장
	float xAtDown;
	float xAtUp;

	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		String term = "bear";
		ArrayList <String> imageUrlArr = getImageURLs(term);
		flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
		flipper.setOnTouchListener(this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		for (int i=0; i < imageUrlArr.size(); i++){
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(layoutParams);
			iv.setImageBitmap(ImageManager.UrlToBitmap((imageUrlArr.get(i))));
			flipper.addView(iv);
		}
	}
	
	private ArrayList <String> getImageURLs(String searchText){
		String response = HttpCall.execute("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+searchText);
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

	public ArrayList <String> getImageURLs_old(String searchText, int perPage, int pageNum){

		ArrayList <String> urlArray = new ArrayList <String>();
		String key="05649f8f235881968cb7521bfd528d22";
		//String key="edce333ce82dfc77c47fce4bfb7a2803";
		//String sec = "e48bbb5063ef5943";
		Transport rest;
		try {
			rest = new REST();
			String [] a = new String[1];
			a[0] = new String(searchText);
			Flickr flickr=new Flickr(key,rest);
			SearchParameters searchParams=new SearchParameters();
			//searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);
			//searchParams.setTagMode("all");
			//searchParams.setTags(a);
			searchParams.setText(searchText);
			//searchParams.setTags(a);

			PhotosInterface photosInterface=flickr.getPhotosInterface();
			PhotoList photoList = photosInterface.search(searchParams,perPage,pageNum);
			if(photoList!=null){
				for(int i=0;i<photoList.size();i++){
					Log.e("WOORAM", "photo");
					Photo photo=(Photo)photoList.get(i);
					urlArray.add(photo.getLargeUrl());
				}
			}
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 터치 이벤트가 일어난 뷰가 ViewFlipper가 아니면 return
		if(v != flipper) return false;		

		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // 터치 시작지점 x좌표 저장			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			xAtUp = event.getX(); 	// 터치 끝난지점 x좌표 저장

			if( xAtUp < xAtDown ) {
				// 왼쪽 방향 에니메이션 지정
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));

				// 다음 view 보여줌
				flipper.showNext();
			}
			else if (xAtUp > xAtDown){
				// 오른쪽 방향 에니메이션 지정
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				// 전 view 보여줌
				flipper.showPrevious();			
			}
		}		
		return true;
	}
}