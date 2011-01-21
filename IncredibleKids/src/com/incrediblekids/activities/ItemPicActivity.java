package com.incrediblekids.activities;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class ItemPicActivity extends Activity implements View.OnTouchListener{
	/** Called when the activity is first created. */
	private ProgressDialog m_LoadindDialog = null;
	ViewFlipper flipper;
	// 터치 이벤트 발생 지점의 x좌표 저장
	float xAtDown;
	float xAtUp;
	Bitmap bitmap[] = null;
	private ArrayList <String> imageUrlArr = null;

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
				imageUrlArr = getImageURLs(term, Const.PIC_ANIMAL);
				bitmap = new Bitmap[imageUrlArr.size()];
				for (int count=0; count < imageUrlArr.size(); count++){
					bitmap[count] = ImageManager.UrlToBitmap((imageUrlArr.get(count)));
				}
				mainHandler.sendEmptyMessage(0);
			} 
		}); 
		thread.start();
		
	}
	private Handler mainHandler = new Handler() {
		public void handleMessage(Message msg) {
			m_LoadindDialog.dismiss();
			flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
			flipper.setOnTouchListener(ItemPicActivity.this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			for (int i=0; i < imageUrlArr.size(); i++){
				ImageView iv = new ImageView(ItemPicActivity.this);
				iv.setLayoutParams(layoutParams);
				iv.setImageBitmap(bitmap[i]);
				flipper.addView(iv);
			}
		}
	};
	private ArrayList <String> getImageURLs(String searchText, String pic_source){
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
			turnNaviOn(flipper.getDisplayedChild());
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
		}
	}
}